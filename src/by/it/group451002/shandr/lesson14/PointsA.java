package by.it.group451002.shandr.lesson14;

import java.util.*;

public class PointsA {

    // Класс для реализации системы непересекающихся множеств (DSU)
    static class DSU {
        int[] parent; // массив для хранения родительских вершин
        int[] size;   // массив для хранения размеров множеств

        // Конструктор DSU: инициализация для n элементов
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // каждый элемент - корень своего множества
                size[i] = 1;   // начальный размер каждого множества = 1
            }
        }

        // Метод поиска корня множества с эвристикой сжатия пути
        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // рекурсивно ищем корень и сжимаем путь
            return parent[x];
        }

        // Объединение двух множеств
        void union(int a, int b) {
            int rootA = find(a); // находим корень множества a
            int rootB = find(b); // находим корень множества b

            if (rootA == rootB) return; // если уже в одном множестве - ничего не делаем

            // Эвристика по размеру: меньшее дерево присоединяем к большему
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;        // корень A теперь указывает на B
                size[rootB] += size[rootA];   // увеличиваем размер множества B
            } else {
                parent[rootB] = rootA;        // корень B теперь указывает на A
                size[rootA] += size[rootB];   // увеличиваем размер множества A
            }
        }

        // Получение размера множества, содержащего элемент x
        int getSize(int x) {
            return size[find(x)];
        }
    }

    // Класс для представления точки в 3D пространстве
    static class Point {
        int x, y, z; // координаты точки

        // Конструктор точки
        Point(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }

        // Метод вычисления евклидова расстояния до другой точки
        double distanceTo(Point other) {
            int dx = x - other.x; // разница по X
            int dy = y - other.y; // разница по Y
            int dz = z - other.z; // разница по Z
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Чтение входных данных
        double D = sc.nextDouble(); // максимальное расстояние для объединения точек
        int N = sc.nextInt();       // количество точек

        Point[] points = new Point[N]; // массив точек

        // Чтение координат всех точек
        for (int i = 0; i < N; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();
            points[i] = new Point(x, y, z);
        }

        // Создание DSU для N точек
        DSU dsu = new DSU(N);

        // Объединение точек в кластеры по расстоянию
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Если расстояние между точками меньше D - объединяем их
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Подсчет размеров кластеров
        boolean[] counted = new boolean[N]; // массив для отметки уже посчитанных корней
        List<Integer> clusterSizes = new ArrayList<>(); // список размеров кластеров

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i); // находим корень для текущей точки
            if (!counted[root]) {
                // Если этот корень еще не встречался
                clusterSizes.add(dsu.getSize(root)); // добавляем размер кластера
                counted[root] = true;                // отмечаем корень как посчитанный
            }
        }

        // Сортировка размеров кластеров по убыванию
        clusterSizes.sort(Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1)
                System.out.print(" "); // пробел между числами, кроме последнего
        }
        System.out.println(); // перевод строки в конце
    }
}