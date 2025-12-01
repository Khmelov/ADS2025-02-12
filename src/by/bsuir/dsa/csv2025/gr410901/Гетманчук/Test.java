package by.bsuir.dsa.csv2025.gr410901.Гетманчук; ////////////////////////////////////////////////////// ТЕСТЫ_ТЕСТЫ //////////////////////////////////////////////////////

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class Test {

    // Считывание тестовой строки по номеру из файла конфигурации/тестов
    static String readCase(int index) throws Exception {
        Path cfg = Paths.get("data.txt");
        String testsFile = null;

        // Чтение файла конфигурации и поиск параметра testsFile
        if (Files.exists(cfg)) {
            for (String line : Files.readAllLines(cfg)) {
                int i = line.indexOf('=');
                if (i > 0) {
                    String k = line.substring(0, i).trim();
                    String v = line.substring(i + 1).trim();
                    if (k.equals("testsFile")) testsFile = v;
                }
            }
        }

        // Определение файла содержащего тесты
        Path tf = Paths.get(testsFile != null ? testsFile : "data.txt");

        // Чтение всех строк файла тестов
        List<String> lines = Files.readAllLines(tf);

        // Перевод номера теста к индексу строки
        int i = index - 1;

        // Проверка выхода за границы списка строк
        if (i < 0 || i >= lines.size()) return "";

        // Извлечение строки теста по индексу
        String line = lines.get(i);

        // Удаление префикса вида "1) "
        int p = line.indexOf(") ");
        return p >= 0 ? line.substring(p + 2).trim() : line.trim();
    }


    // Запуск одного тест-кейса
    static void run(int index) throws Exception {

        // Получение входной строки
        String s = readCase(index);

        Main.Result r = Main.encode(s);

        // Вычисление эталонной длины
        int fixed = s.length() * 8;

        // Подсчет длины сжатой последовательности
        int h = Main.bitLength(r.bits);

        // Декодирование строки
        String d = Main.decode(r.bits, r.tree);

        System.out.println(String.format("%s %s", d, r.bits));

        // Проверка, что длина сжатия не превышает длину обычного представления
        assertTrue(h <= fixed);

        // Проверка префиксности всех кодов
        assertTrue(Main.isPrefixFree(r.codes));

        // Проверка совпадения декодированного текста с исходным
        assertEquals(s, d);
    }


    // Тест для первого случая
    @org.junit.Test
    public void Test_01() throws Exception { run(1); }

    // Тест для второго случая
    @org.junit.Test
    public void Test_02() throws Exception { run(2); }

    // Тест для третьего случая
    @org.junit.Test
    public void Test_03() throws Exception { run(3); }

    // Тест для четвертого случая
    @org.junit.Test
    public void Test_04() throws Exception { run(4); }

    // Тест для пятого случая
    @org.junit.Test
    public void Test_05() throws Exception { run(5); }

    // Тест для шестого случая
    @org.junit.Test
    public void Test_06() throws Exception { run(6); }

    // Тест для седьмого случая
    @org.junit.Test
    public void Test_07() throws Exception { run(7); }

    // Тест для восьмого случая
    @org.junit.Test
    public void Test_08() throws Exception { run(8); }

    // Тест для девятого случая
    @org.junit.Test
    public void Test_09() throws Exception { run(9); }

    // Тест для десятого случая
    @org.junit.Test
    public void Test_10() throws Exception { run(10); }

}