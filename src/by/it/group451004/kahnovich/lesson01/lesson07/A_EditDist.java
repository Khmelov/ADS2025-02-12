package by.it.group451004.kahnovich.lesson01.lesson07;

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
        // Начинаем вычислять расстояние Левенштейна между строками one и two

        int result = 99999; // Переменная для хранения минимального расстояния (инициализация большим числом)
        int del = 99999, add = 99999, cng = 99999; // Стоимости операций: удаление, вставка, замена
        int diff = 1; // Стоимость замены символа (1, если символы разные, 0 если одинаковые)
        int n = one.length(), m = two.length(); // Длины строк

        // Базовый случай: если одна из строк пустая, расстояние равно длине другой (нужно столько операций вставки/удаления)
        if (one == "" || two == "")
            return m + n;

        // Если последние символы обеих строк совпадают, замена не нужна — стоимость 0
        if (one.charAt(n - 1) == two.charAt(m - 1)) diff = 0;

        // Рекурсивно считаем:
        // 1) Стоимость удаления последнего символа из первой строки + 1 операция удаления
        del = getDistanceEdinting(one.substring(0, n - 1), two.substring(0, m)) + 1;

        // 2) Стоимость вставки последнего символа второй строки в первую + 1 операция вставки
        add = getDistanceEdinting(one.substring(0, n), two.substring(0, m - 1)) + 1;

        // 3) Стоимость замены последнего символа первой строки на последний символ второй строки
        // (или 0 если символы совпадают) + результат рекурсии по оставшимся частям строк
        cng = getDistanceEdinting(one.substring(0, n - 1), two.substring(0, m - 1)) + diff;

        // Выбираем минимальную из трех операций — это и будет расстоянием Левенштейна для текущих строк
        result = Math.min(del, Math.min(add, cng));

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
