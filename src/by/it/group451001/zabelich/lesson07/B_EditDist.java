package by.it.group451001.zabelich.lesson07;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Получаем длины обеих строк
        int len1 = one.length();
        int len2 = two.length();

        // Создаем матрицу для хранения расстояний между подстроками
        // dp[i][j] будет содержать расстояние между первыми i символами первой строки
        // и первыми j символами второй строки
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Инициализация базовых случаев:
        // Преобразование пустой строки в строку длины j требует j операций вставки
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        // Преобразование строки длины i в пустую строку требует i операций удаления
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        // Заполняем матрицу по строкам
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                // Если символы совпадают, берем значение из диагонали без изменений
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Если символы разные, выбираем минимальное из трех возможных операций:
                    // 1. Удаление (dp[i-1][j] + 1)
                    // 2. Вставка (dp[i][j-1] + 1)
                    // 3. Замена (dp[i-1][j-1] + 1)
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        // Результат находится в правом нижнем углу матрицы
        int result = dp[len1][len2];

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
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