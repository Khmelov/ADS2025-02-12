package by.it.group451003.rashchenya.lesson13;

import java.util.Scanner;

public class GraphB {

    static boolean[] visited;
    static boolean[] stack;
    static int getMax(int[] startVertex, int[] endVertex) {
        int size = Integer.MIN_VALUE;
        for (int i = 0; i < startVertex.length; i++) {
            if (startVertex[i] > size)
                size = startVertex[i];
            if (endVertex[i] > size)
                size = endVertex[i];
        }
        return size;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String[] arr = scan.nextLine().split(",");

        int[] startVertex = new int[arr.length];
        int[] endVertex = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            String[] current = arr[i].trim().split(" ");
            startVertex[i] = Integer.parseInt(current[0]);
            endVertex[i] = Integer.parseInt(current[current.length - 1]);
        }

        GraphUtil graph = new GraphUtil(getMax(startVertex, endVertex) + 1);
        for (int i = 0; i < arr.length; i++)
            graph.addOrientedEdge(startVertex[i], endVertex[i]);
        System.out.println(isCyclic(graph) ? "yes" : "no");
        scan.close();
    }

    public static boolean isCyclic(GraphUtil graph) {
        visited = new boolean[graph.vertexCount];
        stack = new boolean[graph.vertexCount];

        for (int i = 0; i < graph.vertexCount; i++) {
            if (!visited[i] && isCyclicUtil(graph, i))
                return true;
        }

        return false;
    }

    static boolean isCyclicUtil(GraphUtil graph, int vertex) {
        if (!visited[vertex]) {
            visited[vertex] = true;
            stack[vertex] = true;

            for (int neighbor : graph.getNeighbors(vertex)) {

                if (!visited[neighbor] && isCyclicUtil(graph, neighbor)) return true;
                if (stack[neighbor]) return true;
            }
        }

        stack[vertex] = false;
        return false;
    }

}
