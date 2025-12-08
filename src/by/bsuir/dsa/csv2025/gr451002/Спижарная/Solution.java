package by.bsuir.dsa.csv2025.gr451002.Спижарная;

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
}