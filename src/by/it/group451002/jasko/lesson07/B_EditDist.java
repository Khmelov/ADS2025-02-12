package by.it.group451002.jasko.lesson07;

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

    // Метод вычисления расстояния Левенштейна
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Получаем длины строк
        int n = one.length(); // Длина первой строки
        int m = two.length(); // Длина второй строки

        // Создаем матрицу dp размером (n+1) x (m+1)
        // dp[i][j] будет содержать расстояние между подстроками one[0..i-1] и two[0..j-1]
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация базовых случаев:

        // Если вторая строка пустая, нужно удалить все символы первой строки
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // i операций удаления
        }

        // Если первая строка пустая, нужно вставить все символы второй строки
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // j операций вставки
        }

        // Заполняем матрицу dp:
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Проверяем, совпадают ли текущие символы
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Если символы совпадают, стоимость не увеличивается
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Если символы разные, выбираем минимальную стоимость из трех операций:
                    dp[i][j] = Math.min(dp[i - 1][j - 1], // Замена
                            Math.min(dp[i - 1][j],        // Удаление
                                    dp[i][j - 1])) + 1;   // Вставка
                }
            }
        }
       //   0 к и т
       // 0 0 1 2 3
       // к 1 0 1 2
       // о 2 1 1 2
       // т 3 2 2 1

        // Результат находится в правом нижнем углу матрицы
        return dp[n][m];

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        assert stream != null;
        Scanner scanner = new Scanner(stream);

        // Вычисление и вывод расстояния для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}