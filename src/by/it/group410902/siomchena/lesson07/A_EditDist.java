package by.it.group410902.siomchena.lesson07;

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
        return editDistance(one, two, one.length(), two.length());
    }

    private int editDistance(String s1, String s2, int i, int j) {
        if (i == 0) return j;
        if (j == 0) return i;

        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return editDistance(s1, s2, i - 1, j - 1);
        } else {
            int insert = editDistance(s1, s2, i, j - 1);     // вставка
            int delete = editDistance(s1, s2, i - 1, j);     // удаление
            int replace = editDistance(s1, s2, i - 1, j - 1); // замена
            return 1 + Math.min(insert, Math.min(delete, replace));
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
