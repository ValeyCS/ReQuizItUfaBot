package ru.valeevaz.requizitufabot.enums;

public enum ModeEnum {
    GAMERECORD("gamerecord"),
    VIEW("view"),;

    private String code;

    ModeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
