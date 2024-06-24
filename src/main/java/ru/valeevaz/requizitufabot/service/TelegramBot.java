package ru.valeevaz.requizitufabot.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.valeevaz.requizitufabot.config.BotConfig;
import ru.valeevaz.requizitufabot.entity.GameEntity;
import ru.valeevaz.requizitufabot.entity.RecordEntity;
import ru.valeevaz.requizitufabot.enums.MenuEnum;
import ru.valeevaz.requizitufabot.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static ru.valeevaz.requizitufabot.enums.DayOfWeekEnum.findByCodeShort;
import static ru.valeevaz.requizitufabot.enums.ModeEnum.GAMERECORD;
import static ru.valeevaz.requizitufabot.enums.ModeEnum.VIEW;
import static ru.valeevaz.requizitufabot.enums.StatusEnum.*;
import static ru.valeevaz.requizitufabot.service.TelegramBotUtil.buildJsonData;
import static ru.valeevaz.requizitufabot.service.TelegramBotUtil.getGameInfo;

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

    private static final String REGISTERTEXT = """
            Регистрация на игры в г.Уфа:
            Выберите игру, на которую хотите зарегистрироваться""";
    private static final String VIEWTEXT = "Игры на которые вы записаны:";

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> commandList = getStartMenu();
        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
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
//        DeleteMessage deleteMessage = new DeleteMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            messageAnswer(update);
        } else if (update.hasCallbackQuery()) {
            callbackQueryAnswer(update.getCallbackQuery());
        }
    }

    private void messageAnswer(Update update){
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getChat().getFirstName();
        Long telegramUserId = update.getMessage().getFrom().getId();

        switch (message) {
            case "/start" -> {
                answerForUser(chatId, userName);
                List<GameEntity> gameEntities = gameService.getAllActiveGames();
                getMenuListMessage(chatId, REGISTERTEXT, GAMERECORD.getCode(),NEW.name().toLowerCase(),gameEntities);
            }
            case "/games" -> {
                List<GameEntity> gameEntities = gameService.getAllActiveGames();
                getMenuListMessage(chatId, REGISTERTEXT, GAMERECORD.getCode(),NEW.name().toLowerCase(),gameEntities);
            }
            case "/mygames" -> {
                List<GameEntity> gameEntities = gameService.getMyActiveGames(telegramUserId);
                getMenuListMessage(chatId, VIEWTEXT, VIEW.getCode(), NONE.name().toLowerCase(),gameEntities);
            }
            default -> questionGame(message, telegramUserId);
        }
    }

    private void callbackQueryAnswer(CallbackQuery callbackQuery){

        JsonObject jsonObject = JsonParser.parseString(callbackQuery.getData()).getAsJsonObject();
        Long telegramUserId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String status = jsonObject.get("status").getAsString();
        String mode = jsonObject.get("mode").getAsString();
        Integer gameId = jsonObject.get("gameId").getAsInt();
        GameEntity gameEntity = gameService.getGameById(gameId);

        switch (mode) {
            case "gamerecord" -> {
                switch (status) {
                    case "new" -> {
                        var recordGame = RecordEntity.builder()
                                .telegramUserId(telegramUserId)
                                .game(gameEntity)
                                .status(SET_NAME)
                                .createdDate(LocalDateTime.now())
                                .chatId(chatId)
                                .canDelete(true).build();

                        recordService.saveRecordGame(recordGame);
                        var message = telegramBotHelper.setNameUser(chatId, callbackQuery.getFrom().getFirstName(), gameEntity.getId());
                        executeMessage(message);
                    }
                    case "set_name" -> questionGame(callbackQuery.getFrom().getFirstName(), telegramUserId);
                    case "finish" -> {
                        var activeRecord = recordService.getActiveRecords(telegramUserId);
                        var activeRecordSave = activeRecord.toBuilder().status(FINISH).canDelete(false).build();
                        saveRecordAndSendMessage(activeRecordSave);
                    }
                    case "delete" -> {
                        recordService.deleteRecord(telegramUserId,gameId);
                        sendMessage(chatId, "Запись на игру отменена. Можете записаться на другие наши игры.");
                        List<GameEntity> gameEntities = gameService.getAllActiveGames();
                        getMenuListMessage(chatId, REGISTERTEXT, GAMERECORD.getCode(),NEW.name().toLowerCase(),gameEntities);
                    }
                    case "cancel" -> {
                        recordService.cancelRecord(telegramUserId,gameId);
                        sendMessage(chatId, "Запись на игру отменена. Мы растроенны, но если вы передумаете, мы всегда вам рады!");
                    }
                    default ->
                            sendMessage(chatId, "Простите мы не можем пока понять, что вы хотели сделать. Но обязатлельно разберемся");
                }
            }
            case "view" -> viewGame(gameId,chatId);
            default -> sendMessage(chatId, "Неизвестный режим работы");
        }
    }

    private void questionGame(String message, Long telegramUserId) {
        var activeRecord = recordService.getActiveRecords(telegramUserId);
        switch (activeRecord.getStatus()) {
            case SET_NAME -> {
                var activeRecordSave = activeRecord.toBuilder().recordedName(message).status(SET_TEAM).build();
                saveRecordAndSendMessage(activeRecordSave);
            }
            case SET_TEAM -> {
                var activeRecordSave = activeRecord.toBuilder().recordedTeam(message).status(SET_AMOUNT).build();
                saveRecordAndSendMessage(activeRecordSave);
            }
            case SET_AMOUNT -> {
                Integer amount = Integer.parseInt(message);
                var activeRecordSave = activeRecord.toBuilder().amount(amount).status(SET_PHONE).build();
                saveRecordAndSendMessage(activeRecordSave);
            }
            case SET_PHONE -> {
                var activeRecordSave = activeRecord.toBuilder().recordedName(message).build();
                recordService.saveRecordGame(activeRecordSave);
                approveGame(activeRecord.getGame().getId(), activeRecordSave.getChatId());
            }
            default -> sendMessage(activeRecord.getChatId(), "Простите мы не можем пока понять, что вы хотели сделать. Но обязатлельно разберемся");
        }
    }

    private void answerForUser(Long chatId, String userName) {
        String answer = "Добро пожаловать, " + userName + "! Здесь вы можете зарегистрироваться на игры Ре-Квиз-ит и других проектов";
        log.info("Ответ пользователю " + userName);
        sendMessage(chatId, answer);
    }

    private void saveRecordAndSendMessage(RecordEntity recordEntity) {
        recordService.saveRecordGame(recordEntity);
        sendMessage(recordEntity.getChatId(), recordEntity.getStatus().getTextMessage());
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
        for (MenuEnum anEnum : menuEnums) {
            commandList.add(new BotCommand(anEnum.getCommand(), anEnum.getDescription()));
        }
        return commandList;
    }

    public void getMenuListMessage(Long chatId, String messageText, String mode, String status, List<GameEntity> gameEntities) {

        String ddMMPattern = "dd.MM";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(ddMMPattern);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        for (GameEntity game : gameEntities) {
            var buttonMenu = new InlineKeyboardButton();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            var menuText = new StringJoiner(" ");
            menuText.add(findByCodeShort(game.getDateGame().getDayOfWeek().getValue()));
            menuText.add(europeanDateFormatter.format(game.getDateGame()));
            if (game.getName() == null) {
                menuText.add(game.getGamesType().getName());
            } else {
                menuText.add(game.getName());
            }
            menuText.add(game.getLocation().getName());
            buttonMenu.setText(menuText.toString());
            buttonMenu.setCallbackData(buildJsonData(game.getId(), status, mode));
            rowInLine.add(buttonMenu);

            keyboardButtons.add(rowInLine);
        }
        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }


    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения " + e.getMessage());
        }
    }

    private void approveGame(Integer gameId, Long chatId) {
        var game = gameService.getGameById(gameId);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        SendMessage message = new SendMessage();
        var messageText = new StringJoiner(" ");
        messageText.add("Подтвердите выбранную игру:\n");
        var gameInfo = getGameInfo(game);
        messageText.merge(gameInfo);

        message.setChatId(chatId);
        message.setText(messageText.toString());

        var buttonApprove = new InlineKeyboardButton();
        buttonApprove.setText("Верно");
        buttonApprove.setCallbackData(buildJsonData(game.getId(),FINISH.name().toLowerCase(), GAMERECORD.getCode()));
        rowInLine.add(buttonApprove);

        var buttonCancel = new InlineKeyboardButton();
        buttonCancel.setText("Отмена");
        buttonCancel.setCallbackData(buildJsonData(game.getId(),DELETE.name().toLowerCase(), GAMERECORD.getCode()));
        rowInLine.add(buttonCancel);

        keyboardButtons.add(rowInLine);

        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void viewGame(Integer gameId, Long chatId) {
        var game = gameService.getGameById(gameId);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        SendMessage message = new SendMessage();
        var messageText = new StringJoiner(" ");
        messageText.add("Вы записаны на игру:\n");
        var gameInfo = getGameInfo(game);
        messageText.merge(gameInfo);

        message.setChatId(chatId);
        message.setText(messageText.toString());

        var buttonCancel = new InlineKeyboardButton();
        buttonCancel.setText("Отменить запись");
        buttonCancel.setCallbackData(buildJsonData(game.getId(),CANCEL.name().toLowerCase(), GAMERECORD.getCode()));
        rowInLine.add(buttonCancel);

        keyboardButtons.add(rowInLine);

        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }
}
