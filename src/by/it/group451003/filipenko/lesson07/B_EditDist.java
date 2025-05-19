package by.it.group451003.filipenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return calcEditDist(one, two);
    }

    private static int calcEditDist(String str1, String str2) {
        //int[][] matrix = new int[str1.length() + 1][str2.length() + 1];
        int[] currRow = new int[str2.length()+1];
        int[] prevRow;


        for (int i = 0; i <= str1.length(); i++)
        {
            prevRow = currRow;
            currRow = new int[str2.length()+1];

            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    //matrix[i][j] = j;
                    currRow[j] = j;
                }

                else if (j == 0) {
                    //matrix[i][j] = i;
                    currRow[j] = i;
                }

                else {
                    /*   matrix[i][j] = minEdits(matrix[i - 1][j - 1]
                                     + replacementNum(str1.charAt(i - 1),str2.charAt(j - 1)),
                            matrix[i - 1][j] + 1,
                            matrix[i][j - 1] + 1); */
                    currRow[j] = minEdits(prevRow[j-1]
                                    + replacementNum(str1.charAt(i - 1),str2.charAt(j - 1)),
                            prevRow[j] + 1,
                            currRow[j-1] + 1);
                }
            }
        }
        //return matrix[str1.length()][str2.length()];
        return currRow[str2.length()];
    }

    static int replacementNum(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    static int minEdits(int... nums) {
        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}