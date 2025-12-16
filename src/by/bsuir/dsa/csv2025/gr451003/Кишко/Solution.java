package by.bsuir.dsa.csv2025.gr451003.Кишко;

import java.util.*;
import java.io.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        runAllTests();
        runWithUserInput();
    }

    @Test
    public void testSimpleMaze() {
        String[] input = {
                "3 3",
                "S..",
                "...",
                "..E"
        };
        int expected = 4;
        runTest("testSimpleMaze", input, expected);
    }

    @Test
    public void testSolution() {
        String[] input = {
                "5 5",
                "S....",
                ".###.",
                "..a..",
                ".a#E.",
                "....."
        };
        int expected = 6;
        runTest("testSolution", input, expected);
    }

    @Test
    public void testImpossiblePath() {
        String[] input = {
                "3 3",
                "S#.",
                "###",
                "..E"
        };
        int expected = -1;
        runTest("testImpossiblePath", input, expected);
    }

    @Test
    public void testStartNearEnd() {
        String[] input = {
                "2 2",
                "SE",
                ".."
        };
        int expected = 1;
        runTest("testStartNearEnd", input, expected);
    }

    @Test
    public void testMazeWithChoice() {
        String[] input = {
                "5 5",
                "S...a",
                "####.",
                ".....",
                ".####",
                "a...E"
        };
        int expected = 8;
        runTest("testMazeWithChoice", input, expected);
    }

    @Test
    public void testMinimalMaze() {
        String[] input = {
                "1 2",
                "SE"
        };
        int expected = 1;
        runTest("testMinimalMaze", input, expected);
    }

    @Test
    public void testFastPathThroughTeleport() {
        String[] input = {
                "3 3",
                "S#.",
                ".#a",
                "a.E"
        };
        int expected = 4;
        runTest("testFastPathThroughTeleport", input, expected);
    }

    @Test
    public void testMultipleTeleportTypes() {
        String[] input = {
                "4 5",
                "Sa.b.",
                ".#.#.",
                ".c.d.",
                "b.c.E"
        };
        int expected = 5;
        runTest("testMultipleTeleportTypes", input, expected);
    }

    @Test
    public void testTeleportChain() {
        String[] input = {
                "3 5",
                "Sa.bE",
                ".....",
                "Sb.a."
        };
        int expected = 2;
        runTest("testTeleportChain", input, expected);
    }

    @Test
    public void testTeleportOnFinish() {
        String[] input = {
                "3 3",
                "S.a",
                "...",
                "..E"
        };
        int expected = 4;
        runTest("testTeleportOnFinish", input, expected);
    }

    @Test
    public void testImpossible1x1Maze() {
        String[] input = {
                "1 1",
                "S"
        };
        int expected = -1;
        runTest("testImpossible1x1Maze", input, expected);
    }

    @Test
    public void test1x3Maze() {
        String[] input = {
                "1 3",
                "S.E"
        };
        int expected = 2;
        runTest("test1x3Maze", input, expected);
    }

    private static void runWithUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] dimensions = reader.readLine().split(" ");
        int n = Integer.parseInt(dimensions[0]);
        int m = Integer.parseInt(dimensions[1]);

        char[][] maze = new char[n][m];
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;

        Map<Character, List<int[]>> teleports = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            for (int j = 0; j < m; j++) {
                maze[i][j] = line.charAt(j);

                if (maze[i][j] == 'S') {
                    startX = i;
                    startY = j;
                } else if (maze[i][j] == 'E') {
                    endX = i;
                    endY = j;
                } else if (maze[i][j] >= 'a' && maze[i][j] <= 'z') {
                    char teleportChar = maze[i][j];
                    teleports.computeIfAbsent(teleportChar, k -> new ArrayList<>()).add(new int[]{i, j});
                }
            }
        }

        int result = findShortestPath(maze, startX, startY, endX, endY, teleports, n, m);
        System.out.println(result);
    }

    private static int findShortestPath(char[][] maze, int startX, int startY,
                                        int endX, int endY, Map<Character, List<int[]>> teleports,
                                        int n, int m) {
        boolean[][] visited = new boolean[n][m];
        Queue<int[]> queue = new LinkedList<>();
        Set<Character> usedTeleports = new HashSet<>();

        queue.offer(new int[]{startX, startY, 0});
        visited[startX][startY] = true;

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int distance = current[2];

            if (x == endX && y == endY) {
                return distance;
            }

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (isValid(newX, newY, n, m) && !visited[newX][newY] && maze[newX][newY] != '#') {
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY, distance + 1});
                }
            }

            char currentCell = maze[x][y];
            if (currentCell >= 'a' && currentCell <= 'z' && !usedTeleports.contains(currentCell)) {
                List<int[]> teleportPositions = teleports.get(currentCell);
                if (teleportPositions != null) {
                    for (int[] pos : teleportPositions) {
                        int teleX = pos[0];
                        int teleY = pos[1];

                        if (teleX != x || teleY != y) {
                            if (!visited[teleX][teleY]) {
                                visited[teleX][teleY] = true;
                                queue.offer(new int[]{teleX, teleY, distance});
                            }
                        }
                    }
                    usedTeleports.add(currentCell);
                }
            }
        }

        return -1;
    }

    private static boolean isValid(int x, int y, int n, int m) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }

    private static void runAllTests() {
        System.out.println("=== ТЕСТИРОВАНИЕ РЕШЕНИЯ ЛАБИРИНТА С ТЕЛЕПОРТАМИ ===\n");
        Solution tester = new Solution();
        tester.testSimpleMaze();
        tester.testSolution();
        tester.testImpossiblePath();
        tester.testStartNearEnd();
        tester.testMazeWithChoice();
        tester.testMinimalMaze();
        tester.testFastPathThroughTeleport();
        tester.testMultipleTeleportTypes();
        tester.testTeleportChain();
        tester.testTeleportOnFinish();
        tester.testImpossible1x1Maze();
        tester.test1x3Maze();

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }

    private void runTest(String testName, String[] input, int expected) {
        try {
            String[] dimensions = input[0].split(" ");
            int n = Integer.parseInt(dimensions[0]);
            int m = Integer.parseInt(dimensions[1]);
            char[][] maze = new char[n][m];
            int startX = -1, startY = -1;
            int endX = -1, endY = -1;
            Map<Character, List<int[]>> teleports = new HashMap<>();

            for (int i = 0; i < n; i++) {
                String line = input[i + 1];
                for (int j = 0; j < m; j++) {
                    maze[i][j] = line.charAt(j);

                    if (maze[i][j] == 'S') {
                        startX = i;
                        startY = j;
                    } else if (maze[i][j] == 'E') {
                        endX = i;
                        endY = j;
                    } else if (maze[i][j] >= 'a' && maze[i][j] <= 'z') {
                        char teleportChar = maze[i][j];
                        teleports.computeIfAbsent(teleportChar, k -> new ArrayList<>()).add(new int[]{i, j});
                    }
                }
            }

            int result = findShortestPath(maze, startX, startY, endX, endY, teleports, n, m);

            String status = (result == expected) ? "ПРОЙДЕН" : "НЕ ПРОЙДЕН";
            String color = (result == expected) ? "\u001B[32m" : "\u001B[31m";
            String reset = "\u001B[0m";

            System.out.printf("%-25s: %s%-10s%s | Ожидалось: %3d | Получено: %3d\n",
                    testName, color, status, reset, expected, result);

        } catch (Exception e) {
            System.out.printf("%-25s: \u001B[31mОШИБКА\u001B[0m | Исключение: %s\n", testName, e.getMessage());
        }
    }

    @interface Test {}
}