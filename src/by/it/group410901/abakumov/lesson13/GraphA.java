package by.it.group410901.abakumov.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Считываем строку, описывающую структуру ориентированного графа.
        String input = sc.nextLine().trim();
        sc.close();

        // TreeMap используется для автоматической сортировки ключей по алфавиту
        Map<String, List<String>> graph = new TreeMap<>();

        // Храним количество входящих рёбер для каждой вершины
        Map<String, Integer> indegree = new TreeMap<>();

        // Разделяем входную строку по запятым, чтобы получить отдельные связи
        String[] relations = input.split(",");

        for (String rel : relations) {
            // Разделяем по "->", слева — from, справа — to
            String[] parts = rel.trim().split("->");

            String from = parts[0].trim(); // начальная вершина
            String to = parts[1].trim();   // конечная вершина

            // Добавляем вершины в структуру графа, если их ещё не было
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            // Добавляем направленное ребро: from -> to
            graph.get(from).add(to);

            // Обновляем количество входящих рёбер для to
            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
            // Убедимся, что вершина from тоже есть в таблице входящих степеней
            indegree.putIfAbsent(from, indegree.getOrDefault(from, 0));
        }

        // Очередь для вершин с нулём входящих рёбер (можно ставить в результат)
        // PriorityQueue обеспечивает лексикографический порядок
        Queue<String> queue = new PriorityQueue<>();

        // Добавляем в очередь все вершины, у которых входящих рёбер нет
        for (String vertex : indegree.keySet()) {
            if (indegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        // Строим результат топологической сортировки
        StringBuilder result = new StringBuilder();

        // Пока есть вершины, доступные к добавлению
        while (!queue.isEmpty()) {
            // Извлекаем вершину с минимальным лексикографическим значением
            String v = queue.poll();
            result.append(v).append(" ");

            // Проходим по всем вершинам, в которые из неё есть ребро
            for (String neighbor : graph.get(v)) {
                // Уменьшаем входящую степень этой вершины
                indegree.put(neighbor, indegree.get(neighbor) - 1);

                // Если входящих рёбер больше не осталось — добавляем в очередь
                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Выводим итог в консоль
        System.out.println(result.toString().trim());
    }
}
