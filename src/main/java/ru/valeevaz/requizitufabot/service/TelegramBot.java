package ru.valeevaz.requizitufabot.service;

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
import ru.valeevaz.requizitufabot.enums.MenuEnum;
import ru.valeevaz.requizitufabot.config.BotConfig;
import ru.valeevaz.requizitufabot.entity.UserEntity;
import ru.valeevaz.requizitufabot.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static ru.valeevaz.requizitufabot.enums.ButtonEnum.GAME;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameService gameService;

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
            switch (message) {
                case "/start" -> {
                    AnswerForUser(chatId, userName);
//                    SetUsers(update.getMessage());
                }
                case "/games" -> {
                    getMenuListMessage(chatId);
//                    SetUsers(update.getMessage());
                }
                default -> sendMessage(chatId, "Sorry");
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

//            if(callbackData.matches(GAME.getNameButton())){
//
//            }else if(callbackData.equals(YES_BUTTON)){
//                String text = "You pressed YES button";
//                executeEditMessageText(text, chatId, messageId);
//            }
//            else if(callbackData.equals(NO_BUTTON)){
//                String text = "You pressed NO button";
//                executeEditMessageText(text, chatId, messageId);
//            }
        }
    }

    private void AnswerForUser(Long chatId, String userName) {
        String answer = "Hi, " + userName + ".! Bla bla";
        log.info("Ответ пользователю " + userName);
        sendMessage(chatId, answer);
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

    public void getMenuListMessage(Long chatId){

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Регистрация на игры в г.Уфа:\n" +
                "\n" +
                "Выберите игру, на которую хотите зарегистрироваться");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();

        String ddMMPattern = "dd.MM";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(ddMMPattern);

        List<GameEntity> gameEntities = gameService.getAllGames();
        for (GameEntity game: gameEntities) {
            var buttonMenu = new InlineKeyboardButton();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            var menuText = new StringJoiner(" ");
            menuText.add(game.getDateGame().getDayOfWeek().toString());
            menuText.add(europeanDateFormatter.format(game.getDateGame()));
            menuText.add(game.getName());
            menuText.add(game.getLocation().getName());

            buttonMenu.setText(menuText.toString());
            buttonMenu.setCallbackData(GAME.getNameButton()+"_"+game.getId());
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
}
