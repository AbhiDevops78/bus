package bus.persistence.base;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import bus.persistence.base.PersistenceTestState.PersistenceTestContext;
import bus.persistence.module.PersistenceModule;
import io.gitbub.harishb2k.easy.helper.ApplicationContext;
import io.gitbub.harishb2k.easy.helper.metrics.IMetrics;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class PersistenceBaseTestCase extends TestCase {
    protected PersistenceTestContext persistenceTestContext;
    protected Injector injector;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        persistenceTestContext = PersistenceTestState.instance().getContext();
        persistenceTestContext.setLongs(new HashMap<>());
        persistenceTestContext.setUuids(new HashMap<>());
        persistenceTestContext.setStrings(new HashMap<>());
        setupLogging();
        setupPersistenceTestContext();
        setupGuice();
        setupDatabase();
        bootstrapDatabase();
    }

    protected void setupPersistenceTestContext() {
    }

    private void setupLogging() {
    }

    private void setupGuice() {
        injector = Guice.createInjector(getModuleList());
        ApplicationContext.setInjector(injector);
    }

    protected void setupDatabase() {
    }

    protected void bootstrapDatabase() {
    }

    protected List<AbstractModule> getModuleList() {
        List<AbstractModule> modules = new ArrayList<>();
        modules.add(new PersistenceModule());
        modules.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IMetrics.class).to(IMetrics.NoOpMetrics.class);
            }
        });
        return modules;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
