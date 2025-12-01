package by.it.group410901.borisdubinin.lesson07;

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


    int getDistanceEditing(String one, String two) {
        int n = one.length();

        int[] arr = new int[n + 1];
        for(int i = 0; i <= n; i++){
            arr[i] = i;
        }

        for(int i = 0; i < two.length(); i++){
            int temp = arr[0];
            arr[0]++;
            for(int j = 1; j < arr.length; j++){
                int temp2 = arr[j];
                arr[j] = Math.min(Math.min(arr[j]+1, arr[j-1]+1), temp + (one.charAt(j-1) == two.charAt(i) ? 0 : 1));
                temp = temp2;
            }
        }

        return arr[n];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEditing(scanner.nextLine(), scanner.nextLine()));
    }

}