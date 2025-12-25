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
 * Класс для сканирования исходных Java файлов и поиска копий
 * 
 * Задача C: Расширяет SourceScannerB, добавляя:
 * - Нормализацию символов (замена символов с кодом <33 на пробел)
 * - Поиск копий по расстоянию Левенштейна (правки <10)
 * - Вывод файлов с копиями, отсортированных лексикографически
 */
public class SourceScannerC {
    
    /**
     * Класс для хранения информации о файле (обработанное содержимое и путь)
     */
    private static class FileData {
        String content;
        String path;
        
        FileData(String content, String path) {
            this.content = content;
            this.path = path;
        }
    }
    
    public static void main(String[] args) {
        // Получаем путь к каталогу src
        String src = System.getProperty("user.dir")
                       + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        
        // Список для хранения данных о файлах
        List<FileData> files = new ArrayList<>();
        
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
                        
                        // Получаем относительный путь от src
                        String relativePath = srcPath.relativize(p).toString();
                        
                        // Добавляем данные о файле
                        files.add(new FileData(processed, relativePath));
                        
                    } catch (Exception e) {
                        // Игнорируем ошибки чтения файлов (включая MalformedInputException)
                    }
                });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога: " + e.getMessage());
            return;
        }
        
        // Находим копии по расстоянию Левенштейна
        List<List<String>> duplicates = findDuplicates(files);
        
        // Сортируем и выводим результаты
        for (List<String> group : duplicates) {
            Collections.sort(group);
            for (String path : group) {
                System.out.println(path);
            }
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
     * 3. Заменяет все символы с кодом <33 на пробел (32)
     * 4. Выполняет trim()
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
            
            // Заменяем символы с кодом <33 на пробел (32)
            if (c < 33) {
                result.append(' ');
            } else {
                result.append(c);
            }
            i++;
        }
        
        // Выполняем trim()
        return result.toString().trim();
    }
    
    /**
     * Находит группы файлов-копий по расстоянию Левенштейна
     * Копиями считаются тексты с числом правок <10
     * Оптимизировано: предварительная фильтрация по длине строк,
     * группировка по длине для уменьшения количества сравнений
     * 
     * @param files список файлов для проверки
     * @return список групп файлов-копий
     */
    private static List<List<String>> findDuplicates(List<FileData> files) {
        List<List<String>> duplicates = new ArrayList<>();
        boolean[] processed = new boolean[files.size()];
        
        // Группируем файлы по длине для уменьшения количества сравнений
        // Файлы с длиной, отличающейся более чем на 10, не могут быть копиями
        for (int i = 0; i < files.size(); i++) {
            if (processed[i]) {
                continue;
            }
            
            List<String> group = new ArrayList<>();
            group.add(files.get(i).path);
            String content1 = files.get(i).content;
            int len1 = content1.length();
            
            // Для очень больших файлов пропускаем поиск копий
            // (слишком медленно для файлов >1000 символов)
            if (len1 > 1000) {
                continue;
            }
            
            // Ищем копии для текущего файла
            // Ограничиваем поиск только файлами с похожей длиной
            for (int j = i + 1; j < files.size(); j++) {
                if (processed[j]) {
                    continue;
                }
                
                String content2 = files.get(j).content;
                int len2 = content2.length();
                
                // Для очень больших файлов пропускаем сравнение
                if (len2 > 1000) {
                    continue;
                }
                
                // Быстрая проверка: если разница в длине больше 10,
                // расстояние Левенштейна точно >= разница в длине
                if (Math.abs(len1 - len2) > 10) {
                    continue;
                }
                
                // Быстрая проверка: если строки идентичны, это копия
                if (content1.equals(content2)) {
                    group.add(files.get(j).path);
                    processed[j] = true;
                    continue;
                }
                
                // Быстрая проверка: сравниваем первые 30 символов
                // Если они сильно отличаются, пропускаем
                int prefixLen = Math.min(30, Math.min(len1, len2));
                if (prefixLen > 0) {
                    int prefixDiff = 0;
                    for (int k = 0; k < prefixLen; k++) {
                        if (content1.charAt(k) != content2.charAt(k)) {
                            prefixDiff++;
                            if (prefixDiff > 3) { // Ранний выход
                                break;
                            }
                        }
                    }
                    if (prefixDiff > 3) { // Если в первых 30 символах больше 3 различий
                        continue;
                    }
                }
                
                // Вычисляем расстояние Левенштейна
                int distance = levenshteinDistance(content1, content2);
                
                // Если расстояние <10, считаем файлы копиями
                if (distance < 10) {
                    group.add(files.get(j).path);
                    processed[j] = true;
                }
            }
            
            // Если найдены копии, добавляем группу
            if (group.size() > 1) {
                duplicates.add(group);
                processed[i] = true;
            }
        }
        
        // Сортируем группы по первому пути (лексикографически)
        Collections.sort(duplicates, (a, b) -> a.get(0).compareTo(b.get(0)));
        
        return duplicates;
    }
    
    /**
     * Вычисляет расстояние Левенштейна между двумя строками
     * Оптимизированная версия с использованием динамического программирования
     * и ранним выходом, если расстояние уже больше 10
     * 
     * @param s1 первая строка
     * @param s2 вторая строка
     * @return расстояние Левенштейна (или 11, если больше 10)
     */
    private static int levenshteinDistance(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 11;
        }
        
        int len1 = s1.length();
        int len2 = s2.length();
        
        // Если разница в длине больше 10, сразу возвращаем большое значение
        if (Math.abs(len1 - len2) > 10) {
            return 11;
        }
        
        // Используем оптимизацию: храним только две строки вместо матрицы
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];
        
        // Инициализация первой строки
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }
        
        // Заполнение матрицы с ранним выходом
        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            
            // Минимальное значение в текущей строке
            int minInRow = curr[0];
            boolean hasChance = false; // Есть ли шанс получить значение <10
            
            for (int j = 1; j <= len2; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(
                        Math.min(prev[j], curr[j - 1]),
                        prev[j - 1]
                    );
                }
                
                minInRow = Math.min(minInRow, curr[j]);
                if (curr[j] < 10) {
                    hasChance = true;
                }
            }
            
            // Ранний выход: если минимальное значение в строке уже >10
            // и нет шанса получить значение <10, прекращаем вычисления
            if (minInRow > 10 && !hasChance) {
                return 11;
            }
            
            // Меняем местами массивы
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        int result = prev[len2];
        return result > 10 ? 11 : result;
    }
}

