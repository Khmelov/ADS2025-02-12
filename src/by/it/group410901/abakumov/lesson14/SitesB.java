package by.it.group410901.abakumov.lesson14;

import java.util.*;

public class SitesB {
    
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
        
        Map<String, Integer> siteToIndex = new HashMap<>(); // Мапа: имя сайта -> его индекс
        List<String> sites = new ArrayList<>(); // Список всех уникальных сайтов
        List<int[]> connections = new ArrayList<>(); // Список связей между сайтами
        
        // Читаем связи между сайтами до строки "end"
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim(); // Считываем строку и убираем пробелы
            if (line.equals("end")) {
                break; // Если встретили "end", прекращаем чтение
            }
            
            String[] parts = line.split("\\+"); // Разделяем строку по символу '+'
            if (parts.length != 2) continue; // Если формат неправильный, пропускаем строку
            
            String site1 = parts[0].trim(); // Первый сайт из связи
            String site2 = parts[1].trim(); // Второй сайт из связи
            
            // Добавляем первый сайт, если его ещё нет
            if (!siteToIndex.containsKey(site1)) {
                siteToIndex.put(site1, sites.size()); // Присваиваем индекс равный текущему размеру списка
                sites.add(site1); // Добавляем сайт в список
            }
            // Добавляем второй сайт, если его ещё нет
            if (!siteToIndex.containsKey(site2)) {
                siteToIndex.put(site2, sites.size()); // Присваиваем индекс равный текущему размеру списка
                sites.add(site2); // Добавляем сайт в список
            }
            
            // Сохраняем связь между двумя сайтами в виде пары индексов
            connections.add(new int[]{siteToIndex.get(site1), siteToIndex.get(site2)});
        }
        
        int n = sites.size(); // Количество уникальных сайтов
        DSU dsu = new DSU(n); // Создаём структуру DSU для n сайтов
        
        // Объединяем связанные сайты в кластеры
        for (int[] conn : connections) {
            dsu.union(conn[0], conn[1]); // Объединяем два сайта из одной связи
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

