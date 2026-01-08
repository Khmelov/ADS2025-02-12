package by.bsuir.dsa.csv2025.gr451002.Спижарная;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        String[] res = new Solution().alg(System.in);

        for (String str : res)
            System.out.println(str);
    }

    String[] alg(InputStream inputstream) throws FileNotFoundException {

        // читаем граф из файла
        Scanner input = new Scanner(inputstream);
        int n_vertex = input.nextInt(); //количество вершин графа
        // массив вершин, каждый элемент массива - список смежных вершин и веса ребер до них
        List<Item>[] graph = new List[n_vertex];
        for (int i = 0; i< n_vertex; i++){
            int n_adjacent = input.nextInt(); //количество смежных вершин
            graph[i] = new ArrayList<Item>(); //список смежных вершин
            for (int j=0; j<n_adjacent; j++)
                graph[i].add(new Item(input.nextInt(), input.nextInt()));
        }

        // массив длины кратчайшего найденного пути до каждой вершины
        int[] path_arr = new int[n_vertex];
        Arrays.fill(path_arr, Integer.MAX_VALUE);
        path_arr[0] = 0;
        // из какой вершины пришли в текущую по оптимальному пути
        int[] prev_arr = new int[n_vertex];
        Arrays.fill(prev_arr, -1); //-1 если отсутствует
        // куча чтобы оптимально искать минимум и добавлять элементы
        PriorityQueue<Pair> queue = new PriorityQueue<>();
        queue.add(new Pair(0, 0));

        while(!queue.isEmpty()){

            // берем вершину до которой путь минимальный
            // (из тех, для которых еще не считается что найден оптимальный путь)
            Pair min = queue.poll(); //минимальный путь считаем оптимальным

            // элементы не удаляются из произвольного места кучи тк это затратно,
            // поэтому могут встречаться повторяющиеся элементы со старыми значениями, их удаляем
            if (min.path > path_arr[min.vertex])
                continue;

            // релаксация (попытка уменьшить путь до смежных вершин)
            Iterator<Item> iterator = graph[min.vertex].iterator();
            while (iterator.hasNext()) {
                Item curr = iterator.next();
                if (min.path + curr.edge < path_arr[curr.vertex]) {
                    path_arr[curr.vertex] = min.path + curr.edge;
                    prev_arr[curr.vertex] = min.vertex;
                    queue.add(new Pair(curr.vertex, path_arr[curr.vertex]));
                }
            }
        }

        //вывод минимальных путей от нулевой(исток) до каждой из вершин
        String[] res = new String[n_vertex-1];
        for (int i=1; i<n_vertex; i++){
            StringBuilder sb = new StringBuilder(String.valueOf(i));
            int curr = prev_arr[i];
            while (curr != -1){
                sb.insert(0, " - ");
                sb.insert(0, curr);
                curr = prev_arr[curr];
            }
            res[i-1] = "path sum: " + path_arr[i] + " path: " + sb;
        }

        return res;
    }

    private class Item {
        int vertex;
        int edge;

        Item(int vertex, int edge) {
            this.vertex = vertex;
            this.edge = edge;
        }
    }

    private class Pair implements Comparable<Pair> {
        int vertex;
        int path;

        Pair(int vertex, int path) {
            this.vertex = vertex;
            this.path = path;
        }

        @Override
        public int compareTo(Pair o) {
            return this.path - o.path;
        }
    }

    @Test
    public void check_1() throws Exception {
        InputStream inputstream = new ByteArrayInputStream("6 2 1 7 4 4 3 0 7 5 2 2 5 3 1 5 5 6 3 11 3 2 11 5 9 4 8 3 0 4 5 3 3 8 4 1 2 2 6 3 9 4 3".getBytes());
        Solution instance = new Solution();
        String[] actual = instance.alg(inputstream);
        String[] expected = new String[] {
                "path sum: 7 path: 0 - 1",
                "path sum: 12 path: 0 - 1 - 2",
                "path sum: 12 path: 0 - 4 - 3",
                "path sum: 4 path: 0 - 4",
                "path sum: 7 path: 0 - 4 - 5" };
        //в случае несовпадения выводит где не совпало
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void check_2() throws Exception {
        InputStream inputstream = new ByteArrayInputStream("9  2  1 10  2 12  3  0 10  2 9  3 8  4  0 12  1 9  5 3  6 1  4  1 8  5 7  7 5  4 8  3  3 8  7 9  8 2  3  3 7  2 3  6 3  3  2 1  5 3  7 6  4  6 6  3 5  4 9  8 11  2  4 2  7 11".getBytes());
        Solution instance = new Solution();
        String[] actual = instance.alg(inputstream);
        String[] expected = new String[] {
                "path sum: 10 path: 0 - 1",
                "path sum: 12 path: 0 - 2",
                "path sum: 18 path: 0 - 1 - 3",
                "path sum: 26 path: 0 - 1 - 3 - 4",
                "path sum: 15 path: 0 - 2 - 5",
                "path sum: 13 path: 0 - 2 - 6",
                "path sum: 19 path: 0 - 2 - 6 - 7",
                "path sum: 28 path: 0 - 1 - 3 - 4 - 8" };
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void check_3() throws Exception {
        InputStream inputstream = new ByteArrayInputStream("6 2 1 7 4 4 3 0 7 5 20 2 5 3 1 5 5 6 3 1 3 2 1 5 9 4 8 3 0 4 5 10 3 8 4 1 20 2 6 3 9 4 10".getBytes());
        Solution instance = new Solution();
        String[] actual = instance.alg(inputstream);
        String[] expected = new String[] {
                "path sum: 7 path: 0 - 1",
                "path sum: 12 path: 0 - 1 - 2",
                "path sum: 12 path: 0 - 4 - 3",
                "path sum: 4 path: 0 - 4",
                "path sum: 14 path: 0 - 4 - 5" };
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void check_4() throws Exception {
        InputStream inputstream = new ByteArrayInputStream("6 1 1 7 3 0 7 5 30 2 5 3 1 5 5 29 3 11 3 2 11 5 9 4 8 3 0 4 5 3 3 8 4 1 30 2 29 3 9 4 3".getBytes());
        Solution instance = new Solution();
        String[] actual = instance.alg(inputstream);
        String[] expected = new String[] {
                "path sum: 7 path: 0 - 1",
                "path sum: 12 path: 0 - 1 - 2",
                "path sum: 23 path: 0 - 1 - 2 - 3",
                "path sum: 31 path: 0 - 1 - 2 - 3 - 4",
                "path sum: 32 path: 0 - 1 - 2 - 3 - 5" };
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void check_5() throws Exception {
        InputStream inputstream = new ByteArrayInputStream("9 2 1 10 2 12 3 0 10 2 2 3 8 4 0 12 1 2 5 3 6 10 4 1 8 5 7 7 5 4 8 3 3 8 7 3 8 2 3 3 7 2 3 6 3 3 2 10 5 3 7 6 4 6 6 3 5 4 3 8 11 2 4 2 7 11".getBytes());
        Solution instance = new Solution();
        String[] actual = instance.alg(inputstream);
        String[] expected = new String[] {
                "path sum: 10 path: 0 - 1",
                "path sum: 12 path: 0 - 2",
                "path sum: 18 path: 0 - 1 - 3",
                "path sum: 26 path: 0 - 1 - 3 - 4",
                "path sum: 15 path: 0 - 2 - 5",
                "path sum: 18 path: 0 - 2 - 5 - 6",
                "path sum: 23 path: 0 - 1 - 3 - 7",
                "path sum: 28 path: 0 - 1 - 3 - 4 - 8" };
        Assert.assertArrayEquals(expected, actual);
    }
}