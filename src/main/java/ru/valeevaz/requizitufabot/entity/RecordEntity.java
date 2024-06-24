package ru.valeevaz.requizitufabot.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.valeevaz.requizitufabot.enums.StatusEnum;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Entity(name = "records")
@Builder(toBuilder = true)
@Data
//@Entity
@Setter(PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Data

//@NoArgsConstructor
//@AllArgsConstructor

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
    private Integer amount;
    //    @ManyToOne
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    @Column(name = "chat_id")
    private Long chatId;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;
    @Column(name = "can_delete")
    private boolean canDelete;


//    public boolean isCanDelete(boolean b) {
//        return canDelete;
//    }

}
