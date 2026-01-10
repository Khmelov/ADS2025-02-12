package by.it.group451004.akbulatov.lesson13;

import java.util.*;

public class GraphC
{
    private static final Map<Character, List<Character>> graph = new HashMap<>();
    private static final Map<Character, List<Character>> reverseGraph = new HashMap<>();
    private static final Set<Character> visited = new HashSet<>();
    private static final Stack<Character> stack = new Stack<>();
    private static final List<TreeSet<Character>> components = new ArrayList<>();

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] vertices = edge.split("->");
            char from = vertices[0].charAt(0);
            char to = vertices[1].charAt(0);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            reverseGraph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.get(to).add(from);

            graph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.putIfAbsent(from, new ArrayList<>());
        }

        for (char vertex : graph.keySet())
            if (!visited.contains(vertex))
                dfs1(vertex);

        visited.clear();

        while (!stack.isEmpty())
        {
            char vertex = stack.pop();
            if (!visited.contains(vertex))
            {
                TreeSet<Character> component = new TreeSet<>();
                dfs2(vertex, component);
                components.add(component);
            }
        }

        for (TreeSet<Character> component : components)
        {
            for (char vertex : component)
                System.out.print(vertex);

            System.out.println();
        }
    }

    private static void dfs1(char vertex)
    {
        visited.add(vertex);
        for (char neighbor : graph.get(vertex))
            if (!visited.contains(neighbor))
                dfs1(neighbor);

        stack.push(vertex);
    }

    private static void dfs2(char vertex, TreeSet<Character> component)
    {
        visited.add(vertex);
        component.add(vertex);
        for (char neighbor : reverseGraph.get(vertex))
            if (!visited.contains(neighbor))
                dfs2(neighbor, component);
    }
}