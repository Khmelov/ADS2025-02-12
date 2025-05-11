package by.it.group451001.klevko.lesson07;

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
        int[][] fieldMatrix = new int[one.length()+1][two.length()+1];
        for (int i = 0; i < fieldMatrix.length; i++) {fieldMatrix[i][0] = i;}
        for (int i = 1; i < fieldMatrix[0].length; i++) {fieldMatrix[0][i] = i;}

        for (int i = 1; i <= one.length(); i++) {
            for (int j = 1; j <= two.length(); j++) {
                int min = Math.min(fieldMatrix[i-1][j]+1, fieldMatrix[i][j-1]+1);
                int item;
                if (one.charAt(i-1) == two.charAt(j-1)) item = 0;
                else item = 1;
                min = Math.min(min, fieldMatrix[i-1][j-1] + item);
                fieldMatrix[i][j] = min;
            }
        }
        StringBuilder ans = new StringBuilder();
        int i = one.length(), j = two.length();
        while ((i != 0) || (j != 0)){
            int element = fieldMatrix[i][j];
            ans.insert(0, ',');
            if ((i > 0) && (element - 1 == fieldMatrix[i-1][j])) {
                --i;
                ans.insert(0, one.charAt(i));
                ans.insert(0, '-');
            } else if ((j > 0) && (element - 1 == fieldMatrix[i][j-1])) {
                --j;
                ans.insert(0, two.charAt(j));
                ans.insert(0, '+');
            } else {
                --i; --j;
                if (one.charAt(i) == two.charAt(j)) {
                    ans.insert(0, '#');
                } else {
                    ans.insert(0, two.charAt(j));
                    ans.insert(0, '~');
                }
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return ans.toString();
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