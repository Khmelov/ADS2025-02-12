package by.it.group410901.galitskiy.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();

        // dp[i][j] — минимальное количество операций,
        // чтобы преобразовать первые i символов строки one в первые j символов строки two
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация первой строки и первого столбца:
        // Преобразование в пустую строку — это i удалений или j вставок
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // удалить i символов
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // вставить j символов
        }

        // Основной цикл динамики
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Если символы равны, то операция не требуется
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Выбираем минимум из трех операций:
                // 1. Замена (i-1, j-1) + cost
                // 2. Вставка (i, j-1) + 1
                // 3. Удаление (i-1, j) + 1
                dp[i][j] = Math.min(
                        dp[i - 1][j - 1] + cost,
                        Math.min(
                                dp[i][j - 1] + 1,
                                dp[i - 1][j] + 1
                        )
                );
            }
        }

        // Последняя ячейка содержит ответ — расстояние редактирования
        return dp[n][m];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}