package fabricsdk;

import org.hyperledger.fabric.gateway.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

public class FabricSDK {
    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream("C:\\Users\\Administrator\\AggregateGraph\\pythonProject\\DVE\\FabricUpload\\src\\main\\java\\config\\config.properties");
        prop.load(input);


        String certificatePath = prop.getProperty("certificatePath");
        X509Certificate certificate = readX509Certificate(Paths.get(certificatePath));
        String privateKeyPath = prop.getProperty("privateKeyPath");
        PrivateKey privateKey = getPrivateKey(Paths.get(privateKeyPath));
        Wallet wallet = Wallets.newInMemoryWallet();
        wallet.put("appUser", Identities.newX509Identity("Org1Msp", certificate, privateKey));

//        Gateway.Builder builder = Gateway.createBuilder()
//                .identity(wallet, "appUser")
//                .networkConfig(Paths.get(networkConfigPath));
//        Gateway gateway = builder.connect();
//        System.out.println(gateway);
//        Network network = gateway.getNetwork("fsims-channel");
//        Contract contract = network.getContract("jr");

        Path networkConfigPath = Paths.get("C:\\Users\\Administrator\\AggregateGraph\\pythonProject\\DVE\\FabricUpload\\src\\main\\java\\config\\fabric_connection.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath);
        Gateway gateway = builder.connect();
        Network network = gateway.getNetwork("fsims-channel");
    }

    private static X509Certificate readX509Certificate(final Path certificatePath) throws CertificateException, IOException {
        try (Reader reader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)){
            return Identities.readX509Certificate(reader);
        }
    }

    private static  PrivateKey getPrivateKey(final Path privateKeyPath) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)){
            return Identities.readPrivateKey(privateKeyReader);
        } catch (IOException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
