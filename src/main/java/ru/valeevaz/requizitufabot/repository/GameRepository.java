package ru.valeevaz.requizitufabot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.valeevaz.requizitufabot.entity.GameEntity;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {

}
