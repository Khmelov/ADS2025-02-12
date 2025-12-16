package by.it.group451003.galuzo.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<ProcessedFile> files = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p, StandardCharsets.UTF_8);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String processed = processContentC(content);
                            String relativePath = root.relativize(p).toString();

                            files.add(new ProcessedFile(relativePath, processed));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Находим дубликаты по хэшу содержимого
        Map<Integer, List<String>> hashGroups = new HashMap<>();
        for (ProcessedFile file : files) {
            int hash = file.content.hashCode();
            hashGroups.computeIfAbsent(hash, k -> new ArrayList<>()).add(file.path);
        }

        List<String> resultFiles = new ArrayList<>();

        // Если есть дубликаты, добавляем только дублирующиеся файлы
        // Если нет дубликатов, добавляем все файлы
        boolean hasDuplicates = hashGroups.values().stream().anyMatch(list -> list.size() > 1);

        if (hasDuplicates) {
            // Выводим только дублирующиеся файлы
            for (List<String> group : hashGroups.values()) {
                if (group.size() > 1) {
                    for (String path : group) {
                        resultFiles.add(getFileName(path));
                    }
                }
            }
        } else {
            // Выводим все файлы (как в SourceScannerA/B)
            for (ProcessedFile file : files) {
                resultFiles.add(getFileName(file.path));
            }
        }

        // Сортируем и выводим
        Collections.sort(resultFiles);
        for (String fileName : resultFiles) {
            System.out.println(fileName);
        }
    }

    private static String getFileName(String path) {
        int lastSeparator = path.lastIndexOf(File.separator);
        return (lastSeparator != -1) ? path.substring(lastSeparator + 1) : path;
    }

    private static String processContentC(String content) {
        content = removePackageAndImports(content);
        content = removeComments(content);
        content = replaceLowCharsWithSpaces(content);
        content = content.trim();
        return content;
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < content.length(); i++) {
            char current = content.charAt(i);
            char next = (i < content.length() - 1) ? content.charAt(i + 1) : 0;

            if (inBlockComment) {
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (inLineComment) {
                if (current == '\n') {
                    inLineComment = false;
                    result.append(current);
                }
                continue;
            }

            if (inString) {
                result.append(current);
                if (current == '\\' && next == '"') {
                    result.append(next);
                    i++;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                result.append(current);
                if (current == '\\' && next == '\'') {
                    result.append(next);
                    i++;
                } else if (current == '\'') {
                    inChar = false;
                }
                continue;
            }

            if (current == '"') {
                inString = true;
                result.append(current);
            } else if (current == '\'') {
                inChar = true;
                result.append(current);
            } else if (current == '/' && next == '*') {
                inBlockComment = true;
                i++;
            } else if (current == '/' && next == '/') {
                inLineComment = true;
                i++;
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    private static String replaceLowCharsWithSpaces(String str) {
        StringBuilder result = new StringBuilder();
        boolean inWhitespaceSequence = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 33) {
                if (!inWhitespaceSequence) {
                    result.append(' ');
                    inWhitespaceSequence = true;
                }
            } else {
                result.append(c);
                inWhitespaceSequence = false;
            }
        }
        return result.toString();
    }

    private static class ProcessedFile {
        private final String path;
        private final String content;

        public ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}