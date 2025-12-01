package by.it.group451003.kharkevich.lesson14;

import java.util.*;

public class PointsA {

    private static class DSU {
        int[] parent;  // Массив для хранения родительских элементов каждого узла
        int[] rank;    // Массив для хранения ранга (высоты) деревьев
        int[] size;    // Массив для хранения размера каждого множества

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {  // Инициализируем каждый элемент
                parent[i] = i;  // Изначально каждый элемент - корень своего множества
                size[i] = 1;    // Изначально размер каждого множества = 1
            }
        }

        int find(int x) {
            if (parent[x] != x) {  // Если x не является корнем
                parent[x] = find(parent[x]);  // Рекурсивно ищем корень и сжимаем путь
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);  // Находим корень первого элемента
            int rootY = find(y);  // Находим корень второго элемента

            if (rootX != rootY) {  // Если элементы в разных множествах
                if (rank[rootX] < rank[rootY]) {  // Если ранг первого дерева меньше
                    parent[rootX] = rootY;      // Подвешиваем первое дерево ко второму
                    size[rootY] += size[rootX]; // Обновляем размер объединенного множества
                } else if (rank[rootX] > rank[rootY]) {  // Если ранг второго дерева меньше
                    parent[rootY] = rootX;      // Подвешиваем второе дерево к первому
                    size[rootX] += size[rootY]; // Обновляем размер объединенного множества
                } else {  // Если ранги равны
                    parent[rootY] = rootX;  // Подвешиваем второе дерево к первому
                    size[rootX] += size[rootY]; // Обновляем размер объединенного множества
                    rank[rootX]++;  // Увеличиваем ранг получившегося дерева
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double d = scanner.nextDouble();
        int n = scanner.nextInt();
        int[][] points = new int[n][3];   // Создаем массив для хранения координат точек
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(n);      // Создаем объект DSU для управления кластерами
        double dSq = d * d;

        for (int i = 0; i < n; i++) {           // Перебираем все пары точек
            for (int j = i + 1; j < n; j++) {
                double dx = points[i][0] - points[j][0];  // Разность по x
                double dy = points[i][1] - points[j][1];  // Разность по y
                double dz = points[i][2] - points[j][2];  // Разность по z

                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < dSq) {  // Если точки находятся достаточно близко
                    dsu.union(i, j); // Объединяем их множества
                }
            }
        }

        int[] clusterSize = new int[n];  // Массив для хранения размеров кластеров
        for (int i = 0; i < n; i++) {
            clusterSize[dsu.find(i)]++;   // Увеличиваем счетчик размера ее кластера
        }

        List<Integer> sizes = new ArrayList<>();  // Список для ненулевых размеров кластеров
        for (int i = 0; i < n; i++) {
            if (clusterSize[i] > 0) {             // Если кластер существует
                sizes.add(clusterSize[i]);        // Добавляем его размер в список
            }
        }

        Collections.sort(sizes, Collections.reverseOrder());  // Сортируем размеры по убыванию

        for (int i = 0; i < sizes.size(); i++) {  // Выводим результаты
            if (i > 0) {                          // Если не первый элемент
                System.out.print(" ");            // Печатаем пробел-разделитель
            }
            System.out.print(sizes.get(i));       // Печатаем размер кластера
        }
        System.out.println();
    }
}