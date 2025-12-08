package by.bsuir.dsa.csv2025.gr410901.Гетманчук; ////////////////////////////////////////////////////// ТЕСТЫ_ТЕСТЫ //////////////////////////////////////////////////////

import static org.junit.Assert.*;

public class Test {

    static final String[] CASES = new String[]{
        "abacabad",
        "xyzyxzyx",
        "11223311",
        "ababacac",
        "mnomnomn",
        "pqqrpqqr",
        "a1b2a1b2",
        "xyzxyzyz",
        "11234112",
        "abcdeabc"
    };

    // Считывание тестовой строки по номеру
    static String readCase(int index) throws Exception {
        int i = index - 1;
        if (i < 0 || i >= CASES.length) return "";
        return CASES[i];
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
