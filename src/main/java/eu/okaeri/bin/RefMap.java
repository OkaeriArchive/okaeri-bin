package eu.okaeri.bin;

import lombok.Data;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class RefMap implements Binable {

    public static final char T_MARKER = '\3';
    public static final char T_VALUE_SEPARATOR = '\1';
    public static final char T_ELEMENT_SEPARATOR = '\0';

    private Map<Ref, Ref> map;

    public static RefMap of(@NonNull String value) {
        RefMap ref = new RefMap();
        ref.load(value);
        return ref;
    }

    public static RefMap of(@NonNull Map<Ref, Ref> map) {
        RefMap ref = new RefMap();
        ref.map = map;
        return ref;
    }

    @Override
    public void load(@NonNull String value) {

        if (value.charAt(0) != T_MARKER) {
            throw new IllegalArgumentException("cannot use refMap#load for non-marked object");
        }

        value = value.substring(1);
        Map<Ref, Ref> map = new LinkedHashMap<>();
        String[] elements = value.split(String.valueOf(T_ELEMENT_SEPARATOR));

        for (String element : elements) {
            String[] keyValuePair = element.split(String.valueOf(T_VALUE_SEPARATOR));
            map.put(Ref.of(keyValuePair[0]), Ref.of(keyValuePair[1]));
        }

        this.map = map;
    }

    @Override
    public String render() {
        return T_MARKER + this.map.entrySet().stream()
            .map((entry) -> entry.getKey().render() + T_VALUE_SEPARATOR + entry.getValue().render())
            .collect(Collectors.joining(String.valueOf(T_ELEMENT_SEPARATOR)));
    }
}
