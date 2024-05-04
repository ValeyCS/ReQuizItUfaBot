package ru.valeevaz.requizitufabot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.valeevaz.requizitufabot.entity.GamesTypeEntity;

@Repository
public interface GamesTypeRepository extends JpaRepository<GamesTypeEntity, Long> {

}
