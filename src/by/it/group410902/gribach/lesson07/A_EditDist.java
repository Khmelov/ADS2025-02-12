package by.it.group410902.gribach.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

    // memo[i][j] хранит минимальное расстояние между первыми i символами строки a и первыми j символами строки b
    private int[][] memo;

    // Рекурсивная функция с мемоизацией для вычисления редакционного расстояния
    private int dp(String a, String b, int i, int j) {
        // Если строка a пуста, нужно j вставок, чтобы получить b
        if (i == 0) return j;

        // Если строка b пуста, нужно i удалений, чтобы из a получить пустую строку
        if (j == 0) return i;

        // Если уже вычисляли это подзадачу — возвращаем из кэша
        if (memo[i][j] != -1) return memo[i][j];

        // Стоимость замены: если символы равны — 0, иначе — 1
        int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

        // Опции:
        int delete = dp(a, b, i - 1, j) + 1;         // Удаление символа из a
        int insert = dp(a, b, i, j - 1) + 1;         // Вставка символа в a
        int replace = dp(a, b, i - 1, j - 1) + cost; // Замена символа в a (если нужно)

        // Минимальное из трёх возможных действий
        int res = Math.min(delete, Math.min(insert, replace));

        // Сохраняем результат в таблицу
        memo[i][j] = res;

        return res;
    }

    // Метод-обёртка, вызывающий рекурсивную функцию и инициализирующий таблицу мемоизации
    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // Инициализация таблицы для хранения результатов подзадач
        memo = new int[n + 1][m + 1];

        // Заполняем таблицу значениями -1 (ещё не вычисляли)
        for (int i = 0; i <= n; i++) {
            Arrays.fill(memo[i], -1);
        }

        // Вызываем основную рекурсивную функцию
        return dp(one, two, n, m);
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
