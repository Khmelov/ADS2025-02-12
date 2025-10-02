package by.it.group451002.kita.lesson07;

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
        int[][] D = new int[one.length()+1][two.length()+1];
        String result = "";
        result = editDistBU(D, one, two);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    //сравниваем определенные символы двух строк. Если символы равны, то возвращаем 0, если нет - 1
    static int diff(char a, char b){
        if (a == b){
            return 0;
        }
        else{
            return 1;
        }
    }

    private static String editDistBU(int[][] D, String one, String two){
        //заполняем первый столбец
        for (int i = 0; i <= one.length(); i++){
            D[i][0] = i;
        }
        //заполняем первую строку
        for (int j = 0; j <= two.length(); j++){
            D[0][j] = j;
        }
        //заполняем оставшуюся часть массива
        for (int i = 1; i <= one.length(); i++){
            for (int j = 1; j <= two.length(); j++){
                int c = diff(one.charAt(i-1), two.charAt(j-1));
                D[i][j] = Math.min(D[i][j-1]+1, Math.min(D[i-1][j]+1,D[i-1][j-1]+c));
            }
        }
        StringBuilder res = new StringBuilder();
        int i = one.length();
        int j = two.length();
        while (i > 0 || j > 0){
            if (i > 0 && j > 0 && one.charAt(i-1)==two.charAt(j-1)){
                res.append("#,");
                i--;
                j--;
            }
            else {
                //проверяем на замену
                if (i > 0 && j>0 && D[i][j] == D[i-1][j-1]+1){
                    res.append("~").append(two.charAt(j-1)).append(",");
                    i--;
                    j--;
                }
                //проверка на удаление
                else if (i > 0 && D[i][j] == D[i-1][j]+1){
                    res.append("-").append(one.charAt(i-1)).append(",");
                    i--;
                }
                //проверка на вставку
                else if (j > 0 && D[i][j] == D[i][j-1]+1){
                    res.append("+").append(two.charAt(j-1)).append(",");
                    j--;
                }
            }
        }

        return res.toString();
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