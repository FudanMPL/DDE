package fabricsdk;


import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
        try {
            byte[] queryData = contract.evaluateTransaction("QueryData", key);

            if (queryData != null) {
                result.put("payload", StringUtils.newStringUtf8(queryData));
                result.put("status", "ok");
                return result;
            }
        }catch (ContractException e){
            result.put("status", "error");
            result.put("payload", "keyNotExist");
        }
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
        try {
            // 先查询链上是否存在key, 若存在
            String existingData = StringUtils.newStringUtf8(contract.evaluateTransaction("QueryData", key));
            if(existingData != null && ! existingData.isEmpty()){
                result.put("status", "error");
                result.put("payload", existingData);
            }
        } catch (ContractException e) {
            byte[] tx = contract.createTransaction("StoreRequestOperation")
                    .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                    .submit(key, data);
            result.put("payload", StringUtils.newStringUtf8(contract.evaluateTransaction("QueryData", key)));
            result.put("status", "ok");
        }
        return result;
    }

    public Map<String, Object> updateData(String key, String data) throws GatewayException, InterruptedException, TimeoutException {
        Network network = gateway.getNetwork(CHANNEL_NAME);
        Map<String, Object> result = Maps.newConcurrentMap();

        try {
            // 查询链上是否存在该key
            String existingData = StringUtils.newStringUtf8(contract.evaluateTransaction("QueryData", key));

            if (existingData == null || existingData.isEmpty()) {
                // 如果key不存在，返回错误状态
                result.put("status", "error");
                result.put("payload", "keyNotExist");
            } else {
                // 如果key存在，执行更新操作
                byte[] ans = contract.createTransaction("UpdateData")
                        .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                        .submit(key, data);

                // 更新成功后查询最新数据并返回成功状态
                result.put("status", "ok");
                result.put("payload", StringUtils.newStringUtf8(contract.evaluateTransaction("QueryData", key)));
            }
        } catch (ContractException e) {
            // 处理异常并标记为错误
            result.put("status", "error");
            result.put("payload", "keyNotExist");
        }

        return result;
    }
}
