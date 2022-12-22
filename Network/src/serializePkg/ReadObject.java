package serializePkg;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadObject {

    public String readOb() {
        try (var ois = new ObjectInputStream(new FileInputStream("msg.bin"))) {
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
