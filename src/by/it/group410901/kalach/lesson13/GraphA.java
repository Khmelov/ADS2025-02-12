package by.it.group410901.kalach.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        // Создаем сканер для чтения входных данных
        Scanner sc = new Scanner(System.in);
        // Читаем строку с описанием графа и убираем пробелы по краям
        String line = sc.nextLine().trim();

        // Создаем граф в виде словаря: вершина -> список смежных вершин
        Map<String, List<String>> g = new HashMap<>();
        // Используем TreeSet для автоматической сортировки вершин в алфавитном порядке
        TreeSet<String> verts = new TreeSet<>();

        // Разбираем входную строку на ребра
        for (String e : line.split(",\\s*")) {
            // Разделяем каждое ребро на начальную и конечную вершину
            String[] p = e.split("->");
            String u = p[0].trim(); // Начальная вершина ребра
            String v = p[1].trim(); // Конечная вершина ребра

            // Добавляем ребро в граф
            // computeIfAbsent: если вершины u нет в графе, создаем для нее пустой список
            g.computeIfAbsent(u, x -> new ArrayList<>()).add(v);

            // Добавляем обе вершины в множество всех вершин
            verts.add(u);
            verts.add(v);
        }

        // Убеждаемся, что все вершины есть в графе (даже те, у которых нет исходящих ребер)
        for (String v : verts)
            g.putIfAbsent(v, new ArrayList<>());

        // Создаем словарь для подсчета полустепеней захода (количество входящих ребер)
        Map<String, Integer> in = new HashMap<>();
        // Инициализируем все полустепени захода нулями
        for (String v : verts)
            in.put(v, 0);

        // Подсчитываем полустепени захода для каждой вершины
        for (List<String> adj : g.values())
            for (String to : adj)
                in.put(to, in.get(to) + 1);

        // Создаем приоритетную очередь для вершин с нулевой полустепенью захода
        // PriorityQueue автоматически сортирует вершины в алфавитном порядке
        PriorityQueue<String> q = new PriorityQueue<>();

        // Добавляем в очередь все вершины, у которых нет входящих ребер
        for (String v : verts)
            if (in.get(v) == 0)
                q.add(v);

        // Строим результат - топологически отсортированный список вершин
        StringBuilder res = new StringBuilder();

        // Алгоритм Кана для топологической сортировки
        while (!q.isEmpty()) {
            // Извлекаем вершину с наименьшим алфавитным порядком
            String u = q.poll();
            // Добавляем вершину в результат
            res.append(u).append(" ");

            // Обходим все смежные вершины
            for (String v : g.get(u)) {
                // Уменьшаем полустепень захода для смежной вершины
                in.put(v, in.get(v) - 1);
                // Если полустепень захода стала нулевой, добавляем вершину в очередь
                if (in.get(v) == 0)
                    q.add(v);
            }
        }

        // Выводим результат, убирая последний пробел
        System.out.println(res.toString().trim());
    }
}