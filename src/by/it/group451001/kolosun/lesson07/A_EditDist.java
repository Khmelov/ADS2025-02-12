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
    int elem;
    String first,second;

    int GetDist(int[][]Arr,int n,int m){
        if(Arr[n][m]==elem){
            if(n*m==0){
                Arr[n][m]=(n==0)?m:n;
            }
            else{
                int del=GetDist(Arr,n-1,m)+1;
                int rep=GetDist(Arr,n-1,m-1)+((first.charAt(n-1)==second.charAt(m-1))?0:1);
                int ins=GetDist(Arr,n,m-1)+1;
                Arr[n][m]= min(del, min(rep,ins));
            }
        }
        return Arr[n][m];
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n=one.length()+1;
        int m=two.length()+1;
        int[][] matrix=new int[n][m];
        first=one;
        second=two;
        if (n<m){
            elem=m+1;
        }
        else{
            elem=n+1;
        }
        for (int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                matrix[i][j]=elem;
            }
        }

        int result=GetDist(matrix,n-1,m-1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
