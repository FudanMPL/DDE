package fabricsdk;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;

import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        DavexService davexService = new DavexService();
        Map<String, Object> result = davexService.updateData("85245", "xsxx");
        System.out.println(result);
    }
}
