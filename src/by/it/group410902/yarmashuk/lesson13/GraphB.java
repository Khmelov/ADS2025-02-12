package by.it.group410902.yarmashuk.lesson13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue; // Можно использовать обычную очередь, т.к. лексикографический порядок здесь не требуется, но PriorityQueue тоже работает
import java.util.Scanner;
import java.util.Set;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Выводим приглашение в System.err, чтобы System.out был чистым для результата

        String graphInput = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        // Обработка пустого ввода: если строка пуста, массив edges будет пустым
        String[] edges = graphInput.isEmpty() ? new String[0] : graphInput.split(", ");

        boolean malformedInputDetected = false;
        if (graphInput.trim().isEmpty() && edges.length == 0) { // Специальный случай для полностью пустого ввода
            // Ничего не делаем, граф пуст, цикла нет
        } else {
            for (String edge : edges) {
                String[] parts = edge.split(" -> ");
                if (parts.length != 2) {
                    System.err.println("Некорректный формат ребра: " + edge);
                    malformedInputDetected = true;
                    break; // Прекращаем обработку при первой некорректной части
                }
                String u = parts[0].trim(); // Источник
                String v = parts[1].trim(); // Назначение

                allNodes.add(u);
                allNodes.add(v);

                adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            }
        }

        if (malformedInputDetected) {
            // Если была ошибка формата, то выходим, но не печатаем "yes/no", т.к. граф некорректен
            return;
        }

        // Инициализируем входящие степени для всех обнаруженных узлов
        for (String node : allNodes) {
            inDegree.put(node, 0);
        }

        // Вычисляем входящие степени
        for (Map.Entry<String, List<String>> entry : adj.entrySet()) {
            for (String neighbor : entry.getValue()) {
                // Используем getOrDefault для безопасного увеличения, хотя allNodes должен гарантировать наличие
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Инициализируем очередь узлами с входящей степенью 0
        // Для поиска циклов лексикографический порядок не важен, можно использовать LinkedList как Queue
        PriorityQueue<String> readyNodes = new PriorityQueue<>(); // PriorityQueue работает, но LinkedList/ArrayDeque тоже подошли бы
        for (String node : allNodes) {
            if (inDegree.get(node) == 0) {
                readyNodes.add(node);
            }
        }

        // Выполняем "частичную" топологическую сортировку (алгоритм Кана)
        int nodesProcessed = 0;
        while (!readyNodes.isEmpty()) {
            String u = readyNodes.poll();
            nodesProcessed++;

            // Итерируем по соседям 'u'
            for (String v : adj.getOrDefault(u, Collections.emptyList())) {
                inDegree.put(v, inDegree.get(v) - 1); // Уменьшаем входящую степень соседа
                if (inDegree.get(v) == 0) {
                    readyNodes.add(v); // Если степень стала 0, добавляем соседа в очередь
                }
            }
        }

        // Если количество обработанных узлов не равно общему количеству узлов, значит, есть цикл
        if (nodesProcessed != allNodes.size()) {
            System.out.println("yes"); // Граф содержит цикл
        } else {
            System.out.println("no");  // Граф не содержит цикла
        }
    }
}
