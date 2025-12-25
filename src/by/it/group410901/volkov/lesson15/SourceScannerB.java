package by.it.group410901.volkov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс для сканирования исходных Java файлов
 * 
 * Задача B: Расширяет SourceScannerA, добавляя:
 * - Удаление всех комментариев за O(n)
 * - Удаление пустых строк
 */
public class SourceScannerB {
    
    /**
     * Класс для хранения информации о файле (размер и путь)
     */
    private static class FileInfo implements Comparable<FileInfo> {
        int size;
        String path;
        
        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }
        
        @Override
        public int compareTo(FileInfo other) {
            // Сначала сортируем по размеру, затем лексикографически по пути
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            return this.path.compareTo(other.path);
        }
    }
    
    public static void main(String[] args) {
        // Получаем путь к каталогу src
        String src = System.getProperty("user.dir")
                       + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        
        // Список для хранения информации о файлах
        List<FileInfo> fileInfos = new ArrayList<>();
        
        try {
            // Рекурсивно обходим все файлы в каталоге src
            Files.walk(srcPath)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        // Читаем содержимое файла с обработкой ошибок кодировки
                        String content = readFileWithFallback(p);
                        
                        // Пропускаем тестовые файлы
                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }
                        
                        // Обрабатываем содержимое файла
                        String processed = processContent(content);
                        
                        // Вычисляем размер в байтах
                        int size = processed.getBytes(StandardCharsets.UTF_8).length;
                        
                        // Получаем относительный путь от src
                        String relativePath = srcPath.relativize(p).toString();
                        
                        // Добавляем информацию о файле
                        fileInfos.add(new FileInfo(size, relativePath));
                        
                    } catch (Exception e) {
                        // Игнорируем ошибки чтения файлов (включая MalformedInputException)
                    }
                });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога: " + e.getMessage());
            return;
        }
        
        // Сортируем файлы по размеру, затем по пути
        Collections.sort(fileInfos);
        
        // Выводим результаты
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path);
        }
    }
    
    /**
     * Читает файл с обработкой ошибок кодировки
     * Пробует UTF-8, затем ISO-8859-1, затем другие кодировки
     * 
     * @param path путь к файлу
     * @return содержимое файла как строка
     */
    private static String readFileWithFallback(Path path) {
        // Список кодировок для попытки чтения
        Charset[] charsets = {
            StandardCharsets.UTF_8,
            StandardCharsets.ISO_8859_1,
            Charset.forName("Windows-1251"),
            Charset.defaultCharset()
        };
        
        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {
                // Пробуем следующую кодировку
                continue;
            }
        }
        
        // Если все кодировки не подошли, возвращаем пустую строку
        return "";
    }
    
    /**
     * Обрабатывает содержимое файла:
     * 1. Удаляет строку package и все импорты за O(n)
     * 2. Удаляет все комментарии за O(n)
     * 3. Удаляет символы с кодом <33 в начале и конце
     * 4. Удаляет пустые строки
     * 
     * @param content исходное содержимое файла
     * @return обработанное содержимое
     */
    private static String processContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder(content.length());
        int i = 0;
        
        // Проходим по содержимому один раз (O(n))
        while (i < content.length()) {
            char c = content.charAt(i);
            
            // Пропускаем символы с кодом <33 в начале
            if (result.length() == 0 && c < 33) {
                i++;
                continue;
            }
            
            // Проверяем начало строки package или import
            if (i == 0 || content.charAt(i - 1) == '\n') {
                // Проверяем, начинается ли строка с package
                if (i + 7 < content.length() && 
                    content.substring(i, i + 7).equals("package")) {
                    // Пропускаем строку package до конца строки
                    while (i < content.length() && content.charAt(i) != '\n') {
                        i++;
                    }
                    if (i < content.length()) {
                        i++; // Пропускаем символ новой строки
                    }
                    continue;
                }
                
                // Проверяем, начинается ли строка с import
                if (i + 6 < content.length() && 
                    content.substring(i, i + 6).equals("import")) {
                    // Пропускаем строку import до конца строки
                    while (i < content.length() && content.charAt(i) != '\n') {
                        i++;
                    }
                    if (i < content.length()) {
                        i++; // Пропускаем символ новой строки
                    }
                    continue;
                }
            }
            
            // Обработка комментариев
            
            // Однострочный комментарий //
            if (i + 1 < content.length() && 
                c == '/' && content.charAt(i + 1) == '/') {
                // Пропускаем до конца строки
                while (i < content.length() && content.charAt(i) != '\n') {
                    i++;
                }
                if (i < content.length()) {
                    i++; // Пропускаем символ новой строки
                }
                continue;
            }
            
            // Многострочный комментарий /* ... */
            if (i + 1 < content.length() && 
                c == '/' && content.charAt(i + 1) == '*') {
                // Пропускаем до */
                i += 2; // Пропускаем /*
                while (i + 1 < content.length()) {
                    if (content.charAt(i) == '*' && content.charAt(i + 1) == '/') {
                        i += 2; // Пропускаем */
                        break;
                    }
                    i++;
                }
                continue;
            }
            
            // Добавляем символ к результату
            result.append(c);
            i++;
        }
        
        // Удаляем символы с кодом <33 в конце
        String processed = result.toString();
        int end = processed.length();
        while (end > 0 && processed.charAt(end - 1) < 33) {
            end--;
        }
        processed = processed.substring(0, end);
        
        // Удаляем пустые строки
        return removeEmptyLines(processed);
    }
    
    /**
     * Удаляет пустые строки из текста
     * 
     * @param text исходный текст
     * @return текст без пустых строк
     */
    private static String removeEmptyLines(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder(text.length());
        boolean wasNewline = false;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            if (c == '\n') {
                // Если предыдущий символ был не переносом строки, добавляем перенос
                if (!wasNewline && result.length() > 0) {
                    result.append('\n');
                }
                wasNewline = true;
            } else if (c >= 33) {
                // Непустой символ
                result.append(c);
                wasNewline = false;
            } else {
                // Символ с кодом <33 (пробел, табуляция и т.д.)
                // Добавляем только если это не начало строки
                if (!wasNewline) {
                    result.append(c);
                }
            }
        }
        
        return result.toString();
    }
}



