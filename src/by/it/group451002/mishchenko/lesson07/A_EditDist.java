package by.it.group451002.mishchenko.lesson07;

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

        // Инициализируем массив для мемоизации. Размерность: (длина one+1) x (длина two+1).
        // Элементы инициализируются значением -1, что означает «не вычислено».
        int[][] memo = new int[one.length() + 1][two.length() + 1];
        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }
        // Вычисляем расстояние редактирования с позиции 0 для обеих строк.
        int result = editDistance(one, two, 0, 0, memo);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивная функция, которая вычисляет расстояние редактирования для строк,
    // начиная с позиций i в строке one и j в строке two.
    private int editDistance(String one, String two, int i, int j, int[][] memo) {
        // Если достигнут конец первой строки, остаётся вставить все оставшиеся символы второй строки.
        if (i == one.length()) {
            return two.length() - j;
        }
        // Если достигнут конец второй строки, остаётся удалить все оставшиеся символы первой строки.
        if (j == two.length()) {
            return one.length() - i;
        }

        // Если результат для данных позиций уже вычислен, возвращаем его.
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Если текущие символы совпадают, переходим к следующим символам без дополнительных расходов.
        if (one.charAt(i) == two.charAt(j)) {
            memo[i][j] = editDistance(one, two, i + 1, j + 1, memo);
        } else {
            // Если символы не совпадают, рассматриваем три варианта:
            // 1. Заменить символ: переходим на (i+1, j+1) с расходом 1.
            int substitution = editDistance(one, two, i + 1, j + 1, memo) + 1;
            // 2. Вставить символ: переходим на (i, j+1) с расходом 1.
            int insertion = editDistance(one, two, i, j + 1, memo) + 1;
            // 3. Удалить символ: переходим на (i+1, j) с расходом 1.
            int deletion = editDistance(one, two, i + 1, j, memo) + 1;
            memo[i][j] = Math.min(substitution, Math.min(insertion, deletion));
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
