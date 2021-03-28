package eu.okaeri.bin;

import lombok.Data;

@Data
public class Ref implements Binable {

    private long value;

    public static Ref of(String value) {
        Ref ref = new Ref();
        ref.load(value);
        return ref;
    }

    public static Ref of(int value) {
        Ref ref = new Ref();
        ref.value = value;
        return ref;
    }

    @Override
    public void load(String value) {
        this.value = Long.parseLong(value);
    }

    @Override
    public String render() {
        return String.valueOf(this.value);
    }
}
