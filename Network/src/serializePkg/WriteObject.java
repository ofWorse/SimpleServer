package serializePkg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteObject {
    private Object object;

    public WriteObject(Object object) {
        this.object = object;
        writeObject();
    }

    private void writeObject() {
        try (var oos = new ObjectOutputStream(new FileOutputStream("msg.bin"))) {
            oos.writeObject(this.object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
