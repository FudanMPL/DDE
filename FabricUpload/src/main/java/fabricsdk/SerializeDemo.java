package fabricsdk;

import java.io.Serializable;

public class SerializeDemo implements Serializable {
    private String name;
    private int age;

    public SerializeDemo(String name, int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "SerializeDemo{" + "name=" + name + ", age=" + age + '}';
    }
}


