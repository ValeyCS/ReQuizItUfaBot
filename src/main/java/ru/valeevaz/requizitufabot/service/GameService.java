package ru.valeevaz.requizitufabot.service;

import org.springframework.stereotype.Service;
import ru.valeevaz.requizitufabot.entity.GameEntity;
import ru.valeevaz.requizitufabot.repository.GameRepository;
import ru.valeevaz.requizitufabot.repository.GamesTypeRepository;
import ru.valeevaz.requizitufabot.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final LocationRepository locationRepository;
    private final GamesTypeRepository gamesTypeRepository;

    public GameService(GameRepository gameRepository, LocationRepository locationRepository, GamesTypeRepository gamesTypeRepository) {
        this.gameRepository = gameRepository;
        this.locationRepository = locationRepository;
        this.gamesTypeRepository = gamesTypeRepository;
    }

    public Optional<GameEntity> getGame(){
//        UUID id = UUID.fromString("348b1f84-8592-48e3-841b-520929ec2f98");
        Integer id = 1;
        Optional<GameEntity> gameEntity = gameRepository.findById(id);
        return gameEntity;
    }

    public List<GameEntity> getAllGames(){
        List<GameEntity> gameEntities = gameRepository.findAll();
        return gameEntities;
    }
}
