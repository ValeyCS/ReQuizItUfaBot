package ru.valeevaz.requizitufabot.entity;

import jakarta.persistence.*;
import ru.valeevaz.requizitufabot.enums.StatusEnum;

import java.time.LocalDateTime;

@Entity(name = "records")
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "telegram_user_id")
    private Long telegramUserId;
    @Column(name = "recorder_name")
    private String recordedName;
    @Column(name = "recorder_phone")
    private String recordedPhone;
    @Column(name = "recorder_team")
    private String recordedTeam;
//    @ManyToOne
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    @Column(name = "chat_id")
    private Long chatId;
    @ManyToOne
    private GameEntity game;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getRecordedName() {
        return recordedName;
    }

    public void setRecordedName(String recordedName) {
        this.recordedName = recordedName;
    }

    public String getRecordedPhone() {
        return recordedPhone;
    }

    public void setRecordedPhone(String recordedPhone) {
        this.recordedPhone = recordedPhone;
    }

    public String getRecordedTeam() {
        return recordedTeam;
    }

    public void setRecordedTeam(String recordedTeam) {
        this.recordedTeam = recordedTeam;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }
}
