package by.bsuir.dsa.csv2025.gr451004.Кабердин;

import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        
        int[][] city = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                city[i][j] = scanner.nextInt();
            }
        }
        
        int startX = scanner.nextInt();
        int startY = scanner.nextInt();
        int endX = scanner.nextInt();
        int endY = scanner.nextInt();
        
        int result = bfs(city, startX, startY, endX, endY, N, M);
        System.out.println(result);
        
        scanner.close();
    }
    
    private static int bfs(int[][] city, int startX, int startY, int endX, int endY, int N, int M) {
        if (startX == endX && startY == endY) {
            return 0;
        }
        
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[N][M];
        
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        queue.offer(new int[]{startX, startY, 0});
        visited[startX][startY] = true;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int distance = current[2];
            
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                
                if (newX >= 0 && newX < N && newY >= 0 && newY < M 
                    && city[newX][newY] == 0 && !visited[newX][newY]) {
                    
                    if (newX == endX && newY == endY) {
                        return distance + 1;
                    }
                    
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY, distance + 1});
                }
            }
        }
        
        return -1;
    }
}