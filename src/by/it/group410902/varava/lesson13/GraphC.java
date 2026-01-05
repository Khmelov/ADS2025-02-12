package by.it.group410902.varava.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();// закрываем сканер для освобождения ресурсов

        // создаём прямой граф: вершина -> список вершин, в которые ведут стрелки
        // например: a -> [b, c] означает: из a есть стрелки в b и c
        Map<String, List<String>> graph = new HashMap<>();

        Map<String, List<String>> reverseGraph = new HashMap<>(); // обратный граф: вершина -> список вершин, из которых ведут стрелки

        // входная строка разбивается на строки с запятыми
        // a->b, b->c превращается в массив [a->b, b->c]
        String[] edges = input.split("\\s*,\\s*");

        // обрабатываем каждое ребро в массиве
        for (String edge : edges) {
            // разбиваем ребро на две части по стрелке ->
            // a->b превращается в массив [a, b]
            String[] parts = edge.split("\\s*->\\s*");
            // извлекаем начальную вершину(откуда идёт стрелка)
            String from = parts[0];
            // извлекаем конечную вершину(куда идёт стрелка)
            String to = parts[1];

            // добавляем ребро в прямой граф
            // если вершины "from" еще нет в графе, создаем для нее запись
            if (!graph.containsKey(from)) {
                graph.put(from, new ArrayList<>());// если нет, то создаём новый список соседей
            }
            graph.get(from).add(to);// добавляем конечную вершину в список соседей начальной вершины

            // добавляем ребро в обратный граф (меняем направление стрелки)
            // если вершины "to" еще нет в обратном графе, создаем запись
            if (!reverseGraph.containsKey(to)) {
                reverseGraph.put(to, new ArrayList<>());// пустой список
            }
            reverseGraph.get(to).add(from);// в обратном графе из "to" ведет стрелка в "from"

            // убеждаемся, что все вершины есть в обоих графах
            // даже если у вершины нет исходящих стрелок
            graph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.putIfAbsent(from, new ArrayList<>());
        }

        // вызываем алгоритм косарайю для поиска компонент сильной связности
        List<Set<String>> components = kosaraju(graph, reverseGraph);

        // подготавливаем результат для вывода
        // создаем список для хранения отформатированных компонент
        List<String> output = new ArrayList<>();

        // обрабатываем каждую найденную компоненту
        for (Set<String> component : components) {
            // преобразуем множество вершин в отсортированный список
            // для лексикографической сортировки (по алфавиту)
            List<String> sortedComponent = new ArrayList<>(component);
            // сортируем вершины в компоненте по алфавиту
            Collections.sort(sortedComponent);

            // создаем строку, объединяя все вершины компоненты
            // например: [a, b, c] -> abc
            StringBuilder sb = new StringBuilder();
            for (String vertex : sortedComponent) {
                sb.append(vertex);
            }
            // добавляем полученную строку в список вывода
            output.add(sb.toString());
        }

        // выводим результат на экран
        // каждая компонента выводится на отдельной строке
        for (String component : output) {
            System.out.println(component);
        }
    }

    // метод, реализующий алгоритм косарайю для поиска компонент сильной связности
    // алгоритм состоит из двух проходов dfs: первого по прямому графу и второго по обратному
    private static List<Set<String>> kosaraju(Map<String, List<String>> graph,
                                              Map<String, List<String>> reverseGraph) {
        // создаем множество для отслеживания посещенных вершин
        Set<String> visited = new HashSet<>();
        // создаем стек для запоминания порядка завершения обработки вершин
        Stack<String> stack = new Stack<>();

        // первый проход: обходим прямой граф и заполняем стек
        // перебираем все вершины прямого графа
        for (String vertex : graph.keySet()) {
            // если вершина еще не посещена, запускаем из нее dfs
            if (!visited.contains(vertex)) {
                dfsFirstPass(graph, vertex, visited, stack);
            }
        }

        // второй проход: обходим обратный граф в порядке из стека
        // очищаем множество посещенных вершин для второго прохода
        visited.clear();
        // создаем список для хранения найденных компонент связности
        List<Set<String>> components = new ArrayList<>();

        // обрабатываем вершины в обратном порядке (из стека)
        while (!stack.isEmpty()) {
            // извлекаем вершину из верхушки стека
            String vertex = stack.pop();
            // если вершина еще не посещена во втором проходе
            if (!visited.contains(vertex)) {
                // создаем новую компоненту связности
                Set<String> component = new HashSet<>();
                // запускаем dfs по обратному графу для поиска всей компоненты
                dfsSecondPass(reverseGraph, vertex, visited, component);
                // добавляем найденную компоненту в список результатов
                components.add(component);
            }
        }

        // возвращаем список всех найденных компонент связности
        return components;
    }

    // метод первого прохода dfs (для прямого графа)
    // цель: заполнить стек порядком завершения обработки вершин
    private static void dfsFirstPass(Map<String, List<String>> graph, String vertex,
                                     Set<String> visited, Stack<String> stack) {
        // помечаем текущую вершину как посещенную
        visited.add(vertex);

        // перебираем всех соседей текущей вершины в прямом графе
        for (String neighbor : graph.get(vertex)) {
            // если сосед еще не посещен, рекурсивно обрабатываем его
            if (!visited.contains(neighbor)) {
                dfsFirstPass(graph, neighbor, visited, stack);
            }
        }

        // после того, как обработали всех соседей, добавляем вершину в стек
        // вершины добавляются в стек в порядке обратном порядку завершения обработки
        stack.push(vertex);
    }

    // метод второго прохода dfs (для обратного графа)
    // цель: найти все вершины, достижимые из данной в обратном графе
    private static void dfsSecondPass(Map<String, List<String>> reverseGraph, String vertex,
                                      Set<String> visited, Set<String> component) {
        // помечаем текущую вершину как посещенную
        visited.add(vertex);
        // добавляем вершину в текущую компоненту связности
        component.add(vertex);

        // перебираем всех соседей текущей вершины в обратном графе
        for (String neighbor : reverseGraph.get(vertex)) {
            // если сосед еще не посещен, рекурсивно обрабатываем его
            if (!visited.contains(neighbor)) {
                dfsSecondPass(reverseGraph, neighbor, visited, component);
            }
        }
    }
}