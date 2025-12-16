package by.bsuir.dsa.csv2025.gr451002.Спицына;

import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Solution {

    int getMinSteps(InputStream stream){
        Scanner scanner = new Scanner(stream);
        //Координаты коня на доске
        int knightX = scanner.nextInt();
        int knightY = scanner.nextInt();

        //Массив с координатами монет
        List<Point> coins = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            coins.add(new Point(x,y));
        }

        //Количество монет, которые надо найти
        int coinsToFind = scanner.nextInt();

        //Класс для состояния коня в каждой клетке доски
        class State {
            int x, y;
            int mask;
            int steps;

            State(int x, int y, int mask, int steps){
                this.x = x;
                this.y = y;
                this.mask = mask;
                this.steps = steps;
            }
        }

        Queue<State> queue = new LinkedList<>();

        int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};

        queue.add(new State(knightX, knightY, 0,0));

        boolean[][][] visited = new boolean[8][8][1<<8];

        visited[knightX][knightY][0] = true;
        while (!queue.isEmpty()){
            State currState = queue.poll();

            //Если все монеты собраны, то заканчиваем проходы
            if (Integer.bitCount(currState.mask) >= coinsToFind)
                return currState.steps;

            for (int i = 0; i < 8; i++){
                int newX = currState.x + dx[i];
                int newY = currState.y + dy[i];
                int newMask = currState.mask;

                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8){
                    //Проверяем, есть ли в этой клетке монета
                    Point currPoint = new Point(newX, newY);
                    if (coins.contains(currPoint)){
                        newMask = currState.mask | (1 << coins.indexOf(currPoint));
                    }

                    if (!visited[newX][newY][newMask]){
                        visited[newX][newY][newMask] = true;
                        queue.add(new State(newX, newY, newMask, currState.steps+1));
                    }
                }
            }

        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Solution instance = new Solution();
        int res = instance.getMinSteps(System.in);
        System.out.println(res);
    }

    @Test
    public void test1() throws Exception {
        InputStream stream = new ByteArrayInputStream("5 3  1 2  2 2  4 5  6 5  1 7  7 5  7 7  3 4  3".getBytes());
        Solution instance = new Solution();
        int res = instance.getMinSteps(stream);
        assertEquals("Test1 failed", res, 4);
    }

    @Test
    public void test2() throws Exception {
        InputStream stream = new ByteArrayInputStream("0 0  7 7  6 5  5 1  3 6  2 2  1 5  4 4  7 0  1".getBytes());
        Solution instance = new Solution();
        int res = instance.getMinSteps(stream);
        assertEquals("Test2 failed", res, 3);
    }

    @Test
    public void test3() throws Exception {
        InputStream stream = new ByteArrayInputStream("2 3  0 0  7 7  1 6  6 1  3 5  5 3  2 2  4 4  8".getBytes());
        Solution instance = new Solution();
        int res = instance.getMinSteps(stream);
        assertEquals("Test3 failed", res, 15);
    }

    @Test
    public void test4() throws Exception {
        InputStream stream = new ByteArrayInputStream("1 1  2 3  3 5  4 4  6 2  0 6  7 7  5 1  1 6  5".getBytes());
        Solution instance = new Solution();
        int res = instance.getMinSteps(stream);
        assertEquals("Test4 failed", res, 7);
    }

    @Test
    public void test5() throws Exception {
        InputStream stream = new ByteArrayInputStream("5 5  0 3  1 1  2 5  4 7  6 1  7 3  3 2  5 0  6".getBytes());
        Solution instance = new Solution();
        int res = instance.getMinSteps(stream);
        assertEquals("Test5 failed", res, 9);
    }
}
