package ru.valeevaz.requizitufabot.service;

import org.springframework.stereotype.Service;
import ru.valeevaz.requizitufabot.entity.UserEntity;
import ru.valeevaz.requizitufabot.repository.UserRepository;

@Service
public class UserService {

    //    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUser(){
//        gameRepository.save(gameEntity);
        System.out.println("hello world");
    }

    public UserEntity getUser(){
        var entity = userRepository.findByChatID(1L);
//        var userEntity = new UserEntity();
//        userEntity.setChatID(1L);
//        userEntity.setUserName("Vasya");
//        userEntity.setUserName("vas");
        return entity;
    }
}
