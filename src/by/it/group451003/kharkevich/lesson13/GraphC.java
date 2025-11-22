package by.it.group451003.kharkevich.lesson13;

import java.util.*;

public class GraphC {

    // Компаратор для обратного лексикографического порядка
    static class LexicalComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            return s2.compareTo(s1); // Сравниваем в обратном порядке
        }
    }

    public static void main(String[] args) {
        Map<String, ArrayList<String>> neighbours = new HashMap<>(); // Исходный граф
        Stack<String> st = new Stack<>(); // Стек для вершин по времени завершения
        Set<String> vis = new HashSet<>(); // Множество посещенных вершин

        Scanner scanner = new Scanner(System.in);
        String[] vals = scanner.nextLine().split(","); // Читаем и разбиваем входную строку
        scanner.close();

        String start;
        String end;

        // Строим исходный граф из входных данных
        for (String s : vals) {
            String[] current = s.trim().split(""); // Разбиваем каждое ребро
            start = current[0]; // Исходная вершина
            end = (current[current.length - 1]); // Целевая вершина

            if (neighbours.get(start) == null) // Если вершины еще нет в графе
                neighbours.put(start, new ArrayList<>()); // Добавляем ее

            neighbours.get(start).add(end); // Добавляем ребро
        }

        // Сортируем списки соседей в обратном лексикографическом порядке
        for (ArrayList<String> list: neighbours.values()) {
            list.sort(new LexicalComparator());
        }

        // Первый проход DFS - заполняем стек временами завершения
        for (String w : neighbours.keySet()) {
            if (!vis.contains(w)) { // Если вершина не посещена
                dfs(neighbours, w, vis, st); // Запускаем DFS
            }
        }

        // Транспонируем граф (обращаем направление всех ребер)
        Map<String, ArrayList<String>> transpNeighbours = new HashMap<>();
        for (String v : neighbours.keySet()) {
            ArrayList<String> list = neighbours.get(v);
            for (String child : list) {
                if (transpNeighbours.get(child) == null) // Если вершины нет в транспонированном графе
                    transpNeighbours.put(child, new ArrayList<>()); // Добавляем ее
                transpNeighbours.get(child).add(v); // Добавляем обратное ребро
            }
        }

        vis.clear(); // Очищаем множество посещенных вершин для второго прохода

        // Второй проход DFS - находим компоненты сильной связности
        while (!st.empty()) {
            String v = st.pop(); // Берем вершину из стека
            if (!vis.contains(v)) { // Если вершина не посещена во втором проходе
                StringBuilder sb = new StringBuilder(); // Для сбора вершин компоненты
                dfs(transpNeighbours, v, vis, sb); // Запускаем DFS в транспонированном графе
                char[] charArr = sb.toString().toCharArray(); // Преобразуем в массив символов
                Arrays.sort(charArr); // Сортируем вершины компоненты
                System.out.println(charArr); // Выводим отсортированную компоненту
            }
        }
    }

    // DFS для первого прохода (заполнение стека)
    private static void dfs(Map<String, ArrayList<String>> neighbours,
                            String v, Set<String> vis, Stack<String> st) {
        vis.add(v); // Помечаем вершину как посещенную

        if (neighbours.get(v) != null) { // Если у вершины есть соседи
            for (String child : neighbours.get(v)) { // Для каждого соседа
                if (!vis.contains(child)) { // Если сосед не посещен
                    dfs(neighbours, child, vis, st); // Рекурсивно обрабатываем
                }
            }
        }

        st.push(v); // После обработки всех соседей добавляем вершину в стек
    }

    // DFS для второго прохода (поиск компонент связности)
    private static void dfs(Map<String, ArrayList<String>> neighbours,
                            String v, Set<String> vis, StringBuilder sb) {
        vis.add(v); // Помечаем вершину как посещенную
        sb.append(v); // Добавляем вершину в текущую компоненту

        if (neighbours.get(v) != null) { // Если у вершины есть соседи
            for (String child : neighbours.get(v)) { // Для каждого соседа
                if (!vis.contains(child)) { // Если сосед не посещен
                    dfs(neighbours, child, vis, sb); // Рекурсивно обрабатываем
                }
            }
        }
    }
}