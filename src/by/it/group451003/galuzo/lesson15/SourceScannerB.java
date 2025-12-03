package by.it.group451003.galuzo.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class SourceScannerB extends SourceScannerA {

    protected static String cleanContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // Удаляем package и import statements
        content = content.replaceAll("package\\s+[^;]+;", "")
                .replaceAll("import\\s+[^;]+;", "");

        // Удаляем блочные комментарии
        content = content.replaceAll("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", "");

        // Удаляем однострочные комментарии
        content = content.replaceAll("//[^\\r\\n]*", "");

        // Удаляем начальные и конечные пробельные символы
        content = content.trim();

        // Удаляем множественные пустые строки
        content = content.replaceAll("\\r\\n\\s*\\r\\n", "\r\n")
                .replaceAll("\\n\\s*\\n", "\n")
                .replaceAll("\\r\\s*\\r", "\r");

        return content;
    }

    protected static void getInformation() throws IOException {
        ArrayList<String> size_directory = new ArrayList<>();
        Path src = Path.of(System.getProperty("user.dir") + File.separator + "src" + File.separator);

        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(directory -> {
                if (directory.toString().endsWith(".java")) {
                    try {
                        String content = Files.readString(directory);

                        // Пропускаем тестовые файлы
                        if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                            content = cleanContent(content);

                            if (!content.isEmpty()) {
                                int size = content.getBytes().length;
                                String relativePath = src.relativize(directory).toString();
                                size_directory.add(size + " " + relativePath);
                            }
                        }
                    } catch (IOException e) {
                        // Игнорируем ошибки чтения файлов
                    }
                }
            });

            Collections.sort(size_directory, new myStringComparator());

            for (String info : size_directory) {
                System.out.println(info);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        getInformation();
    }
}