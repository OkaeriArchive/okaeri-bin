package eu.okaeri.bin;

import lombok.Data;

@Data
public class RefNull implements Binable {

    public static final String T_MARKER = "\0\3";
    public static final RefNull INSTANCE = new RefNull();

    @Override
    public void load(String value) {
        if (T_MARKER.equals(value)) {
            return;
        }
        throw new IllegalArgumentException("cannot use RefNull#load for non-marked object");
    }

    @Override
    public String render() {
        return T_MARKER;
    }
}
