package by.it.group410902.derzhavskaya_ludmila.lesson15;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

//1. Удалить строку package и все импорты.
//2. Удалить все комментарии за O(n) от длины текста.
//3. Заменить все последовательности символов с кодом <33 на 32 (один пробел), т.е привести текст к строке.
//4. Выполнить trim() для полученной строки.
//Найти группы похожих файлов по расстоянию Левенштейна
//и вывести пути файлов-копий с редактированием меньше 10 символов.
public class SourceScannerC {
    // Максимальное допустимое расстояние редактирования для считания файлов копиями
    static final int NORMAL_DISTANCE = 7;

    // Определяет стоимость замены символа (0 если символы одинаковы, 1 если разные)
    private static int areReplacementNumbers(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    // Находит минимальное значение из переданных чисел
    private static int getMinEdit(int... numbers) {
        return Arrays.stream(numbers).min().orElse(
                Integer.MAX_VALUE);
    }

    // Удаляет нулевые символы из начала и конца массива
    protected static char[] move(char[] array) {
        char[] temp;
        int i = 0, size;

        // Пропускаем нулевые символы в начале массива
        while(array[i] == 0)
            i++;

        // Копируем не нулевую часть массива
        size = array.length - i;
        temp = new char[size];
        System.arraycopy(array, i, temp, 0, size);
        array = temp;

        // Находим последний ненулевой символ с конца
        i = array.length - 1;
        while (array[i] == 0)
            i--;

        // Копируем до последнего ненулевого символа
        size = i + 1;
        temp = new char[size];
        System.arraycopy(array, 0, temp, 0, size);
        return temp;
    }

    // Проверяет, являются ли два файла похожими на основе расстояния Левенштейна
    private static boolean checkDistance(String file1, String file2) {
        // Сначала проверяем разницу в длине - быстрая проверка
        int distance = Math.abs(file1.length() - file2.length());

        // Если разница в длине уже превышает порог, файлы точно не похожи
        if (distance > NORMAL_DISTANCE)
            return false;

        // Разбиваем содержимое на слова по пробелам
        String s1, s2;
        String[] array_s1 = file1.split(" "), array_s2 = file2.split(" ");

        // Вычисляем расстояние Левенштейна для каждой пары слов
        for (int index = 0; index < array_s1.length; index++) {
            s1 = array_s1[index];
            s2 = array_s2[index];
            int length = s2.length() + 1;
            int[] currRow = new int[length];
            int[] prevRow;

            // Алгоритм Вагнера-Фишера для вычисления расстояния Левенштейна
            for (int i = 0; i <= s1.length(); i++) {
                prevRow = currRow;
                currRow = new int[length];

                for (int j = 0; j <= s2.length(); j++) {
                    if (i == 0) {
                        // Если первая строка пустая - стоимость = длина второй строки
                        currRow[j] = j;
                    } else if (j == 0) {
                        // Если вторая строка пустая - стоимость = длина первой строки
                        currRow[j] = i;
                    } else {
                        // Минимальная стоимость из трех операций:
                        // 1. Замена символа
                        // 2. Удаление символа
                        // 3. Вставка символа
                        currRow[j] = getMinEdit(prevRow[j - 1]
                                        + areReplacementNumbers(s1.charAt(i - 1), s2.charAt(j - 1)),
                                prevRow[j] + 1,
                                currRow[j - 1] + 1);
                    }
                }
            }
            // Суммируем расстояния для всех слов
            distance += currRow[s2.length()];
            // Если общее расстояние превысило порог, прекращаем вычисления
            if (distance > NORMAL_DISTANCE)
                return false;
        }
        return true;
    }

    // Компаратор для сортировки списков путей
    protected static class MyArrayComparator implements Comparator<ArrayList<Path>> {
        @Override
        public int compare(ArrayList<Path> a1, ArrayList<Path> a2) {
            // Сортируем каждый список путей
            Collections.sort(a1);
            Collections.sort(a2);

            // Сравниваем по первому пути в каждом списке
            return a1.get(0).compareTo(a2.get(0));
        }
    }

    // Находит группы похожих файлов
    private static ArrayList<ArrayList<Path>> findEqualFiles(HashMap<Path, String> filePaths) {
        ArrayList<ArrayList<Path>> equalFiles = new ArrayList<>();
        ArrayList<Path> array, used = new ArrayList<>(); // used - уже обработанные файлы

        for(Path filePath1 : filePaths.keySet()) {
            // Если файл еще не был обработан
            if (!used.contains(filePath1)) {
                array = new ArrayList<>();
                array.add(filePath1);

                // Сравниваем с остальными файлами
                for (Path filePath2 : filePaths.keySet())
                    if (filePath1 != filePath2 && checkDistance(filePaths.get(filePath1), filePaths.get(filePath2))) {
                        array.add(filePath2);
                        used.add(filePath2); // Помечаем как обработанный
                    }

                // Если нашли похожие файлы (больше 1 в группе)
                if (array.size() > 1)
                    equalFiles.add(array);
            }
        }
        return equalFiles;
    }

    // Основной метод для поиска и вывода копий
    private static void findCopies(HashMap<String, HashMap<Path, String>> classes) {
        ArrayList<ArrayList<Path>> equalFiles;
        Set<String> classNames = classes.keySet();

        int count;

        // Для каждого имени класса (группы файлов с одинаковым именем)
        for (String className : classNames) {
            count = 0;
            // Находим похожие файлы в этой группе
            equalFiles = findEqualFiles(classes.get(className));
            // Сортируем группы файлов
            Collections.sort(equalFiles, new MyArrayComparator());

            // Если нашли копии, выводим информацию
            if (!equalFiles.isEmpty()) {
                System.out.println("\n" + className + ":");
                for (ArrayList<Path> paths : equalFiles) {
                    System.out.println("\nКлон №" + ++count);
                    for (Path path : paths)
                        System.out.println(path);
                }
            }
        }
    }

    // Основной метод обработки файлов
    protected static void getInformation() throws IOException {
        // Структура данных: Имя_файла -> (Путь -> Обработанное_содержимое)
        HashMap<String, HashMap<Path, String>> javaClasses = new HashMap<>();

        Path src = Path.of(System.getProperty("user.dir")
                + File.separator + "src" + File.separator);

        // Рекурсивно обходим все файлы в src
        try (Stream<Path> fileTrees = Files.walk(src)) {
            fileTrees.forEach(
                    directory -> {
                        if (directory.toString().endsWith(".java")) {
                            try {
                                char[] charArr;
                                String str = Files.readString(directory);
                                // Пропускаем тестовые файлы
                                if (!str.contains("@Test") && !str.contains("org.junit.Test")) {
                                    // Удаляем package и import
                                    str = str.replaceAll("package.*;", "")
                                            .replaceAll("import.*;", "");

                                    // Удаляем многострочные и однострочные комментарии
                                    str = str.replaceAll("/\\*[\\w\\W\r\n\t]*?\\*/", "")
                                            .replaceAll("//.*?\r\n\\s*", "");

                                    // Удаляем множественные переносы строк
                                    while (str.contains("\r\n\r\n"))
                                        str = str.replaceAll("\r\n\r\n", "\r\n");

                                    // Обрабатываем непечатаемые символы по краям
                                    if (!str.isEmpty() && (str.charAt(0) < 33 || str.charAt(str.length() - 1) < 33)) {
                                        charArr = str.toCharArray();
                                        int indexF = 0, indexL = charArr.length - 1;

                                        // Заменяем непечатаемые символы в начале на 0
                                        while (indexF < charArr.length && charArr[indexF] < 33 && charArr[indexF] != 0)
                                            charArr[indexF++] = 0;

                                        // Заменяем непечатаемые символы в конце на 0
                                        while (indexL >= 0 && charArr[indexL] < 33 && charArr[indexL] != 0)
                                            charArr[indexL--] = 0;

                                        // Удаляем нулевые символы
                                        str = new String(move(charArr));
                                    }
                                    // Заменяем все последовательности пробельных символов на один пробел
                                    str = str.replaceAll("[\u0000- ]++", " ");

                                    // Сохраняем обработанное содержимое
                                    if (!javaClasses.containsKey(directory.getFileName().toString()))
                                        javaClasses.put(directory.getFileName().toString(), new HashMap<>());
                                    javaClasses.get(directory.getFileName().toString()).put(src.relativize(directory), str);
                                }
                            } catch (IOException e) {
                                // Обработка ошибок ввода-вывода (игнорируем проблемные файлы)
                                if (System.currentTimeMillis() < 0) {
                                    System.err.println(directory);
                                }
                            }
                        }
                    }
            );
            // Запускаем поиск копий после обработки всех файлов
            findCopies(javaClasses);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("FiboA.java");
        getInformation();
    }
}