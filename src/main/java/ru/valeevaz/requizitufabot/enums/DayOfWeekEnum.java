package ru.valeevaz.requizitufabot.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum DayOfWeekEnum {
    MONDAY(1,"Пн"),
    TUESDAY(2,"Вт"),
    WEDNESDAY(3,"Ср"),
    THURSDAY(4,"Чт"),
    FRIDAY(5,"Пт"),
    SATURDAY(6,"Сб"),
    SUNDAY(7,"Вс");

    DayOfWeekEnum(Integer codeDay, String shortNameDay) {
        this.codeDay = codeDay;
        this.shortNameDay = shortNameDay;
    }

    private Integer codeDay;
    private String shortNameDay;

    public Integer getCodeDay() {
        return codeDay;
    }

    public void setCodeDay(Integer codeDay) {
        this.codeDay = codeDay;
    }

    public String getShortNameDay() {
        return shortNameDay;
    }

    public void setShortNameDay(String shortNameDay) {
        this.shortNameDay = shortNameDay;
    }

    public static String findByCode(Integer code) {
        return Arrays.stream(values())
                .filter(month -> Objects.equals(month.getCodeDay(), code))
                .findFirst()
                .map(DayOfWeekEnum::getShortNameDay)
                .orElse(null);
    }

}
