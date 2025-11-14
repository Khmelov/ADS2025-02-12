package by.it.group451001.strogonov.lesson13;

import java.util.*;

public class GraphC {

    private static void dfs1(ArrayList<HashSet<Integer>> pc, ArrayList<Boolean> visited, Stack<Integer> out, int cur){
        visited.set(cur, true);
        for (var i : pc.get(cur))
            if (!visited.get(i))
                dfs1(pc, visited, out, i);
        out.push(cur);
    }

    private static void dfs2(ArrayList<HashSet<Integer>> g, ArrayList<Boolean> visited, LinkedList<ArrayList<Integer>> res, int cur){
        visited.set(cur, true);
        res.getLast().add(cur);
        for (var i : g.get(cur))
            if (!visited.get(i))
                dfs2(g, visited, res, i);
    }

    private static void findComps(ArrayList<HashSet<Integer>> pc, ArrayList<HashSet<Integer>> cp, ArrayList<Boolean> visited, int cur, ArrayList<Integer> comps, int curComp){
        visited.set(cur, true);
        comps.set(cur, curComp);
        for (var i : pc.get(cur))
            if (!visited.get(i))
                findComps(pc, cp, visited, i, comps, curComp);
        for (var i : cp.get(cur))
            if (!visited.get(i))
                findComps(pc, cp, visited, i, comps, curComp);
    }

    private static LinkedList<LinkedList<ArrayList<Integer>>> Kosaraju(ArrayList<HashSet<Integer>> pc, ArrayList<HashSet<Integer>> cp){
        LinkedList<LinkedList<ArrayList<Integer>>> result = new LinkedList<>();
        ArrayList<Boolean> visited = new ArrayList<>(pc.size());
        while(visited.size() != pc.size())
            visited.add(false);
        ArrayList<Integer> comps = new ArrayList<>(pc.size());
        while (comps.size() != pc.size())
            comps.add(0);
        int nComp = 0;
        for (int i = 0; i < comps.size(); i++)
            if (!visited.get(i))
                findComps(pc, cp, visited, i, comps, nComp++);
        for (int comp = 0; comp < nComp; comp++) {
            Collections.fill(visited, false);
            Stack<Integer> out = new Stack<>();
            for (int i = 0; i < pc.size(); i++)
                if (comps.get(i).equals(comp) && !visited.get(i))
                    dfs1(pc, visited, out, i);
            Collections.fill(visited, false);
            LinkedList<ArrayList<Integer>> res = new LinkedList<>();
            while (!out.empty()) {
                if (!visited.get(out.peek())) {
                    res.add(new ArrayList<>());
                    dfs2(cp, visited, res, out.peek());
                }
                out.pop();
            }
            result.add(res);
        }
        return result;
    }

    private static int getMp(HashMap<Integer, String> mp, String name){
        for (var i : mp.entrySet())
            if (i.getValue().equals(name))
                return i.getKey();
        mp.put(mp.size(), name);
        return mp.size() - 1;
    }

    private static void setG(ArrayList<HashSet<Integer>> g, int p, Integer c){
        if (c == null)
            while (g.size() <= p)
                g.add(new HashSet<>());
        else{
            while (g.size() <= Math.max(p, c))
                g.add(new HashSet<>());
            g.get(p).add(c);
        }
    }

    public static void main(String[] args) {
        HashMap <Integer, String> mp = new HashMap<>();
        String[][] input;
        {
            Scanner in = new Scanner(System.in);
            String[] tmp = in.nextLine().split("\\s*,\\s*");
            in.close();
            input = new String[tmp.length][];
            for (int i = 0; i < tmp.length; i++)
                input[i] = tmp[i].split("\\s*->\\s*");
        }
        ArrayList<HashSet<Integer>> pc = new ArrayList<>();
        ArrayList<HashSet<Integer>> cp = new ArrayList<>();
        for (var arrS : input){
            if (arrS.length == 2){
                int n1 = getMp(mp, arrS[0]), n2 = getMp(mp, arrS[1]);
                setG(pc, n1, n2);
                setG(cp, n2, n1);
            }
            else{
                int n = getMp(mp, arrS[0]);
                setG(pc, n, null);
                setG(cp, n, null);
            }
        }
        var res = Kosaraju(pc, cp);
        TreeSet<String> output = new TreeSet<>();
        while (!res.isEmpty()){
            var tmp = res.poll();
            StringBuilder sb = new StringBuilder();
            while (!tmp.isEmpty()){
                TreeSet<String> s = new TreeSet<>();
                for (var i : tmp.getFirst())
                    s.add(mp.get(i));
                for (var i : s)
                    sb.append(i);
                sb.append('\n');
                tmp.removeFirst();
            }
            output.add(sb.toString());
        }
        StringBuilder sbLast = new StringBuilder();
        for (var i : output)
            sbLast.append(i);
        sbLast.deleteCharAt(sbLast.length() - 1);
        System.out.println(sbLast);
    }

}
