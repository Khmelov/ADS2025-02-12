package by.it.group451003.plyushchevich.lesson07;

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
            return editDistRec(one, two, one.length(), two.length());
        }

        int editDistRec(String a, String b, int i, int j) {
            // Базовые случаи
            if (i == 0) return j;
            if (j == 0) return i;

            int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

            // Возвращаем минимум из трех операций
            return Math.min(
                    Math.min(
                            editDistRec(a, b, i - 1, j) + 1,     // удаление
                            editDistRec(a, b, i, j - 1) + 1      // вставка
                    ),
                    editDistRec(a, b, i - 1, j - 1) + cost       // замена
            );
        }

        public static void main(String[] args) {
            A_EditDist instance = new A_EditDist();
            System.out.println(instance.getDistanceEdinting("ab", "ab"));         // 0
            System.out.println(instance.getDistanceEdinting("short", "ports"));   // 3
            System.out.println(instance.getDistanceEdinting("distance", "editing")); // 5
        }
    }

