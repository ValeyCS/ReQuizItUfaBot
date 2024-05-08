package ru.valeevaz.requizitufabot.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "games")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    private GamesTypeEntity gamesType;
    private String name;
    private String description;
    @Column(name = "date_game")
    private LocalDateTime dateGame;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @ManyToOne
    private LocationEntity location;
    private Long price;
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<RecordEntity> records;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GamesTypeEntity getGamesType() {
        return gamesType;
    }

    public void setGamesType(GamesTypeEntity gamesType) {
        this.gamesType = gamesType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateGame() {
        return dateGame;
    }

    public void setDateGame(LocalDateTime dateGame) {
        this.dateGame = dateGame;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createDate) {
        this.createdDate = createDate;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecordEntity> getRecords() {
        return records;
    }

    public void setRecords(List<RecordEntity> records) {
        this.records = records;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(gamesType, that.gamesType) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(dateGame, that.dateGame) && Objects.equals(isActive, that.isActive) && Objects.equals(createdDate, that.createdDate) && Objects.equals(location, that.location) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gamesType, name, description, dateGame, isActive, createdDate, location, price);
    }
}
