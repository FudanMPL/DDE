package fabricsdk;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.context.annotation.Configuration;


import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;


@Configuration
@Slf4j

public class HyperLedgerConfig {
    final HyperLedgerFabricYaml hyperLedgerFabricYaml;

    public HyperLedgerConfig(HyperLedgerFabricYaml hyperLedgerFabricYaml) {
        this.hyperLedgerFabricYaml = hyperLedgerFabricYaml;
    }

    @Bean
    public Gateway gateway() throws Exception{
        BufferedReader certificateReader = Files.newBufferedReader(Paths.get(hyperLedgerFabricYaml.getCertificatePath()), StandardCharsets.UTF_8);
        X509Certificate certificate = Identities.readX509Certificate(certificateReader);

        BufferedReader privateKeyReader = Files.newBufferedReader(Paths.get(hyperLedgerFabricYaml.getPrivateKeyPath()), StandardCharsets.UTF_8);

        PrivateKey privateKey = Identities.readPrivateKey(privateKeyReader);

        Wallet wallet = Wallets.newInMemoryWallet();
        wallet.put("user1", Identities.newX509Identity("Org1MSP", certificate, privateKey));

        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "user1")
                .networkConfig(Paths.get("./src/main/java/config/fabric_connection.yaml"));

        Gateway gateway = builder.connect();

        log.info("==================================================Connected to Fabric gateway==================================================");

        return gateway;
    }
}
