package by.it.group410901.kalach.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        // Создаем сканер для чтения входных данных
        Scanner sc = new Scanner(System.in);
        // Читаем строку с описанием графа и убираем пробелы по краям
        String line = sc.nextLine().trim();

        // Основной граф: вершина -> список смежных вершин (исходящие ребра)
        Map<String, List<String>> g = new HashMap<>();
        // Транспонированный граф: вершина -> список смежных вершин (входящие ребра)
        Map<String, List<String>> rg = new HashMap<>();
        // Множество всех вершин (TreeSet для автоматической сортировки)
        Set<String> verts = new TreeSet<>();

        // Разбираем входную строку на ребра
        for (String e : line.split(",\\s*")) {
            // Пропускаем пустые строки
            if (e.isEmpty()) continue;

            // Разделяем каждое ребро на начальную и конечную вершину
            String[] p = e.split("->");
            String u = p[0].trim(); // Начальная вершина ребра
            String v = p[1].trim(); // Конечная вершина ребра

            // Добавляем ребро в основной граф (u -> v)
            g.computeIfAbsent(u, x -> new ArrayList<>()).add(v);
            // Добавляем обратное ребро в транспонированный граф (v -> u)
            rg.computeIfAbsent(v, x -> new ArrayList<>()).add(u);

            // Добавляем обе вершины в множество всех вершин
            verts.add(u);
            verts.add(v);
        }

        // Убеждаемся, что все вершины есть в обоих графах
        for (String v : verts) {
            g.putIfAbsent(v, new ArrayList<>());
            rg.putIfAbsent(v, new ArrayList<>());
        }

        // АЛГОРИТМ КОСАРАЙЮ ДЛЯ ПОИСКА КОМПОНЕНТ СИЛЬНОЙ СВЯЗНОСТИ

        // Стек для хранения вершин в порядке завершения их обработки в первом DFS
        Deque<String> stack = new ArrayDeque<>();
        // Массив посещенных вершин для первого обхода
        boolean[] visited = new boolean[verts.size()];
        // Словарь для сопоставления вершин с индексами в массиве visited
        Map<String, Integer> indexMap = new HashMap<>();

        // Преобразуем множество вершин в массив для удобства работы с индексами
        String[] arr = verts.toArray(new String[0]);
        // Заполняем словарь сопоставления вершин и индексов
        for (int i = 0; i < arr.length; i++)
            indexMap.put(arr[i], i);

        // ПЕРВЫЙ DFS: обход в глубину по исходному графу
        // Заполняем стек в порядке завершения обработки вершин
        for (String v : verts) {
            if (!visited[indexMap.get(v)]) {
                dfs1(v, g, visited, indexMap, stack);
            }
        }

        // ВТОРОЙ DFS: обход в глубину по транспонированному графу
        // Сбрасываем массив посещенных вершин
        Arrays.fill(visited, false);
        // Список для хранения компонент сильной связности
        List<List<String>> scc = new ArrayList<>();

        // Обрабатываем вершины из стека (в порядке убывания времени завершения)
        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited[indexMap.get(v)]) {
                // Новая компонента сильной связности
                List<String> component = new ArrayList<>();
                dfs2(v, rg, visited, indexMap, component);
                // Сортируем вершины компоненты в алфавитном порядке
                component.sort(String::compareTo);
                scc.add(component);
            }
        }

        // Выводим компоненты сильной связности
        // Каждая компонента выводится на отдельной строке
        for (List<String> comp : scc) {
            StringBuilder sb = new StringBuilder();
            for (String v : comp) {
                sb.append(v);
            }
            System.out.println(sb);
        }
    }

    /**
     * Первый DFS обход - заполнение стека в порядке завершения обработки вершин
     * @param v текущая вершина
     * @param graph исходный граф
     * @visited массив посещенных вершин
     * @param idx словарь сопоставления вершин и индексов
     * @param stack стек для хранения вершин в порядке завершения
     */
    private static void dfs1(String v, Map<String, List<String>> graph,
                             boolean[] visited, Map<String, Integer> idx, Deque<String> stack) {
        // Помечаем вершину как посещенную
        visited[idx.get(v)] = true;

        // Рекурсивно обходим всех соседей
        for (String to : graph.get(v)) {
            if (!visited[idx.get(to)]) {
                dfs1(to, graph, visited, idx, stack);
            }
        }

        // После обработки всех потомков добавляем вершину в стек
        stack.push(v);
    }

    /**
     * Второй DFS обход - построение компонент сильной связности
     * @param v текущая вершина
     * @param graph транспонированный граф
     * @visited массив посещенных вершин
     * @param idx словарь сопоставления вершин и индексов
     * @param comp текущая компонента сильной связности
     */
    private static void dfs2(String v, Map<String, List<String>> graph,
                             boolean[] visited, Map<String, Integer> idx, List<String> comp) {
        // Помечаем вершину как посещенную
        visited[idx.get(v)] = true;
        // Добавляем вершину в текущую компоненту
        comp.add(v);

        // Рекурсивно обходим всех соседей в транспонированном графе
        for (String to : graph.get(v)) {
            if (!visited[idx.get(to)]) {
                dfs2(to, graph, visited, idx, comp);
            }
        }
    }
}