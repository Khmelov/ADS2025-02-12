package by.it.group451004.redko.lesson07;

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
    private Integer[][] memo; // Массив для мемоизации

    int getDistanceEdinting(String one, String two) {
        memo = new Integer[one.length()][two.length()];
        return calculateDistance(one, two, one.length() - 1, two.length() - 1);
    }

    private int calculateDistance(String s1, String s2, int i, int j) {
        // Базовые случаи
        if (i < 0) return j + 1; // Если s1 пустая, нужно вставить все символы s2
        if (j < 0) return i + 1; // Если s2 пустая, нужно удалить все символы s1

        // Если результат уже вычислен, возвращаем его
        if (memo[i][j] != null) {
            return memo[i][j];
        }

        // Если символы совпадают, переходим к следующим символам
        if (s1.charAt(i) == s2.charAt(j)) {
            memo[i][j] = calculateDistance(s1, s2, i - 1, j - 1);
            return memo[i][j];
        }

        // Рассматриваем три возможные операции
        int insert = calculateDistance(s1, s2, i, j - 1) + 1;
        int delete = calculateDistance(s1, s2, i - 1, j) + 1;
        int replace = calculateDistance(s1, s2, i - 1, j - 1) + 1;

        // Выбираем минимальное из трех значений
        memo[i][j] = Math.min(Math.min(insert, delete), replace);
        return memo[i][j];
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
