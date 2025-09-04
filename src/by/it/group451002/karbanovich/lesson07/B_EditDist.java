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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Вызываем функцию для получения расстояния редактирования
        int result = editDistBU(one, two);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int editDistBU(String one, String two) {
        // Инициализация двумерного массива (таблички)
        int[][] levenTable = new int[one.length() + 1][two.length() + 1];

        // Заполняем первый столбец соответствующими индексами
        for (int i = 0; i <= one.length(); i++) {
            levenTable[i][0] = i;
        }

        // Заполняем первую строку соответствующими индексами
        for (int j = 0; j <= two.length(); j++) {
            levenTable[0][j] = j;
        }

        // Итерационно заполняем табличку Левенштейна
        for (int i = 1; i <= one.length(); i++) {
            for (int j = 1; j <= two.length(); j++) {
                int c = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                levenTable[i][j] = Math.min(Math.min(levenTable[i - 1][j] + 1, levenTable[i][j - 1] + 1),
                        levenTable[i-1][j-1] + c);
            }
        }

        // Возвращаем расстояние редактирования (значение последней ячейки)
        return levenTable[one.length()][two.length()];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}