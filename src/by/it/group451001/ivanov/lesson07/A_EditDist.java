package by.it.group451001.ivanov.lesson07;

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
        int result = levenshtein(one, one.length(), two, two.length());


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private static int levenshtein(String s, int i, String t, int j) {
        // Если одна из строк пустая, расстояние — длина другой строки
        if (i == 0) return j;
        if (j == 0) return i;

        // Вычисляем стоимость замены: 0, если последние символы равны, иначе 1
        int cost = s.charAt(i - 1) == t.charAt(j - 1) ? 0 : 1;

        // Рекурсивно вычисляем минимальное расстояние из трёх вариантов:
        // 1. Удаление символа(из строки s)
        // 2. Вставка символа (в строку s)
        // 3. Замена символа (в строке s)
        int deletion    = levenshtein(s, i - 1, t, j) + 1;
        int insertion   = levenshtein(s, i, t, j - 1) + 1;
        int substitution = levenshtein(s, i - 1, t, j - 1) + cost;

        return Math.min(Math.min(deletion, insertion), substitution);
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
