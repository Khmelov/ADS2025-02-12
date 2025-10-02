package by.it.group451002.koltsov.lesson07;

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
        // создаём таблицу
        int[][] table = new int[one.length() + 1][two.length() + 1];

        // заполняем начальными значениями
        for (int i = 1; i < one.length() + 1; i++) {
            table[i][0] = i;
        }
        for (int i = 1; i < two.length() + 1; i++) {
            table[0][i] = i;
        }

        calcDistance(one, two, table, 1, 1);
        int result = table[one.length()][two.length()];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void calcDistance(String one, String two, int[][] table, int i, int j) {
        // рассчитыванем элемент
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            table[i][j] = Math.min(table[i - 1][j - 1], Math.min(table[i - 1][j], table[i][j-1]));
        }
        else {
            table[i][j] = Math.min(table[i - 1][j - 1], Math.min(table[i - 1][j], table[i][j-1])) + 1;
        }

        // рекурсивно вызываем/не вызываем рассчитывание следущего
        if (i + 1 < one.length() + 1) {
            calcDistance(one, two, table, ++i, j);
        }
        else if (j + 1 < two.length() + 1) {
            calcDistance(one, two, table, 1, ++j);
        }
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
