package by.it.group451001.kazakov.lesson07;

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

        // инициализация memo значением -1 (не вычислено)
        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }

        return recursive(one, two, one.length(), two.length(), memo);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    int recursive(String a, String b, int i, int j, int[][] memo) {
        // если результат уже вычислен — возвращаем
        if (memo[i][j] != -1) return memo[i][j];

        // базовые случаи
        if (i == 0) return memo[i][j] = j; // нужно вставить все j символов
        if (j == 0) return memo[i][j] = i; // нужно удалить все i символов

        // если последние символы равны — переход без изменений
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            memo[i][j] = recursive(a, b, i - 1, j - 1, memo);
        } else {
            // иначе — минимум из 3 операций: вставка, удаление, замена
            int insert = recursive(a, b, i, j - 1, memo);
            int delete = recursive(a, b, i - 1, j, memo);
            int replace = recursive(a, b, i - 1, j - 1, memo);
            memo[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
        }

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