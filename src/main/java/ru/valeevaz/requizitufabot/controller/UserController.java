package ru.valeevaz.requizitufabot.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valeevaz.requizitufabot.entity.UserEntity;
import ru.valeevaz.requizitufabot.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserEntity> handleGetAllTasks() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.getUser());
    }

//    @PostMapping
//    private void setGame(@RequestBody GamePayload gamePayload){
//        var gameEntity = new GameEntity();
//        gameEntity.setName(gamePayload.name());
//        userService.setGame();
//    }
}
