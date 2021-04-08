package bus.persistence.mysql.dao;

import bus.persistence.dao.IUserDao;
import bus.persistence.domain.User;
import io.github.harishb2k.easy.database.mysql.IMysqlHelper;
import io.github.harishb2k.easy.database.mysql.IMysqlHelper.IRowMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class UserDaoImpl implements IUserDao {
    private final IMysqlHelper mysqlHelper;
    private final IRowMapper<User> userIRowMapper;

    @Inject
    public UserDaoImpl(IMysqlHelper mysqlHelper) {
        this.mysqlHelper = mysqlHelper;
        this.userIRowMapper = new UserRowMapper();
    }

    @Override
    public void persist(User user) {
        Long id = mysqlHelper.persist(
                "user_persist",
                "INSERT INTO users(name) VALUES(?);",
                statement -> {
                    statement.setString(1, user.getName());
                }
        );
        user.setId(id);
        log.debug("Saved user={}", user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return mysqlHelper.findOne(
                "user_find_by_id",
                "SELECT * FROM users WHERE id=?",
                statement -> {
                    statement.setLong(1, id);
                },
                userIRowMapper,
                User.class
        );
    }

    private static class UserRowMapper implements IRowMapper<User> {
        @Override
        public User map(ResultSet resultSet) throws SQLException {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            return user;
        }
    }
}