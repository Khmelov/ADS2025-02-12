package by.bsuir.dsa.csv2025.gr451004.Киселёв;

import java.util.*;
import java.io.*;

public class Solution {
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        // Чтение размеров сетки
        String[] dimensions = reader.readLine().split(" ");
        int n = Integer.parseInt(dimensions[0]);
        int m = Integer.parseInt(dimensions[1]);
        
        // Чтение сетки
        char[][] grid = new char[n][m];
        int startX = -1, startY = -1;
        
        for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            for (int j = 0; j < m; j++) {
                grid[i][j] = line.charAt(j);
                if (grid[i][j] == 'R') {
                    startX = i;
                    startY = j;
                }
            }
        }
        
        // Вычисление результата
        int result = findMinSteps(grid, startX, startY, n, m);
        System.out.println(result);
    }
    
    private static int findMinSteps(char[][] grid, int startX, int startY, int n, int m) {
        // Проверка на случай, если робот не найден (по условию гарантируется, но для надежности)
        if (startX == -1 || startY == -1) {
            return -1;
        }
        
        // Очередь для BFS: хранит координаты и количество шагов
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startX, startY, 0});
        
        // Массив для отслеживания посещенных ячеек
        boolean[][] visited = new boolean[n][m];
        visited[startX][startY] = true;
        
        // Возможные направления движения: вверх, вниз, влево, вправо
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int steps = current[2];
            
            // Если достигли цели, возвращаем количество шагов
            if (grid[x][y] == 'C') {
                return steps;
            }
            
            // Исследуем всех соседей
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                
                // Проверяем, что новая ячейка в пределах сетки,
                // не является препятствием и еще не посещена
                if (newX >= 0 && newX < n && newY >= 0 && newY < m 
                    && grid[newX][newY] != '#' 
                    && !visited[newX][newY]) {
                    
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY, steps + 1});
                }
            }
        }
        
        // Если очередь пуста, а цель не найдена
        return -1;
    }
}