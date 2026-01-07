package by.it.group451002.dirko.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {
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

    static private void dfs_2(HashMap<String, ArrayList<String>> graphs, String[] graphKeys, boolean[] isVisited,
                            int index, String graphKey, StringBuilder component) {
        component.append(graphKey);
        isVisited[index] = true;
        for (String value: graphs.get(graphKey))
            if (!isVisited[getIndex(graphKeys, value)])
                dfs_2(graphs, graphKeys, isVisited, getIndex(graphKeys, value), value, component);
    }

    static private void kosarajiu(HashMap<String, ArrayList<String>> graphs, String[] graphKeys,
                                  HashMap<String, ArrayList<String>> transGraphs, String[] transGraphKeys) {
        boolean[] isVisited = new boolean[graphKeys.length];
        Stack<String> stack = new Stack<>();
        for (int i = graphKeys.length - 1; i >= 0; i--)
            dfs(graphs, graphKeys, isVisited, i, graphKeys[i], stack);

        isVisited = new boolean[transGraphKeys.length];
        for (int i = stack.size() - 1; i >= 0; i--)
            if (!isVisited[getIndex(transGraphKeys, stack.get(i))]) {
                StringBuilder component = new StringBuilder();
                dfs_2(transGraphs, transGraphKeys, isVisited, getIndex(transGraphKeys, stack.get(i)), stack.get(i), component);
                char[] temp = component.toString().toCharArray();
                Arrays.sort(temp);
                System.out.println(new String(temp));
            }
    }

    private static void fillGraphs(HashMap<String, ArrayList<String>> graphs, String[] vs, int keyInd, int valInd) {
        for (String v: vs) {
            String[] info = v.split("->");
            String key = info[keyInd];
            String value = info[valInd];
            if (!graphs.containsKey(key))
                graphs.put(key, new ArrayList<>(Collections.singletonList(value)));
            else graphs.get(key).add(value);
            if (!graphs.containsKey(value))
                graphs.put(value, new ArrayList<>());
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String s = r.readLine();
        String[] vs = s.split(", ");

        HashMap<String, ArrayList<String>> graphs = new HashMap<>();
        fillGraphs(graphs, vs, 0, 1);
        String[] graphKeys = graphs.keySet().toArray(new String[0]);

        HashMap<String, ArrayList<String>> transGraphs = new HashMap<>();
        fillGraphs(transGraphs, vs, 1, 0);
        String[] transGraphKeys = graphs.keySet().toArray(new String[0]);

        kosarajiu(graphs, graphKeys, transGraphs, transGraphKeys);
    }
}
