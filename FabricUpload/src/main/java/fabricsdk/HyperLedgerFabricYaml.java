package fabricsdk;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fabric")
@Data

public class HyperLedgerFabricYaml {
    String networkConnectionConfigPath;
    String certificatePath;
    String privateKeyPath;
}
