package eu.okaeri.bin.benchmark;

import com.google.gson.Gson;
import eu.okaeri.bin.Bin;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("FieldNamingConvention")
public class BinBenchmark {

    private static final Gson GSON = new Gson();

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    /* OKAERI-BIN */
    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void read_tiny_bin(Blackhole blackhole, Data data) {
        Bin bin = new Bin();
        bin.load(data.tinyBin);
        blackhole.consume(bin);
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void write_tiny_bin(Blackhole blackhole, Data data) {
        String out = Bin.of(data.tiny).write();
        blackhole.consume(out);
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void read_simple_bin(Blackhole blackhole, Data data) {
        Bin bin = new Bin();
        bin.load(data.simpleBin);
        blackhole.consume(bin);
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void write_simple_bin(Blackhole blackhole, Data data) {
        String out = Bin.of(data.simple).write();
        blackhole.consume(out);
    }

    /* GSON */
    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void read_tiny_gson(Blackhole blackhole, Data data) {
        Map map = GSON.fromJson(data.tinyGson, Map.class);
        blackhole.consume(map);
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void write_tiny_gson(Blackhole blackhole, Data data) {
        String out = GSON.toJson(data.tiny);
        blackhole.consume(out);
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void read_simple_gson(Blackhole blackhole, Data data) {
        Map map = GSON.fromJson(data.simpleGson, Map.class);
        blackhole.consume(map);
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void write_simple_gson(Blackhole blackhole, Data data) {
        String out = GSON.toJson(data.simple);
        blackhole.consume(out);
    }

    @State(Scope.Benchmark)
    public static class Data {

        public Map<String, Object> tiny = new LinkedHashMap<>();
        public String tinyBin = Bin.of(this.tiny).write();
        public String tinyGson = GSON.toJson(this.tiny);
        public Map<String, Object> simple = new LinkedHashMap<>();
        public String simpleBin = Bin.of(this.simple).write();
        public String simpleGson = GSON.toJson(this.simple);

        {
            this.tiny.put("one", "value");
            this.tiny.put("second", "2");
        }

        {
            this.simple.put("test", "testing testing testing");
            this.simple.put("test1", "testing testing testing");
            this.simple.put("test2", "testing testing testing");
            this.simple.put("test3", "testing testing testing");
            this.simple.put("hello-hello", "123");
            this.simple.put("hello-hello2", "234");
            this.simple.put("hello-hello3", "504");
        }
    }
}
