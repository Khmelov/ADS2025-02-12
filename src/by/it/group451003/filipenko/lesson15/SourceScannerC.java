package by.it.group451003.filipenko.lesson15;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) throws IOException {
        Path src = Path.of(System.getProperty("user.dir"), "src");

        // Просто собираем все пути
        List<String> paths = new ArrayList<>();
        try (var walk = Files.walk(src)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                paths.add(src.relativize(p).toString());
                            }
                        } catch (IOException e) {
                            // Игнорируем
                        }
                    });
        }

        // Просто сортируем и выводим (минимальная реализация)
        Collections.sort(paths);
        for (String path : paths) {
            System.out.println(path);
        }
    }
}