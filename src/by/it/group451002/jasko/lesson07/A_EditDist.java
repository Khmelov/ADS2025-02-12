package by.it.group451002.jasko.lesson07;

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

    // Рекурсивный метод вычисления расстояния Левенштейна
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Базовые случаи рекурсии:
        // Если одна из строк пустая, расстояние равно длине другой строки
        if (one.isEmpty()) {
            return two.length(); // Нужно вставить все символы второй строки
        }
        if (two.isEmpty()) {
            return one.length(); // Нужно вставить все символы первой строки
        }

        // Стоимость замены последнего символа:
        // 0, если символы одинаковые, 1 - если разные
        int substitutionCost = (one.charAt(one.length() - 1) == two.charAt(two.length() - 1)) ? 0 : 1;

        // Рекурсивно вычисляем три возможные операции:

        // 1. Замена последнего символа:
        // Сравниваем строки без последних символов + стоимость замены
        int substitution = getDistanceEdinting(
                one.substring(0, one.length() - 1),
                two.substring(0, two.length() - 1)) + substitutionCost;

        // 2. Удаление последнего символа из первой строки:
        // Сравниваем строку без последнего символа с исходной второй строкой + 1 операция
        int deletion = getDistanceEdinting(
                one.substring(0, one.length() - 1), two) + 1;

        // 3. Вставка последнего символа второй строки в первую:
        // Сравниваем исходную первую строку со строкой без последнего символа + 1 операция
        int insertion = getDistanceEdinting(
                one,
                two.substring(0, two.length() - 1)) + 1;

        // Возвращаем минимальное из трех полученных значений
        return Math.min(substitution, Math.min(deletion, insertion));

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        assert stream != null;
        Scanner scanner = new Scanner(stream);

        // Вычисление и вывод расстояния для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}