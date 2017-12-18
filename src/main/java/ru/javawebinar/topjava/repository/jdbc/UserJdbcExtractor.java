package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserJdbcExtractor implements ResultSetExtractor<List<User>> {

    @Nullable
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> users = new LinkedHashMap<>();
        while (rs.next()){
            int id = rs.getInt("id");
            if (users.containsKey(id) && rs.getString("role") != null){
                users.get(id).getRoles().add(Role.valueOf(rs.getString("role")));
            }else {
                User user = new User();
                user.setId(id);
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getDate("registered"));
                if (rs.getString("role") != null) {
                    Set<Role> roles = Collections.singleton(Role.valueOf(rs.getString("role")));
                    user.setRoles(roles);
                }
                users.put(id, user);
            }
        }
        return new ArrayList<>(users.values());
    }
}
