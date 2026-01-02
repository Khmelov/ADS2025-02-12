package by.it.group451001.kazakov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        // Определяем абсолютный путь к каталогу src проекта
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        // Список для хранения обработанных файлов (путь + очищенное содержимое)
        List<ProcessedFile> files = new ArrayList<>();

        // Рекурсивный обход всех файлов в каталоге src с использованием Stream API
        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")) // Фильтруем только Java-файлы
                    .forEach(p -> {
                        try {
                            // Чтение всего содержимого файла в кодировке UTF-8
                            String content = Files.readString(p, StandardCharsets.UTF_8);

                            // Пропускаем тестовые файлы (содержащие аннотации @Test)
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Очистка содержимого файла от комментариев и форматирования
                            String processed = processContentC(content);

                            // Получение относительного пути файла (относительно src)
                            String relativePath = root.relativize(p).toString();

                            // Сохранение обработанного файла
                            files.add(new ProcessedFile(relativePath, processed));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения отдельных файлов
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Группировка файлов по хеш-коду их очищенного содержимого
        Map<Integer, List<String>> hashGroups = new HashMap<>();

        for (ProcessedFile file : files) {
            int hash = file.content.hashCode();
            // Группируем файлы с одинаковым хеш-кодом содержимого
            hashGroups.computeIfAbsent(hash, k -> new ArrayList<>()).add(file.path);
        }

        // Формирование списка имен файлов для вывода
        boolean hasCopies = false;
        List<String> allFileNames = new ArrayList<>();

        for (List<String> group : hashGroups.values()) {
            if (group.size() > 1) {
                // Если в группе больше одного файла - это копии
                hasCopies = true;
                for (String path : group) {
                    allFileNames.add(getFileName(path));
                }
            } else if (!hasCopies) {
                // Если копий нет, добавляем все файлы
                allFileNames.add(getFileName(group.get(0)));
            }
        }

        // Сортировка имен файлов в лексикографическом порядке
        Collections.sort(allFileNames);

        // Вывод результатов
        for (String fileName : allFileNames) {
            System.out.println(fileName);
        }
    }


    private static String getFileName(String path) {
        int lastSeparator = path.lastIndexOf(File.separator);
        return (lastSeparator != -1) ? path.substring(lastSeparator + 1) : path;
    }

    private static String processContentC(String content) {
        // Удаление строк package и import
        content = removePackageAndImports(content);

        // Удаление всех комментариев (однострочных и многострочных)
        content = removeComments(content);

        // Замена управляющих символов на пробелы и нормализация пробелов
        content = replaceLowCharsWithSpaces(content);

        // Удаление пробелов в начале и конце
        content = content.trim();

        return content;
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            // Сохраняем только строки, не начинающиеся с package или import
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false; // внутри /* ... */
        boolean inLineComment = false;  // внутри // ...
        boolean inString = false;       // внутри "..."
        boolean inChar = false;         // внутри '.'

        for (int i = 0; i < content.length(); i++) {
            char current = content.charAt(i);
            char next = (i < content.length() - 1) ? content.charAt(i + 1) : 0;

            if (inBlockComment) {
                // Выход из многострочного комментария при обнаружении */
                if (current == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // пропускаем символ '/'
                }
                continue; // пропускаем все символы внутри комментария
            }

            if (inLineComment) {
                // Выход из однострочного комментария при переводе строки
                if (current == '\n') {
                    inLineComment = false;
                    result.append(current); // сохраняем перевод строки
                }
                continue; // пропускаем символы внутри комментария
            }

            if (inString) {
                result.append(current);
                // Обработка экранированных кавычек в строках
                if (current == '\\' && next == '"') {
                    result.append(next);
                    i++;
                } else if (current == '"') {
                    inString = false; // конец строкового литерала
                }
                continue;
            }

            if (inChar) {
                result.append(current);
                // Обработка экранированных апострофов
                if (current == '\\' && next == '\'') {
                    result.append(next);
                    i++;
                } else if (current == '\'') {
                    inChar = false; // конец символьного литерала
                }
                continue;
            }

            // Обработка начала различных конструкций
            if (current == '"') {
                inString = true;
                result.append(current);
            } else if (current == '\'') {
                inChar = true;
                result.append(current);
            } else if (current == '/' && next == '*') {
                inBlockComment = true; // начало многострочного комментария
                i++; // пропускаем '*'
            } else if (current == '/' && next == '/') {
                inLineComment = true; // начало однострочного комментария
                i++; // пропускаем '/'
            } else {
                // Обычный символ кода - сохраняем
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
                // Заменяем последовательности управляющих символов на один пробел
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
        private final String path;    // относительный путь файла
        private final String content; // очищенное содержимое файла

        public ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}