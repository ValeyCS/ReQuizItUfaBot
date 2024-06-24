package ru.valeevaz.requizitufabot.service;

import lombok.experimental.UtilityClass;
import ru.valeevaz.requizitufabot.entity.GameEntity;

import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import static ru.valeevaz.requizitufabot.enums.DayOfWeekEnum.findByCodeFull;

@UtilityClass
public class TelegramBotUtil {
    public static String buildJsonData(Integer gameId, String status, String mode) {

        var joiner = new StringJoiner(" ");
        joiner.add("{ gameId:");
        joiner.add(String.valueOf(gameId));
        joiner.add(", status:");
        joiner.add(status);
        joiner.add(", mode:");
        joiner.add(mode);
//        joiner.add(", messageId:");
//        joiner.add(messageId);
        joiner.add(" }");
        return joiner.toString();
    }

    public static StringJoiner getGameInfo(GameEntity game) {

        String ddMMyyyyPattern = "dd.MM.yyyy";
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern(ddMMyyyyPattern);
        String hhmmPattern = "HH.mm";
        DateTimeFormatter timeformat = DateTimeFormatter.ofPattern(hhmmPattern);

        StringJoiner gameInfo = new StringJoiner(" ");
        gameInfo.add("Дата игры: ");
        gameInfo.add(findByCodeFull(game.getDateGame().getDayOfWeek().getValue()));
        gameInfo.add(dateformat.format(game.getDateGame()));
        gameInfo.add("\n");
        gameInfo.add("Время: ");
        gameInfo.add(timeformat.format(game.getDateGame()));
        gameInfo.add("\n");
        gameInfo.add("Место проведения:");
        gameInfo.add(game.getLocation().getName());
        gameInfo.add("\n");
        gameInfo.add("Формат игры:");
        gameInfo.add(game.getName()==null? game.getGamesType().getName(): game.getName());
        gameInfo.add("\n");
        gameInfo.add("Цена:");
        gameInfo.add(String.valueOf(game.getPrice()));
        gameInfo.add("\n");
        gameInfo.add("Описание игры:");
        gameInfo.add(game.getDescription()==null? game.getGamesType().getDescription(): game.getDescription());;

        return gameInfo;
    }
}
