package by.it.group410901.bukshta.lesson07;

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

    // Массив для мемоизации результатов рекурсии
    private Integer[][] memo;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Инициализируем массив memo размером (m+1) x (n+1), где m и n — длины строк
        memo = new Integer[one.length() + 1][two.length() + 1];
        // Вызываем рекурсивную функцию
        int result = editDistance(one, two, one.length(), two.length());
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивная функция с мемоизацией для вычисления расстояния Левенштейна
    private int editDistance(String one, String two, int m, int n) {
        // Если результат уже вычислен, возвращаем его из memo
        if (memo[m][n] != null) {
            return memo[m][n];
        }

        // Базовые случаи
        if (m == 0) {
            // Если первая строка пуста, нужно вставить n символов
            return n;
        }
        if (n == 0) {
            // Если вторая строка пуста, нужно удалить m символов
            return m;
        }

        // Если текущие символы совпадают, не требуется операций
        if (one.charAt(m - 1) == two.charAt(n - 1)) {
            memo[m][n] = editDistance(one, two, m - 1, n - 1);
        } else {
            // Иначе выбираем минимум из трех операций:
            // 1. Замена: editDistance(m-1, n-1) + 1
            // 2. Вставка: editDistance(m, n-1) + 1
            // 3. Удаление: editDistance(m-1, n) + 1
            memo[m][n] = 1 + Math.min(
                    Math.min(editDistance(one, two, m - 1, n - 1), // замена
                            editDistance(one, two, m, n - 1)),      // вставка
                    editDistance(one, two, m - 1, n)                // удаление
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