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

        int result = arr[l1-1][l2-1];
//        System.out.println(result);
//        System.out.println();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}