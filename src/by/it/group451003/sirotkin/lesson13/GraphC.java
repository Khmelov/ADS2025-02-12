package by.it.group451003.sirotkin.lesson13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class GraphC {
    public GraphC() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            Map<String, List<String>> graph = new HashMap();
            Map<String, List<String>> reverseGraph = new HashMap();
            Set<String> allNodes = new HashSet();
            String[] edges = input.split(",\\s*");
            String[] var7 = edges;
            int var8 = edges.length;

            String component;
            for(int var9 = 0; var9 < var8; ++var9) {
                String edge = var7[var9];
                String[] parts = edge.split("\\s*->\\s*");
                component = parts[0];
                String to = parts[1];
                ((List)graph.computeIfAbsent(component, (k) -> {
                    return new ArrayList();
                })).add(to);
                ((List)reverseGraph.computeIfAbsent(to, (k) -> {
                    return new ArrayList();
                })).add(component);
                allNodes.add(component);
                allNodes.add(to);
            }

            Iterator var16 = allNodes.iterator();

            while(var16.hasNext()) {
                String node = (String)var16.next();
                graph.putIfAbsent(node, new ArrayList());
                reverseGraph.putIfAbsent(node, new ArrayList());
            }

            Set<String> visited = new HashSet();
            Stack<String> stack = new Stack();
            List<String> sortedNodes = new ArrayList(allNodes);
            Collections.sort(sortedNodes);
            Iterator var21 = sortedNodes.iterator();

            String node;
            while(var21.hasNext()) {
                node = (String)var21.next();
                if (!visited.contains(node)) {
                    fillOrder(node, graph, visited, stack);
                }
            }

            visited.clear();
            List<String> result = new ArrayList();

            while(true) {
                do {
                    if (stack.isEmpty()) {
                        Iterator var24 = result.iterator();

                        while(var24.hasNext()) {
                            component = (String)var24.next();
                            System.out.println(component);
                        }

                        return;
                    }

                    node = (String)stack.pop();
                } while(visited.contains(node));

                List<String> component1 = new ArrayList();
                dfsReverse(node, reverseGraph, visited, component1);
                Collections.sort(component1);
                StringBuilder sb = new StringBuilder();
                Iterator var14 = component1.iterator();

                while(var14.hasNext()) {
                    String s = (String)var14.next();
                    sb.append(s);
                }

                result.add(sb.toString());
            }
        }
    }

    private static void fillOrder(String node, Map<String, List<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(node);
        List<String> neighbors = (List)graph.get(node);
        Iterator var5 = neighbors.iterator();

        while(var5.hasNext()) {
            String neighbor = (String)var5.next();
            if (!visited.contains(neighbor)) {
                fillOrder(neighbor, graph, visited, stack);
            }
        }

        stack.push(node);
    }

    private static void dfsReverse(String node, Map<String, List<String>> reverseGraph, Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        List<String> neighbors = (List)reverseGraph.get(node);
        Iterator var5 = neighbors.iterator();

        while(var5.hasNext()) {
            String neighbor = (String)var5.next();
            if (!visited.contains(neighbor)) {
                dfsReverse(neighbor, reverseGraph, visited, component);
            }
        }

    }
}
