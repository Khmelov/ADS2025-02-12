package by.bsuir.dsa.csv2025.gr410901.Лобач;



import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/*
Задача на программирование: подсчет суммы элементов в подмассиве

Дано:
    Массив целых чисел размером n (1 <= n <= 10^5)
    Количество запросов q (1 <= q <= 10^5)
    Каждый запрос задает подмассив [l, r] (1 <= l <= r <= n, индексация с 1)
    Элементы массива - целые числа от -10^9 до 10^9

Необходимо:
    Для каждого запроса вывести сумму всех элементов в указанном подмассиве.
    
    Использовать алгоритм префиксных сумм (Prefix Sums) для эффективного
    ответа на запросы за O(1) после предобработки за O(n).

Решить задачу МЕТОДАМИ ПРЕФИКСНЫХ СУММ

    Sample Input 1:
    5
    1 4 2 10 3
    3
    1 3
    2 4
    1 5
    
    Sample Output 1:
    7
    16
    20
    
    Пояснение:
    Запрос 1: сумма элементов с индексами 1-3: 1 + 4 + 2 = 7
    Запрос 2: сумма элементов с индексами 2-4: 4 + 2 + 10 = 16
    Запрос 3: сумма элементов с индексами 1-5: 1 + 4 + 2 + 10 + 3 = 20
    
    Sample Input 2:
    6
    -1 4 -2 10 -5 3
    2
    1 6
    3 4
    
    Sample Output 2:
    9
    8
    
    Пояснение:
    Запрос 1: сумма всех элементов: -1 + 4 + (-2) + 10 + (-5) + 3 = 9
    Запрос 2: сумма элементов с индексами 3-4: (-2) + 10 = 8
*/

public class Solution {

    public static void main(String[] args) {
        Solution instance = new Solution();
        instance.calc(System.in);
    }

    long[] calc(InputStream inputStream) {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(inputStream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        
        // Читаем размер массива
        int n = scanner.nextInt();
        
        // Читаем массив
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextLong();
        }
        
        // Строим массив префиксных сумм
        // prefix[i] = сумма элементов от 0 до i-1 (индексация с 0)
        // prefix[0] = 0, prefix[1] = arr[0], prefix[2] = arr[0] + arr[1], ...
        long[] prefix = new long[n + 1];
        prefix[0] = 0;
        for (int i = 1; i <= n; i++) {
            prefix[i] = prefix[i - 1] + arr[i - 1];
        }
        
        // Читаем количество запросов
        int q = scanner.nextInt();
        long[] results = new long[q];
        
        // Обрабатываем запросы
        // Для запроса [l, r] (1-indexed) сумма = prefix[r] - prefix[l-1]
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            int l = scanner.nextInt(); // левая граница (1-indexed)
            int r = scanner.nextInt(); // правая граница (1-indexed)
            
            // Переводим в 0-indexed для работы с массивом
            // l-1 до r-1 включительно
            // Сумма = prefix[r] - prefix[l-1]
            long sum = prefix[r] - prefix[l - 1];
            results[i] = sum;
            
            if (i > 0) sb.append('\n');
            sb.append(sum);
        }
        
        System.out.println(sb);
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        scanner.close();
        return results;
    }
    
    // Тесты
    @Test(timeout = 2000)
    public void test1() throws Exception {
        // Тест 1: Базовый пример
        // Input: 5
        //        1 4 2 10 3
        //        3
        //        1 3
        //        2 4
        //        1 5
        // Output: 7, 16, 20
        String input = "5\n1 4 2 10 3\n3\n1 3\n2 4\n1 5";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 3) && (results[0] == 7) && (results[1] == 16) && (results[2] == 20);
        assertTrue("Test 1 failed: expected [7, 16, 20], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test2() throws Exception {
        // Тест 2: Один элемент
        // Input: 1
        //        5
        //        1
        //        1 1
        // Output: 5
        String input = "1\n5\n1\n1 1";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 1) && (results[0] == 5);
        assertTrue("Test 2 failed: expected [5], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test3() throws Exception {
        // Тест 3: Весь массив
        // Input: 3
        //        10 20 30
        //        1
        //        1 3
        // Output: 60
        String input = "3\n10 20 30\n1\n1 3";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 1) && (results[0] == 60);
        assertTrue("Test 3 failed: expected [60], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test4() throws Exception {
        // Тест 4: Отрицательные числа
        // Input: 6
        //        -1 4 -2 10 -5 3
        //        2
        //        1 6
        //        3 4
        // Output: 9, 8
        String input = "6\n-1 4 -2 10 -5 3\n2\n1 6\n3 4";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 2) && (results[0] == 9) && (results[1] == 8);
        assertTrue("Test 4 failed: expected [9, 8], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test5() throws Exception {
        // Тест 5: Все отрицательные числа
        // Input: 4
        //        -10 -5 -3 -1
        //        2
        //        1 2
        //        3 4
        // Output: -15, -4
        String input = "4\n-10 -5 -3 -1\n2\n1 2\n3 4";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 2) && (results[0] == -15) && (results[1] == -4);
        assertTrue("Test 5 failed: expected [-15, -4], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test6() throws Exception {
        // Тест 6: Один элемент в запросе
        // Input: 5
        //        1 5 3 2 4
        //        3
        //        1 1
        //        3 3
        //        5 5
        // Output: 1, 3, 4
        String input = "5\n1 5 3 2 4\n3\n1 1\n3 3\n5 5";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 3) && (results[0] == 1) && (results[1] == 3) && (results[2] == 4);
        assertTrue("Test 6 failed: expected [1, 3, 4], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test7() throws Exception {
        // Тест 7: Большое количество запросов
        // Input: 10
        //        1 2 3 4 5 6 7 8 9 10
        //        5
        //        1 1
        //        1 2
        //        1 5
        //        5 10
        //        1 10
        // Output: 1, 3, 15, 45, 55
        String input = "10\n1 2 3 4 5 6 7 8 9 10\n5\n1 1\n1 2\n1 5\n5 10\n1 10";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 5) && (results[0] == 1) && (results[1] == 3) && 
                     (results[2] == 15) && (results[3] == 45) && (results[4] == 55);
        assertTrue("Test 7 failed: expected [1, 3, 15, 45, 55], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test8() throws Exception {
        // Тест 8: Нули в массиве
        // Input: 5
        //        0 5 0 -3 0
        //        3
        //        1 5
        //        2 4
        //        3 3
        // Output: 2, 2, 0
        String input = "5\n0 5 0 -3 0\n3\n1 5\n2 4\n3 3";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 3) && (results[0] == 2) && (results[1] == 2) && (results[2] == 0);
        assertTrue("Test 8 failed: expected [2, 2, 0], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test9() throws Exception {
        // Тест 9: Большие числа
        // Input: 3
        //        1000000000 2000000000 -1000000000
        //        2
        //        1 2
        //        1 3
        // Output: 3000000000, 2000000000
        String input = "3\n1000000000 2000000000 -1000000000\n2\n1 2\n1 3";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 2) && (results[0] == 3000000000L) && (results[1] == 2000000000L);
        assertTrue("Test 9 failed: expected [3000000000, 2000000000], got different", ok);
    }
    
    @Test(timeout = 2000)
    public void test10() throws Exception {
        // Тест 10: Смешанные случаи
        // Input: 7
        //        2 -1 5 -3 4 1 -2
        //        4
        //        1 1
        //        1 7
        //        2 5
        //        4 6
        // Output: 2, 6, 5, 2
        String input = "7\n2 -1 5 -3 4 1 -2\n4\n1 1\n1 7\n2 5\n4 6";
        InputStream inputStream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.calc(inputStream);
        boolean ok = (results.length == 4) && (results[0] == 2) && (results[1] == 6) && 
                     (results[2] == 5) && (results[3] == 2);
        assertTrue("Test 10 failed: expected [2, 6, 5, 2], got different", ok);
    }
}
