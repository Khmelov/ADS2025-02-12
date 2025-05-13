package by.it.group451002.stsefanovich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        return editDistRecursive(one, two, one.length(), two.length());
    }

    private int editDistRecursive(String one, String two, int m, int n) {
        if (m == 0) return n;
        if (n == 0) return m;

        if (one.charAt(m - 1) == two.charAt(n - 1)) {
            return editDistRecursive(one, two, m - 1, n - 1);
        }

        return 1 + Math.min(editDistRecursive(one, two, m - 1, n - 1), // замена
                Math.min(editDistRecursive(one, two, m - 1, n), // удаление
                        editDistRecursive(one, two, m, n - 1))); // вставка
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