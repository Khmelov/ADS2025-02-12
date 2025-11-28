package by.it.group410901.abakumov.lesson14;

import java.util.*;

public class PointsA {
    
    // Класс для представления точки в трёхмерном пространстве
    static class Point {
        int x, y, z; // Координаты точки
        
        Point(int x, int y, int z) {
            this.x = x; // Сохраняем координату x
            this.y = y; // Сохраняем координату y
            this.z = z; // Сохраняем координату z
        }
        
        // Вычисляем расстояние между двумя точками в 3D пространстве
        double distance(Point other) {
            // Используем Math.hypot как в тесте для вычисления евклидова расстояния
            // Сначала находим расстояние в плоскости XY, затем добавляем координату Z
            return Math.hypot(Math.hypot(x - other.x, y - other.y), z - other.z);
        }
    }
    
    // Структура данных DSU (Disjoint Set Union) для объединения множеств
    static class DSU {
        int[] parent; // Массив родителей: parent[i] - родитель элемента i
        int[] size; // Массив размеров: size[i] - размер множества с корнем i
        
        DSU(int n) {
            parent = new int[n]; // Создаём массив родителей
            size = new int[n]; // Создаём массив размеров
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Изначально каждый элемент - корень своего множества
                size[i] = 1; // Размер каждого множества равен 1
            }
        }
        
        // Находим корень множества, к которому принадлежит элемент x
        int find(int x) {
            if (parent[x] != x) {
                // Если x не является корнем, рекурсивно ищем корень
                parent[x] = find(parent[x]); // Path compression: сжимаем путь к корню
            }
            return parent[x]; // Возвращаем корень множества
        }
        
        // Объединяем два множества, содержащие элементы x и y
        void union(int x, int y) {
            int rootX = find(x); // Находим корень множества элемента x
            int rootY = find(y); // Находим корень множества элемента y
            if (rootX == rootY) return; // Если уже в одном множестве, ничего не делаем
            
            // Union by size: присоединяем меньшее дерево к большему
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY; // Корень меньшего дерева указывает на корень большего
                size[rootY] += size[rootX]; // Увеличиваем размер большего дерева
            } else {
                parent[rootY] = rootX; // Корень меньшего дерева указывает на корень большего
                size[rootX] += size[rootY]; // Увеличиваем размер большего дерева
            }
        }
        
        // Получаем размеры всех кластеров (компонент связности)
        List<Integer> getClusterSizes() {
            Map<Integer, Integer> clusterSizes = new HashMap<>(); // Мапа: корень -> размер кластера
            for (int i = 0; i < parent.length; i++) {
                int root = find(i); // Находим корень для каждого элемента
                // Размер кластера хранится только в корне, добавляем его один раз
                if (!clusterSizes.containsKey(root)) {
                    clusterSizes.put(root, size[root]); // Сохраняем размер кластера
                }
            }
            List<Integer> sizes = new ArrayList<>(clusterSizes.values()); // Собираем все размеры
            Collections.sort(sizes); // Сортируем размеры по возрастанию
            return sizes; // Возвращаем отсортированный список размеров
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Читаем ввод пользователя
        
        double distance = scanner.nextDouble(); // Считываем пороговое расстояние
        int n = scanner.nextInt(); // Считываем количество точек
        
        Point[] points = new Point[n]; // Массив для хранения всех точек
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt(); // Считываем координату x
            int y = scanner.nextInt(); // Считываем координату y
            int z = scanner.nextInt(); // Считываем координату z
            points[i] = new Point(x, y, z); // Создаём точку и сохраняем в массив
        }
        
        DSU dsu = new DSU(n); // Создаём структуру DSU для n точек
        
        // Объединяем точки, если расстояние между ними меньше порогового
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].distance(points[j]) < distance) {
                    // Если расстояние меньше порога, объединяем точки в один кластер
                    dsu.union(i, j);
                }
            }
        }
        
        List<Integer> clusterSizes = dsu.getClusterSizes(); // Получаем размеры всех кластеров
        clusterSizes.sort(Collections.reverseOrder()); // Сортировка по убыванию
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) System.out.print(" "); // Выводим пробел между числами
            System.out.print(clusterSizes.get(i)); // Выводим размер кластера
        }
        System.out.println(); // Переход на новую строку
    }
}

