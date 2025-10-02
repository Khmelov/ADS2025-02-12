package by.it.group451002.kravtsov.lesson07;

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
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

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

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        // между двумя строками, определяя минимальное количество операций (вставка, удаление, замена)
        // Создаём таблицу для хранения промежуточных значений
        int[][] dp = new int[n + 1][m + 1];

        // Заполняем базовые случаи
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // Удаление всех символов из первой строки
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // Вставка всех символов второй строки
        }

        // Заполняем таблицу динамическим программированием
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Если символы совпадают — берём предыдущее значение
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Вычисляем минимальную стоимость операции
                    dp[i][j] = Math.min(dp[i - 1][j],       // Удаление
                            Math.min(dp[i][j - 1],      // Вставка
                                    dp[i - 1][j - 1]   // Замена
                            )) + 1;
                }
            }
        }
        return dp[n][m]; // Расстояние Левенштейна
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        // Читаем строки из файла и вычисляем расстояние Левенштейна
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
