package by.it.group410902.jalilova.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        // создаем сканер для чтения ввода
        Scanner scanner = new Scanner(System.in);
        // читаем всю строку ввода
        String input = scanner.nextLine();
        // закрываем сканер
        scanner.close();

        // множество для хранения вершин (используем TreeSet для автоматической сортировки)
        Set<String> vertices = new TreeSet<>();
        // список для хранения ребер (каждое ребро - массив из двух элементов: откуда и куда)
        List<String[]> edges = new ArrayList<>();

        // разбиваем входную строку на пары вершин
        String[] pairs = input.split(", ");
        for (String pair : pairs) {
            // разделяем каждую пару на начальную и конечную вершину
            String[] parts = pair.split(" -> ");
            String from = parts[0].trim();  // начальная вершина
            String to = parts[1].trim();    // конечная вершина

            // добавляем ребро в список ребер
            edges.add(new String[]{from, to});
            // добавляем обе вершины в множество (TreeSet автоматически уберет дубликаты)
            vertices.add(from);
            vertices.add(to);
        }

        // создаем карту для подсчета входящих ребер для каждой вершины
        Map<String, Integer> incomingEdges = new HashMap<>();
        // инициализируем счетчики входящих ребер нулями для всех вершин
        for (String vertex : vertices) {
            incomingEdges.put(vertex, 0);
        }
        // подсчитываем количество входящих ребер для каждой вершины
        for (String[] edge : edges) {
            String to = edge[1];  // конечная вершина ребра
            // увеличиваем счетчик входящих ребер для конечной вершины
            incomingEdges.put(to, incomingEdges.get(to) + 1);
        }

        // список для хранения результата (топологической сортировки)
        List<String> result = new ArrayList<>();
        // очередь с приоритетом для вершин без входящих ребер (автоматически сортирует элементы)
        PriorityQueue<String> queue = new PriorityQueue<>();

        // находим все вершины без входящих ребер и добавляем их в очередь
        for (String vertex : vertices) {
            if (incomingEdges.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        // алгоритм топологической сортировки (алгоритм Кана)
        while (!queue.isEmpty()) {
            // извлекаем вершину с наименьшим приоритетом
            String current = queue.poll();
            // добавляем ее в результат
            result.add(current);

            // обрабатываем все исходящие ребра из текущей вершины
            for (String[] edge : edges) {
                if (edge[0].equals(current)) {
                    String to = edge[1];  // конечная вершина ребра
                    // уменьшаем счетчик входящих ребер для конечной вершины
                    incomingEdges.put(to, incomingEdges.get(to) - 1);
                    // если у вершины больше нет входящих ребер, добавляем ее в очередь
                    if (incomingEdges.get(to) == 0) {
                        queue.add(to);
                    }
                }
            }
        }

        // выводим результат топологической сортировки
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");  // добавляем пробел между вершинами
            System.out.print(result.get(i));
        }
    }
}