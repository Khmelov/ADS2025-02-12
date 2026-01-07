package by.it.group451004.akbulatov.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.Scanner;


public class A_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt();
        int n = scanner.nextInt();
        ArrayList<Integer> gold = new ArrayList<>();

        for (int i = 0; i < n; i++)
            gold.add(scanner.nextInt());

        gold.sort(null);

        int sum = 0;
        for (int i = n - 1; i >= 0; i--)
            while ( (sum + gold.get(i)) <= w )
                sum += gold.get(i);

        return sum;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
