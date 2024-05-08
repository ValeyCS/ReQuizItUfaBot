package ru.valeevaz.requizitufabot.service;

import org.springframework.stereotype.Service;
import ru.valeevaz.requizitufabot.entity.GameEntity;
import ru.valeevaz.requizitufabot.repository.GameRepository;
import ru.valeevaz.requizitufabot.repository.GamesTypeRepository;
import ru.valeevaz.requizitufabot.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    public GameEntity getGameId(Integer id) throws NoSuchElementException {
        return gameRepository.findById(id).get();
    }

    public List<GameEntity> getAllGames(){
        List<GameEntity> gameEntities = gameRepository.findAll();
        return gameEntities;
    }

    public List<GameEntity> getAllActiveGames(){
        LocalDateTime dateNow = LocalDateTime.now();
        List<GameEntity> gameEntities = gameRepository.getAllActiveGames(dateNow);
        return gameEntities;
    }
}
