package by.it.group410901.abakumov.lesson15;

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

public class SourceScannerA {
    
    // Класс для хранения информации о файле
    static class FileInfo {
        String path; // Относительный путь к файлу
        int size; // Размер файла в байтах
        
        FileInfo(String path, int size) {
            this.path = path; // Сохраняем путь к файлу
            this.size = size; // Сохраняем размер файла
        }
    }
    
    // Удаляем строки с package и import из содержимого файла
    static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder(); // Строка для результата
        String[] lines = content.split("\n"); // Разбиваем содержимое на строки
        
        for (String line : lines) {
            String trimmed = line.trim(); // Убираем пробелы в начале и конце строки
            // Пропускаем package и import строки
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue; // Если строка начинается с package или import, пропускаем её
            }
            result.append(line).append("\n"); // Добавляем строку в результат
        }
        
        return result.toString(); // Возвращаем обработанное содержимое
    }
    
    // Удаляем управляющие символы (с кодом < 33) в начале и конце текста
    static String removeControlChars(String text) {
        // Находим начало текста без управляющих символов
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++; // Пропускаем управляющие символы в начале
        }
        // Находим конец текста без управляющих символов
        int end = text.length();
        while (end > start && text.charAt(end - 1) < 33) {
            end--; // Пропускаем управляющие символы в конце
        }
        return text.substring(start, end); // Возвращаем текст без управляющих символов
    }
    
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator; // Получаем путь к папке src
        Path srcPath = Paths.get(src); // Создаём Path объект для папки src
        
        List<FileInfo> fileInfos = new ArrayList<>(); // Список для хранения информации о файлах
        
        try {
            Files.walk(srcPath) // Рекурсивно обходим все файлы в папке src
                .filter(p -> p.toString().endsWith(".java")) // Фильтруем только Java файлы
                .forEach(p -> {
                    // Пробуем разные кодировки для обработки MalformedInputException
                    String content = null; // Переменная для содержимого файла
                    Charset[] charsets = {StandardCharsets.UTF_8, Charset.forName("Windows-1251"), StandardCharsets.ISO_8859_1}; // Список кодировок для попытки

                    for (Charset charset : charsets) {
                        try {
                            content = Files.readString(p, charset); // Пробуем прочитать файл с данной кодировкой
                            break; // Если удалось, прекращаем попытки
                        } catch (IOException e) {
                            // Пробуем следующую кодировку
                        }
                    }

                    if (content == null) {
                        return; // Не удалось прочитать файл, пропускаем его
                    }

                    // Пропускаем тесты
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return; // Если файл содержит тесты, пропускаем его
                    }

                    // Обрабатываем файл
                    content = removePackageAndImports(content); // Удаляем package и import
                    content = removeControlChars(content); // Удаляем управляющие символы

                    // Вычисляем размер в байтах
                    byte[] bytes = content.getBytes(StandardCharsets.UTF_8); // Преобразуем в байты
                    int size = bytes.length; // Получаем размер в байтах

                    // Получаем относительный путь (используем тот же формат, что в тесте)
                    String relativePath = srcPath.relativize(p).toString(); // Получаем путь относительно src
                    // НЕ заменяем разделители - оставляем как есть (Windows использует \)

                    fileInfos.add(new FileInfo(relativePath, size)); // Добавляем информацию о файле

                });
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку, если что-то пошло не так
        }
        
        // Сортируем: сначала по размеру (по возрастанию), затем лексикографически по пути
        fileInfos.sort(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo a, FileInfo b) {
                int sizeCompare = Integer.compare(a.size, b.size); // Сравниваем размеры
                if (sizeCompare != 0) {
                    return sizeCompare; // Если размеры разные, возвращаем результат сравнения размеров
                }
                return a.path.compareTo(b.path); // Если размеры одинаковые, сравниваем пути
            }
        });
        
        // Выводим результаты
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path); // Выводим размер и путь файла
        }
    }
}

