package by.it.group410902.kovalchuck.lesson01.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {

    public static void main(String[] args) {
        //Получаем путь к каталогу src текущего проекта
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<FileData> processedFiles = processJavaFiles(src);

        //Выводим отсортированные результаты
        printResults(processedFiles);
    }

    private static List<FileData> processJavaFiles(String srcDir) {
        List<FileData> result = new ArrayList<>();
        Path srcPath = Paths.get(srcDir);

        try {
            //обходим все файлы и подкаталоги
            Files.walk(srcPath)
                    //Фильтруем только Java файлы
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            //Читаем содержимое файла
                            String content = readFileContent(path);

                            //Проверяем, не является ли файл тестом
                            if (!isTestFile(content)) {
                                //Обрабатываем содержимое файла
                                String processedContent = processContent(content);
                                //Получаем относительный путь от src
                                String relativePath = srcPath.relativize(path).toString();
                                //Вычисляем размер обработанного содержимого в байтах
                                int size = processedContent.getBytes(StandardCharsets.UTF_8).length;
                                //Добавляем данные файла в результат
                                result.add(new FileData(relativePath, size));
                            }
                        } catch (IOException e) {
                            //Игнорируем ошибки чтения файлов, включая MalformedInputException
                        }
                    });
        } catch (IOException e) {
            //Игнорируем ошибки обхода директорий
        }

        return result;
    }

    private static String readFileContent(Path path) throws IOException {
        //Используем UTF-8 для чтения файлов
        Charset charset = StandardCharsets.UTF_8;
        try {
            //Пытаемся прочитать файл как текст в кодировке UTF-8
            return Files.readString(path, charset);
        } catch (IOException e) {
            //Если возникла ошибка кодировки, читаем как байты и преобразуем
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, charset);
        }
    }

    private static boolean isTestFile(String content) {
        //Проверяем, содержит ли файл аннотации тестирования JUnit
        return content.contains("@Test") || content.contains("org.junit.Test");
    }

    private static String processContent(String content) {
        //Все операции за O(n) от длины текста
        StringBuilder result = new StringBuilder();
        boolean inMultiLineComment = false; //нахождение в многострочном комментарии
        boolean inSingleLineComment = false; // нахождение в однострочном комментарии
        boolean inString = false; // нахождение внутри строкового литерала
        boolean inChar = false; //нахождение внутри символьного литерала
        char prevChar = '\0'; //Предыдущий символ для обработки экранирования

        //Посимвольная обработка содержимого файла
        for (int i = 0; i < content.length(); i++) {
            char currentChar = content.charAt(i);

            //Обработка перевода строки (сбрасываем однострочный комментарий)
            if (currentChar == '\n' || currentChar == '\r') {
                inSingleLineComment = false;
                result.append(currentChar);
                prevChar = currentChar;
                continue;
            }

            //Обработка строковых литералов
            if (!inMultiLineComment && !inSingleLineComment && currentChar == '"' && prevChar != '\\') {
                inString = !inString;
                result.append(currentChar);
                prevChar = currentChar;
                continue;
            }

            //Обработка символьных литералов
            if (!inMultiLineComment && !inSingleLineComment && currentChar == '\'' && prevChar != '\\') {
                inChar = !inChar;
                result.append(currentChar);
                prevChar = currentChar;
                continue;
            }

            //Если внутри строки или символа - просто добавляем символ
            if (inString || inChar) {
                result.append(currentChar);
                prevChar = currentChar;
                continue;
            }

            //Обработка начала многострочного комментария
            if (!inMultiLineComment && !inSingleLineComment && currentChar == '/' && i + 1 < content.length() && content.charAt(i + 1) == '*') {
                inMultiLineComment = true;
                i++; //Пропускаем следующий символ
                prevChar = '*';
                continue;
            }

            //Обработка конца многострочного комментария
            if (inMultiLineComment && currentChar == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                inMultiLineComment = false;
                i++; //Пропускаем следующий символ
                prevChar = '/';
                continue;
            }

            //Обработка начала однострочного комментария
            if (!inMultiLineComment && !inSingleLineComment && currentChar == '/' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                inSingleLineComment = true;
                i++; //Пропускаем следующий символ
                prevChar = '/';
                continue;
            }

            //Если не в комментарии - добавляем символ в результат
            if (!inMultiLineComment && !inSingleLineComment) {
                result.append(currentChar);
            }

            prevChar = currentChar;
        }

        String withoutComments = result.toString();

        //Удаляем package и import строки за
        String[] lines = withoutComments.split("\\r?\\n");
        StringBuilder finalResult = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            //Сохраняем только строки, не начинающиеся с package или import
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                //Удаляем полностью пустые строки
                if (!trimmedLine.isEmpty()) {
                    finalResult.append(line).append("\n");
                }
            }
        }

        //Удаляем символы с кодом <33 в начале и конце
        String processed = finalResult.toString();
        String trimmed = removeLeadingTrailingControlChars(processed);

        return trimmed;
    }

    private static String removeLeadingTrailingControlChars(String text) {
        if (text.isEmpty()) return text;

        //Удаляем управляющие символы в начале строки
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }

        //Если весь текст состоит из управляющих символов
        if (start >= text.length()) return "";

        //Удаляем управляющие символы в конце строки
        int end = text.length();
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }

    private static void printResults(List<FileData> files) {
        //Сортируем файлы по размеру (по возрастанию), а при равных размерах - лексикографически по пути
        files.stream()
                .sorted(Comparator.comparingInt(FileData::getSize)
                        .thenComparing(FileData::getPath))
                .forEach(file -> System.out.println(file.getSize() + " " + file.getPath()));
    }

    //Вспомогательный класс для хранения данных о файле
    private static class FileData {
        private final String path; //Относительный путь файла
        private final int size; //Размер обработанного содержимого в байтах

        public FileData(String path, int size) {
            this.path = path;
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public int getSize() {
            return size;
        }
    }
}