package ru.valeevaz.requizitufabot.enums;

import java.util.Arrays;
import java.util.Objects;

public enum DayOfWeekEnum {
    MONDAY(1,"Пн", "Понедельник"),
    TUESDAY(2,"Вт", "Вторник"),
    WEDNESDAY(3,"Ср","Среда"),
    THURSDAY(4,"Чт","Четверг"),
    FRIDAY(5,"Пт","Пятница"),
    SATURDAY(6,"Сб","Суббота"),
    SUNDAY(7,"Вс","Воскресенье");

    DayOfWeekEnum(Integer codeDay, String shortNameDay, String fullNameDay) {
        this.codeDay = codeDay;
        this.shortNameDay = shortNameDay;
        this.fullNameDay = fullNameDay;
    }

    private Integer codeDay;
    private String shortNameDay;
    private String fullNameDay;

    public Integer getCodeDay() {
        return codeDay;
    }

    public String getShortNameDay() {
        return shortNameDay;
    }

    public String getFullNameDay() {
        return fullNameDay;
    }

    public static String findByCodeShort(Integer code) {
        return Arrays.stream(values())
                .filter(month -> Objects.equals(month.getCodeDay(), code))
                .findFirst()
                .map(DayOfWeekEnum::getShortNameDay)
                .orElse(null);
    }

    public static String findByCodeFull(Integer code) {
        return Arrays.stream(values())
                .filter(month -> Objects.equals(month.getCodeDay(), code))
                .findFirst()
                .map(DayOfWeekEnum::getFullNameDay)
                .orElse(null);
    }
}
