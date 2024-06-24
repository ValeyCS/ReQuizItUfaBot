package ru.valeevaz.requizitufabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.valeevaz.requizitufabot.enums.ModeEnum;

import java.util.ArrayList;
import java.util.List;

import static ru.valeevaz.requizitufabot.enums.StatusEnum.SET_NAME;
import static ru.valeevaz.requizitufabot.service.TelegramBotUtil.buildJsonData;

@Slf4j
@Component
public class TelegramBotHelper {

    public SendMessage setNameUser(Long chatId, String name, Integer gameId) {

        SendMessage message = new SendMessage();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        message.setChatId(chatId);
        message.setText(SET_NAME.getTextMessage());

        var buttonName = new InlineKeyboardButton();
        buttonName.setText("Имя: " + name);
        buttonName.setCallbackData(buildJsonData(gameId,SET_NAME.toString().toLowerCase(),ModeEnum.GAMERECORD.getCode()));
        rowInLine.add(buttonName);
        keyboardButtons.add(rowInLine);

        keyboardMarkup.setKeyboard(keyboardButtons);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
