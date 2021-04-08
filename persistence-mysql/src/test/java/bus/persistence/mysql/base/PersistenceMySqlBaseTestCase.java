package bus.persistence.mysql.base;

import com.google.inject.AbstractModule;
import bus.persistence.base.PersistenceBaseTestCase;
import bus.persistence.mysql.module.PersistenceMySqlModule;
import io.github.harishb2k.easy.database.IDatabaseService;
import io.github.harishb2k.easy.database.mysql.IMysqlHelper;
import io.github.harishb2k.easy.database.mysql.config.MySqlConfig;
import io.github.harishb2k.easy.database.mysql.config.MySqlConfigs;
import io.github.harishb2k.easy.database.mysql.module.DatabaseMySQLModule;

import java.util.List;

public abstract class PersistenceMySqlBaseTestCase extends PersistenceBaseTestCase {
    protected IDatabaseService databaseService;
    protected IMysqlHelper mysqlHelper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void setupDatabase() {
        super.setupDatabase();
        databaseService = injector.getInstance(IDatabaseService.class);
        databaseService.startDatabase();
        mysqlHelper = injector.getInstance(IMysqlHelper.class);
    }

    @Override
    protected List<AbstractModule> getModuleList() {
        List<AbstractModule> modules = super.getModuleList();
        modules.add(new PersistenceMySqlModule());
        modules.add(new DatabaseMySQLModule());
        modules.add(new AbstractModule() {
            @Override
            protected void configure() {
                MySqlConfig dbConfig = new MySqlConfig();
                dbConfig.setDriverClassName("com.mysql.jdbc.Driver");
                dbConfig.setJdbcUrl(persistenceTestContext.getJdbcUrl());
                dbConfig.setUsername(persistenceTestContext.getMysqlUser());
                dbConfig.setPassword(persistenceTestContext.getMysqlPassword());
                MySqlConfigs mySqlConfigs = new MySqlConfigs();
                mySqlConfigs.addConfig(dbConfig);
                bind(MySqlConfigs.class).toInstance(mySqlConfigs);
            }
        });
        return modules;
    }

    public void testDummy() {
    }
}
