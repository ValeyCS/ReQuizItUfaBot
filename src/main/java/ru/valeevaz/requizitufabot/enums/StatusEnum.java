package ru.valeevaz.requizitufabot.enums;

public enum StatusEnum {

    NEW("Новая заявка"),
    SET_NAME("Напишите ваше имя:\n или мы можем взять его из вашего акуаунта"),
    SET_TEAM("Напишите, название вашей команды."),
    SET_AMOUNT("Укажите количество людей в вашей команде на игру."),
    SET_PHONE("Напишите ваш номер телефона."),
    FINISH("Регистрация на игру завершена."),
    CANCEL("Отмена записи на игру"),
    DELETE("Удалить записи на игру"),
    NONE("Статус не указан");
    private final String textMessage;

    StatusEnum(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }
}
