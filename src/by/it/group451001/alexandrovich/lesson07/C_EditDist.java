package by.it.group451001.alexandrovich.lesson07;

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
    int[][] D;
    String first;
    String second;
    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length()+1;
        int m = two.length()+1;
        D = new int[n][m];
        first = one;
        second = two;
        for (int i = 0; i<n; i++ ){
            D[i][0]=i;
        }
        for (int j = 1; j<m; j++ ){
            D[0][j]=j;
        }
        for (int i = 1; i < n; i++){
            for(int j = 1; j < m; j++){
                int ins = D[i][j - 1] + 1;
                int del = D[i - 1][j] + 1;
                int sub = D[i - 1][j - 1] + ((one.charAt(i-1) == two.charAt(j-1))? 0 : 1);
                D[i][j] =  Math.min(ins, Math.min(del, sub));
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return getPath(n-1,m-1).toString();
    }

    StringBuilder getPath(int i,int j){
        if (i==0){
            if (j==0) return new StringBuilder();
            else{
                return getPath(i, j - 1).append("+").append(second.charAt(j - 1)).append(",");
            }
        }
        else if (j==0){
            return getPath(i - 1, j).append("-").append(first.charAt(i - 1)).append(",");
        }
        else {
            if (D[i][j] == D[i][j - 1] + 1){
                return getPath(i, j - 1).append("+").append(second.charAt(j - 1)).append(",");
            }
            if (D[i][j] ==  D[i - 1][j] + 1){
                return getPath(i - 1, j).append("-").append(first.charAt(i - 1)).append(",");
            }
            if ((D[i][j] ==  D[i - 1][j-1] + 1)){
                return getPath(i - 1, j - 1).append("~").append(second.charAt(j - 1)).append(",");
            }
            else{
                return getPath(i-1,j-1).append("#,");
            }
        }
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