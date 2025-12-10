package by.it.group410901.galitskiy.lesson07;

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
        return editDistRecursive(one, two, one.length(), two.length());
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }
    private int editDistRecursive(String a, String b, int i, int j) {
        // Базовые случаи: если одна из строк пустая
        if (i == 0) return j; // Нужно j вставок
        if (j == 0) return i; // Нужно i удалений

        // Если последние символы равны, продолжаем без операции
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return editDistRecursive(a, b, i - 1, j - 1);
        }

        // Иначе минимум из трех операций:
        // 1. Удаление (сокращаем i)
        // 2. Вставка (сокращаем j)
        // 3. Замена (сокращаем оба)
        int delete = editDistRecursive(a, b, i - 1, j);     // удаление
        int insert = editDistRecursive(a, b, i, j - 1);     // вставка
        int replace = editDistRecursive(a, b, i - 1, j - 1); // замена

        return 1 + Math.min(delete, Math.min(insert, replace));
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
