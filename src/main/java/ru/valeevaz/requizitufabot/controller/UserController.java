package ru.valeevaz.requizitufabot.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valeevaz.requizitufabot.entity.GameEntity;
import ru.valeevaz.requizitufabot.entity.UserEntity;
import ru.valeevaz.requizitufabot.service.GameService;
import ru.valeevaz.requizitufabot.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    private final GameService gameService;


    public UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<UserEntity> handleGetAllTasks() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.getUser());
    }

    @GetMapping("game")
    public ResponseEntity<Optional<GameEntity>> handleGetAllGames() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.gameService.getGame());
    }

//    @PostMapping
//    private void setGame(@RequestBody GamePayload gamePayload){
//        var gameEntity = new GameEntity();
//        gameEntity.setName(gamePayload.name());
//        userService.setGame();
//    }
}
