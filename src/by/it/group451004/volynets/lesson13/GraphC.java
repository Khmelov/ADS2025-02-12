package by.it.group451004.volynets.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return;

        // Парсинг графа
        Map<String, List<String>> g = new HashMap<>();
        Map<String, List<String>> rg = new HashMap<>();
        Set<String> verts = new HashSet<>();

        input = input.replaceAll("\\s*->\\s*", "->").replaceAll("\\s*,\\s*", ",");
        for (String e : input.split(",")) {
            if (e.isEmpty()) continue;
            String[] p = e.split("->");
            if (p.length == 1) {
                String v = p[0];
                g.putIfAbsent(v, new ArrayList<>());
                rg.putIfAbsent(v, new ArrayList<>());
                verts.add(v);
            } else {
                String from = p[0], to = p[1];
                g.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                g.putIfAbsent(to, new ArrayList<>());
                rg.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                rg.putIfAbsent(from, new ArrayList<>());
                verts.add(from);
                verts.add(to);
            }
        }

        // Косарайю
        List<List<String>> comps = kosaraju(g, rg, verts);
        for (List<String> c : comps) Collections.sort(c);
        // сортируем вершины внутри каждой компоненты лексикографически

        // Создаём отображение: вершина -> номер компоненты
        Map<String, Integer> v2c = new HashMap<>();
        for (int i = 0; i < comps.size(); i++)
            for (String v : comps.get(i))
                v2c.put(v, i);

        // Строим граф
        List<Set<Integer>> cond = new ArrayList<>();
        int[] indeg = new int[comps.size()]; // массив входящих степеней для компонент
        for (int i = 0; i < comps.size(); i++) cond.add(new HashSet<>());

        // Проходим по рёбрам исходного графа
        for (var e : g.entrySet()) {
            int ci = v2c.get(e.getKey());
            for (String to : e.getValue()) {
                int cj = v2c.get(to);
                if (ci != cj && cond.get(ci).add(cj))
                    indeg[cj]++;          // добавляем ребро между компонентами и увеличиваем входящую степень
            }
        }

        PriorityQueue<Integer> q = new PriorityQueue<>(Comparator.comparing(i -> comps.get(i).get(0)));
        // очередь с приоритетом: выбираем компоненту по первой вершине

        for (int i = 0; i < comps.size(); i++)
            if (indeg[i] == 0) q.add(i); // кладём все истоки (inDegree=0)

        List<Integer> topo = new ArrayList<>();
        while (!q.isEmpty()) {
            int c = q.poll();
            topo.add(c); // достаём компоненту
            for (int nb : cond.get(c))
                if (--indeg[nb] == 0) q.add(nb); // уменьшаем входящую степень соседей, добавляем если она стала 0
        }

        for (int i = 0; i < topo.size(); i++) {
            System.out.print(String.join("", comps.get(topo.get(i))));
            if (i < topo.size() - 1) System.out.println();
        }
    }
    private static List<List<String>> kosaraju(Map<String,List<String>> g, Map<String,List<String>> rg, Set<String> verts){
        List<List<String>> res=new ArrayList<>();
        Set<String> vis=new HashSet<>();
        Stack<String> st=new Stack<>();
        List<String> vs=new ArrayList<>(verts); Collections.sort(vs);
        for(String v:vs) if(!vis.contains(v)) dfs1(v,g,vis,st);
        vis.clear();
        while(!st.isEmpty()){
            String v=st.pop();
            if(!vis.contains(v)){
                List<String> comp=new ArrayList<>();
                dfs2(v,rg,vis,comp);
                res.add(comp);
            }
        }
        return res;
    }
    private static void dfs1(String v,Map<String,List<String>> g,Set<String> vis,Stack<String> st){
        vis.add(v);
        List<String> ns=new ArrayList<>(g.getOrDefault(v,List.of())); Collections.sort(ns);
        for(String u:ns) if(!vis.contains(u)) dfs1(u,g,vis,st);
        st.push(v);
    }
    private static void dfs2(String v,Map<String,List<String>> rg,Set<String> vis,List<String> comp){
        vis.add(v); comp.add(v);
        List<String> ns=new ArrayList<>(rg.getOrDefault(v,List.of())); Collections.sort(ns);
        for(String u:ns) if(!vis.contains(u)) dfs2(u,rg,vis,comp);
    }
}
