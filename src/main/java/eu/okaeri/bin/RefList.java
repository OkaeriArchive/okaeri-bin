package eu.okaeri.bin;

import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class RefList implements Binable {

    public static final char T_MARKER = '\2';
    public static final char T_ELEMENT_SEPARATOR = '\0';

    private Collection<Ref> list;

    public static RefList of(@NonNull String value) {
        RefList ref = new RefList();
        ref.load(value);
        return ref;
    }

    public static RefList of(@NonNull Collection<Ref> list) {
        RefList ref = new RefList();
        ref.list = list;
        return ref;
    }

    @Override
    public void load(@NonNull String value) {

        if (value.charAt(0) != T_MARKER) {
            throw new IllegalArgumentException("cannot use RefList#load for non-marked object");
        }

        value = value.substring(1);
        this.list = Arrays.stream(value.split(String.valueOf(T_ELEMENT_SEPARATOR)))
                .map(Ref::of)
                .collect(Collectors.toList());
    }

    @Override
    public String render() {
        return T_MARKER + this.list.stream()
                .map(Ref::render)
                .collect(Collectors.joining(String.valueOf(T_ELEMENT_SEPARATOR)));
    }
}
