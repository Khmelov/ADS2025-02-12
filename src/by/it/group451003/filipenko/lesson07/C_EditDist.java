package by.it.group451003.filipenko.lesson07;

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
        return strEditDist(one, two);
    }

    private static String strEditDist(String str1, String str2) {
        int[][] dist = new int[str1.length() + 1][str2.length() + 1];
        String[][] inst = new String[str1.length() + 1][str2.length() + 1];

        for (int i = 1; i <= str1.length(); i++) {
            dist[i][0] = i;
            inst[i][0] = ","+str1.charAt(i-1)+"-";
        }

        for (int i = 1; i <= str2.length(); i++) {
            dist[0][i] = i;
            inst[0][i] = ","+str2.charAt(i-1)+"+";
        }

        for (int i = 1; i <= str1.length(); i++)
            for (int j = 1; j <= str2.length(); j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                if(dist[i][j - 1] < dist[i - 1][j] && dist[i][j - 1] < dist[i - 1][j - 1] + cost) {
                    dist[i][j] = dist[i][j - 1] + 1;
                    inst[i][j] = ","+str2.charAt(j-1)+"+";
                }
                else if(dist[i - 1][j] < dist[i - 1][j - 1] + cost) {
                    dist[i][j] = dist[i - 1][j] + 1;
                    inst[i][j] = ","+str1.charAt(i-1)+"-";
                }
                else {
                    dist[i][j] = dist[i - 1][j - 1] + cost;
                    inst[i][j] = (cost == 1) ? ","+str2.charAt(j-1)+"~" : ",#";
                }
            }

        StringBuilder instruction = new StringBuilder("");
        int i = str1.length(), j = str2.length();
        do {
            String someStr = inst[i][j];
            instruction.append(someStr);
            if(someStr.length() == 2 || someStr.charAt(2) == '~') {
                i --;
                j --;
            }
            else if(someStr.charAt(2) == '-') {
                i --;
            }
            else {
                j --;
            }
        } while((i != 0) || (j != 0));

        return instruction.reverse().toString();
    }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}