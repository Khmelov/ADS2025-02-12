package by.it.group451001.klevko.lesson07;

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
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

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

public class A_EditDist {



    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        class Dist{
            private static int[][] fieldMatrix;
            private static String one, two;

            public static int Do(String first, String second){
                one = first;
                two = second;
                fieldMatrix = new int[one.length()+1][two.length()+1];
                for (int[] matrix : fieldMatrix) {
                    Arrays.fill(matrix, -1);
                    /*for (int j = 0; j < fieldMatrix[i].length; j++) {
                        fieldMatrix[i][j] = -1;
                    }*/
                }
                for (int i = 0; i < fieldMatrix.length; i++) {fieldMatrix[i][0] = i;}
                for (int i = 1; i < fieldMatrix[0].length; i++) {fieldMatrix[0][i] = i;}
                Step(one.length(),two.length());
                return fieldMatrix[one.length()][two.length()];
            }

            private static int Step(int i, int j){
                if (fieldMatrix[i][j] != -1) return fieldMatrix[i][j];
                else {
                    int min = Math.min(Step(i-1, j)+1, Step(i, j-1)+1);
                    int item;
                    if (one.charAt(i-1) == two.charAt(j-1)) item = 0;
                    else item = 1;
                    min = Math.min(min, Step(i-1, j-1) + item);
                    fieldMatrix[i][j] = min;
                    return min;
                }

            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return Dist.Do(one, two);
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
