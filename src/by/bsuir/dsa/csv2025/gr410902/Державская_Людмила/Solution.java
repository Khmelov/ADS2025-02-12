package by.bsuir.dsa.csv2025.gr410902.Державская_Людмила;


import java.util.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    static class Point {
        int x, y, dist;
        Point(int x, int y, int dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        char[][] maze = new char[n][m];
        boolean[][] guarded = new boolean[n][m]; // Массив охраняемых клеток
        int startX = -1, startY = -1;

        // Чтение лабиринта и разметка охраняемых зон
        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            for (int j = 0; j < m; j++) {
                maze[i][j] = line.charAt(j);
                if (maze[i][j] == 'S') {
                    startX = i;
                    startY = j;
                }
            }
        }

        // Разметка охраняемых зон вокруг собак
        markGuardedZones(maze, guarded, n, m);

        // BFS для поиска кратчайшего пути
        int result = bfs(maze, guarded, startX, startY, n, m);
        System.out.println(result);
    }

    static void markGuardedZones(char[][] maze, boolean[][] guarded, int n, int m) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (maze[i][j] == 'D') {
                    // Помечаем все 4 соседние клетки как охраняемые
                    for (int k = 0; k < 4; k++) {
                        int ni = i + dx[k];
                        int nj = j + dy[k];
                        if (ni >= 0 && ni < n && nj >= 0 && nj < m) {
                            guarded[ni][nj] = true;
                        }
                    }
                }
            }
        }

        // Выход всегда безопасен
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (maze[i][j] == 'E') {
                    guarded[i][j] = false;
                }
            }
        }
    }

    static int bfs(char[][] maze, boolean[][] guarded, int startX, int startY, int n, int m) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        boolean[][] visited = new boolean[n][m];
        Queue<Point> queue = new LinkedList<>();

        // Проверяем, что стартовая позиция не охраняется
        if (guarded[startX][startY]) {
            return -1;
        }

        queue.add(new Point(startX, startY, 0));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            // Если нашли выход
            if (maze[current.x][current.y] == 'E') {
                return current.dist;
            }

            // Проверяем все 4 направления
            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];

                // Проверка границ, посещения, стен и охраняемости
                if (nx >= 0 && nx < n && ny >= 0 && ny < m &&
                        !visited[nx][ny] && maze[nx][ny] != '#' &&
                        !guarded[nx][ny]) {

                    visited[nx][ny] = true;
                    queue.add(new Point(nx, ny, current.dist + 1));
                }
            }
        }

        // Выход не найден
        return -1;
    }
    @Test
    public void test1(){
        String input = "3 3\nS.D\n...\n..E";
        int expected = 4;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test2(){
        String input = "1 5\nS.D.E";
        int expected = -1;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test3(){
        String input = "3 5\nS...E\n.....\nD....";
        int expected = 4;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test4(){
        String input = "3 4\nS.D.\n...E\n#...";
        int expected = 6;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test5(){
        String input = "5 7\nS......\n.#.....\nD..#D..\n.#.....\n.....E.";
        int expected = 9;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test6(){
        String input = "6 6\nS....D\n......\n##.#..\n.#.#..\n.D.#..\n.....E";
        int expected = 10;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test7(){
        String input = "5 5\nS...D\n.....\n.D...\n...##\nD...E";
        int expected = -1;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test8(){
        String input = "7 9\nS.....#..\n...#....D\n.D.......\n.#...D...\nD..#.....\n......#..\nE..#.....";
        int expected = 14;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test9(){
        String input = "7 9\nS.....#..\n...#....D\n.D.......\n.#...D...\nD..#.....\n...D..#..\nE........";
        int expected = -1;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    @Test
    public void test10(){
        String input = "11 10\nS.........\n.#.##.#..D\n......#...\n###...#...\n...D..#D..\n......#...\n.#....#...\n.#####...D\n........#.\n######..#.\n.E.......D";
        int expected = 27;
        int result = runTest(input);
        assertEquals(expected, result);
    }

    // Вспомогательный метод для запуска тестов
    private int runTest(String input) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            System.setIn(in);
            System.setOut(new PrintStream(out));

            // Сохраняем оригинальные System.in и System.out
            InputStream originalIn = System.in;
            PrintStream originalOut = System.out;

            // Запускаем main метод
            Solution.main(new String[0]);

            // Восстанавливаем оригинальные потоки
            System.setIn(originalIn);
            System.setOut(originalOut);

            // Получаем результат
            String output = out.toString().trim();
            return Integer.parseInt(output);

        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
            return -999; // никогда не будет достигнуто
        }
    }

}