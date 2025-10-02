package by.it.group410902.yarmashuk.lesson07;

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

    private int[][] dp;
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        int lenOne = one.length();
        int lenTwo = two.length();
        dp = new int[lenOne + 1][lenTwo + 1];
        // Инициализация таблицы значением -1
        for (int i = 0; i <= lenOne; i++) {
            for (int j = 0; j <= lenTwo; j++) {
                dp[i][j] = -1;
            }
        }
        return calculateDistance(lenOne, lenTwo, one, two);
    }
    private int calculateDistance(int i, int j, String one, String two) {
        // Базовые случаи
        if (i == 0) return j; // Если первая строка пустая
        if (j == 0) return i; // Если вторая строка пустая
        // Проверка, уже ли вычислено
        if (dp[i][j] != -1) return dp[i][j];
        // Если последние символы одинаковы, игнорируем их и рекурсивно вызываем для оставшейся подстроки
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            dp[i][j] = calculateDistance(i - 1, j - 1, one, two);
        } else {
            // Вычисляем минимум из трех операций
            int insert = calculateDistance(i, j - 1, one, two); // Вставка
            int delete = calculateDistance(i - 1, j, one, two); // Удаление
            int replace = calculateDistance(i - 1, j - 1, one, two); // Замена
            dp[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
        }
        return dp[i][j];
    }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
