package by.it.group451001.yarkovich.lesson14;
import java.util.*;
import java.io.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Считываем максимальное расстояние для объединения точек в кластер
        double distance = scanner.nextDouble();
        // Считываем количество точек в 3D пространстве
        int n = scanner.nextInt();

        // Массив для хранения координат точек (x, y, z)
        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt(); // координата X
            points[i][1] = scanner.nextInt(); // координата Y
            points[i][2] = scanner.nextInt(); // координата Z
        }

        // Создаем структуру DSU для объединения точек в кластеры
        DSU dsu = new DSU(n);

        // Проверяем все пары точек на возможность объединения
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Вычисляем евклидово расстояние между точками i и j
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                // Если расстояние меньше заданного, объединяем точки в один кластер
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        // Сборка информации о размерах кластеров
        int[] clusterSizes = new int[n];
        for (int i = 0; i < n; i++) {
            // Для каждой точки находим корень ее кластера и увеличиваем счетчик размера
            clusterSizes[dsu.find(i)]++;
        }

        // Формируем список размеров кластеров (исключаем нулевые значения)
        List<Integer> result = new ArrayList<>();
        for (int size : clusterSizes) {
            if (size > 0) {
                result.add(size);
            }
        }
        // Сортируем размеры кластеров по убыванию
        result.sort(Collections.reverseOrder());

        // Выводим результат - размеры кластеров в порядке убывания
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }

    // Класс DSU (Disjoint Set Union) для объединения множеств
    static class DSU {
        int[] parent; // массив родительских элементов
        int[] rank;   // массив рангов для оптимизации объединения

        // Конструктор инициализирует DSU для n элементов
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // изначально каждый элемент - корень своего множества
            }
        }

        // Метод find с эвристикой сжатия пути
        int find(int x) {
            if (parent[x] != x) {
                // Рекурсивно находим корень и сжимаем путь
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // Метод union для объединения двух множеств с эвристикой объединения по рангу
        void union(int x, int y) {
            int rx = find(x); // находим корень множества для x
            int ry = find(y); // находим корень множества для y

            if (rx != ry) {
                // Объединяем множества с учетом рангов для поддержания баланса
                if (rank[rx] < rank[ry]) {
                    parent[rx] = ry; // дерево с меньшим рангом присоединяется к дереву с большим рангом
                } else if (rank[rx] > rank[ry]) {
                    parent[ry] = rx;
                } else {
                    parent[ry] = rx;
                    rank[rx]++; // если ранги равны, увеличиваем ранг нового корня
                }
            }
        }
    }
}