package by.it.group451001.apanovich.lesson13;

import java.util.*;

public class GraphA {
    @SuppressWarnings("all")
    private static Stack<ArrayList<Integer>> topSort(ArrayList<HashSet<Integer>> pc, ArrayList<ArrayList<Integer>> cp){
        var res = new Stack<ArrayList<Integer>>();
        var deleted = new HashSet<Integer>();
        while (deleted.size() != pc.size()){
            res.push(new ArrayList<Integer>());
            for (int i = 0; i < pc.size(); i++)
                if (pc.get(i).size() == 0 && !deleted.contains(i))
                    res.peek().add(i);
            for (var i : res.peek()){
                deleted.add(i);
                for (var j : cp.get(i))
                    pc.get(j).remove(i);
            }
        }
        return res;
    }

    public static void main(String[] arg){
        var mp = new HashMap<Integer, String>();
        String s;
        {
            Scanner in = new Scanner(System.in);
            s = in.nextLine();
            in.close();
        }
        var pc = new ArrayList<HashSet<Integer>>();
        var cp = new ArrayList<ArrayList<Integer>>();
        String[] sArr = s.split("\\s*,\\s*");
        for (int i = 0; i < sArr.length; i++){
            String[] tmp = sArr[i].split("\\s*->\\s*");
            String sn1 = tmp[0], sn2 = tmp[1];
            int i1 = 0, i2 = 0;
            if (!mp.containsValue(sn1)){
                mp.put(mp.size(), sn1);
                i1 = mp.size() - 1;
            }
            else
                while (!Objects.equals(mp.get(i1), sn1))
                    i1++;
            if (!mp.containsValue(sn2)){
                mp.put(mp.size(), sn2);
                i2 = mp.size() - 1;
            }
            else
                while (!Objects.equals(mp.get(i2), sn2))
                    i2++;
            if (pc.size() <= Math.max(i1, i2))
                while (pc.size() <= Math.max(i1, i2))
                    pc.add(new HashSet<>());
            if (cp.size() <= Math.max(i1, i2))
                while (cp.size() <= Math.max(i1,i2))
                    cp.add(new ArrayList<>());
            pc.get(i1).add(i2);
            cp.get(i2).add(i1);
        }
        var res = topSort(pc, cp);
        while (!res.isEmpty()){
            TreeSet<String> r = new TreeSet<>();
            for (var i : res.peek())
                r.add(mp.get(i));
            res.pop();
            for (var i : r)
                System.out.print(i + ' ');
        }
    }
}

