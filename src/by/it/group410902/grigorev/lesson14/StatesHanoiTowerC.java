package by.it.group410902.grigorev.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    static void rekr(Integer[] s, Integer h, Integer[] ans, Integer fr, Integer to){

        if(h == 1){
            s[to]++;
            s[fr]--;
            // Находим наибольшую высоту на стержнях A, B и C
            ans[s[0] > s[1] ? (s[0] > s[2] ? s[0] : s[2]) : (s[2] > s[1] ? s[2] : s[1])]++;
        } else {

            int temp = (fr == 0 ? to == 1 ? 2 : 1 : fr == 1 ? to == 0 ? 2 : 0 : to == 1 ? 0 : 1);

            rekr(s, h - 1, ans, fr, temp);
            s[to]++;
            s[fr]--;

            ans[s[0] > s[1] ? (s[0] > s[2] ? s[0] : s[2]) : (s[2] > s[1] ? s[2] : s[1])]++;

            rekr(s, h - 1, ans, temp, to);
        }
    }

    public static void main(String[] args){

        Scanner in = new Scanner(System.in);

        Integer n;
        n = in.nextInt();

        Integer[] ans = new Integer[n + 1], s = new Integer[3];

        s[0] = n; s[1] = 0; s[2] = 0;

        for(int i = 0; i <= n; ++i)
            ans[i] = 0;

        rekr(s, n, ans, 0, 2);

        Arrays.sort(ans);

        for(int i = 0; i < ans.length; ++i)
            if(ans[i] > 0) {
                System.out.print(ans[i]);
                System.out.print(' ');
            }
    }
}
