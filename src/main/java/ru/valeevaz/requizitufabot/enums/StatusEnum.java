package ru.valeevaz.requizitufabot.enums;

public enum StatusEnum {

    NEW("new"),
    SET_NAME("name"),
    SET_TEAM("Напишите, название вашей команды."),
    SET_AMOUNT("Укажите количество людей в вашей команде на игру."),
    SET_PHONE("Напишите ваш номер телефона."),
    FINISH("Регистрация на игру завершена."),
    VIEW("Регистрация на игру завершена.");

    private final String textMessage;

    StatusEnum(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }
}
