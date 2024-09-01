package fabricsdk;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.springframework.validation.ObjectError;

import java.util.Map;

public class DavexService {
    private final Gateway gateway;
    private final Contract contract;
    private final DavexController davexController;

    public DavexService() throws Exception{
        HyperLedgerConfig hyperLedgerConfig = new HyperLedgerConfig();
        this.gateway = hyperLedgerConfig.gateway();
        this.contract = hyperLedgerConfig.davex(gateway);
        this.davexController = new DavexController(gateway, contract);
    }

    public Map<String, Object> uploadData(String key, String value) throws Exception{
        return davexController.addData(key, value);
    }

    public Map<String, Object> queryData(String key) throws Exception{
        return davexController.queryDataByKey(key);
    }

    public Map<String, Object> queryAllDatas() throws Exception{
        return davexController.queryAll();
    }

    public Map<String, Object> updateData(String key, String value) throws Exception{
        return davexController.updateData(key, value);
    }
}
