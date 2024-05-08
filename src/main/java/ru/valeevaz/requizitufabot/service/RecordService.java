package ru.valeevaz.requizitufabot.service;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.valeevaz.requizitufabot.entity.RecordEntity;
import ru.valeevaz.requizitufabot.repository.RecordRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public RecordEntity saveRecordGame(RecordEntity recordGame){
        return recordRepository.save(recordGame);
    }

//    public GameEntity getGameId(Integer id) throws NoSuchElementException {
//        return gameRepository.findById(id).get();
//    }

    public RecordEntity getActiveRecords(Long telegramUserId) throws NotFoundException{
        List<RecordEntity> gameEntities = recordRepository.getActiveRecords(telegramUserId);
        if (gameEntities.isEmpty()) {
            throw new NotFoundException();
        } else if (gameEntities.size() > 1) {
            log.info("Удаленны не завершенные записи для пользователя {}", telegramUserId);
            recordRepository.deleteOldRecords(telegramUserId);
        }
        Optional<RecordEntity> recordEntity = gameEntities.stream().findFirst();
        return recordEntity.get();
    }
//
//    public List<GameEntity> getAllActiveGames(){
//        LocalDateTime dateNow = LocalDateTime.now();
//        List<GameEntity> gameEntities = gameRepository.getAllActiveGames(dateNow);
//        return gameEntities;
//    }
}
