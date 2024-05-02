package ru.valeevaz.requizitufabot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.valeevaz.requizitufabot.entity.UserEntity;

import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByChatID(Long chatId);
}
