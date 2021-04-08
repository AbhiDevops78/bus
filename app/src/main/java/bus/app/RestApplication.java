package bus.app;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import bus.app.config.ApplicationConfig;
import bus.core.module.CoreModule;
import bus.external.module.ExternalServicesModule;
import bus.persistence.module.PersistenceModule;
import bus.persistence.mysql.module.PersistenceMySqlModule;
import bus.resources.UserResource;
import bus.resources.proto.UserProtoResource;
import io.dropwizard.setup.Environment;
import io.gitbub.harishb2k.easy.helper.ApplicationContext;
import io.gitbub.harishb2k.easy.helper.metrics.IMetrics;
import io.github.harishb2k.easy.app.dropwizard.BaseApplication;
import io.github.harishb2k.easy.app.dropwizard.healthcheck.ApplicationHealthCheck;
import io.github.harishb2k.easy.database.IDatabaseService;
import io.github.harishb2k.easy.database.mysql.config.MySqlConfigs;
import io.github.harishb2k.easy.database.mysql.module.DatabaseMySQLModule;
import io.github.harishb2k.easy.http.module.EasyHttpModule;
import io.github.harishb2k.easy.http.util.EasyHttp;
import io.github.harishb2k.easy.lock.IDistributedLockService;
import io.github.harishb2k.easy.lock.config.LockConfigs;
import io.github.harishb2k.easy.metrics.prometheus.PrometheusMetrics;

public class RestApplication extends BaseApplication<ApplicationConfig> {

    public static void main(String[] args) throws Exception {
        new RestApplication().run("server", args[0]);
    }

    /**
     * Enable protocol buffer support
     */
    @Override
    protected boolean enableProtobufSupport() {
        return true;
    }

    @Override
    public void run(ApplicationConfig applicationConfig, Environment environment) throws Exception {
        super.run(applicationConfig, environment);

        // Project module
        AbstractModule module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(IMetrics.class).to(PrometheusMetrics.class).in(Scopes.SINGLETON);
                bind(MySqlConfigs.class).toInstance(applicationConfig.getMySqlConfigs());
                bind(MetricRegistry.class).toInstance(environment.metrics());
                bind(LockConfigs.class).toInstance(applicationConfig.getLocks());
            }
        };

        // Add all other modules
        Injector injector = Guice.createInjector(
                module,
                new CoreModule(),
                new PersistenceModule(),
                new PersistenceMySqlModule(),
                new EasyHttpModule(),
                new ExternalServicesModule(),
                new DatabaseMySQLModule()
        );
        ApplicationContext.setInjector(injector);

        // Setup EasyHttp
        EasyHttp.setup(applicationConfig.getEasyHttpConfig());

        IDatabaseService databaseService = injector.getInstance(IDatabaseService.class);
        databaseService.startDatabase();

        // Setup lock service
        IDistributedLockService distributedLockService = injector.getInstance(IDistributedLockService.class);
        distributedLockService.initialize();

        // Register resources
        registerResource(applicationConfig, environment);

        // Register prometheus
        registerPrometheus(environment);

        // Setup health checks for app
        injector.getInstance(ApplicationHealthCheck.class).setupHealthChecks(environment);
    }

    private void registerResource(ApplicationConfig applicationConfig, Environment environment) {
        environment.jersey().register(ApplicationContext.getInstance(UserResource.class));
        environment.jersey().register(ApplicationContext.getInstance(UserProtoResource.class));
    }
}
