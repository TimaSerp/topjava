package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.RoleWithId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final BeanPropertyRowMapper<RoleWithId> ROLE_ROW_MAPPER = BeanPropertyRowMapper.newInstance(RoleWithId.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(@NotNull JdbcTemplate jdbcTemplate, @NotNull NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, user.getId());
                    for (Role role : user.getRoles()) {
                        ps.setString(2, role.name());
                    }
                }

                @Override
                public int getBatchSize() {
                    return user.getRoles().size();
                }
            });
        } else if (namedParameterJdbcTemplate.update("""
                UPDATE users SET name=:name, email=:email, password=:password,
                registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id 
                """, parameterSource)
                + namedParameterJdbcTemplate.update("""
                UPDATE user_roles r SET role=:roles[0].name WHERE user_id=:id
                """, parameterSource)
                + (user.getRoles().size() == 2 ?
                namedParameterJdbcTemplate.update("UPDATE user_roles r SET role=:roles[1].name WHERE user_id=:id", parameterSource) : 0) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(@Positive int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) + jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id) != 0;
    }

    @Override
    public User get(@Positive int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE users.id = ?", ROW_MAPPER, id);
        List<String> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", String.class, id);
        User user = getSingleResult(users);
        if (user != null) {
            EnumSet<Role> enumRoles = EnumSet.noneOf(Role.class);
            for (String role : roles) {
                enumRoles.add(Role.valueOf(role));
            }
            user.setRoles(enumRoles);
        }
        return user;
    }

    @Override
    public User getByEmail(@Email String email) {
        return getSingleResult(jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ur on users.id = ur.user_id " +
                "WHERE email=?", ROW_MAPPER, email));
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        List<RoleWithId> rolesWithId = jdbcTemplate.query("SELECT user_id, role FROM user_roles", ROLE_ROW_MAPPER);
        Map<Integer, Role> roles = new HashMap<>();
        for (RoleWithId role: rolesWithId) {
            roles.put(role.getUserId(), Role.valueOf(role.getRole()));
        }
        for (User user: users) {
            int id = user.id();
            Role role = roles.get(id);
            if (role != null) {
                user.addRole(role);
            }
        }
////        List<Integer> ids = jdbcTemplate.queryForList("SELECT user_id FROM user_roles ORDER BY user_id", Integer.class);
////        List<String> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles ORDER BY user_id", String.class);
//        for (User user: users) {
//            int id = user.id();
//            if (ids.contains(id)) {
//                user.setRoles(List.of(Role.valueOf(roles.get(ids.indexOf(id)))));
//                if (ids.indexOf(id) != ids.lastIndexOf(id)) {
//                    user.addRole(Role.valueOf(roles.get(ids.lastIndexOf(id))));
//                }
//            } else {
//                user.setRoles(Collections.emptyList());
//            }
//        }
        return users;
    }

    private User getSingleResult(List<User> users) {
        if (!users.isEmpty()) {
            User user = users.get(0);
            if (users.size() > 1) {
                Set<Role> roles = new HashSet<>();
                for (User currentUser : users) {
                    roles.addAll(currentUser.getRoles());
                }
                user.setRoles(roles);
            }
            return user;
        }
        return null;
    }
}
