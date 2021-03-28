package eu.okaeri.bin;

import lombok.Data;

@Data
public class RefString implements Binable {

    public static final String T_MARKER = "\0\0";

    private String value;

    public static RefString of(String value) {
        RefString ref = new RefString();
        ref.load(value);
        return ref;
    }

    public static RefString ofRaw(String value) {
        RefString ref = new RefString();
        ref.value = value;
        return ref;
    }

    @Override
    public void load(String value) {

        String marker = value.substring(0, 2);
        if (!T_MARKER.equals(marker)) {
            throw new IllegalArgumentException("cannot use RefObject#load for non-marked object");
        }

        value = value.substring(2);
        this.value = value
                .replaceAll("(?<!\\\\)\\\\n", "\n")
                .replace("\\\\n", "\\n");
    }

    @Override
    public String render() {
        return T_MARKER + this.value
                .replace("\\n", "\\\\n")
                .replace("\n", "\\n");
    }
}
