package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcUserRepositoryImpl extends JdbcTramsactionManager implements UserRepository {

    private static final ResultSetExtractor<List<User>> ROW_MAPPER = new UserJdbcExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final PlatformTransactionManager transactionManager;
    private final SimpleJdbcInsert insertRoles;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate,
                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  PlatformTransactionManager transactionManager) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.insertRoles = new SimpleJdbcInsert(dataSource)
                .withTableName("user_roles");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        TransactionStatus txStatus = getTransactionStatus("User save transaction", false);
        try {
            if (user.isNew()) {
                Number newKey = insertUser.executeAndReturnKey(parameterSource);
                user.setId(newKey.intValue());
                insertBatchRoles(user.getId(), new ArrayList<>(user.getRoles()));
            } else {
                int update = namedParameterJdbcTemplate.update(
                        "UPDATE users SET name=:name, email=:email, password=:password, " +
                                "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
                if (update == 0){
                    transactionManager.commit(txStatus);
                    return null;
                }else {
                    jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
                    insertBatchRoles(user.getId(), new ArrayList<>(user.getRoles()));
                }
            }

            transactionManager.commit(txStatus);
            return user;
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    private void insertBatchRoles(final int userId, final List<Role> roles){
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setString(2, roles.get(i).name());
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                }
        );
    }

    @Override
    public boolean delete(int id) {
        TransactionStatus txStatus = getTransactionStatus("User delete transaction", false);
        try {
            int update = jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
            transactionManager.commit(txStatus);
            return update != 0;
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    public User get(int id) {
        TransactionStatus txStatus = getTransactionStatus("User get transaction", true);
        try {
            List<User> users = jdbcTemplate.query("" +
                    "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, r.role " +
                    "FROM users AS u LEFT JOIN user_roles AS r ON u.id=r.user_id " +
                    "WHERE u.id=?", ROW_MAPPER, id);
            transactionManager.commit(txStatus);
            return DataAccessUtils.singleResult(users);
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    public User getByEmail(String email) {
        TransactionStatus txStatus = getTransactionStatus("User getByEmail transaction", true);
        try {
    //        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
            List<User> users = jdbcTemplate.query(
                    "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, r.role " +
                        "FROM users AS u LEFT JOIN user_roles AS r ON u.id=r.user_id " +
                        "WHERE u.email=?", ROW_MAPPER, email);
            transactionManager.commit(txStatus);
            return DataAccessUtils.singleResult(users);
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
    }

    @Override
    public List<User> getAll() {
        TransactionStatus txStatus = getTransactionStatus("User getAll transaction", true);
        try {
            List<User> query = jdbcTemplate.query(
                    "SELECT u.id, u.name, u.email, u.password, u.enabled, u.registered, u.calories_per_day, r.role " +
                    "FROM users AS u LEFT JOIN user_roles AS r ON u.id=r.user_id " +
                    "ORDER BY u.name, u.email", ROW_MAPPER);
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
