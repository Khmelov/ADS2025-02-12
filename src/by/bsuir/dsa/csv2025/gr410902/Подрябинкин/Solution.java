package by.bsuir.dsa.csv2025.gr410902.Подрябинкин;

import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
public class Solution {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String test_data = sc.nextLine();
        if (test_data == "") return;
        String output_data = StationGraphic(test_data);
        System.out.println(output_data);
        sc.close();
    }

    public static String StationGraphic(String input) {

        //Перегоняет вход String в int[]
        String[] parts = input.split(" ");
        int[] D = new int[parts.length]; //Массив входных данных
        for (int i = 0; i < parts.length; i++) {
            D[i] = Integer.parseInt(parts[i]);
        }
        String result = ""; //Результат на вывод
        //Константы задачи
        int time = D.length; //Количество часов работы (в задаче рекомендую добавить +1 час до запуска)
        int p_max = 20; //Максимальная мощность котла
        int start_cost = 10; //Стоимость запуска котла из состояния нулевой мощности
        int delta_p_cost = 20; //Стоимость изменения мощности на 1Вт
        //Стоимость работы 1 час на мощности p: p^2
        int max_delta_p = 5; //Максимально допустимое изменение мощности в начале часа
        int INF = 90000; //Условная бесконечность в задаче
        //============Решение==============

        //В данном решении реализована оптимизация памяти до 2 * O(p_max).

        //Инициализируем массивы стоимости состояния
        int[] dp_prev = new int[p_max + 1];
        int[] dp_cur = new int[p_max + 1];
        for (int j = 0; j <= p_max; j++) {
            dp_prev[j] = INF;
            dp_cur[j] = INF;
        }

        //Нулевое состояние (предсостояние перед запуском)
        dp_prev[0] = 0;

        //Считаем слои
        for (int i = 1; i <= time; i++) {
            for (int j = D[i - 1]; j <= p_max; j++) {
                for (int k = j - max_delta_p; k <= j + max_delta_p && k <= p_max; k++) {
                    if (k < 0) k = 0;
                    int cost = 0;
                    if (k == 0 && j != 0) cost += start_cost;
                    cost += Math.abs(j - k) * delta_p_cost + j * j;
                    if (dp_cur[j] > cost + dp_prev[k]) {
                        dp_cur[j] = cost + dp_prev[k];
                    }
                }
            }
            //Берём следующий слой
            dp_prev = dp_cur.clone();
            if (i != time) {
                for (int a = 0; a <= p_max; a++)
                    dp_cur[a] = INF;
            }

        }

        //поиск минимально возможных расходов за день
        int minimum_cost = INF;
        for (int j = 0; j <= p_max; j++) {
            if (dp_cur[j] < minimum_cost) {
                minimum_cost = dp_cur[j];
            }
        }
        result = "" + minimum_cost;
        //============Конец решения==============


        return result;
    }


    public String runTest(String input){
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Solution.main(new String[]{});
        String result = output.toString().trim();
        return result;
    }
    //Тест для проверки вашего кода
    @Test
    public void RunAllTests() throws Exception {
        assertEquals("135", runTest("0 0 5\n"));
        assertEquals("160", runTest("0 5 0\n"));
        assertEquals("335", runTest("0 0 10\n"));
        assertEquals("202", runTest("5 0 0 0\n"));
        assertEquals("577", runTest("0 10 5 5 5 0 0 0\n"));
        assertEquals("577", runTest("0 10 5 5 5 3 1 1\n"));
        assertEquals("90000", runTest("20\n"));
        assertEquals("360", runTest("5 0 10\n"));
        assertEquals("1160", runTest("0 5 10 20\n"));
        assertEquals("202", runTest("5 1 1 1\n"));
        assertEquals("1160", runTest("0 0 0 20\n"));
        assertEquals("202", runTest("5 0 0 2\n"));
    }
}