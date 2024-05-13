package ru.valeevaz.requizitufabot.enums;


public enum MenuEnum {
    START("/start", "Запуск бота"),
    GAMES("/games", "Список игр доступных для записи"),
    MYGAMES("/mygames", "Игры на которые вы или ваша команда уже зарегистрированы"),
    MYDATA("/mydata", "Мои данные"),
    HELP("/help", "Помощь");

    private String command;
    private String description;

    MenuEnum(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
