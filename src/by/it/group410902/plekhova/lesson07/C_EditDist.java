package by.it.group410902.plekhova.lesson07;

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
        int lenOne = one.length();
        int lenTwo = two.length();


        // Создаем двумерный массив для хранения расстояний
        int[][] dp = new int[lenOne + 1][lenTwo + 1];
        String[][] result =new String [lenOne+1][lenTwo+1] ;
        // Инициализация базовых случаев
        for (int i = 0; i <= lenOne; i++) {
            dp[i][0] = i; // Расстояние от строки к пустой строке
        }
        for (int j = 0; j <= lenTwo; j++) {
            dp[0][j] = j; // Расстояние от пустой строки к строке
        }
        for(int i=1 ; i<=lenOne;i++){

            result[i][0]= "-" + one.charAt(i-1) + ",";
        }
        for(int j=1 ; j<=lenTwo;j++){

            result[0][j]= "+" + two.charAt(j-1) + ",";
        }


        // Заполнение массива dp
        for (int i = 1; i <= lenOne; i++) {
            for (int j = 1; j <= lenTwo; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // Символы равны
                    if(i==1 && j==1){
                        result[i][j]="#,";
                    }else{
                        result[i][j] = result[i-1][j-1]+ "#,";
                    }

                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], // Удаление
                            Math.min(dp[i][j - 1], // Вставка
                                    dp[i - 1][j - 1])); // Замена
                    if((Math.min(dp[i - 1][j],Math.min(dp[i][j - 1],dp[i - 1][j - 1])))==dp[i - 1][j]){
                        result[i][j] = result[i-1][j]+"-"+one.charAt(i - 1)+",";

                    }else if((Math.min(dp[i - 1][j],Math.min(dp[i][j - 1],dp[i - 1][j - 1])))==dp[i][j - 1]){
                        result[i][j] =result[i][j-1]+ "+"+two.charAt(j - 1)+",";

                    }else if ((Math.min(dp[i - 1][j],Math.min(dp[i][j - 1],dp[i - 1][j - 1])))==dp[i - 1][j - 1]){
                        if(i==1&&j==1){
                            result[i][j]="~"+two.charAt(j - 1)+",";
                        }else{
                            result[i][j] =result[i-1][j-1]+ "~"+two.charAt(j - 1)+",";}

                    }

                }
            }
        }

        return result[lenOne][lenTwo];


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

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


