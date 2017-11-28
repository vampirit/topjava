package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "meals")
@NamedQueries({
        @NamedQuery(name = Meal.GET_ALL, query = "SELECT meal FROM Meal meal JOIN meal.user user WHERE user.id=:userId order by meal.dateTime desc"),
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal meal where meal in(" +
                "select meal From Meal meal JOIN meal.user user where meal.id=:mealId AND user.id=:userId)"),
        @NamedQuery(name = Meal.GET, query = "SELECT meal from Meal meal WHERE meal.id=:mealId"),
        @NamedQuery(name = Meal.GET_BETWEEN, query = "SELECT meal FROM Meal meal JOIN meal.user user " +
                "WHERE user.id=:userId AND meal.dateTime>=:startDay AND meal.dateTime<=:endDay order by meal.dateTime desc")
})
public class Meal extends AbstractBaseEntity {

    public static final String GET_ALL = "getAll";
    public static final String DELETE = "deleteMeal";
    public static final String GET = "get";
    public static final String GET_BETWEEN = "getBetween";


    @Column(name = "date_time")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "calories")
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
