package by.it.group451002.shutko.lesson07;

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
/* В этой задаче требуется вычислить минимальное количество операций (вставка, удаление, замена символа),
необходимых для преобразования одной строки в другую, используя методы динамического программирования.
 */

public class A_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();
        int n = two.length();
        // Создаем матрицу DP размером (m+1) x (n+1), где m и n - длины строк.
        // Первая строка матрицы (i=0) заполняется числами от 0 до n (расстояние от пустой строки)
        // Первый столбец (j=0) заполняется числами от 0 до m (расстояние до пустой строки)
        int[][] dp = new int[m + 1][n + 1];

        /*После заполнения граничных случаев (первая строка и первый столбец) мы переходим к заполнению оставшейся части матрицы.
        Для каждой ячейки dp[i][j] (где i > 0 и j > 0) мы вычисляем минимальное количество операций, сравнивая три варианта: удаление вставка и замена*/

        // Для каждой пары символов сравниваем:
        // Если символы совпадают, берем значение из диагонали (без изменений)
        // Если разные, берем минимальное из трех соседних значений +1 (операция замены)
        // Результат находится в правом нижнем углу матрицы.

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;  // Если первая строка пустая, расстояние равно длине второй
                } else if (j == 0) {
                    dp[i][j] = i;  // Если вторая строка пустая, расстояние равно длине первой
                } else {
                    int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                    // Минимальное количество операций для получения строки two из one
                    dp[i][j] = Math.min(dp[i - 1][j] + 1, Math.min(dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost));
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return dp[m][n];
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