package by.it.group451002.spitsyna.lesson13;

import com.sun.source.tree.Tree;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        TreeMap<String, List<String>> graph = new TreeMap<>();
        TreeMap<String, List<String>> reversedGraph = new TreeMap<>();

        String[] parseStr = input.split(",");
        for (int i = 0; i < parseStr.length; i++){
            parseStr[i] = parseStr[i].trim();
            String strSrc = parseStr[i].substring(0, parseStr[i].indexOf("-"));
            String strDest = parseStr[i].substring(parseStr[i].indexOf(">")+1);

            //создание графа
            if (!graph.containsKey(strSrc))
                graph.put(strSrc, new ArrayList<>());
            if (!graph.get(strSrc).contains(strDest))
                graph.get(strSrc).add(strDest);

            //создание обратного графа
            if (!reversedGraph.containsKey(strDest))
                reversedGraph.put(strDest, new ArrayList<>());
            if (!reversedGraph.get(strDest).contains(strSrc))
                reversedGraph.get(strDest).add(strSrc);
        }

        //Алгоритм Косараджу
        // DFS для обычного графа
        Set<String> visited = new HashSet<>();
        Stack<String> order = new Stack<>();
        for (String v : graph.keySet()) {
            if (!visited.contains(v))
                dfs1(graph, v, visited, order);
        }

        // DFS на обратном графе, чтобы найти компоненты сильной связности
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        //Проходимся в порядке выхода вершин, полученном из первого DFS
        while (!order.isEmpty()) {
            String currVert = order.pop();
            if (!visited.contains(currVert)) {
                List<String> component = new ArrayList<>();
                dfs2(reversedGraph, currVert, visited, component);
                Collections.sort(component); // лексикографический порядок внутри компоненты
                components.add(component);
            }
        }

        // 3. Вывод
        for (List<String> comp : components) {
            for (String s : comp)
                System.out.print(s);
            System.out.println();
        }
    }

    // DFS для построения порядка выхода вершин(когда все смежные вершины обошли, либо же их нет)
    private static void dfs1(TreeMap<String, List<String>> lgraph, String currVertex, Set<String> visited, Stack<String> order) {
        visited.add(currVertex);
        if (lgraph.get(currVertex) != null) {
            for (String adjVert : lgraph.get(currVertex)) {
                if (!visited.contains(adjVert))
                    dfs1(lgraph, adjVert, visited, order);
            }
        }
        order.push(currVertex);
    }

    // DFS на обратном графе для поиска компонентов сильной связности
    private static void dfs2(TreeMap<String, List<String>> lgraph, String currVertex, Set<String> visited, List<String> comp) {
        visited.add(currVertex);
        comp.add(currVertex);
        if (lgraph.get(currVertex) != null) {
            for (String adjVert : lgraph.get(currVertex)) {
                if (!visited.contains(adjVert))
                    dfs2(lgraph, adjVert, visited, comp);
            }
        }
    }
}
