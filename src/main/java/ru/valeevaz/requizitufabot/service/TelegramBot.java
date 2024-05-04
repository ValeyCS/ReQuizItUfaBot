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

import java.util.ArrayList;
import java.util.List;

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

        }
    }

    private void AnswerForUser(Long chatId, String userName) {
        String answer = "Hi, " + userName + ".! Bla bla";
        log.info("Ответ пользователю " + userName);
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sendmessage " + e.getMessage());
        }
    }

    public List<BotCommand> getStartMenu() {
        List<BotCommand> commandList = new ArrayList<>();
        MenuEnum[] menuEnums = MenuEnum.values();
        for (MenuEnum anEnum: menuEnums) {
            commandList.add(new BotCommand(anEnum.getCommand(), anEnum.getDescription()));
        }
//        commandList.add(new BotCommand(MenuEnum.START.getCommand(), MenuEnum.START.getDescription()));
//        commandList.add(new BotCommand("/start", "Запуск бота"));
//        commandList.add(new BotCommand("/games", "Список игр; доступных для записи"));
//        commandList.add(new BotCommand("/mygames", "Игры; на которые вы записаны"));
//        commandList.add(new BotCommand("/mydata", "Мои данные"));
//        commandList.add(new BotCommand("/delmydata", "Удалить мои данные из бота и запретить их сохранять"));
//        commandList.add(new BotCommand("/settings", "Личные настройки по работе с ботом"));
//        commandList.add(new BotCommand("/help", "Помощь по работе с ботом"));
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

        List<GameEntity> gameEntities = gameService.getAllGames();
        for (GameEntity game: gameEntities) {
            var buttonMenu = new InlineKeyboardButton();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();

            buttonMenu.setText(game.getName());
            buttonMenu.setCallbackData(game.getName());
            rowInLine.add(buttonMenu);
            keyboardButtons.add(rowInLine);
        }
        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);

//        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
//        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
//        var yesButton = new InlineKeyboardButton();
//
//        yesButton.setText("Yes");
//        yesButton.setCallbackData(YES_BUTTON);
//
//        var noButton = new InlineKeyboardButton();
//
//        noButton.setText("No");
//        noButton.setCallbackData(NO_BUTTON);
//
//        rowInLine.add(yesButton);
//        rowInLine.add(noButton);
//
//        rowsInLine.add(rowInLine);
//
//        markupInLine.setKeyboard(rowsInLine);
//        message.setReplyMarkup(markupInLine);
//
//        executeMessage(message);
    }

    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения " + e.getMessage());
        }
    }
}
