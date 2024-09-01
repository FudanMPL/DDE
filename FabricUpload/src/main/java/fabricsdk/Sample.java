package fabricsdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Peer;
import org.yaml.snakeyaml.Yaml;
import org.hyperledger.fabric.gateway.Contract;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.EnumSet;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Sample {
    public  static void main(String[] args) throws Exception {

            Properties prop = new Properties();

            //系统找到了指定路径
            InputStream input = new FileInputStream("./FabricUpload/src/main/java/config/config.properties");
            prop.load(input);

            String walletPath = prop.getProperty("walletPath");
            String networkConfigPath = prop.getProperty("networkConfigPath");
            System.out.println(networkConfigPath);
            Path walletDirectory = Paths.get(walletPath);
            Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);
            System.out.println(wallet);
            Gateway.Builder builder = Gateway.createBuilder()
                    .identity(wallet, "appUser")
                    .networkConfig(Paths.get(networkConfigPath));

            //连接网关
            Gateway gateway = builder.connect();

            Network network = gateway.getNetwork("fsims-channel");
            System.out.println("network: " + network);
            Contract contract = network.getContract("jr");

            System.out.println("*****************************************************");
            System.out.println(network.getChannel().getPeers());
            System.out.println("*****************************************************");
            byte[] queryById = contract.evaluateTransaction("QueryData", "6666");
            System.out.println("ID："+new String(queryById, StandardCharsets.UTF_8));


            byte[] queryAllAssets = contract.createTransaction("StoreRequestOperation").setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER))).submit("7777", "grpcsd");
            System.out.println("所有资产："+new String(queryAllAssets, StandardCharsets.UTF_8));
    }
}
