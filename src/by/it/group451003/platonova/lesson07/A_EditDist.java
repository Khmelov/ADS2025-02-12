package by.it.group451003.platonova.lesson07;

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

    // Рекурсивная функция с мемоизацией для вычисления расстояния Левенштейна
    private int editDistanceRecursive(String one, String two, int i, int j, Integer[][] memo) {
        // Базовые случаи
        if (i == 0) return j; // Если первая строка пуста, вернуть длину второй строки
        if (j == 0) return i; // Если вторая строка пуста, вернуть длину первой строки

        // Проверяем, вычислялось ли уже это значение
        if (memo[i][j] != null) {
            return memo[i][j];
        }

        // Если символы равны, никаких операций не требуется
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = editDistanceRecursive(one, two, i - 1, j - 1, memo);
            return memo[i][j];
        }

        // Вычисляем минимум из трех операций:
        // 1. Вставка (рекурсивный вызов для первой строки и второй строки без последнего символа + 1)
        // 2. Удаление (рекурсивный вызов для первой строки без последнего символа и второй строки + 1)
        // 3. Замена (рекурсивный вызов для обеих строк без последних символов + 1)
        int insert = editDistanceRecursive(one, two, i, j - 1, memo);
        int delete = editDistanceRecursive(one, two, i - 1, j, memo);
        int replace = editDistanceRecursive(one, two, i - 1, j - 1, memo);

        // Выбираем минимум из трех операций и добавляем 1 (стоимость операции)
        memo[i][j] = 1 + Math.min(Math.min(insert, delete), replace);
        return memo[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Создаем массив для мемоизации
        Integer[][] memo = new Integer[one.length() + 1][two.length() + 1];

        // Вызываем рекурсивную функцию
        int result = editDistanceRecursive(one, two, one.length(), two.length(), memo);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
