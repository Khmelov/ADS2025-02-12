package by.it.group410901.abakumov.lesson13;
import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Читаем ввод пользователя
        String input = sc.nextLine().trim(); // Считываем строку графа
        sc.close(); // Закрываем Scanner

        Map<String, List<String>> graph = new TreeMap<>(); // Граф: вершина -> список соседей
        Map<String, List<String>> reverseGraph = new TreeMap<>(); // Обратный граф для 2-го прохода

        String[] relations = input.split(","); // Разделяем строку на отдельные ребра

        for (String rel : relations) { // Обрабатываем каждое ребро
            String[] parts = rel.trim().split("->"); // Делим A->B на A и B
            String from = parts[0].trim(); // Левая вершина
            String to = parts[1].trim(); // Правая вершина

            graph.putIfAbsent(from, new ArrayList<>()); // Добавляем вершину в граф, если нет
            graph.putIfAbsent(to, new ArrayList<>()); // Добавляем вершину to, если нет
            reverseGraph.putIfAbsent(from, new ArrayList<>()); // В обратный граф
            reverseGraph.putIfAbsent(to, new ArrayList<>()); // В обратный граф

            graph.get(from).add(to); // Добавляем ребро from -> to
            reverseGraph.get(to).add(from); // В обратном графе to -> from
        }

        Set<String> visited = new HashSet<>(); // Множество посещённых вершин
        Stack<String> stack = new Stack<>(); // Стек для порядка обхода (первый DFS)

        // Первый обход DFS по исходному графу
        for (String v : graph.keySet()) { // Идём по всем вершинам
            if (!visited.contains(v)) { // Если вершина ещё не посещена
                dfs1(v, graph, visited, stack); // Запускаем DFS
            }
        }

        visited.clear(); // Очищаем посещённые для второго DFS

        // Второй обход DFS по обратному графу
        while (!stack.isEmpty()) { // Пока есть вершины в стеке
            String v = stack.pop(); // Берём вершину из стека
            if (!visited.contains(v)) { // Если не посещена во втором проходе
                List<String> component = new ArrayList<>(); // Создаём новую КСС
                dfs2(v, reverseGraph, visited, component); // Второй DFS собирает КСС
                Collections.sort(component); // Сортируем вершины КСС по алфавиту
                System.out.println(String.join("", component)); // Выводим компоненту без пробелов
            }
        }
    }

    // DFS для заполнения стека (по исходному графу)
    private static void dfs1(String v, Map<String, List<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(v); // Помечаем как посещённую
        for (String neigh : graph.get(v)) { // Обходим соседей
            if (!visited.contains(neigh)) { // Если сосед не посещён
                dfs1(neigh, graph, visited, stack); // Рекурсивный DFS
            }
        }
        stack.push(v); // Кладём вершину в стек после обработки
    }

    // DFS для поиска компонент (по обратному графу)
    private static void dfs2(String v, Map<String, List<String>> graph, Set<String> visited, List<String> component) {
        visited.add(v); // Помечаем как посещённую
        component.add(v); // Добавляем вершину в текущую компоненту
        for (String neigh : graph.get(v)) { // Обходим соседей в обратном графе
            if (!visited.contains(neigh)) { // Если сосед не посещён
                dfs2(neigh, graph, visited, component); // Рекурсивно добавляем в компоненту
            }
        }
    }
}
