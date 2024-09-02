package fabricsdk;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        DavexService davexService = new DavexService();
//        Map<String, Object> result = davexService.updateData("85ds245", "xdsxx");
//        Map<String, Object> result2 = davexService.queryData("85245");
//        Map<String, Object> result3 = davexService.queryAllDatas();
//        Map<String, Object> result4 = davexService.uploadData("85245", "asdasd");

        try {
            SerializeDemo person = new SerializeDemo("John Doe", 30);

            Map<String, Object> result4 = davexService.uploadData("1", "asdasd");
            System.out.println(result4);
            // 序列化+ 上传数据
            String serializedPerson = SerializationUtil.serializeObjectToString(person);
            Map<String, Object> res1 = davexService.uploadData("demoData", serializedPerson);
            System.out.println("Serialized Person: " + serializedPerson);
            System.out.println(res1);
//            // 查询数据 + 反序列化
//            Map<String, Object> res2 = davexService.queryData("demoData");
//            String object = res2.get("payload").toString();
//            SerializeDemo deserializedPerson = (SerializeDemo) SerializationUtil.deserializeStringToObject(object);
//            System.out.println("Deserialized Person: " + deserializedPerson);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
