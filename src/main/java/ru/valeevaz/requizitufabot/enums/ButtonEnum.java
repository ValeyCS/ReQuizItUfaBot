package ru.valeevaz.requizitufabot.enums;

public enum ButtonEnum {
    GAME("gameButton");

    private final String nameButton;

    ButtonEnum(String nameButton) {
        this.nameButton = nameButton;
    }

    public String getNameButton() {
        return nameButton;
    }
}
