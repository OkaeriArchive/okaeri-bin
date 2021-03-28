package eu.okaeri.bin;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class RefMap implements Binable {

    public static final String T_MARKER = "\0\2";
    public static final String T_VALUE_SEPARATOR = "\1";
    public static final String T_ELEMENT_SEPARATOR = "\0";

    private Map<Ref, Ref> map;

    public static RefMap of(String value) {
        RefMap ref = new RefMap();
        ref.load(value);
        return ref;
    }

    public static RefMap of(Map<Ref, Ref> map) {
        RefMap ref = new RefMap();
        ref.map = map;
        return ref;
    }

    @Override
    public void load(String value) {

        String marker = value.substring(0, 2);
        if (!T_MARKER.equals(marker)) {
            throw new IllegalArgumentException("cannot use refMap#load for non-marked object");
        }

        value = value.substring(2);
        Map<Ref, Ref> map = new LinkedHashMap<>();
        String[] elements = value.split(T_ELEMENT_SEPARATOR);

        for (String element : elements) {
            String[] keyValuePair = element.split(T_VALUE_SEPARATOR);
            map.put(Ref.of(keyValuePair[0]), Ref.of(keyValuePair[1]));
        }

        this.map = map;
    }

    @Override
    public String render() {
        return T_MARKER + this.map.entrySet().stream()
                .map((entry) -> entry.getKey().render() + T_VALUE_SEPARATOR + entry.getValue().render())
                .collect(Collectors.joining(T_ELEMENT_SEPARATOR));
    }
}
