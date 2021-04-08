package bus.app.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.Configuration;
import io.github.harishb2k.easy.database.mysql.config.MySqlConfigs;
import io.github.harishb2k.easy.http.config.Config;
import io.github.harishb2k.easy.lock.config.LockConfigs;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationConfig extends Configuration {
    public MySqlConfigs mySqlConfigs;
    public Config easyHttpConfig;
    public LockConfigs locks;
}
