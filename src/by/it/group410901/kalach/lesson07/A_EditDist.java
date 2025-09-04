package by.it.group410901.kalach.lesson07;

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

    // Кэш для мемоизации результатов
    private Integer[][] memo;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Инициализация массива для мемоизации
        memo = new Integer[one.length() + 1][two.length() + 1];
        // Вызов рекурсивной функции с мемоизацией
        return levenshteinRecursive(one, two, one.length(), two.length());
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    private int levenshteinRecursive(String one, String two, int m, int n) {
        // Базовые случаи
        if (m == 0) return n; // Если первая строка пуста, нужно n вставок
        if (n == 0) return m; // Если вторая строка пуста, нужно m удалений

        // Проверка кэша мемоизации
        if (memo[m][n] != null) {
            return memo[m][n];
        }

        // Если последние символы совпадают, операция не нужна
        if (one.charAt(m - 1) == two.charAt(n - 1)) {
            memo[m][n] = levenshteinRecursive(one, two, m - 1, n - 1);
        } else {
            // Минимальная из трех операций: вставка, удаление, замена
            memo[m][n] = 1 + Math.min(
                    Math.min(
                            levenshteinRecursive(one, two, m, n - 1), // Вставка
                            levenshteinRecursive(one, two, m - 1, n)   // Удаление
                    ),
                    levenshteinRecursive(one, two, m - 1, n - 1)   // Замена
            );
        }

        return memo[m][n];
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