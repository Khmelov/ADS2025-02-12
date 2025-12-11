package by.it.group451003.plyushchevich.lesson07;

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
        int n = one.length();
        int m = two.length();

        int[][] D = new int[n + 1][m + 1];

        // Заполнение первой строки и первого столбца
        for (int i = 0; i <= n; i++) {
            D[i][0] = i;  // удаление всех символов
        }
        for (int j = 0; j <= m; j++) {
            D[0][j] = j;  // вставка всех символов
        }

        // Основной цикл
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                D[i][j] = Math.min(
                        Math.min(
                                D[i - 1][j] + 1,       // удаление
                                D[i][j - 1] + 1        // вставка
                        ),
                        D[i - 1][j - 1] + cost    // замена
                );
            }
        }

        return D[n][m];
    }

    public static void main(String[] args) {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // 0
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // 3
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // 5
    }
}
