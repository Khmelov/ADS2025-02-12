package by.bsuir.dsa.csv2025.gr410901.Абакумов;

import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class Solution {

    public static void main(String[] args) {
        Solution instance = new Solution();
        instance.processQueries(System.in);
    }

    long[] processQueries(InputStream stream) {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        // Читаем матрицу
        long[][] matrix = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scanner.nextLong();
            }
        }
        
        // Строим двумерный массив префиксных сумм
        // prefix[i][j] = сумма всех элементов от (0,0) до (i-1, j-1)
        long[][] prefix = new long[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                prefix[i][j] = matrix[i-1][j-1] 
                             + prefix[i-1][j] 
                             + prefix[i][j-1] 
                             - prefix[i-1][j-1];
            }
        }
        
        int k = scanner.nextInt();
        long[] results = new long[k];
        
        // Обрабатываем запросы
        for (int q = 0; q < k; q++) {
            int x1 = scanner.nextInt() - 1; // Переводим в 0-based индексацию
            int y1 = scanner.nextInt() - 1;
            int x2 = scanner.nextInt() - 1;
            int y2 = scanner.nextInt() - 1;
            
            // Используем формулу для суммы в прямоугольнике:
            // sum = prefix[x2+1][y2+1] - prefix[x1][y2+1] - prefix[x2+1][y1] + prefix[x1][y1]
            long sum = prefix[x2 + 1][y2 + 1] 
                     - prefix[x1][y2 + 1] 
                     - prefix[x2 + 1][y1] 
                     + prefix[x1][y1];
            
            results[q] = sum;
        }
        // Выводим все ответы через Enter одним обращением к потоку вывода
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < results.length; i++) {
            if (i > 0) sb.append('\n');
            sb.append(results[i]);
        }
        System.out.println(sb);
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        scanner.close();
        return results;
    }
    // Тесты
    @Test(timeout = 1000)
    public void testSmallMatrix() throws Exception {
        // Тест на маленькой матрице
        String input = "2 2\n1 2\n3 4\n2\n1 1 1 1\n1 1 2 2";
        InputStream stream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.processQueries(stream);
        // Запрос 1: (1,1) до (1,1) = 1
        // Запрос 2: (1,1) до (2,2) = 1+2+3+4 = 10
        boolean ok = (results.length == 2) && (results[0] == 1) && (results[1] == 10);
        assertTrue("Small matrix test failed", ok);
    }
    
    @Test(timeout = 1000)
    public void testSingleElement() throws Exception {
        // Тест на одном элементе
        String input = "1 1\n5\n1\n1 1 1 1";
        InputStream stream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.processQueries(stream);
        boolean ok = (results.length == 1) && (results[0] == 5);
        assertTrue("Single element test failed", ok);
    }
    
    @Test(timeout = 1000)
    public void testNegativeNumbers() throws Exception {
        // Тест с отрицательными числами
        String input = "2 2\n-1 -2\n-3 -4\n1\n1 1 2 2";
        InputStream stream = new java.io.ByteArrayInputStream(input.getBytes());
        Solution instance = new Solution();
        long[] results = instance.processQueries(stream);
        // Сумма всех элементов: -1-2-3-4 = -10
        boolean ok = (results.length == 1) && (results[0] == -10);
        assertTrue("Negative numbers test failed", ok);
    }
}
