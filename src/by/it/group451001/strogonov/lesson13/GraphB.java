package by.it.group451001.strogonov.lesson13;

import java.util.ArrayList;
import java.util.Scanner;

public class GraphB {

    private static enum color {white, gray, black};

    private static void resizeArray(ArrayList<ArrayList<Integer>> arr, int len){
        for (int i = 0; i <= len - arr.size(); i++)
            arr.add(new ArrayList<>());
    }

    private static Boolean res;
    private static void dfs(ArrayList<ArrayList<Integer>> m, ArrayList<color> f, int cur){
        if (res)
            return;
        f.set(cur, color.gray);
        for (var i : m.get(cur)){
            if (f.get(i).equals(color.white))
                dfs(m, f, i);
            else
                if (f.get(i).equals(color.gray))
                    res = true;
        }
        f.set(cur, color.black);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            int i = 0;
            i += 2;
        }
        String s;
        {
            Scanner in = new Scanner(System.in);
            s = in.nextLine();
            in.close();
        }
        var m = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < s.length();){
            StringBuilder sb_n1 = new StringBuilder();
            while (i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9')
                sb_n1.append(s.charAt(i++));
            if (i >= s.length() || s.charAt(i) == ','){
                int n1 = Integer.parseInt(sb_n1.toString());
                if (m.size() <= n1)
                    resizeArray(m, n1 + 1);
                i += 2;
            }
            else {
                i += 4;
                StringBuilder sb_n2 = new StringBuilder();
                while (i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9')
                    sb_n2.append(s.charAt(i++));
                i += 2;
                int n1 = Integer.parseInt(sb_n1.toString()), n2 = Integer.parseInt(sb_n2.toString());
                if (m.size() <= Math.max(n1, n2))
                    resizeArray(m, Math.max(n1, n2) + 1);
                m.get(n1).add(n2);
            }
        }
        var f = new ArrayList<color>();
        for (int i = 0; i < m.size(); i++)
            f.add(color.white);
        res = false;
        for (int i = 0; i < f.size() && !res; i++)
            if (f.get(i).equals(color.white))
                dfs(m, f, i);
        System.out.println(res ? "yes" : "no");
    }
}
