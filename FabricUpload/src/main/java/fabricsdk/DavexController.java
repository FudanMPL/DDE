package fabricsdk;


import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping
@Slf4j
@AllArgsConstructor
public class DavexController {
    private static final String CHANNEL_NAME = "fsims-channel";
    final Gateway gateway;
    final Contract contract;

    public Map<String, Object> queryDataByKey(String key) throws GatewayException {
        Map<String, Object> result = Maps.newConcurrentMap();
        byte[] queryData = contract.evaluateTransaction("QueryData", key);

        if (queryData != null) {
            result.put("payload", StringUtils.newStringUtf8(queryData));
            result.put("status", "ok");
            return result;
        }
        result.put("status", "error");
        result.put("payload", "");
        return result;
    }

    public Map<String, Object> queryAll() throws GatewayException {
        Map<String, Object> result = Maps.newConcurrentMap();
        byte[]  queryAll = contract.evaluateTransaction("GetAllAssets");

        result.put("payload", StringUtils.newStringUtf8(queryAll));
        result.put("status", "ok");
        return result;
    }

    public Map<String, Object> addData(String key, String data) throws GatewayException, TimeoutException, InterruptedException {
        Network network = gateway.getNetwork(CHANNEL_NAME);
        Map<String, Object> result = Maps.newConcurrentMap();
        byte[] tx = contract.createTransaction("StoreRequestOperation")
                .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(key, data);
        result.put("payload", StringUtils.newStringUtf8(contract.evaluateTransaction("QueryData", key)));
        result.put("status", "ok");
        return result;
    }

    public Map<String, Object> updateData(String key, String data) throws GatewayException, InterruptedException, TimeoutException {
        Network network = gateway.getNetwork(CHANNEL_NAME);
        byte[] ans = contract.createTransaction("UpdateData")
                .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(key, data);
        Map<String, Object> result = Maps.newConcurrentMap();
        result.put("status", "ok");
        result.put("payload", StringUtils.newStringUtf8(contract.evaluateTransaction("QueryData", key)));
        return result;
    }
}
