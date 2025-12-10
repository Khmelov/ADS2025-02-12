package by.bsuir.dsa.csv2025.gr451004.Рублевская;

import org.junit.Test;

import java.util.*;

public class Solution {

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static int findShortestPath(int[][] maze) {
        int n = maze.length;
        int m = maze[0].length;

        int startX = -1, startY = -1;
        int exitX = -1, exitY = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (maze[i][j] == 2) {
                    startX = i;
                    startY = j;
                } else if (maze[i][j] == 3) {
                    exitX = i;
                    exitY = j;
                }
            }
        }

        int[][] dist = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1);
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startX, startY});
        dist[startX][startY] = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == exitX && y == exitY) {
                return dist[x][y];
            }

            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newX < n && newY >= 0 && newY < m) {
                    if ((maze[newX][newY] == 0 || maze[newX][newY] == 3)
                            && dist[newX][newY] == -1) {

                        dist[newX][newY] = dist[x][y] + 1;
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[][] maze = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                maze[i][j] = scanner.nextInt();
            }
        }

        int result = findShortestPath(maze);
        System.out.println(result);
    }
    @Test
    public void testFindShortestPath1() {
        int[][] maze = {
                {1, 0, 0, 0, 0},
                {1, 1, 0, 1, 0},
                {0, 1, 2, 1, 3}
        };
        int result = findShortestPath(maze);
        assert result == 6 : "Test failed. Expected 6 but got " + result;
    }

    @Test
    public void testFindShortestPath2() {
        int[][] maze = {
                {1, 1, 1, 1},
                {1, 2, 0, 1},
                {1, 0, 3, 1},
                {1, 1, 1, 1}
        };
        int result = findShortestPath(maze);
        assert result == 2 : "Test failed. Expected 2 but got " + result;
    }

    @Test
    public void testFindShortestPath3() {
        int[][] maze = {
                {1, 1, 1, 1},
                {1, 2, 0, 0},
                {1, 0, 0, 3},
                {1, 1, 1, 1}
        };
        int result = findShortestPath(maze);
        assert result == 3 : "Test failed. Expected 3 but got " + result;
    }

    @Test
    public void testFindShortestPath4() {
        int[][] maze = {
                {1, 1, 1, 1},
                {1, 2, 0, 0},
                {1, 1, 1, 1}
        };
        int result = findShortestPath(maze);
        assert result == -1 : "Test failed. Expected -1 (no path) but got " + result;
    }

    @Test
    public void testFindShortestPath5() {
        int[][] maze = {
                {2, 0, 0, 0, 0},
                {0, 0, 0, 1, 1},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 1, 3}
        };
        int result = findShortestPath(maze);
        assert result == 7 : "Test failed. Expected 7 but got " + result;
    }
}