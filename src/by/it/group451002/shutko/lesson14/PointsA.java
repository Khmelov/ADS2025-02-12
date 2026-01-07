package by.it.group451002.shutko.lesson14;

import java.util.*;

// у нас есть набор точек в трехмерном пространстве,
// и надо разбить их на кластеры на основе расстояний между ними.

// С консоли вводится допустимое расстояние D между точками НЕ ВКЛЮЧИТЕЛЬНО [0,D) и число точек N,
// а затем в каждой новой строке вводится точка с координатами X Y Z через пробел.
public class PointsA {

    // Внутренний класс для реализации структуры данных "Система непересекающихся множеств" (DSU)
    static class DSU {
        int[] parent;  // массив для хранения родителя каждого элемента
        int[] size;  // массив для хранения размера каждого множества

        // Конструктор DSU: инициализирует структуру для n элементов
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        // Метод для нахождения корневого элемента множества, содержащего x
        int find(int x) {
            if (parent[x] != x) // если x не корень
                parent[x] = find(parent[x]); // рекурсивно ищем корень и сжимаем путь
            return parent[x]; // возвращаем корень
        }

        // Метод для объединения двух множеств, содержащих элементы a и b
        void union(int a, int b) {
            int rootA = find(a); // находим корень множества для a
            int rootB = find(b); // находим корень множества для b

            if (rootA == rootB) return; // если уже в одном множестве, ничего не делаем

            // Эвристика по размеру: меньшее дерево присоединяем к большему
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;        // корень A становится ребенком корня B
                size[rootB] += size[rootA];   // обновляем размер множества B
            } else {
                parent[rootB] = rootA;        // корень B становится ребенком корня A
                size[rootA] += size[rootB];   // обновляем размер множества A
            }
        }

        // Метод для получения размера множества, содержащего элемент x
        int getSize(int x) {
            return size[find(x)]; // находим корень и возвращаем размер его множества
        }
    }

    // Внутренний класс для представления точки в трехмерном пространстве
    static class Point {
        int x, y, z; // координаты точки

        // Конструктор точки
        Point(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }

        // Метод для вычисления евклидова расстояния до другой точки
        double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            // Формула расстояния между двумя точками в 3D: sqrt((x1-x2)² + (y1-y2)² + (z1-z2)²)
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    // Основной метод программы
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Чтение входных данных: допустимое расстояние D и количество точек N
        double D = sc.nextDouble(); // расстояние, меньше которого точки считаются близкими
        int N = sc.nextInt();       // количество точек

        Point[] points = new Point[N]; // массив для хранения всех точек

        // Чтение координат всех точек
        for (int i = 0; i < N; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();
            points[i] = new Point(x, y, z); // создаем и сохраняем точку
        }

        // Создаем DSU для N элементов (каждая точка изначально в своем собственном кластере)
        DSU dsu = new DSU(N);

        // Проходим по всем парам точек для объединения в кластеры
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Если расстояние между точками i и j меньше допустимого D
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j); // объединяем их в один кластер
                }
            }
        }

        // Массив для отслеживания уже учтенных корней кластеров
        boolean[] counted = new boolean[N];
        // Список для хранения размеров найденных кластеров
        List<Integer> clusterSizes = new ArrayList<>();

        // Собираем размеры всех кластеров
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i); // находим корень кластера для точки i
            if (!counted[root]) {   // если этот кластер еще не учитывался
                clusterSizes.add(dsu.getSize(root)); // добавляем размер кластера
                counted[root] = true; // отмечаем кластер как учтенный
            }
        }

        // Сортируем размеры кластеров в порядке убывания (по условию вывода)
        clusterSizes.sort(Collections.reverseOrder());

        // Выводим размеры кластеров через пробел
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) System.out.print(" "); // пробел между числами
        }
        System.out.println(); // перевод строки в конце
    }
}