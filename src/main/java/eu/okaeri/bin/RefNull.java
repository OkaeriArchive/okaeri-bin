package eu.okaeri.bin;

import lombok.Data;

@Data
public class RefNull implements Binable {

    public static final char T_MARKER = '\4';
    public static final RefNull INSTANCE = new RefNull();

    @Override
    public void load(String value) {

        if (value.charAt(0) == T_MARKER) {
            return;
        }

        throw new IllegalArgumentException("cannot use RefNull#load for non-marked object");
    }

    @Override
    public String render() {
        return String.valueOf(T_MARKER);
    }
}
