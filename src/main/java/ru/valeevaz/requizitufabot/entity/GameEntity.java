package ru.valeevaz.requizitufabot.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "games")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
//    @Column(name = "type_code")
//    private Long typeCode;
    @ManyToOne
    private GamesTypeEntity gamesType;
    private String name;
    private String description;
    @Column(name = "date_game")
    private LocalDate dateGame;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "create_date")
    private LocalDate createDate;
//    @Column(name = "location_code")
//    private Integer location;
    @ManyToOne
//    @JoinColumn(name = "location_code")
    private LocationEntity location;
    private Long price;

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

    public LocalDate getDateGame() {
        return dateGame;
    }

    public void setDateGame(LocalDate dateGame) {
        this.dateGame = dateGame;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(gamesType, that.gamesType) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(dateGame, that.dateGame) && Objects.equals(isActive, that.isActive) && Objects.equals(createDate, that.createDate) && Objects.equals(location, that.location) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gamesType, name, description, dateGame, isActive, createDate, location, price);
    }
}
