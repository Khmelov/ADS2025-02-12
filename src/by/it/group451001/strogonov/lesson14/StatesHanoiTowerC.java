package by.it.group451001.strogonov.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    private static void hanoi(int n, int from, int to, int[] cnt, int[] res) {
        final int aux = 3 - from - to;
        if (n == 1){
            cnt[from]--;
            cnt[to]++;
            res[Arrays.stream(cnt).max().getAsInt()]++;
        }
        else{
            hanoi(n - 1, from, aux, cnt, res);
            cnt[from]--;
            cnt[to]++;
            res[Arrays.stream(cnt).max().getAsInt()]++;
            hanoi(n - 1, aux, to, cnt, res);
        }
    }

    private static int[] solve(int N){
        if (N <= 0)
            throw new IllegalArgumentException("Аргумент меньше нуля");
        final int[] res = new int[N + 1], cnt = new int[3];
        cnt[0] = N;
        hanoi(N, 0, 1, cnt, res);
        return res;
    }

    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)){
            final int N = sc.nextInt();
            final int[] res = solve(N);
            Arrays.parallelSort(res);
            int i = 0;
            while (res[i] == 0)
                i++;
            for (; i <= N; i++)
                System.out.print(res[i] + " ");
        }
        catch(Exception e){
            System.out.println("Пу-пу-пу в методе " + e.getStackTrace()[0] + ", а именно: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}
