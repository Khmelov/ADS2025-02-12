package by.it.group410902.varava.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {// Читаем входные данные

        Scanner scanner = new Scanner(System.in);//чтения ввода с клавиатуры
        String input = scanner.nextLine();
        scanner.close();// Закрытие сканера для освобождения ресурсов

        // Разбиваем на ребра: "0->2, 1->3" -> ["0->2", "1->3"]
        String[] edges = input.split("\\s*,\\s*");// Разбивка входной строки на отдельные ребра по запятым
        // \\s* означает "ноль или больше пробельных символов"

        // Собираем информацию о графе
        // Создание графа в виде словаря: вершина -> список соседей


        Map<String, List<String>> graph = new HashMap<>();  // куда ведут вершины

        // Создание словаря для подсчета входящих ребер для каждой вершины
        Map<String, Integer> incomingEdges = new HashMap<>(); // incomingEdges хранит количество стрелок, входящих в каждую вершину
        // Создание множества для хранения всех уникальных вершин графа
        Set<String> allVertices = new HashSet<>(); // HashSet автоматически удаляет дубликаты

        // Обрабатываем каждое ребро
        for (String edge : edges) {
            // Разбивка ребра на две части по стрелке "->"
            String[] parts = edge.split("\\s*->\\s*"); // Удаляем пробелы вокруг стрелки с помощью \\s*
            String from = parts[0]; // parts[0] - начальная вершина ребра (откуда идет стрелка)
            String to = parts[1]; // parts[1] - конечная вершина ребра (куда идет стрелка)

            // Добавляем вершины в общий список
            allVertices.add(from);
            allVertices.add(to);

            // Проверка, есть ли уже начальная вершина в графе
            // Если нет - создаем для нее новую запись с пустым списком соседей
            if (!graph.containsKey(from)) {
                graph.put(from, new ArrayList<>());
            }
            graph.get(from).add(to);// Добавление конечной вершины в список соседей начальной вершины

            // Увеличиваем счетчик входящих ребер для конечной вершины
            incomingEdges.put(to, incomingEdges.getOrDefault(to, 0) + 1);// getOrDefault возвращает текущее значение или 0, если вершины еще нет в словаре
        }

        // Обработка вершин, у которых нет входящих ребер
        // Для вершин без входящих ребер устанавливаем 0
        for (String vertex : allVertices) {
            incomingEdges.putIfAbsent(vertex, 0); // putIfAbsent добавляет запись только если ключа еще нет в словаре
        }

        // Топологическая сортировка
        List<String> result = topologicalSort(graph, incomingEdges, allVertices); // result будет содержать вершины в порядке топологической сортировки

        // Выводим результат
        for (String vertex : result) {
            System.out.print(vertex + " ");
        }
    }
    // Метод для выполнения топологической сортировки графа
    private static List<String> topologicalSort(Map<String, List<String>> graph,// - graph: структура графа (вершина -> список соседей)
                                                Map<String, Integer> incomingEdges, // - incomingEdges: счетчики входящих ребер для каждой вершины
                                                Set<String> allVertices) {// - allVertices: множество всех вершин графа
        List<String> result = new ArrayList<>();// Создание списка для хранения результата сортировки
        // В этот список будут добавляться вершины в правильном порядке

        // Очередь вершин без входящих ребер
        PriorityQueue<String> queue = new PriorityQueue<>(); // автоматически сортирует по алфавиту

        // Находим стартовые вершины (без входящих ребер)
        for (String vertex : allVertices) {
            if (incomingEdges.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        // Основной алгоритм
        while (!queue.isEmpty()) {
            // Берем вершину с наименьшим именем
            String current = queue.poll();// poll() удаляет и возвращает вершину из начала очереди
            result.add(current); // Добавление извлеченной вершины в результат сортировки

            // Уменьшаем счетчики входящих ребер для соседей
            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    incomingEdges.put(neighbor, incomingEdges.get(neighbor) - 1);

                    // Если у соседа больше нет входящих ребер - добавляем в очередь
                    if (incomingEdges.get(neighbor) == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return result;
    }
}