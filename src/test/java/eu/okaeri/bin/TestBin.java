package eu.okaeri.bin;

import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

public class TestBin {

    @SneakyThrows
    public static void main(String[] args) {

        Bin bin = new Bin();
        bin.put("hello", "welcome everyone");
        bin.put("hiii", String.valueOf(12312));
        bin.put("list-of-values", Arrays.asList(String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4), String.valueOf(5)));
        bin.put("xxxxxxxx", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
        bin.put("nenenene", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
        bin.put("nananana", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
        bin.put("hackin", "\0\0hallo");

        Map<String, String> map = new LinkedHashMap<>();
        map.put("hackin", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
        map.put("hackin2", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
        bin.put("submap", map);

        String data = bin.write();
        writeFile(new File("test.obdf"), data);

        bin.load(data);
        bin.getData().forEach((key, value) -> {
            System.out.println(key.getClass() + " " + key);
            System.out.println(value.getClass() + " " + value);
            System.out.println();
        });
    }

    private static String readFile(File file) throws IOException {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append("\n");
            }
            return fileContents.toString();
        }
    }

    private static void writeFile(File file, String text) throws FileNotFoundException {
        try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
            out.print(text);
        }
    }
}
