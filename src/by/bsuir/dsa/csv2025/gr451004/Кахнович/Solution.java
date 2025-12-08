package by.bsuir.dsa.csv2025.gr451004.Кахнович;

import java.util.*;
import java.io.*;

public class Solution {
    static final int INF = 1_000_000_000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                dist[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        int[][] dp = new int[1 << n][n];
        for (int i = 0; i < (1 << n); i++) {
            Arrays.fill(dp[i], INF);
        }
        
        dp[1][0] = 0;
        
        List<Integer> validMasks = new ArrayList<>();
        for (int mask = 0; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) == k && (mask & 1) == 1) {
                validMasks.add(mask);
            }
        }
        
        for (int mask = 0; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (dp[mask][last] == INF) continue;
                if ((mask & (1 << last)) == 0) continue;
                
                for (int next = 0; next < n; next++) {
                    if ((mask & (1 << next)) != 0) continue;
                    if (dist[last][next] == 0 && last != next) continue;

                    int newMask = mask | (1 << next);
                    int newDist = dp[mask][last] + dist[last][next];

                    if (newDist < dp[newMask][next]) {
                        dp[newMask][next] = newDist;
                    }
                }
            }
        }
        
        int answer = INF;
        for (int mask : validMasks) {
            for (int last = 0; last < n; last++) {
                if (dp[mask][last] != INF && dist[last][0] != 0) {
                    answer = Math.min(answer, dp[mask][last] + dist[last][0]);
                }
            }
        }

        System.out.println(answer == INF ? -1 : answer);
    }
}