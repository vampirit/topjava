package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepositoryImpl extends JdbcTransactionManager implements MealRepository {

    private static final RowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    private final PlatformTransactionManager transactionManager;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate,
                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  PlatformTransactionManager transactionManager) {
        this.insertMeal = new SimpleJdbcInsert(dataSource)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", meal.getDateTime())
                .addValue("user_id", userId);

        TransactionStatus txStatus = getTransactionStatus("Save meal transaction", false);
        try {
            if (meal.isNew()) {
                Number newId = insertMeal.executeAndReturnKey(map);
                meal.setId(newId.intValue());
                transactionManager.commit(txStatus);
            } else {
                int update = namedParameterJdbcTemplate.update("" +
                                "UPDATE meals " +
                                "  SET description=:description, calories=:calories, date_time=:date_time " +
                                " WHERE id=:id AND user_id=:user_id"
                        , map);
                transactionManager.commit(txStatus);
                if (update == 0) {
                    return null;
                }
            }
            return meal;
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        TransactionStatus txStatus = getTransactionStatus("Delete meal transaction", false);
        try {
            int update = jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId);
            transactionManager.commit(txStatus);
            return update != 0;
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        TransactionStatus txStatus = getTransactionStatus("Get meal transaction", true);
        try {
            List<Meal> meals = jdbcTemplate.query(
                    "SELECT * FROM meals WHERE id = ? AND user_id = ?", ROW_MAPPER, id, userId);
            transactionManager.commit(txStatus);
            return DataAccessUtils.singleResult(meals);
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        TransactionStatus txStatus = getTransactionStatus("Get all meal transaction", true);
        try {
            List<Meal> query = jdbcTemplate.query(
                    "SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId);
            transactionManager.commit(txStatus);
            return query;
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        TransactionStatus txStatus = getTransactionStatus("Get all meal between dates transaction", true);
        try {
            List<Meal> query = jdbcTemplate.query(
                    "SELECT * FROM meals WHERE user_id=?  AND date_time BETWEEN  ? AND ? ORDER BY date_time DESC",
                    ROW_MAPPER, userId, startDate, endDate);
            transactionManager.commit(txStatus);
            return query;
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }
}
