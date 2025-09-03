package by.it.group451004.struts.lesson07;

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
        String result;

        int[][] table = new int[one.length() + 1][two.length() + 1];
        for (int i = 0; i <= one.length(); i++)
            table[i][0] = i;
        for (int j = 0; j <= two.length(); j++)
            table[0][j] = j;

        for (int i = 0; i < one.length(); i++)
            for (int j = 0; j < two.length(); j++) {
                if (one.charAt(i) == two.charAt(j))
                    table[i + 1][j + 1] = table[i][j];
                else
                    table[i + 1][j + 1] = Math.min(Math.min(table[i][j], table[i][j + 1]), table[i + 1][j]) + 1;
            }

        // После построения таблицы, начинаем добавлять в строку операции
        int i = one.length(), j = two.length();
        StringBuilder sb = new StringBuilder();
        while (i > 0 || j > 0) {
            if (table[i][j] == table[i][j - 1] + 1) {
                sb.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            } else if (table[i][j] == table[i - 1][j] + 1) {
                sb.append("-").append(one.charAt(i - 1)).append(",");
                i--;
            } else if (one.charAt(i - 1) == two.charAt(j - 1)) {
                sb.append("#").append(",");
                i--;
                j--;
            } else {
                sb.append("~").append(two.charAt(j - 1)).append(",");
                i--;
                j--;
            }
        }

        result = sb.toString(); // Строка операций получается перевернутой, но, вроде бы, не критично
        // Ну если хочется, то можно поделить строку по запятой и перевернуть массив :)
        // Или же изначально сделать список операций, а потом его перевернуть

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