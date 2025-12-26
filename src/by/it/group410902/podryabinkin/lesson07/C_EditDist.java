package by.it.group410902.podryabinkin.lesson07;

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



    public String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        String result = "";

        //        System.out.println(one);
//        System.out.println(two);
        int l1 =one.length() + 1;
        int l2 = two.length() + 1;
        int[][] arr = new int[l1][l2];
        for(int i = 0; i < l1; i++) arr[i][0] = i;
        for(int i = 0; i < l2; i++) arr[0][i] = i;
        int add = 0;
        for(int i = 1; i < l1; i++){
            for(int j = 1; j < l2; j++) {
                if(one.charAt(i-1) == two.charAt(j-1)) add = 0;
                else add = 1;
                arr[i][j] = Integer.min(Integer.min(arr[i-1][j] + 1,arr[i][j-1] + 1) , arr[i-1][j-1] + add);
            }
        }

//        for(int i = 0; i < l1; i++){
//            for(int j = 0; j < l2; j++){
//                System.out.print(arr[i][j] + " ");
//            }
//            System.out.println();
//        }

//        System.out.println(result);
//        System.out.println();
        int val = arr[l1 - 1][l2 - 1];
        int i = l1 -1, j = l2 - 1;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i-1) == two.charAt(j-1)) {
                result += ",#";  // Совпадение
                i--;
                j--;
            }
            else if (i > 0 && (j == 0 || arr[i][j] == arr[i-1][j] + 1)) {
                result+= "," + one.charAt(i-1) + "-";  // Удаление
                i--;
            }
            else if (j > 0 && (i == 0 || arr[i][j] == arr[i][j-1] + 1)) {
                result +="," + two.charAt(j-1) + "+";  // Вставка
                j--;
            }
            else if (i > 0 && j > 0) {
                result +="," + two.charAt(j-1) +   "~";  // Замена
                i--;
                j--;
            }
        }
        String temp = "";
        for(int c = result.length() -1; c >= 0 ; c--){
            temp += result.charAt(c);
        }
        return temp;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }


//-----------------------------------------КУСОК КОДА ДЛЯ 15 ЛАБЫ НЕ ПУГАТЬСЯ-------------------------------
    public int getDistanceEdintingCount(String one, String two) {
        int l1 = one.length() + 1;
        int l2 = two.length() + 1;
        int[][] arr = new int[l1][l2];

        for (int i = 0; i < l1; i++) arr[i][0] = i;
        for (int j = 0; j < l2; j++) arr[0][j] = j;

        for (int i = 1; i < l1; i++) {
            for (int j = 1; j < l2; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                arr[i][j] = Math.min(Math.min(arr[i - 1][j] + 1, arr[i][j - 1] + 1), arr[i - 1][j - 1] + cost);
            }
        }

        int edits = 0;
        int i = l1 - 1, j = l2 - 1;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                i--; j--; // совпадение
            } else if (i > 0 && (j == 0 || arr[i][j] == arr[i - 1][j] + 1)) {
                edits++; // удаление
                i--;
            } else if (j > 0 && (i == 0 || arr[i][j] == arr[i][j - 1] + 1)) {
                edits++; // вставка
                j--;
            } else if (i > 0 && j > 0) {
                edits++; // замена
                i--; j--;
            }
        }

        return edits;
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