package by.it.group451001.serganovskij.lesson07;

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
*/

public class A_EditDist {

    // Рекурсивная функция для вычисления расстояния Левенштейна между двумя строками
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Инициализируем результат большим числом (как "бесконечность")
        int result = 99999;

        // Получаем длины строк
        int n = one.length();
        int m = two.length();

        // Базовый случай: если одна из строк пустая, расстояние равно длине другой строки
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }

        // Проверяем, совпадают ли последние символы строк
        // Если да, то разница (diff) равна 0, иначе 1
        int diff = (one.charAt(n - 1) == two.charAt(m - 1)) ? 0 : 1;

        // Рекурсивно вычисляем стоимость трех возможных операций:
        // 1. Удаление последнего символа из первой строки
        int del = getDistanceEdinting(one.substring(0, n - 1), two) + 1;

        // 2. Вставка последнего символа второй строки в первую
        int add = getDistanceEdinting(one, two.substring(0, m - 1)) + 1;

        // 3. Замена последнего символа первой строки на последний символ второй (если они разные)
        int cng = getDistanceEdinting(one.substring(0, n - 1), two.substring(0, m - 1)) + diff;

        // Выбираем минимальную стоимость из трех возможных операций
        result = Math.min(del, Math.min(add, cng));

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Создаем экземпляр класса для тестирования
        A_EditDist instance = new A_EditDist();

        // Получаем входные данные из файла
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        Scanner scanner = new Scanner(stream);

        // Читаем и выводим результаты для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}