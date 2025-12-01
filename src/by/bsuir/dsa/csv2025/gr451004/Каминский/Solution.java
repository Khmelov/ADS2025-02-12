package by.bsuir.dsa.csv2025.gr451004.Каминский;

import java.io.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());

        HashSet<Integer> set = new HashSet<>();

        for (int i = 0; i < q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String cmd = st.nextToken();
            int x = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case "add":
                    set.add(x);
                    break;
                case "del":
                    set.remove(x);
                    break;
                case "find":
                    out.append(set.contains(x) ? "YES\n" : "NO\n");
                    break;
            }
        }
        System.out.print(out.toString());
    }
}
