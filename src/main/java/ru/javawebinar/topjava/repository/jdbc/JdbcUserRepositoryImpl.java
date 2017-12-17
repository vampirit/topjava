package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcUserRepositoryImpl extends JdbcTramsactionManager implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final PlatformTransactionManager transactionManager;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate,
                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  PlatformTransactionManager transactionManager) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

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
                transactionManager.commit(txStatus);
            } else if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                transactionManager.commit(txStatus);
                return null;
            }
            return user;
        }catch (TransactionException ex){
            transactionManager.rollback(txStatus);
            throw ex;
        }
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
            List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
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
            List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
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
            List<User> query = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
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
