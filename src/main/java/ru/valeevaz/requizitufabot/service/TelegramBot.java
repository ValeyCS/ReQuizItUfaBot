package ru.valeevaz.requizitufabot.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.valeevaz.requizitufabot.entity.GameEntity;
import ru.valeevaz.requizitufabot.entity.RecordEntity;
import ru.valeevaz.requizitufabot.enums.MenuEnum;
import ru.valeevaz.requizitufabot.config.BotConfig;
import ru.valeevaz.requizitufabot.entity.UserEntity;
import ru.valeevaz.requizitufabot.enums.StatusEnum;
import ru.valeevaz.requizitufabot.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.valeevaz.requizitufabot.enums.DayOfWeekEnum.findByCodeFull;
import static ru.valeevaz.requizitufabot.enums.DayOfWeekEnum.findByCodeShort;
import static ru.valeevaz.requizitufabot.enums.StatusEnum.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameService gameService;
    @Autowired
    private TelegramBotHelper telegramBotHelper;
    @Autowired
    private RecordService recordService;
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> commandList = getStartMenu();
        try{
            this.execute(new SetMyCommands(commandList,new BotCommandScopeDefault(),null));
        }catch (TelegramApiException e){
            log.error("Ошибка создание в меню для бота.  " + e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();
            Long telegramUserId = update.getMessage().getFrom().getId();

            Long userId = update.getMessage().getFrom().getId();
            switch (message) {
                case "/start" -> {
                    answerForUser(chatId, userName);
//                    SetUsers(update.getMessage());
                }
                case "/games" -> {
                    String messageText = """
                            Регистрация на игры в г.Уфа:\\n
                            \\n
                            Выберите игру, на которую хотите зарегистрироваться""";
                    getMenuListMessage(chatId, messageText, );
//                    SetUsers(update.getMessage());
                }
                case "/mygames" ->{

                }
                default -> {
                    questionGame(chatId, message, telegramUserId);
                }
            }

        } else if (update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();

            JsonObject jsonObject = JsonParser.parseString(callbackQuery.getData()).getAsJsonObject();
            Long telegramUserId =  update.getCallbackQuery().getFrom().getId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String status = jsonObject.get("status").getAsString();
            GameEntity gameEntity = gameService.getGameById(jsonObject.get("gameId").getAsInt());
            switch (status){
                case "new" -> {
                    var recordGame = new RecordEntity();
                    recordGame.setTelegramUserId(telegramUserId);
                    recordGame.setGame(gameEntity);
                    recordGame.setStatus(SET_NAME);
                    recordGame.setCreatedDate(LocalDateTime.now());
                    recordService.saveRecordGame(recordGame);
                    var message = telegramBotHelper.setNameUser(chatId, callbackQuery.getFrom().getFirstName(), gameEntity.getId());
                    executeMessage(message);
                }
                case "name" ->{
                    questionGame(chatId, callbackQuery.getFrom().getFirstName(), telegramUserId);
                }
                default -> sendMessage(chatId, "Простите мы не можем пока понять, что вы хотели сделать. Но обязатлельно разберемся");
            }

        }
    }

    private void questionGame(Long chatId, String message, Long telegramUserId){
        var activeRecord = recordService.getActiveRecords(telegramUserId);
        if (SET_NAME.equals(activeRecord.getStatus())){
            activeRecord.setRecordedName(message);
            saveStatusAndSendMessage(activeRecord,SET_TEAM,chatId);
        } else if(SET_TEAM.equals(activeRecord.getStatus())) {
            activeRecord.setRecordedTeam(message);
            saveStatusAndSendMessage(activeRecord,SET_AMOUNT,chatId);
        } else if(SET_AMOUNT.equals(activeRecord.getStatus())) {
            Integer amount = Integer.parseInt(message);
            activeRecord.setAmount(amount);
            saveStatusAndSendMessage(activeRecord,SET_PHONE,chatId);
        } else if(SET_PHONE.equals(activeRecord.getStatus())) {
            activeRecord.setRecordedPhone(message);
            saveStatusAndSendMessage(activeRecord,FINISH,chatId);
        }else{
            sendMessage(chatId, "Простите мы не можем пока понять, что вы хотели сделать. Но обязатлельно разберемся");
        }
    }


    private void answerForUser(Long chatId, String userName) {
        String answer = "Hi, " + userName + ".! Bla bla";
        log.info("Ответ пользователю " + userName);
        sendMessage(chatId, answer);
    }

    private void saveStatusAndSendMessage(RecordEntity recordEntity, StatusEnum status, Long chatId){
        recordEntity.setStatus(status);
        recordService.saveRecordGame(recordEntity);
        sendMessage(chatId,status.getTextMessage());
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        executeMessage(message);
    }

    public List<BotCommand> getStartMenu() {
        List<BotCommand> commandList = new ArrayList<>();
        MenuEnum[] menuEnums = MenuEnum.values();
        for (MenuEnum anEnum: menuEnums) {
            commandList.add(new BotCommand(anEnum.getCommand(), anEnum.getDescription()));
        }
        return commandList;
    }

    public void SetUsers(Message msg){
        var userEntity = new UserEntity();
        userEntity.setChatID(msg.getChatId());
        userEntity.setFirstName(msg.getChat().getFirstName());
        userEntity.setUserName(msg.getChat().getUserName());
        userRepository.save(userEntity);
    }

    public void getMenuListMessage(Long chatId, String messageText, String status){

        String ddMMPattern = "dd.MM";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(ddMMPattern);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText();

        List<GameEntity> gameEntities = gameService.getAllActiveGames();
        for (GameEntity game: gameEntities) {
            var buttonMenu = new InlineKeyboardButton();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            var menuText = new StringJoiner(" ");
            menuText.add(findByCodeShort(game.getDateGame().getDayOfWeek().getValue()));
//            menuText.add(game.getDateGame().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.of("ru","RU")));
            menuText.add(europeanDateFormatter.format(game.getDateGame()));
            if (game.getName() == null) {
                menuText.add(game.getGamesType().getName());
            }else{
                menuText.add(game.getName());
            }
            menuText.add(game.getLocation().getName());

            var dataGame = new StringJoiner(" ");
            dataGame.add("{ gameId:");
            dataGame.add(game.getId().toString());
            dataGame.add(", status:");
            dataGame.add(status);
            dataGame.add(" }");

            buttonMenu.setText(menuText.toString());
            buttonMenu.setCallbackData(dataGame.toString());
            rowInLine.add(buttonMenu);

            keyboardButtons.add(rowInLine);
        }
        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }


    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения " + e.getMessage());
        }
    }

    private void sendGameInfo(Integer id, Long chatId){
        var game = gameService.getGameById(id);
        SendMessage message = new SendMessage();

        String ddMMyyyyPattern = "dd.MM.yyyy";
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern(ddMMyyyyPattern);
        String HHmmPattern = "HH.mm";
        DateTimeFormatter timeformat = DateTimeFormatter.ofPattern(HHmmPattern);

        StringJoiner gameInfo = new StringJoiner(" ");
        gameInfo.add("Подтвердите выбранную игру:\n");
        gameInfo.add("Дата игры: ");
        gameInfo.add(findByCodeFull(game.getDateGame().getDayOfWeek().getValue()));
        gameInfo.add(dateformat.format(game.getDateGame()));
        gameInfo.add("\n");
        gameInfo.add("Время: ");
        gameInfo.add(timeformat.format(game.getDateGame()));
        gameInfo.add("\n");
        gameInfo.add("Место проведения:");
        gameInfo.add(game.getLocation().getName());
        gameInfo.add("\n");
        gameInfo.add("Формат игры:");
        gameInfo.add(game.getGamesType().getName());
        gameInfo.add("\n");
        gameInfo.add("Цена:");
        gameInfo.add(String.valueOf(game.getPrice()));
        gameInfo.add("\n");
        gameInfo.add("Описание игры:");
        gameInfo.add(game.getGamesType().getDescription());

        message.setChatId(chatId);
        message.setText(gameInfo.toString());
        executeMessage(message);
    }
}
