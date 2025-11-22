package by.it.group451003.kharkevich.lesson07;

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

    static int Minimum(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    int LevenshteinDistance(String str1, int length1, String str2, int length2) {
        if (length1 == 0) {
            return length2;
        }
        if (length2 == 0) {
            return length1;
        }

        int substitutionCost = 0;
        if(str1.charAt(length1 - 1) != str2.charAt(length2 - 1))
        {
            substitutionCost = 1;
        }

        int deletion = LevenshteinDistance(str1, length1 - 1, str2, length2) + 1;
        int insertion = LevenshteinDistance(str1, length1, str2, length2 - 1) + 1;
        int substitution = LevenshteinDistance(str1, length1 - 1, str2, length2 - 1) + substitutionCost;

        return Minimum(deletion, insertion, substitution);

    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int result = LevenshteinDistance(one, one.length(), two, two.length());
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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
