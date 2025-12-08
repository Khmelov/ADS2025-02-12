package by.bsuir.dsa.csv2025.gr410901.Зданович;

import java.util.*;
import java.io.*;

/*
Задача на программирование: Связность и минимальное остовное дерево
    https://ru.algorithmica.org/cs/spanning-trees/

Дано:
    1. Неориентированный взвешенный граф в формате:
       "A-B(5), B-C(3), C-D(4), A-D(10), ..."
       где число в скобках — вес ребра (стоимость дороги между районами).
    2. Число Q — количество запросов.
    3. Далее Q строк-запросов одного из двух видов:
       - MST
       - CONNECTED A B

Требуется:
    1. Построить минимальное остовное дерево (MST), если оно существует.
    2. Для запроса "MST" вывести:
           суммарную стоимость MST,
       либо строку:
           NO MST
       если граф несвязен и остовного дерева не существует.

    3. Для запроса "CONNECTED A B" определить,
       находятся ли вершины A и B в одной компоненте связности
       (используя структуру DSU или уже построенный остов).
       Вывести:
           YES
       или:
           NO
    Sample Input 1:
    A-B(5), B-C(3), C-D(4), A-D(10)
    3
    MST
    CONNECTED A C
    CONNECTED A E

    Sample Output 1:
    12
    YES
    NO

    Пояснение:
        Один из минимальных остовов:
            A-B(5), B-C(3), C-D(4)  ->  5+3+4 = 12
        Вершины A и C лежат в одной компоненте, A и E — нет (E нет в графе).

*/

public class Solution {

    // Ребро неориентированного графа
    static class Edge {
        int u;
        int v;
        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    // DSU (Union-Find) для связности и Краскала
    static class DSU {
        int[] parent;
        int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // первая строка: список рёбер
        if (!scanner.hasNextLine()) {
            return;
        }
        String graphInput = scanner.nextLine().trim();

        // вторая строка: количество запросов
        if (!scanner.hasNextLine()) {
            return;
        }
        int q = Integer.parseInt(scanner.nextLine().trim());

        // Парсинг графа
        Map<String, Integer> idByName = new HashMap<>();
        List<Edge> edges = new ArrayList<>();

        if (!graphInput.isEmpty()) {
            String[] parts = graphInput.split(",");
            for (String part : parts) {
                String edgeStr = part.trim();
                if (edgeStr.isEmpty()) continue;

                // формат: A-B(5)
                int dash = edgeStr.indexOf('-');
                int bracketOpen = edgeStr.indexOf('(');
                int bracketClose = edgeStr.indexOf(')');

                if (dash < 0 || bracketOpen < 0 || bracketClose < 0) {
                    continue; // можно бросать исключение, но для надёжности просто пропускаем
                }

                String left = edgeStr.substring(0, dash).trim();
                String right = edgeStr.substring(dash + 1, bracketOpen).trim();
                int w = Integer.parseInt(edgeStr.substring(bracketOpen + 1, bracketClose).trim());

                int u = getId(left, idByName);
                int v = getId(right, idByName);
                edges.add(new Edge(u, v, w));
            }
        }

        int n = idByName.size();

        // Если вершин нет (пустой граф)
        long mstWeight = 0;
        boolean hasMst = false;

        if (n == 0) {
            // пустой граф: MST считается 0 только если запросов нет на реальные вершины,
            // но по условию проще считать, что MST нет.
            hasMst = false;
        } else {
            // Строим MST алгоритмом Краскала
            Collections.sort(edges, Comparator.comparingInt(e -> e.w));
            DSU dsu = new DSU(n);
            long total = 0;
            int used = 0;

            for (Edge e : edges) {
                if (dsu.union(e.u, e.v)) {
                    total += e.w;
                    used++;
                }
            }

            if (used == n - 1) {
                hasMst = true;
                mstWeight = total;
            } else {
                hasMst = false;
            }
        }

        // Для ответов CONNECTED удобно иметь DSU связности по всему графу
        DSU dsuAll = new DSU(Math.max(1, n));
        for (Edge e : edges) {
            dsuAll.union(e.u, e.v);
        }

        // Обработка запросов
        while (q-- > 0 && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                q++; // пустую строку игнорируем
                continue;
            }

            String[] tokens = line.split("\\s+");
            String type = tokens[0];

            if ("MST".equalsIgnoreCase(type)) {
                if (hasMst) {
                    System.out.println(mstWeight);
                } else {
                    System.out.println("NO MST");
                }
            } else if ("CONNECTED".equalsIgnoreCase(type) && tokens.length == 3) {
                String aName = tokens[1];
                String bName = tokens[2];

                Integer aId = idByName.get(aName);
                Integer bId = idByName.get(bName);

                if (aId == null || bId == null) {
                    System.out.println("NO");
                } else {
                    System.out.println(
                            dsuAll.find(aId) == dsuAll.find(bId) ? "YES" : "NO"
                    );
                }
            } else {
                // неизвестный запрос — по желанию можно выводить ошибку
                // здесь просто ничего не делаем
            }
        }
    }

    private static int getId(String name, Map<String, Integer> map) {
        Integer id = map.get(name);
        if (id == null) {
            id = map.size();
            map.put(name, id);
        }
        return id;
    }
}


