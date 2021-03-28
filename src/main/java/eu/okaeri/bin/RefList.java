package eu.okaeri.bin;

import lombok.Data;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class RefList implements Binable {

    public static final String T_MARKER = "\0\1";
    public static final String T_ELEMENT_SEPARATOR = "\0";

    private Collection<Ref> list;

    public static RefList of(String value) {
        RefList ref = new RefList();
        ref.load(value);
        return ref;
    }

    public static RefList of(Collection<Ref> list) {
        RefList ref = new RefList();
        ref.list = list;
        return ref;
    }

    @Override
    public void load(String value) {

        String marker = value.substring(0, 2);
        if (!T_MARKER.equals(marker)) {
            throw new IllegalArgumentException("cannot use RefList#load for non-marked object");
        }

        value = value.substring(2);
        this.list = Arrays.stream(value.split(T_ELEMENT_SEPARATOR))
                .map(Ref::of)
                .collect(Collectors.toList());
    }

    @Override
    public String render() {
        return T_MARKER + this.list.stream()
                .map(Ref::render)
                .collect(Collectors.joining(T_ELEMENT_SEPARATOR));
    }
}
