package ru.valeevaz.requizitufabot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "users")
public class UserEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;
    @Column(name = "chat_id")
    private Long chatID;
    private String firstName;
    private String userName;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getChatID() {
        return chatID;
    }

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return chatID == that.chatID && Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(userName, that.userName) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatID, firstName, userName, dateCreated, dateUpdated);
    }
}
