package bus.persistence.mysql.dao;

import bus.persistence.dao.IUserDao;
import bus.persistence.mysql.base.PersistenceMySqlBaseTestCase;
import org.junit.Assert;

public class UserDaoImplTest extends PersistenceMySqlBaseTestCase {
    private IUserDao userDao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        userDao = injector.getInstance(IUserDao.class);
    }

    public void testUserPersist() {
        // Step 1 - Insert to DB
        Long id = mysqlHelper.persist(
                "",
                "INSERT INTO users(name) VALUES(?)",
                preparedStatement -> {
                    preparedStatement.setString(1, "Some User");
                }
        );
        Assert.assertNotNull(id);
        Assert.assertTrue(id > 0);

        // Step 2 - Read from DB
        String result = mysqlHelper.findOne(
                "",
                "SELECT name from users WHERE id=?",
                statement -> {
                    statement.setLong(1, id);
                },
                rs -> rs.getString(1),
                String.class
        ).orElse("");
        Assert.assertEquals("Some User", result);
    }
}