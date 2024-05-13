package ru.valeevaz.requizitufabot.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.valeevaz.requizitufabot.entity.RecordEntity;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {

    @Query(value = "SELECT r FROM ru.valeevaz.requizitufabot.entity.RecordEntity r WHERE r.status  <> 'FINISH' and r.telegramUserId=:telegramUserId ORDER BY r.createdDate DESC")
    List<RecordEntity> getActiveRecords(@Param("telegramUserId") Long telegramUserId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
            DELETE FROM requizit_bot.records r2 
            WHERE r2.id != (select r1.id from requizit_bot.records r1 WHERE r1.telegram_user_id = :telegramUserId and r1.status!='FINISH' 
            order by r1.created_date desc LIMIT 1);""", nativeQuery = true)
    void deleteOldRecords(@Param("telegramUserId") Long telegramUserId);


}
