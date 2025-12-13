package by.it.group410901.kvitchenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

// Расширяет SourceScannerA.
public class SourceScannerC extends SourceScannerA{

    // Максимальное допустимое расстояние Левенштейна (редакционное расстояние) между файлами,
    // чтобы считать их "одинаковыми" (клонами).
    static final int NORMAL_DISTANCE = 9;

    // ## Вспомогательные методы для расстояния Левенштейна

    // Возвращает 0, если символы совпадают (нет замены), и 1, если не совпадают (требуется замена).
    private static int areReplacementNumbers(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    // Находит минимальное значение среди переданных чисел (используется для выбора минимальной операции:
    // замена, вставка или удаление).
    private static int getMinEdit(int... numbers) {
        return Arrays.stream(numbers).min().orElse(
                Integer.MAX_VALUE);
    }

    // ## Основной метод сравнения: Вычисление расстояния Левенштейна
    // Сравнивает две очищенные строки кода (file1 и file2).
    private static boolean checkDistance(String file1, String file2) {
        // Ранний выход: если разница в длине строк слишком велика, считать их разными.
        int distance = Math.abs(file1.length() - file2.length());
        if (distance > NORMAL_DISTANCE)
            return false;

        String s1, s2;
        // Разделение очищенного кода на токены (слова/идентификаторы/операторы, разделенные пробелами).
        String[] array_s1 = file1.split(" "), array_s2 = file2.split(" ");

        // Если количество токенов разное, файлы считаются разными.
        if (array_s1.length != array_s2.length)
            return false;

        // Итерация по парам токенов и вычисление расстояния Левенштейна для каждой пары.
        for (int index = 0; index < array_s1.length; index++) {
            s1 = array_s1[index];
            s2 = array_s2[index];

            // Инициализация переменных для алгоритма динамического программирования (расстояние Левенштейна).
            int length = s2.length() + 1;
            int[] currRow = new int[length];
            int[] prevRow;

            // Внешний цикл по символам первой строки (s1).
            for (int i = 0; i <= s1.length(); i++) {
                prevRow = currRow;
                currRow = new int[length];

                // Внутренний цикл по символам второй строки (s2).
                for (int j = 0; j <= s2.length(); j++) {
                    // Вычисление стоимости ячейки:
                    currRow[j] = i == 0 ? j : (j == 0 ? i : getMinEdit(
                            // 1. Замена/Совпадение (diagonal)
                            prevRow[j - 1] + areReplacementNumbers(s1.charAt(i - 1), s2.charAt(j - 1)),
                            // 2. Удаление (top)
                            prevRow[j] + 1,
                            // 3. Вставка (left)
                            currRow[j - 1] + 1));
                }
            }
            // Добавление редакционного расстояния для текущей пары токенов к общему расстоянию.
            distance += currRow[s2.length()];

            // Ранний выход, если общее расстояние превысило лимит.
            if (distance > NORMAL_DISTANCE)
                return false;
        }
        return true; // Файлы считаются клонами.
    }

    // ## Компаратор для сортировки списков путей
    protected static class myArrayComparator implements Comparator<ArrayList<Path>> {
        @Override
        public int compare(ArrayList<Path> a1, ArrayList<Path> a2) {
            // Сортировка путей внутри каждого списка для обеспечения детерминированного сравнения.
            Collections.sort(a1);
            Collections.sort(a2);
            // Сравнение по первому элементу (пути) в отсортированном списке.
            return a1.getFirst().compareTo(a2.getFirst());
        }
    }

    // ## Метод поиска групп одинаковых файлов
    private static ArrayList<ArrayList<Path>> findEqualFiles(HashMap<Path, String> filePaths) {
        ArrayList<ArrayList<Path>> equalFiles = new ArrayList<>();
        ArrayList<Path> array, used = new ArrayList<>();

        // Перебор всех файлов для поиска клонов.
        for(Path filePath1 : filePaths.keySet()) {
            if (!used.contains(filePath1)) { // Пропускаем файлы, которые уже были включены в группу.
                array = new ArrayList<>();
                array.add(filePath1);

                // Сравнение filePath1 со всеми остальными.
                for (Path filePath2 : filePaths.keySet())
                    // Проверка, что файл другой и проходит проверку расстояния Левенштейна.
                    if (filePath1 != filePath2 && checkDistance(filePaths.get(filePath1), filePaths.get(filePath2))) {
                        array.add(filePath2);
                        used.add(filePath2); // Помечаем как использованный.
                    }
                // Добавляем группу, если в ней больше одного файла (т.е. найден клон).
                if (array.size() > 1)
                    equalFiles.add(array);
            }
        }
        return equalFiles;
    }

    // ## Вывод групп клонов
    private static void findCopies(HashMap<String, HashMap<Path, String>> classes) {
        ArrayList<ArrayList<Path>> equalFiles;
        Set<String> classNames = classes.keySet();
        int count;

        // Итерация по именам классов.
        for (String className : classNames) {
            count = 0;
            // Поиск клонов для текущего класса.
            equalFiles = findEqualFiles(classes.get(className));

            // Сортировка найденных групп клонов.
            Collections.sort(equalFiles, new myArrayComparator());

            if (!equalFiles.isEmpty()) {
                // Вывод заголовка и результатов.
                System.out.println("\n---" + className + "---");
                for (ArrayList<Path> paths : equalFiles) {
                    System.out.println("\nClones №" + ++count);
                    for (Path path : paths)
                        System.out.println(path);
                }
            }
        }
    }

    // ## Сбор и предобработка кода
    protected static void getInformation() throws IOException {
        // Карта: Имя_файла -> (Путь_к_файлу -> Очищенный_код)
        HashMap<String, HashMap<Path, String>> javaClasses = new HashMap<>();

        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        if (directory.toString().endsWith(".java")) {
                            try {
                                char[] charArr;
                                String str = Files.readString(directory);
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {

                                    // Шаги 1-3: Удаление пакетов/импортов, комментариев и пустых строк (унаследовано от B).
                                    str = str.replaceAll("package.*;", "")
                                            .replaceAll("import.*;", "");
                                    str = str.replaceAll("/\\*[\\w\\W\r\n\t]*?\\*/", "")
                                            .replaceAll("//.*?\r\n\\s*", "");
                                    while (str.contains("\r\n\r\n"))
                                        str = str.replaceAll("\r\n\r\n", "\r\n");

                                    // Шаг 4: Удаление/обрезка "мусорных" символов с концов (унаследовано от A).
                                    if (!str.isEmpty() && (str.charAt(0) < 33 || str.charAt(str.length() - 1) < 33)) {
                                        charArr = str.toCharArray();
                                        int indexF = 0, indexL = charArr.length - 1;
                                        while (indexF < charArr.length && charArr[indexF] < 33 && charArr[indexF] != 0)
                                            charArr[indexF++] = 0;
                                        while (indexL >= 0 && charArr[indexL] < 33 && charArr[indexL] != 0)
                                            charArr[indexL--] = 0;
                                        str = new String(move(charArr));
                                    }

                                    // Шаг 5: Нормализация кода. Замена всех пробельных символов (пробелы, табы, переводы строки, NUL)
                                    // на ОДИН пробел для токенизации и сравнения.
                                    str = str.replaceAll("[\u0000- ]++", " ");

                                    // Сохранение очищенного кода по имени класса.
                                    if (!javaClasses.containsKey(directory.getFileName().toString()))
                                        javaClasses.put(directory.getFileName().toString(), new HashMap<>());
                                    javaClasses.get(directory.getFileName().toString()).put(src.relativize(directory), str);
                                }
                            } catch (IOException e) {
                                if (System.currentTimeMillis() < 0) {
                                    System.err.println(directory);
                                }
                            }
                        }
                    }
            );
            // Запуск поиска клонов.
            findCopies(javaClasses);
        }
    }

    // Точка входа в программу.
    public static void main(String[] args) throws IOException {
        getInformation();
    }
}