package bus.persistence.mysql.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import bus.persistence.dao.IUserDao;
import bus.persistence.mysql.dao.UserDaoImpl;

public class PersistenceMySqlModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IUserDao.class).to(UserDaoImpl.class).in(Scopes.SINGLETON);
    }
}
