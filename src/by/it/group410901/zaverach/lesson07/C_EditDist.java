package by.it.group410901.zaverach.lesson07;

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

        int m = one.length();
        int n = two.length();
        int[][] d = new int[m+1][n+1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 && j == 0) {
                    d[i][j] = 0;
                } else if (i == 0 && j > 0) {
                    d[i][j] = j;
                } else if (j == 0 && i > 0) {
                    d[i][j] = i;
                } else {
                    int min = 0;
                    if (d[i][j - 1] + 1 < d[i - 1][j] + 1) {
                        min = d[i][j - 1] + 1;
                    } else {
                        min = d[i - 1][j] + 1;
                    }
                    int s = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
                    if (min > d[i - 1][j - 1] + s) {
                        min = d[i - 1][j - 1] + s;

                    }
                    d[i][j]=min;
                }
            }
        }
        String result = "";
        int i=m;
        int j=n;
        while (i>0 && j>0){
            int ii=i;
            int jj=j;
            int min = 0;
            if (d[i][j - 1] + 1 < d[i - 1][j] + 1) {
                jj=j-1;
                min = d[i][j - 1] + 1;
            } else {
                ii=i-1;
                min = d[i - 1][j] + 1;
            }
            int s = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
            if (min > d[i - 1][j - 1] + s) {
                min = d[i - 1][j - 1] + s;
                ii=i-1;
                jj=j-1;
            }
            if(i==ii){
              result= result + '+'+two.charAt(j-1)+',';
            } else if (j==jj){
                result= result+'-'+one.charAt(i-1)+',';
            } else {
                if (s==0){
                    result= result+'#'+',';
                } else {
                    result= result+'~'+two.charAt(j-1)+',';
                }
            }
            i=ii;
            j=jj;
        }

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