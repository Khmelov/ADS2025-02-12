package by.it.group451002.vishnevskiy.lesson13;

import java.util.*;

public class GraphA {

    // Главный метод для выполнения топологической сортировки
    public static void main(String[] args) {
        // Создаем сканер для чтения ввода с консоли
        Scanner scanner = new Scanner(System.in);

        // Читаем всю строку ввода, содержащую описание графа
        String input = scanner.nextLine();

        // Вызываем функцию топологической сортировки и получаем упорядоченный список вершин
        List<String> topologicalOrder = topologicalSort(input);

        // Выводим результат на экран
        for (int i = 0; i < topologicalOrder.size(); i++) {
            if (i > 0) {
                System.out.print(" "); // Добавляем пробел между элементами (кроме первого)
            }
            System.out.print(topologicalOrder.get(i)); // Выводим текущую вершину
        }
        System.out.println(); // Переход на новую строку после вывода
    }

    // Функция для выполнения топологической сортировки ориентированного графа
    public static List<String> topologicalSort(String input) {
        // Граф представлен в виде словаря: вершина -> список её соседей (достижимых вершин)
        Map<String, List<String>> graph = new HashMap<>();

        // Словарь для хранения полустепени захода (in-degree) каждой вершины
        // Полустепень захода - сколько рёбер входит в вершину
        Map<String, Integer> inDegree = new HashMap<>();

        // Разбиваем входную строку по запятым, чтобы получить отдельные рёбра
        String[] edges = input.split(",\\s*");

        // Обрабатываем каждое ребро в описании графа
        for (String edge : edges) {
            // Разделяем ребро по символу "->" для получения начальной и конечной вершин
            String[] parts = edge.split("\\s*->\\s*");

            // Извлекаем начальную вершину (откуда идёт ребро)
            String from = parts[0].trim();

            // Извлекаем конечную вершину (куда идёт ребро)
            String to = parts[1].trim();

            // Добавляем ребро в граф:
            // Если вершина "from" еще не в графе, создаем для неё пустой список соседей
            // Затем добавляем вершину "to" в список соседей вершины "from"
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Инициализируем степени захода для обеих вершин, если их еще нет в словаре
            // putIfAbsent добавляет вершину только если её еще нет в словаре
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);
        }

        // Вычисляем полустепени захода для всех вершин графа
        // Проходим по всем спискам соседей в графе
        for (List<String> neighbors : graph.values()) {
            // Для каждого соседа в списке увеличиваем его полустепень захода на 1
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // Очередь с приоритетом для выбора следующей вершины для обработки
        // PriorityQueue автоматически сортирует элементы в лексикографическом порядке
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Находим все вершины с нулевой полустепенью захода (начальные вершины)
        // Проходим по всем записям в словаре полустепеней захода
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) { // Если у вершины нет входящих рёбер
                queue.offer(entry.getKey()); // Добавляем её в очередь с приоритетом
            }
        }

        // Список для хранения результата топологической сортировки
        List<String> result = new ArrayList<>();

        // Основной цикл алгоритма Кана для топологической сортировки
        // Продолжаем, пока в очереди есть вершины для обработки
        while (!queue.isEmpty()) {
            // Извлекаем вершину с наименьшим лексикографическим значением из очереди
            String current = queue.poll();

            // Добавляем эту вершину в результат сортировки
            result.add(current);

            // Если текущая вершина имеет исходящие рёбра (соседей)
            if (graph.containsKey(current)) {
                // Для каждого соседа текущей вершины
                for (String neighbor : graph.get(current)) {
                    // Уменьшаем полустепень захода соседа на 1
                    // Это "удаляем" ребро current -> neighbor из графа
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                    // Если у соседа теперь нет входящих рёбер
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor); // Добавляем его в очередь для обработки
                    }
                }
            }
        }

        // Возвращаем упорядоченный список вершин (топологический порядок)
        return result;
    }
}