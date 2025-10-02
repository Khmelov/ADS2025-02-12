package by.it.group451002.karbanovich.lesson07;

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

        // Инициализируем табличку и заполняем её максимальными значениями
        int[][] levenTable = new int[one.length() + 1][two.length() + 1];
        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                levenTable[i][j] = Integer.MAX_VALUE;
            }
        }

        // Вызываем функцию для получения расстояния редактирования
        int result = EditDistTd(levenTable, one.length(), two.length(), one, two);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Функция для получения значения ячейки в таблице Левенштейна
    int EditDistTd(int[][] levenTable, int i, int j, String one, String two) {
        // Если значение не подсчитано
        if (levenTable[i][j] == Integer.MAX_VALUE) {
            if (i == 0) {
                levenTable[i][j] = j;
            }
            else if (j == 0) {
                levenTable[i][j] = i;
            }
            else {
                // Получаем значение левой клетки (вставок)
                int ins = EditDistTd(levenTable, i, j - 1, one, two) + 1;
                // Получаем значение верхней клетки (удалений)
                int del = EditDistTd(levenTable, i - 1, j, one, two) + 1;
                // Получаем значение левой верхней клетки (замен)
                int sub = EditDistTd(levenTable, i - 1, j - 1, one, two) +
                        ((one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1);
                // Выбираем минимальное и записываем его в текущую клетку
                levenTable[i][j] = Math.min(Math.min(ins, del), sub);
            }
        }
        // Возвращаем расстояние редактирования (значение последней ячейки)
        return levenTable[i][j];
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
