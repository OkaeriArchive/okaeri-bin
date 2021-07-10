package eu.okaeri.bin;

import lombok.Data;
import lombok.NonNull;

@Data
public class RefString implements Binable {

    public static final char T_MARKER = '\1';

    private String value;

    public static RefString of(@NonNull String value) {
        RefString ref = new RefString();
        ref.load(value);
        return ref;
    }

    public static RefString ofRaw(@NonNull String value) {
        RefString ref = new RefString();
        ref.value = value;
        return ref;
    }

    @Override
    public void load(@NonNull String value) {

        if (value.charAt(0) != T_MARKER) {
            throw new IllegalArgumentException("cannot use RefObject#load for non-marked object");
        }

        value = value.substring(1);
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
