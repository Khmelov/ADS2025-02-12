package lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileData> fileDataList = new ArrayList<>();

        try {
            processDirectory(new File(src), src, fileDataList);

            // Сортировка: сначала по размеру, затем по пути
            fileDataList.sort((fd1, fd2) -> {
                if (fd1.size != fd2.size) {
                    return Integer.compare(fd1.size, fd2.size);
                }
                return fd1.relativePath.compareTo(fd2.relativePath);
            });

            // Вывод результатов
            for (FileData fd : fileDataList) {
                System.out.println(fd.size + " " + fd.relativePath);
            }

        } catch (Exception e) {
            System.err.println("Ошибка при обработке: " + e.getMessage());
        }
    }

    private static void processDirectory(File dir, String srcRoot,
                                         List<FileData> fileDataList) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                processDirectory(file, srcRoot, fileDataList);
            } else if (file.getName().endsWith(".java")) {
                processJavaFile(file, srcRoot, fileDataList);
            }
        }
    }

    private static void processJavaFile(File file, String srcRoot,
                                        List<FileData> fileDataList) {
        String content;

        try {
            // Чтение файла с обработкой ошибок кодировки
            content = readFile(file);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла " + file.getPath() + ": " + e.getMessage());
            return;
        }

        // Пропускаем файлы с тестами
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // Обработка содержимого
        String processed = processContent(content);

        // Расчет размера в байтах (UTF-8)
        byte[] bytes = processed.getBytes(StandardCharsets.UTF_8);
        int size = bytes.length;

        // Получаем относительный путь
        String relativePath = file.getPath().substring(srcRoot.length());

        fileDataList.add(new FileData(size, relativePath));
    }

    private static String readFile(File file) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);

        try {
            return Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Попробуем другие кодировки если UTF-8 не сработала
            try {
                return Files.readString(file.toPath(), StandardCharsets.ISO_8859_1);
            } catch (MalformedInputException e2) {
                // Если и это не помогло, вернем пустую строку
                System.err.println("Не удалось прочитать файл " + file.getPath() +
                        " в кодировках UTF-8 или ISO-8859-1");
                return "";
            }
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        boolean inMultiLineComment = false;
        boolean packageAndImportsRemoved = false;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmedLine = line.trim();

            // Удаляем символы с кодом < 33 в начале и конце строки
            line = trimLowChars(line);

            // Пропускаем полностью пустые строки после трима
            if (line.isEmpty()) {
                continue;
            }

            // Обработка многострочных комментариев
            if (inMultiLineComment) {
                int endCommentIndex = line.indexOf("*/");
                if (endCommentIndex != -1) {
                    line = line.substring(endCommentIndex + 2);
                    inMultiLineComment = false;
                    // Продолжаем обработку оставшейся части строки
                    if (!line.isEmpty()) {
                        // Возвращаемся к обработке этой строки снова
                        lines[i] = line;
                        i--;
                    }
                }
                continue;
            }

            // Удаление package и импортов (только в начале файла)
            if (!packageAndImportsRemoved) {
                if (trimmedLine.startsWith("package ") || trimmedLine.startsWith("import ")) {
                    continue;
                }
                // Как только нашли не-package/import строку, отмечаем что удаление завершено
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("//")) {
                    packageAndImportsRemoved = true;
                } else {
                    continue;
                }
            }

            // Обработка однострочных комментариев
            int singleLineCommentIndex = line.indexOf("//");
            if (singleLineCommentIndex != -1) {
                line = line.substring(0, singleLineCommentIndex);
                line = trimLowChars(line);
                if (line.isEmpty()) {
                    continue;
                }
            }

            // Обработка начала многострочного комментария
            int multiLineCommentStart = line.indexOf("/*");
            if (multiLineCommentStart != -1) {
                String beforeComment = line.substring(0, multiLineCommentStart);
                String afterComment = line.substring(multiLineCommentStart + 2);

                int endCommentIndex = afterComment.indexOf("*/");
                if (endCommentIndex != -1) {
                    // Комментарий закрывается в той же строке
                    line = beforeComment + afterComment.substring(endCommentIndex + 2);
                    line = trimLowChars(line);
                    if (!line.isEmpty()) {
                        result.append(line).append("\n");
                    }
                } else {
                    // Комментарий продолжается на следующих строках
                    line = beforeComment;
                    line = trimLowChars(line);
                    if (!line.isEmpty()) {
                        result.append(line).append("\n");
                    }
                    inMultiLineComment = true;
                }
                continue;
            }

            // Добавляем обработанную строку
            result.append(line).append("\n");
        }

        String processed = result.toString();

        // Удаляем символы с кодом < 33 в начале и конце всего текста
        processed = trimLowChars(processed);

        // Удаляем пустые строки
        String[] finalLines = processed.split("\n");
        StringBuilder finalResult = new StringBuilder();
        for (String line : finalLines) {
            if (!line.trim().isEmpty()) {
                finalResult.append(line).append("\n");
            }
        }

        // Удаляем последний \n если он есть
        if (finalResult.length() > 0 && finalResult.charAt(finalResult.length() - 1) == '\n') {
            finalResult.deleteCharAt(finalResult.length() - 1);
        }

        return finalResult.toString();
    }

    private static String trimLowChars(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        int start = 0;
        int end = str.length() - 1;

        // Находим первый символ с кодом >= 33
        while (start <= end && str.charAt(start) < 33) {
            start++;
        }

        // Находим последний символ с кодом >= 33
        while (end >= start && str.charAt(end) < 33) {
            end--;
        }

        if (start > end) {
            return "";
        }

        return str.substring(start, end + 1);
    }

    // Вспомогательный класс для хранения данных о файле
    private static class FileData {
        int size;
        String relativePath;

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}