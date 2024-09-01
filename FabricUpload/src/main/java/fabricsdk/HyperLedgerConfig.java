package fabricsdk;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


@Configuration
@Slf4j

public class HyperLedgerConfig {

    private static final String CHANNEL_NAME = "fsims-channel";
    private static final String CONTRACT_NAME = "dave";

    @Bean
    public Gateway gateway() throws Exception{
        Properties properties = new Properties();

        //系统找到了指定路径
        InputStream input = new FileInputStream("./FabricUpload/src/main/java/config/config.properties");
        properties.load(input);
        String walletPath = properties.getProperty("walletPath");
        String networkConfigPath = properties.getProperty("networkConfigPath");
        System.out.println(networkConfigPath);
        Path walletDirectory = Paths.get(walletPath);
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "appUser")
                .networkConfig(Paths.get(networkConfigPath));

        //连接网关
        Gateway gateway = builder.connect();
        log.info("==================================================Connected to Fabric gateway==================================================");
        return gateway;
    }

    @Bean
    public Contract davex (Gateway gateway){
        Network network = gateway.getNetwork(CHANNEL_NAME);
        return network.getContract(CONTRACT_NAME);
    }
}
