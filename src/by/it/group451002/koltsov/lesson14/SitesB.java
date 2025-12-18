package by.it.group451002.koltsov.lesson14;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SitesB {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String str;
        DSUtaskB dsu = new DSUtaskB();
        while (true) {
            str = scan.nextLine();
            if (Objects.equals(str, "end"))
                break;
            String[] sites = str.split("\\+");
            dsu.add(sites[0]);
            dsu.add(sites[1]);
            dsu.union(sites[0], sites[1]);
        }
        List<Integer> sizes = new ArrayList<>();
        sizes = dsu.getUnionsSizes();
        for (int i = sizes.size() - 1; i >= 0; i--)
            System.out.print(sizes.get(i) + " ");
    }
}
