package fabricsdk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;


public class SerializationUtil {

    // 序列化对象为字节数组，再将字节数组编码为Base64字符串
    public static String serializeObjectToString(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    // 将字符串反序列化为对象
    public static Object deserializeStringToObject(String str) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(str);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    // 将链上查询结果反序列化为对象
    public static SerializeDemo mapToObject(Map<String, Object> map) throws IOException, ClassNotFoundException {
        String payload = map.get("payload").toString();
        JSONObject obj = JSONObject.parseObject(payload);

        //提取value字段
        String value = obj.getString("value");
        return (SerializeDemo) SerializationUtil.deserializeStringToObject(value);
    }

    // 若查询链上所有存储结果调用此接口
    public static SerializeDemo[] mapToObjects(Map<String, Object> map) throws IOException, ClassNotFoundException {
        String payload = map.get("payload").toString();
        JSONArray jsonArray = JSONArray.parseArray(payload);

        SerializeDemo[] deserializedObjects = new SerializeDemo[jsonArray.size()];

        // 遍历JSONArray，反序列化每个对象并存储在数组中
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String value = obj.getString("value");
            System.out.println(value);
            // 反序列化字符串为SerializeDemo对象
            SerializeDemo person = (SerializeDemo) SerializationUtil.deserializeStringToObject(value);
            deserializedObjects[i] = person; // 将对象存储到数组中
        }

        // 返回包含所有反序列化对象的数组
        return deserializedObjects;
    }
}
