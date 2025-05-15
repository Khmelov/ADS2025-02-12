package by.it.group451003.gontsarevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

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


    private Map<String, Integer> cache = new HashMap<>();

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    int getDistanceEdinting(String one, String two) {
        // Используем кэш для оптимизации
        String key = one + "," + two;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        int result;

        // Если одна из строк пустая, расстояние равно длине другой строки
        if (one.isEmpty()) {
            result = two.length();
        } else if (two.isEmpty()) {
            result = one.length();
        } else {
            // Сравниваем последние символы
            int cost = (one.charAt(one.length() - 1) == two.charAt(two.length() - 1)) ? 0 : 1;

            // Рекурсивно вычисляем три возможных операции
            int substitution = getDistanceEdinting(one.substring(0, one.length() - 1), two.substring(0, two.length() - 1)) + cost;
            int insertion = getDistanceEdinting(one, two.substring(0, two.length() - 1)) + 1;
            int deletion = getDistanceEdinting(one.substring(0, one.length() - 1), two) + 1;

            result = min(substitution, insertion, deletion);
        }

        cache.put(key, result);
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
