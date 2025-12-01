package by.it.group410901.abakumov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerC {
    
    // Класс для хранения пути и содержимого файла
    static class FileContent {
        String path; // Относительный путь к файлу
        String content; // Обработанное содержимое файла
        
        FileContent(String path, String content) {
            this.path = path; // Сохраняем путь к файлу
            this.content = content; // Сохраняем содержимое файла
        }
    }
    
    static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            String trimmed = line.trim();
            // Пропускаем package и import строки
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            result.append(line).append("\n");
        }
        
        return result.toString();
    }
    
    static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;
        boolean inString = false;
        char stringChar = 0;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (inString) {
                result.append(c);
                if (c == stringChar && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inString = false;
                }
            } else if (inMultiLineComment) {
                if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    inMultiLineComment = false;
                    i++; // Пропускаем '/'
                }
            } else if (inSingleLineComment) {
                if (c == '\n') {
                    inSingleLineComment = false;
                    result.append(c);
                }
            } else {
                if (c == '"' || c == '\'') {
                    inString = true;
                    stringChar = c;
                    result.append(c);
                } else if (c == '/' && i + 1 < content.length()) {
                    char next = content.charAt(i + 1);
                    if (next == '/') {
                        inSingleLineComment = true;
                        i++; // Пропускаем второй '/'
                    } else if (next == '*') {
                        inMultiLineComment = true;
                        i++; // Пропускаем '*'
                    } else {
                        result.append(c);
                    }
                } else {
                    result.append(c);
                }
            }
        }
        
        return result.toString();
    }
    
    // Нормализуем текст: заменяем управляющие символы на пробелы
    static String normalizeText(String text) {
        // Заменяем все символы с кодом < 33 на пробел (32)
        StringBuilder result = new StringBuilder(); // Строка для результата
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i); // Текущий символ
            if (c < 33) {
                result.append(' '); // Если управляющий символ, заменяем на пробел
            } else {
                result.append(c); // Иначе оставляем как есть
            }
        }
        return result.toString().trim(); // Убираем пробелы в начале и конце
    }
    
    // Вычисляем расстояние Левенштейна между двумя строками (оптимизированная версия)
    static int levenshteinDistance(String s1, String s2) {
        // Оптимизация: если разница в длинах больше 10, сразу возвращаем большое число
        int lenDiff = Math.abs(s1.length() - s2.length()); // Вычисляем разницу длин
        if (lenDiff >= 10) {
            return 10; // Больше порога, не копия
        }
        
        int m = s1.length(); // Длина первой строки
        int n = s2.length(); // Длина второй строки
        
        // Используем только две строки для экономии памяти (динамическое программирование)
        int[] prev = new int[n + 1]; // Предыдущая строка таблицы
        int[] curr = new int[n + 1]; // Текущая строка таблицы
        
        // Инициализируем первую строку: расстояние от пустой строки до префикса s2
        for (int j = 0; j <= n; j++) {
            prev[j] = j; // Для пустой строки нужно j операций
        }
        
        for (int i = 1; i <= m; i++) {
            curr[0] = i; // Расстояние от префикса s1 до пустой строки
            int minInRow = 10; // Начинаем с большого значения для раннего выхода
            
            // Ограничиваем вычисления только в пределах полосы вокруг диагонали
            // Полоса шириной 20: вычисляем только j от max(1, i-20) до min(n, i+20)
            int startJ = Math.max(1, i - 20); // Начало полосы
            int endJ = Math.min(n, i + 20); // Конец полосы
            
            // Заполняем значения вне полосы большими числами
            for (int j = 1; j < startJ; j++) {
                curr[j] = 10; // Вне полосы расстояние заведомо большое
            }
            
            for (int j = startJ; j <= endJ; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // Символы совпадают, расстояние не меняется
                    curr[j] = prev[j - 1];
                } else {
                    // Символы не совпадают, выбираем минимальную стоимость операции
                    int cost1 = prev[j - 1]; // Замена
                    int cost2 = prev[j]; // Удаление
                    int cost3 = (j > startJ) ? curr[j - 1] : 10; // Вставка
                    curr[j] = 1 + Math.min(Math.min(cost1, cost2), cost3); // Минимум из трёх операций
                }
                if (curr[j] < minInRow) minInRow = curr[j]; // Обновляем минимум в строке
            }
            
            // Заполняем значения после полосы
            for (int j = endJ + 1; j <= n; j++) {
                curr[j] = 10; // Вне полосы расстояние заведомо большое
            }
            
            // Ранний выход: если минимальное значение в строке уже >= 10
            if (minInRow >= 10) {
                return 10; // Заведомо не копия
            }
            
            // Меняем местами массивы для следующей итерации
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        // После последней итерации результат в prev
        return prev[n]; // Возвращаем расстояние Левенштейна
    }
    
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator; // Получаем путь к папке src
        Path srcPath = Paths.get(src); // Создаём Path объект для папки src
        
        List<FileContent> files = new ArrayList<>(); // Список для хранения файлов и их содержимого
        
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
                    content = removeComments(content); // Удаляем комментарии
                    content = normalizeText(content); // Нормализуем текст (заменяем управляющие символы на пробелы)

                    // Получаем относительный путь (используем тот же формат, что в тесте)
                    String relativePath = srcPath.relativize(p).toString(); // Получаем путь относительно src
                    // НЕ заменяем разделители - оставляем как есть (Windows использует \)

                    files.add(new FileContent(relativePath, content)); // Добавляем файл в список

                });
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку, если что-то пошло не так
        }
        
        // Находим копии (расстояние Левенштейна < 10)
        // Оптимизация: сначала фильтруем по длине, чтобы не проверять заведомо разные файлы
        Map<String, List<String>> copies = new TreeMap<>(); // TreeMap для лексикографической сортировки
        
        for (int i = 0; i < files.size(); i++) {
            List<String> fileCopies = new ArrayList<>(); // Список копий текущего файла
            String content1 = files.get(i).content; // Содержимое первого файла
            int len1 = content1.length(); // Длина первого файла
            
            for (int j = i + 1; j < files.size(); j++) {
                String content2 = files.get(j).content; // Содержимое второго файла
                int len2 = content2.length(); // Длина второго файла
                
                // Быстрая проверка: если разница в длинах >= 10, точно не копия
                if (Math.abs(len1 - len2) >= 10) {
                    continue; // Пропускаем этот файл
                }
                
                // Еще одна оптимизация: если файлы идентичны, расстояние = 0
                if (content1.equals(content2)) {
                    fileCopies.add(files.get(j).path); // Добавляем путь к копии
                    continue; // Переходим к следующему файлу
                }
                
                // Оптимизация: для очень больших файлов используем упрощенную проверку
                // Если файлы очень большие и различаются значительно, пропускаем
                if (len1 > 10000 || len2 > 10000) {
                    // Для больших файлов проверяем только первые и последние символы
                    int checkLen = Math.min(1000, Math.min(len1, len2)); // Длина проверяемого фрагмента
                    String start1 = content1.substring(0, checkLen); // Начало первого файла
                    String start2 = content2.substring(0, checkLen); // Начало второго файла
                    String end1 = content1.substring(Math.max(0, len1 - checkLen)); // Конец первого файла
                    String end2 = content2.substring(Math.max(0, len2 - checkLen)); // Конец второго файла
                    
                    int startDiff = 0; // Количество различий в начале
                    int endDiff = 0; // Количество различий в конце
                    for (int k = 0; k < checkLen; k++) {
                        if (start1.charAt(k) != start2.charAt(k)) startDiff++; // Считаем различия в начале
                        if (end1.charAt(k) != end2.charAt(k)) endDiff++; // Считаем различия в конце
                    }
                    
                    // Если начало или конец сильно различаются, пропускаем
                    if (startDiff > 20 || endDiff > 20) {
                        continue; // Файлы слишком разные, пропускаем
                    }
                }
                
                int distance = levenshteinDistance(content1, content2); // Вычисляем расстояние Левенштейна
                if (distance < 10) {
                    fileCopies.add(files.get(j).path); // Если расстояние < 10, это копия
                }
            }
            if (!fileCopies.isEmpty()) {
                copies.put(files.get(i).path, fileCopies); // Добавляем файл и его копии в результат
            }
        }
        
        // Выводим результаты
        for (Map.Entry<String, List<String>> entry : copies.entrySet()) {
            System.out.println(entry.getKey()); // Выводим путь к оригинальному файлу
            Collections.sort(entry.getValue()); // Сортируем список копий
            for (String copy : entry.getValue()) {
                System.out.println(copy); // Выводим путь к каждой копии
            }
        }
    }
}


