package by.it.group410901.kalach.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        // Создаем сканер для чтения входных данных
        Scanner sc = new Scanner(System.in);
        // Читаем строку с описанием графа и убираем пробелы по краям
        String line = sc.nextLine().trim();

        // Создаем граф в виде словаря: вершина -> список смежных вершин
        Map<String, List<String>> g = new HashMap<>();
        // Используем HashSet для хранения всех уникальных вершин графа
        Set<String> verts = new HashSet<>();

        // Разбираем входную строку на ребра
        for (String e : line.split(",\\s*")) {
            // Пропускаем пустые строки (на случай, если ввод содержит лишние запятые)
            if (e.trim().isEmpty()) continue;

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
        for (String v : verts) {
            g.putIfAbsent(v, new ArrayList<>());
        }

        // Создаем словарь для подсчета полустепеней захода (количество входящих ребер)
        Map<String, Integer> inDegree = new HashMap<>();
        // Инициализируем все полустепени захода нулями
        for (String v : verts)
            inDegree.put(v, 0);

        // Подсчитываем полустепени захода для каждой вершины
        // Проходим по всем спискам смежности и увеличиваем счетчики для конечных вершин
        for (List<String> adj : g.values()) {
            for (String to : adj) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // Создаем очередь для вершин с нулевой полустепенью захода
        // ArrayDeque используется как обычная FIFO очередь
        Queue<String> q = new ArrayDeque<>();

        // Добавляем в очередь все вершины, у которых нет входящих ребер
        for (String v : verts) {
            if (inDegree.get(v) == 0) {
                q.add(v);
            }
        }

        // Счетчик обработанных вершин
        int processed = 0;

        // Алгоритм Кана для топологической сортировки
        while (!q.isEmpty()) {
            // Извлекаем вершину из начала очереди
            String u = q.poll();
            // Увеличиваем счетчик обработанных вершин
            processed++;

            // Обходим все смежные вершины (вершины, в которые ведут ребра из u)
            for (String v : g.get(u)) {
                // Уменьшаем полустепень захода для смежной вершины
                inDegree.put(v, inDegree.get(v) - 1);
                // Если полустепень захода стала нулевой, добавляем вершину в очередь
                if (inDegree.get(v) == 0) {
                    q.add(v);
                }
            }
        }

        // Проверяем наличие цикла:
        // Если количество обработанных вершин не равно общему количеству вершин,
        // значит в графе есть цикл (некоторые вершины не были обработаны)
        boolean hasCycle = processed != verts.size();

        // Выводим результат: "yes" если есть цикл, "no" если цикла нет
        System.out.println(hasCycle ? "yes" : "no");
    }
}