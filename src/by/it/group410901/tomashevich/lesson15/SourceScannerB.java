// SourceScannerB.java
package by.it.group410901.tomashevich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class SourceScannerB extends SourceScannerA {
    public static void getInformation() throws IOException {
        ArrayList<String> size_directory = new ArrayList<>();
        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);
        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        if (directory.toString().endsWith(".java")) {
                            try {
                                String str = Files.readString(directory);
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {
                                    // Убираем комментарии и лишние пробелы
                                    str = str.replaceAll("/\\*[\\w\\W\r\n\t]*?\\*/", "")
                                            .replaceAll("//.*", "")
                                            .replaceAll("\\s+", " ")
                                            .trim();

                                    String relativePath = src.relativize(directory).toString();
                                    // Добавляем размер и путь
                                    size_directory.add(str.length() + " " + relativePath);
                                }
                            } catch (IOException e) {
                                System.err.println("Error reading: " + directory);
                            }
                        }
                    }
            );
            Collections.sort(size_directory, new myStringComparator());
            for (var info : size_directory)
                System.out.println(info);
        }
    }

    public static void main(String[] args) throws IOException {
        getInformation();
    }
}