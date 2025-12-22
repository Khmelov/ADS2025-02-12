package by.it.group451003.galuzo.lesson13;

import java.util.*;

public class GraphC extends GraphA{
    private static class Component {
        String SCC;

        Component() {
            this.SCC = "";
        }

        public void addComponent(String node) {
            SCC = SCC.concat(node);
        }

        public void sort() {
            char[] charArr = SCC.toCharArray();
            Arrays.sort(charArr);
            SCC = new String(charArr);
        }

        public String toString() {
            return SCC;
        }
    }

    protected HashMap<String, ArrayList<String>> graphT;

    GraphC(Scanner scanner) {
        super(scanner);
        this.graphT = getTransposed();
    }

    private HashMap<String, ArrayList<String>> getTransposed() {
        HashMap<String, ArrayList<String>> graphT = new HashMap<>();
        Set<String> origKeys = graph.keySet();
        ArrayList<String> origValues;

        for (String key_valueT : origKeys) {
            origValues = graph.get(key_valueT);
            for (String value_keyT : origValues) {
                if (!graphT.containsKey(value_keyT))
                    graphT.put(value_keyT, new ArrayList<>());

                graphT.get(value_keyT).add(key_valueT);
            }
        }

        return graphT;
    }

    @Override
    protected HashMap<String, ArrayList<String>> createGraph(Scanner scanner) {
        HashMap<String, ArrayList<String>> graph = new HashMap<>();
        String key, value;

        for (String nodes : scanner.nextLine().split(", ")) {
            String[] twoNodes = nodes.split("->");
            key = twoNodes[0];
            value = twoNodes[1];

            if (graph.get(key) != null && graph.get(key).contains(value)) continue;

            if (!graph.containsKey(key))
                graph.put(key, new ArrayList<>());
            graph.get(key).add(value);
        }

        scanner.close();
        return graph;
    }

    @Override
    protected void DFS(ArrayList<String> order, ArrayList<String> visited, String key) {
        visited.add(key);

        if (graphT.get(key) != null) {
            Collections.sort(graphT.get(key));
            Collections.reverse(graphT.get(key));

            for (String value : graphT.get(key))
                if (!visited.contains(value))
                    DFS(order, visited, value);
        }

        order.add(key);
    }

    private void componentDFS(Component SCC, ArrayList<String> visited, String key) {
        visited.add(key);
        SCC.addComponent(key);

        if (graph.get(key) != null) {
            for (String value : graph.get(key))
                if (!visited.contains(value))
                    componentDFS(SCC, visited ,value);
        }
    }

    private ArrayList<Component> findSCC() {
        ArrayList<Component> components = new ArrayList<>();
        ArrayList<String> order = new ArrayList<>();
        ArrayList<String> visited = new ArrayList<>();

        for (String key : graphT.keySet())
            if (!visited.contains(key))
                DFS(order, visited, key);
        Collections.reverse(order);

        visited = new ArrayList<>();
        for (String key : order) {
            if (!visited.contains(key)) {
                Component component = new Component();
                componentDFS(component, visited, key);
                component.sort();
                components.add(component);
            }
        }

        Collections.reverse(components);
        return components;
    }

    public void getSCC() {
        ArrayList<Component> strongComponents = findSCC();
        for (Component component : strongComponents)
            System.out.println(component);
    }

    public static void main(String[] args) {
        GraphC myGraphC = new GraphC(new Scanner(System.in));
        myGraphC.getSCC();
    }
}
