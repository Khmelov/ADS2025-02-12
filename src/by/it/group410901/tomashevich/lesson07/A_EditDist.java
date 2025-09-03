package by.it.group410901.tomashevich.lesson07;

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
        int[][] memo = new int[one.length() + 1][two.length() + 1];

        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }

        int result = editDistRecursive(one, two, one.length(), two.length(), memo);

        return result;
    }

    private int editDistRecursive(String one, String two, int i, int j, int[][] memo) {
        // Базовые случаи
        if (i == 0) return j; // Если первая строка пуста, нужно вставить все символы второй строки
        if (j == 0) return i; // Если вторая строка пуста, нужно удалить все символы первой строки

        if (memo[i][j] != -1) return memo[i][j];

        // Если последние символы совпадают, рекурсивно вычисляем для подстрок без последних символов
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = editDistRecursive(one, two, i - 1, j - 1, memo);
            return memo[i][j];
        }

        // Иначе выбираем минимум из трех операций: вставка, удаление, замена
        int insert = editDistRecursive(one, two, i, j - 1, memo);
        int delete = editDistRecursive(one, two, i - 1, j, memo);
        int replace = editDistRecursive(one, two, i - 1, j - 1, memo);

        memo[i][j] = 1 + Math.min(Math.min(insert, delete), replace);
        return memo[i][j];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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
