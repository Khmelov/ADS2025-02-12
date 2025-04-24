package by.it.group451004.zarivniak.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        return calculateDistance(one, two, one.length(), two.length());
    }

    private int calculateDistance(String s1, String s2, int m, int n) {
        if (m == 0) {
            return n;
        }
        if (n == 0) {
            return m;
        }
        if (s1.charAt(m - 1) == s2.charAt(n - 1)) {
            return calculateDistance(s1, s2, m - 1, n - 1);
        }
        int insert = calculateDistance(s1, s2, m, n - 1);
        int delete = calculateDistance(s1, s2, m - 1, n);
        int replace = calculateDistance(s1, s2, m - 1, n - 1);
        return 1 + Math.min(Math.min(insert, delete), replace);
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