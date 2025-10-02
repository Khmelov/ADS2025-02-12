package by.it.group451002.yeshmanski.lesson07;

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


    int getDistanceEdinting(String one, String two)
    {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length();
        int n = two.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++)
        {
            for (int j = 0; j <= n; j++)
            {
                if (i == 0)
                {
                    // Если первая строка пустая, количество операций — длина второй строки
                    dp[i][j] = j;
                } else if (j == 0)
                {
                    // Если вторая строка пустая, количество операций — длина первой строки
                    dp[i][j] = i;
                } else {
                    // Определяем стоимость замены (0 — если символы совпадают, 1 — если отличаются)
                    int replaceCost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

                    //Math.min выбирает минимальную стоимость из трёх возможных операций:
                    dp[i][j] = Math.min(Math.min(

                            dp[i - 1][j] + 1, // удаление
                            dp[i][j - 1] + 1), // вставка
                            dp[i - 1][j - 1] + replaceCost); // замена
                }
            }
        }
        int result =  dp[m][n];
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