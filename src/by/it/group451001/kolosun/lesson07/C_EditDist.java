package by.it.group451001.kolosun.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import static java.lang.Math.min;

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
        int n=two.length()+1;
        int m=one.length()+1;
        int[][] matrix = new int[n][m];
        for(int i=0;i<n;i++){
            matrix[i][0]=i;
        }
        for(int i=0;i<m;i++){
            matrix[0][i]=i;
        }

        int del,rep,ins;
        for(int i=1;i<n;i++){
            for(int j=1;j<m;j++){
                del=matrix[i-1][j]+1;
                rep=matrix[i-1][j-1]+((two.charAt(i-1)==one.charAt(j-1))?0:1);
                ins=matrix[i][j-1]+1;
                matrix[i][j]=min(del,min(rep,ins));
            }
        }
        String result = "";
        int i = n - 1,j = m - 1;
        while (i * j != 0){
            if (matrix[i][j] - matrix[i - 1][j - 1] == 1) {
                result = "~" + two.charAt(i-1) + "," + result;
                i--;
                j--;
            }
            else if(one.charAt(j-1) == two.charAt(i-1)){
                result = "#," + result;
                i--;
                j--;
            }
            else if(matrix[i][j] - matrix[i-1][j] == 1){
                result = "+" + two.charAt(i-1) + "," + result;
                i--;
            }
            else{
                result = "-" + one.charAt(j-1) + "," + result;
                j--;
            }
        }

        while(i != 0){
            result = "-" + two.charAt(i-1) + "," + result;
            i--;
        }
        while(j != 0){
            result = "+" + one.charAt(j-1) + "," + result;
            j--;
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