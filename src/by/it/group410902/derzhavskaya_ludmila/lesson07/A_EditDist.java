package by.it.group410902.derzhavskaya_ludmila.lesson07;

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
        int result = levenshteinDistance(one, two, one.length(), two.length());
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Вспомогательная рекурсивная функция
    private int levenshteinDistance(String s1, String s2, int len1, int len2) {
        // Если первая строка пустая, нужно вставить все символы второй строки
        if (len1 == 0) {
            return len2;
        }
        // Если вторая строка пустая, нужно удалить все символы первой строки
        if (len2 == 0) {
            return len1;
        }

        // Если последние символы совпадают, переходим к предыдущим символам
        if (s1.charAt(len1 - 1) == s2.charAt(len2 - 1)) {
            return levenshteinDistance(s1, s2, len1 - 1, len2 - 1);
        }

        // Иначе выбираем минимальную стоимость из трех возможных операций:
        // 1. Удаление (удаляем символ из первой строки)
        // 2. Вставка (вставляем символ второй строки в первую)
        // 3. Замена (заменяем символ первой строки на символ второй)
        int deletion = levenshteinDistance(s1, s2, len1 - 1, len2);
        int insertion = levenshteinDistance(s1, s2, len1, len2 - 1);
        int substitution = levenshteinDistance(s1, s2, len1 - 1, len2 - 1);

        return 1 + Math.min(Math.min(deletion, insertion), substitution);
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
