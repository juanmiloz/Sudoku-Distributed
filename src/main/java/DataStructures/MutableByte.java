package DataStructures;

import java.io.Serializable;

public class MutableByte implements Serializable {
    public byte value;

    public MutableByte(byte i) {
        value = Byte.parseByte(Byte.toString(i));
    }

    @Override
    public String toString() {
        return Byte.toString(value);
    }
}
