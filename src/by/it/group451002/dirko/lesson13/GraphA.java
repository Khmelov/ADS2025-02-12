package by.it.group451002.dirko.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {
    static private int getIndex(String[] graphKeys, String graphKey) {
        for (int i = 0; i < graphKeys.length; i++)
            if (Objects.equals(graphKeys[i], graphKey))
                return i;
        return -1;
    }

    static private void dfs(HashMap<String, ArrayList<String>> graphs, String[] graphKeys, boolean[] isVisited,
                            int index, String graphKey, Stack<String> stack) {
        if (!isVisited[index]) {
            isVisited[index] = true;
            for (String value: graphs.get(graphKey))
                dfs(graphs, graphKeys, isVisited, getIndex(graphKeys, value), value, stack);
            stack.push(graphKey);
        }
    }

    static private void topologicalSort(HashMap<String, ArrayList<String>> graphs, String[] graphKeys) {
        boolean[] isVisited = new boolean[graphKeys.length];
        Stack<String> stack = new Stack<>();
        for (int i = graphKeys.length - 1; i >= 0; i--)
            dfs(graphs, graphKeys, isVisited, i, graphKeys[i], stack);

        int len = stack.size();
        for (int i = 0; i < len; i++)
            System.out.print(stack.pop() + " ");
    }

    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String s = r.readLine();
        String[] vs = s.split(", ");

        HashMap<String, ArrayList<String>> graphs = new HashMap<>();
        String[] graphKeys = new String[0];
        for (String v: vs) {
            String[] info = v.split(" -> ");
            String key = info[0];
            String value = info[1];
            if (!graphs.containsKey(key))
                graphs.put(key, new ArrayList<>(Collections.singletonList(value)));
            else graphs.get(key).add(value);
            if (!graphs.containsKey(value))
                graphs.put(value, new ArrayList<>());
            graphKeys = graphs.keySet().toArray(new String[0]);
        }
        topologicalSort(graphs, graphKeys);
    }
}
