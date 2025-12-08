package by.it.group410902.habrukovich.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        Map<String, String> fileTexts = new TreeMap<>(); // путь -> обработанный текст

        if (!Files.exists(srcPath)) return;

        Files.walk(srcPath)
                .filter(Files::isRegularFile)
                .filter(f -> f.toString().endsWith(".java"))
                .forEach(f -> {
                    String text;
                    try {
                        byte[] bytes = Files.readAllBytes(f);
                        text = new String(bytes, StandardCharsets.UTF_8);
                    } catch (MalformedInputException e) {
                        return;
                    } catch (IOException e) {
                        return;
                    }

                    // пропускаем тестовые файлы
                    if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                    // удаляем package/import
                    StringBuilder sb = new StringBuilder();
                    for (String line : text.split("\\R")) {
                        String t = line.trim();
                        if (t.startsWith("package ") || t.startsWith("import ")) continue;
                        sb.append(line).append("\n");
                    }

                    // удаляем комментарии
                    String cleaned = removeComments(sb.toString());

                    // заменить символы <33 на пробел и trim
                    StringBuilder sb2 = new StringBuilder();
                    for (char c : cleaned.toCharArray()) {
                        sb2.append(c < 33 ? ' ' : c);
                    }
                    cleaned = sb2.toString().trim();

                    String relPath = srcPath.relativize(f).toString().replace(File.separatorChar, '/');
                    fileTexts.put(relPath, cleaned);
                });

        // поиск копий по идентичным текстам
        // Map для хранения копий: ключ - файл, значение - список всех его копий
        Map<String, List<String>> copiesMap = new TreeMap<>();
        // список всех путей к файлам
        List<String> paths = new ArrayList<>(fileTexts.keySet());

        // сравниваем каждый файл с каждым другим файлом
        for (int i = 0; i < paths.size(); i++) {
            String p1 = paths.get(i);          // путь первого файла
            String t1 = fileTexts.get(p1);     // текст первого файла
            for (int j = i + 1; j < paths.size(); j++) {
                String p2 = paths.get(j);      // путь второго файла
                String t2 = fileTexts.get(p2); // текст второго файла
                if (t1.equals(t2)) {           // если тексты идентичны — это копия
                    // добавляем друг другу в список копий
                    copiesMap.computeIfAbsent(p1, k -> new ArrayList<>()).add(p2);
                    copiesMap.computeIfAbsent(p2, k -> new ArrayList<>()).add(p1);
                }
            }
        }

        // вывод
        for (Map.Entry<String, List<String>> entry : copiesMap.entrySet()) {
            System.out.println(entry.getKey());
            List<String> lst = entry.getValue();
            lst.sort(String::compareTo);
            for (String p : lst) System.out.println(p);
        }
    }

    private static String removeComments(String code) {
        StringBuilder sb = new StringBuilder();
        boolean inBlock = false;
        boolean inLine = false;
        int len = code.length();
        for (int i = 0; i < len; i++) {
            if (inBlock) {
                if (i + 1 < len && code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                    inBlock = false;
                    i++;
                }
            } else if (inLine) {
                if (code.charAt(i) == '\n' || code.charAt(i) == '\r') {
                    inLine = false;
                    sb.append(code.charAt(i));
                }
            } else {
                if (i + 1 < len) {
                    if (code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                        inLine = true;
                        i++;
                        continue;
                    } else if (code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                        inBlock = true;
                        i++;
                        continue;
                    }
                }
                sb.append(code.charAt(i));
            }
        }
        return sb.toString();
    }
}


