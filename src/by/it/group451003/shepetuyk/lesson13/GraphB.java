package by.it.group451003.shepetuyk.lesson13;

import java.util.ArrayList;
import java.util.Scanner;

public class GraphB extends GraphA{
    GraphB(Scanner scanner) {
        super(scanner);
    }

    public Boolean hasCycle() {
        return cycleDFS(new ArrayList<>(), graph.keySet().iterator().next());
    }

    private Boolean cycleDFS(ArrayList<String> visited, String key) {
        visited.add(key);

        if (graph.get(key) != null) {
            for (String value : graph.get(key))
                if (visited.contains(value) || cycleDFS(visited, value))
                    return true;
        }

        return false;
    }

    public static void main(String[] args) {
        GraphB myGraphB = new GraphB(new Scanner(System.in));
        System.out.println(myGraphB.hasCycle() ? "yes" : "no");
    }
}
