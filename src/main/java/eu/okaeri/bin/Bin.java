package eu.okaeri.bin;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Bin {

    public static final String T_MARKER_RAW = "\0\0";
    public static final String T_VALUE_SEPARATOR = "\0";
    public static final String T_ELEMENT_SEPARATOR = "\n";

    private Map<String, Object> data = new LinkedHashMap<>();

    public static Bin of(Map<String, Object> data) {
        Bin bin = new Bin();
        bin.data = data;
        return bin;
    }

    public void put(String key, String value) {
        this.data.put(key, value);
    }

    public void put(String key, Collection<?> value) {
        this.data.put(key, value);
    }

    public void put(String key, Map<?, ?> value) {
        this.data.put(key, value);
    }

    public Object get(String key) {
        return this.data.get(key);
    }

    public String getString(String key) {
        return (String) this.get(key);
    }

    @SuppressWarnings("unchecked")
    public List<String> getList(String key) {
        return (List<String>) this.get(key);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getMap(String key) {
        return (Map<String, String>) this.get(key);
    }

    public String write() {

        StringBuilder buf = new StringBuilder();
        Mapper mapper = Mapper.of(this.data);

        for (Map.Entry<Binable, Ref> entry : mapper.getRefMap().entrySet()) {

            Binable value = entry.getKey();
            Ref ref = entry.getValue();

            buf.append(ref.render()).append(T_VALUE_SEPARATOR).append(value.render()).append(T_ELEMENT_SEPARATOR);
        }

        return buf.toString();
    }

    public void load(String data) {

        String[] elements = data.split(T_ELEMENT_SEPARATOR);
        Map<Ref, Binable> refMap = new LinkedHashMap<>();
        Ref lastRef = null;

        for (String element : elements) {

            String[] keyValuePair = element.split(T_VALUE_SEPARATOR, 2);
            if (keyValuePair.length != 2) {
                throw new IllegalArgumentException("cannot parse entry: " + element);
            }

            String key = keyValuePair[0];
            String value = keyValuePair[1];

            if (value.length() < 2) {
                throw new IllegalArgumentException("broken entry: " + element);
            }

            Binable refValue;
            String marker = value.substring(0, 2);

            switch (marker) {
                case RefList.T_MARKER:
                    refValue = RefList.of(value);
                    break;
                case RefMap.T_MARKER:
                    refValue = RefMap.of(value);
                    break;
                case RefString.T_MARKER:
                    refValue = RefString.of(value);
                    break;
                default:
                    throw new IllegalArgumentException("uknown entry: " + element);
            }

            Ref ref = Ref.of(key);
            refMap.put(ref, refValue);
            lastRef = ref;
        }

        this.data = Demapper.of(refMap, lastRef).dereference();
    }

    private static class Demapper {

        private Map<Ref, Binable> refMap = new LinkedHashMap<>();
        private Map<String, Binable> result = new LinkedHashMap<>();
        private Ref lastRef;

        public static Demapper of(Map<Ref, Binable> reversedRefMap, Ref lastRef) {
            Demapper demapper = new Demapper();
            demapper.refMap = reversedRefMap;
            demapper.lastRef = lastRef;
            return demapper;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> dereference() {
            RefMap object = (RefMap) this.refMap.get(this.lastRef);
            return (Map<String, Object>) this.dereference(object, this.refMap);
        }

        private Object dereference(Object object, Map<Ref, Binable> refMap) {
            if (object instanceof RefList) {
                return ((RefList) object).getList().stream()
                        .map(element -> this.dereference(element, refMap))
                        .collect(Collectors.toList());
            } else if (object instanceof RefMap) {
                Map<Ref, Ref> map = ((RefMap) object).getMap();
                Map<Object, Object> result = new LinkedHashMap<>();
                for (Map.Entry<Ref, Ref> entry : map.entrySet()) {
                    result.put(this.dereference(entry.getKey(), refMap), this.dereference(entry.getValue(), refMap));
                }
                return result;
            } else if (object instanceof RefString) {
                return this.dereference(((RefString) object).getValue(), refMap);
            }
            else if (object instanceof Ref) {
                return this.dereference(refMap.get(object), refMap);
            }
            return object;
        }
    }

    @Data
    private static class Mapper {

        private Map<Binable, Ref> refMap = new LinkedHashMap<>();
        private int counter = 0;

        public static Mapper of(Object object) {
            Mapper mapper = new Mapper();
            mapper.map(object);
            return mapper;
        }

        @SuppressWarnings("unchecked")
        public Ref map(Object object) {

            if (object instanceof Ref) {
                return (Ref) object;
            }

            if (object instanceof Map) {
                Map<Ref, Ref> result = new LinkedHashMap<>();
                Map<Object, Object> map = (Map<Object, Object>) object;
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    result.put(this.map(entry.getKey()), this.map(entry.getValue()));
                }
                object = RefMap.of(result);
            } else if (object instanceof Collection) {
                object = ((Collection<?>) object).stream()
                        .map(this::map)
                        .collect(Collectors.toList());
                object = RefList.of((Collection<Ref>) object);
            } else if (object instanceof String) {
                object = RefString.ofRaw((String) object);
            } else {
                throw new IllegalArgumentException("cannot map type: " + object.getClass());
            }

            Ref ref = this.refMap.get(object);
            if (ref != null) {
                return ref;
            }

            ref = Ref.of(this.counter++);
            this.refMap.put((Binable) object, ref);

            return ref;
        }
    }
}
