package ru.valeevaz.requizitufabot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.valeevaz.requizitufabot.entity.GameEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    @Query(value = "SELECT g FROM ru.valeevaz.requizitufabot.entity.GameEntity g WHERE g.dateGame>:date and g.isActive=true ORDER BY g.dateGame")
    List<GameEntity> getAllActiveGames(@Param("date") LocalDateTime date);

}
