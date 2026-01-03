package by.it.group410902.linnik.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());//если списка смежности нет создаем
            graph.get(from).add(to); //получаем список смежности и лобавляем вершину

            graph.putIfAbsent(to, new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        boolean hasCycle = false;

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                Set<String> currentPath = new HashSet<>(); //текущий путь обхода
                Stack<String> stack = new Stack<>(); //для обхода в глубину
                stack.push(node);

                while (!stack.isEmpty()) {
                    String current = stack.peek(); //смотрим верхний элемент стека

                    if (!visited.contains(current)) { //если он не посещен то становится посещенным
                        visited.add(current);
                        currentPath.add(current); //+добавляем в текущий путь

                        boolean added = false;
                        for (String neighbor : graph.get(current)) { //перебираем соседей
                            if (currentPath.contains(neighbor)) { //если кто-то есть в текущем пути то это цикл
                                hasCycle = true;
                                break;
                            }
                            if (!visited.contains(neighbor)) { //если не посещен добавляем в стек для обхода
                                stack.push(neighbor);
                                added = true;
                                break; //прерываем цикл чтобы обойти соседа
                            }
                        }
                        if (hasCycle) break;
                        if (!added) { //если не было добавлено соседей извлекаем вершину из стека
                            stack.pop();
                            currentPath.remove(current); //и убираем из текущего пути
                        }
                    } else {
                        stack.pop(); //если вершина посещена извлекаем из стека
                        currentPath.remove(current); //и убираем из текущего пути
                    }
                }
            }
            if (hasCycle) break;
        }

        System.out.println(hasCycle ? "yes" : "no");
    }
}