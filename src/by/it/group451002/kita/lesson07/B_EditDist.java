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
        int result = 0;
        int[][] D = new int[one.length()+1][two.length()+1];

        result = editDistBU(D,one,two);
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

    private static int editDistBU(int[][] D, String one, String two){
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
        return D[one.length()][two.length()];
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