package by.it.group410902.bobrovskaya.lesson07;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length(); // длина строки 1
        int n = two.length(); // длина строки 2

        int[][] dp = new int[m + 1][n + 1]; // для хранения мин кол-ва операций

        for (int i = 0; i <= m; i++) // вторая строка пустая, нужно удалить i символов из первой
        {
            dp[i][0] = i; // результат = длина 1 строки
        }
        for (int j = 0; j <= n; j++) // первая строка пустая, нужно вставить j символов во вторую
        {
            dp[0][j] = j; // результат = длина 2 строки
        }

        // Динамическое вычисление расстояния Левенштейна
        for (int i = 1; i <= m; i++)
        {
            for (int j = 1; j <= n; j++)
            {
                // соответствующие символы строки 1 и 2 совпадают, то cost = 0 (ничего не меняем)
                // если НЕ совпадают, то cost = 1 (нужна замена)
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // выбирается минимальное количество шагов из трех возможных
                dp[i][j] = Math.min(dp[i - 1][j] + 1, // Удаление
                        Math.min(dp[i][j - 1] + 1, // Вставка
                                dp[i - 1][j - 1] + cost)); // Замена
            }
        }

        int result = dp[m][n]; // Минимальное количество операций

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
