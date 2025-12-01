package by.it.group410902.derzhavskaya_ludmila.lesson07;

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
// Получаем длины строк
        int m = one.length();
        int n = two.length();

        // Создаем двумерный массив для хранения расстояний между подстроками
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев:
        // Если вторая строка пустая, нужно удалить все символы из первой строки
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        // Если первая строка пустая, нужно вставить все символы второй строки
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполняем массив dp
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, берем значение из предыдущей подстроки
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Иначе выбираем минимальное из трех возможных операций:
                    // 1. Удаление (dp[i-1][j] + 1)
                    // 2. Вставка (dp[i][j-1] + 1)
                    // 3. Замена (dp[i-1][j-1] + 1)
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        // Результат находится в правом нижнем углу массива dp
        int result = dp[m][n];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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