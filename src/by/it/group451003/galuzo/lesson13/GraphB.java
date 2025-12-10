package by.it.group451003.galuzo.lesson13;

import java.util.ArrayList;
import java.util.Scanner;

public class GraphB extends GraphA{
    GraphB(Scanner scanner) {
        super(scanner);
    }

    public Boolean hasCycle() {
        return cycleDFS(new ArrayList<>(), graph.keySet().iterator().next());
    }

    private Boolean cycleDFS(ArrayList<String> path, String key) {
        if (path.contains(key)) return true;

        path.add(key);

        if (graph.get(key) != null) {
            for (String neighbor : graph.get(key)) {
                if (cycleDFS(path, neighbor)) {
                    path.remove(path.size() - 1);
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    public static void main(String[] args) {
        GraphB myGraphB = new GraphB(new Scanner(System.in));
        System.out.println(myGraphB.hasCycle() ? "yes" : "no");
    }
}
