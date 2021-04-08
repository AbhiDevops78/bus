package bus.external.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import bus.external.service.jsonplaceholder.IJsonPlaceholderService;
import bus.external.service.jsonplaceholder.impl.JsonPlaceholderServiceImpl;

public class ExternalServicesModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IJsonPlaceholderService.class).to(JsonPlaceholderServiceImpl.class).in(Scopes.SINGLETON);
    }
}
