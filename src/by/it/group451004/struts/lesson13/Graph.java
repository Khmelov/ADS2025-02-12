package by.it.group451004.struts.lesson13;

import java.util.*;

public class Graph {
    private Node begin = null;

    public Hashtable<String, Node> nodes = new Hashtable<>();

    public Graph(String input) {
        String[] edges = input.split(", ");
        String minName = edges[0].split("->")[0].trim();
        for (String edge : edges) {
            String[] nodes = edge.split("->");
            String from = nodes[0].trim();
            String to = nodes[1].trim();

            if (minName.compareTo(from) > 0)
                minName = from;
            if (minName.compareTo(to) > 0)
                minName = to;

            if (!this.nodes.containsKey(from)) {
                this.nodes.put(from, new Node(from));
            }

            if (!this.nodes.containsKey(to)) {
                this.nodes.put(to, new Node(to));
            }

            Node fromNode = this.nodes.get(from);
            Node toNode = this.nodes.get(to);

            fromNode.ways.add(toNode);
            toNode.reversedWays.add(fromNode);
        }

        begin = this.nodes.get(minName);
    }

    public String topologicalSort() {
        StringBuilder result = new StringBuilder();
        HashSet<String> visited = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(n -> n.name));
        queue.add(begin);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (!visited.contains(current.name)) {
                visited.add(current.name);
                result.append(current.name).append(" ");

                for (Node neighbor : current.ways) {
                    if (!visited.contains(neighbor.name)) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return result.toString().trim();
    }

    public boolean hasCycle() {
        Set<Node> whiteSet = new HashSet<>(nodes.values());
        Set<Node> graySet = new HashSet<>();
        Set<Node> blackSet = new HashSet<>();
        while (!whiteSet.isEmpty()) {
            Node current = whiteSet.iterator().next();
            if (dfs(current, whiteSet, graySet, blackSet)) {
                return true;
            }
        }

        return false;
    }

    public String getCycles() {
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        List<Node> sortedNodes = new ArrayList<>(nodes.values());
        sortedNodes.sort(Comparator.comparing(node -> node.name));

        for (Node u : sortedNodes)
            if (!visited.contains(u))
                dfs1(u, visited, stack);

        visited.clear();
        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            Node u = stack.pop();
            if (!visited.contains(u)) {
                TreeSet<String> component = new TreeSet<>();
                dfs2(u, visited, component);

                for (String name : component)
                    result.append(name);
                result.append("\n");
            }
        }

        return result.toString().trim();
    }

    private boolean dfs(Node current, Set<Node> whiteSet, Set<Node> graySet, Set<Node> blackSet) {
        whiteSet.remove(current);
        graySet.add(current);
        for (Node neighbor : current.ways) {
            if (blackSet.contains(neighbor)) {
                continue;
            }
            if (graySet.contains(neighbor)) {
                return true;
            }
            if (dfs(neighbor, whiteSet, graySet, blackSet)) {
                return true;
            }
        }
        graySet.remove(current);
        blackSet.add(current);
        return false;
    }

    private void dfs1(Node u, Set<Node> visited, Stack<Node> stack) {
        visited.add(u);
        for (Node v : u.ways) {
            if (!visited.contains(v)) {
                dfs1(v, visited, stack);
            }
        }
        stack.push(u);
    }

    private void dfs2(Node u, Set<Node> visited, TreeSet<String> component) {
        visited.add(u);
        component.add(u.name);

        for (Node v : u.reversedWays) {
            if (!visited.contains(v)) {
                dfs2(v, visited, component);
            }
        }
    }
}
