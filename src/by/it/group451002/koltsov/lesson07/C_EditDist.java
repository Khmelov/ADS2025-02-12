package by.it.group451002.koltsov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // создаём таблицу
        int[][] table = new int[one.length() + 1][two.length() + 1];

        // заполняем начальными значениями
        for (int i = 1; i < one.length() + 1; i++) {
            table[i][0] = i;
        }
        for (int i = 1; i < two.length() + 1; i++) {
            table[0][i] = i;
        }

        // рассчитываем все элементы таблицы
        for (int i = 1; i < one.length() + 1; i++)
            for (int j = 1; j < two.length() + 1; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    table[i][j] = Math.min(table[i - 1][j - 1], Math.min(table[i - 1][j], table[i][j-1]));
                }
                else {
                    table[i][j] = Math.min(table[i - 1][j - 1], Math.min(table[i - 1][j], table[i][j-1])) + 1;
                }
            }

        String result = "";
        // инициализируем индексы
        int i = one.length(), j = two.length();
        int minValue = 0;
        // лист для хранения результатов
        ArrayList<String> strList = new ArrayList<String>();
        do {
            if (i > 0 && j > 0)
                // находим min элемент среди левого, диагонального и нижнего
                minValue = Math.min(table[i - 1][j - 1], Math.min(table[i - 1][j], table[i][j-1]));
            else if (i > 0)
                // если мы находимся на левом краю, то min элемент - нижний
                minValue = table[i - 1][j];
            else
                // если мы находимся на верхнем краю, то min элемент - левый
                minValue = table[i][j-1];

            // заполняем strList в соответствии с тем, в какую сторону двигаемся в таблице
            if (i >= 1 && j >= 1 && table[i - 1][j - 1] == minValue) {
                if (table[i - 1][j - 1] == table[i][j])
                    strList.add("#");
                else
                    strList.add("~" + two.charAt(j - 1));
                i--;
                j--;
            } else if (i >= 1 && table[i - 1][j] == minValue) {
                strList.add("-" + one.charAt(i - 1));
                i--;
            } else if (j >= 1){
                strList.add("+" + two.charAt(j - 1));
                j--;
            }
        } while (i > 0 || j > 0);

        // переводим strList в обратном порядке в строку
        for (i = strList.size() - 1; i >= 0; i--)
            result += strList.get(i) + ',';
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}