package fabricsdk;

import java.io.IOException;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
         DavexService davexService = new DavexService();
        Map<String, Object> result = davexService.updateData("85ds245", "xdsxx");
        Map<String, Object> result2 = davexService.queryData("85245");
        Map<String, Object> result3 = davexService.queryAllDatas();
        Map<String, Object> result4 = davexService.uploadData("85245", "asdasd");

        try {

            // 序列化上链
            SerializeDemo serializeDemo = new SerializeDemo("John Doe", 30);
            String serializedPerson = SerializationUtil.serializeObjectToString(serializeDemo);
            Map<String, Object> res1 = davexService.uploadData("demoData", serializedPerson);


            System.out.println("Serialized Person: " + serializedPerson);
            System.out.println(res1);

            SerializeDemo ans = SerializationUtil.mapToObject(res1);
            System.out.println(ans);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
