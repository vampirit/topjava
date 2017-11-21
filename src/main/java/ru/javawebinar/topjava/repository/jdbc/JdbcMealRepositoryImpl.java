package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private JdbcTemplate template;

    private SimpleJdbcInsert insert;

    private NamedParameterJdbcTemplate namedTemplate;

    @Autowired
    public JdbcMealRepositoryImpl(JdbcTemplate template, DataSource dataSource, NamedParameterJdbcTemplate namedTemplate) {
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.namedTemplate = namedTemplate;
        this.template = template;
    }


    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("userId", userId);

        if (meal.isNew()) {
            Number number = insert.executeAndReturnKey(map);
            meal.setId(number.intValue());
        }else {
            int update = namedTemplate.update("UPDATE meals SET date_time=:dateTime, description=:description, " +
                    "calories=:calories WHERE id=:id AND user_id=:userId", map);
            if (update == 0)
                return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        int update = template.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId);
        return update > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> query = template.query("SELECT * FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId);
        if (query.isEmpty())
            return null;

        return query.get(0);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return template.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC ", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {

        return template.query("SELECT * FROM meals WHERE user_id=? AND date_time BETWEEN ? AND ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDate, endDate);
    }
}
