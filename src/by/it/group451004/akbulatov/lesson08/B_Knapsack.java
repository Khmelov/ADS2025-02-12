package by.it.group451004.akbulatov.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();

        ArrayList<Integer> gold = new ArrayList<>();
        for (int i = 0; i < n; i++) gold.add(scanner.nextInt());

        gold.sort( null );

        int sum = 0;
        for (int i = n - 1; i >= 0; i--)
            if ( (sum + gold.get(i)) <= w )
                sum += gold.get(i);

        return sum;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
