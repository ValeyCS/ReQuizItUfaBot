package ru.valeevaz.requizitufabot.service;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.valeevaz.requizitufabot.entity.RecordEntity;
import ru.valeevaz.requizitufabot.repository.RecordRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ru.valeevaz.requizitufabot.enums.StatusEnum.CANCEL;

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

    public void deleteRecord(Long telegramUserId, Integer gameId){
        var recordEntity = recordRepository.findByTelegramUserIdAndGameId(telegramUserId, gameId);
        try {
            recordRepository.delete(recordEntity.get());
        }catch (NoSuchElementException e){
            log.error("Ошибка при удалении записи с идентификатором игры" + gameId + " :" + e.getMessage());
        }
    }

    public void cancelRecord(Long telegramUserId, Integer gameId){
        try {
            var recordEntity = recordRepository.findByTelegramUserIdAndGameId(telegramUserId, gameId).get();
            var recordEntitySave = recordEntity.toBuilder().status(CANCEL).canDelete(false).build();
            recordRepository.save(recordEntitySave);
        }catch (NoSuchElementException e){
            log.error("Ошибка при попытке отменить запись с идентификатором игры" + gameId + " :" + e.getMessage());
        }
    }

}
