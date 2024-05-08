package ru.valeevaz.requizitufabot.enums;

public enum StatusEnum {

    NEW("new"),
    SET_NAME("name"),
    SET_TEAM("team"),
    SET_AMOUNT("amount"),
    FINISH("finish");

    private final String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
