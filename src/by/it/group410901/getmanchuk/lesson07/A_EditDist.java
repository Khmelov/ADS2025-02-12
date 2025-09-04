package by.it.group410901.getmanchuk.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int minDistance(String s1, String s2, int i, int j) {
        if (i == 0) return j;
        if (j == 0) return i;

        int cost = s1.charAt(i-1) == s2.charAt(j-1) ? 0 : 1;

        return Math.min(
                minDistance(s1, s2, i-1, j) + 1,
                Math.min(
                        minDistance(s1, s2, i, j-1) + 1,
                        minDistance(s1, s2, i-1, j-1) + cost
                )
        );
    }

    int getDistanceEdinting(String one, String two) {
        return minDistance(one, two, one.length(), two.length());
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