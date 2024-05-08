package ru.valeevaz.requizitufabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.valeevaz.requizitufabot.entity.GameEntity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static ru.valeevaz.requizitufabot.enums.DayOfWeekEnum.findByCode;

@Slf4j
@Component
public class TelegramBotHelper {

    public SendMessage setNameUser(Long chatId, String name, Integer gameId) {

        SendMessage message = new SendMessage();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        message.setChatId(chatId);
        message.setText("Напишите ваше имя:\n" +
                "или мы можем взять его из вашего акуаунта");

        var dataGame = new StringJoiner(" ");
        dataGame.add("{ gameId:");
        dataGame.add(gameId.toString());
        dataGame.add(", status: name }");

        var buttonName = new InlineKeyboardButton();
        buttonName.setText("Имя: " + name);
        buttonName.setCallbackData(dataGame.toString());
        rowInLine.add(buttonName);
        keyboardButtons.add(rowInLine);

        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    public SendMessage setSimpleMessage(Long chatId, String status, String text, Integer gameId) {

        SendMessage message = new SendMessage();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        message.setChatId(chatId);
        message.setText(text);

        var dataGame = new StringJoiner(" ");
        dataGame.add("{ gameId:");
        dataGame.add(gameId.toString());
        dataGame.add(", status: ");
        dataGame.add(status);
        dataGame.add(" }");

//        var buttonName = new InlineKeyboardButton();
//        buttonName.setText("Имя: " + name);
//        buttonName.setCallbackData(dataGame.toString());
//        rowInLine.add(buttonName);
//        keyboardButtons.add(rowInLine);

        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }
}
