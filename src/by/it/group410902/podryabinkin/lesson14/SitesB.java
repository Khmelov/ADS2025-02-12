package by.it.group410902.podryabinkin.lesson14;

import java.util.*;

public class SitesB {
    public class SitesClaster{
        ArrayList<SitesClaster> conn_sites;
        SitesClaster root = null;
        int size = 1;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SitesB cur_site = new SitesB();
        Map<String, SitesClaster> map = new HashMap<>();

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;

            String a = parts[0];
            String b = parts[1];
            map.putIfAbsent(a, cur_site.new SitesClaster());
            map.putIfAbsent(b, cur_site.new SitesClaster());

            cur_site.Union(map.get(a), map.get(b));
        }


        Map<SitesClaster, Integer> clusterSizes = new HashMap<>();
        for (SitesClaster s : map.values()) {
            SitesClaster root = cur_site.Find(s);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }


        ArrayList<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());
        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i != sizes.size() - 1) System.out.print(" ");
        }
    }
    public SitesClaster Find(SitesClaster a){
        if(a.root == null) return a;
        ArrayList<SitesClaster> children = new ArrayList<>();
        while(a.root != null){
            children.add(a);
            a = a.root;
        }
        for (SitesClaster b: children) {
            b.root = a;
        }
        return a;
    }
    public void Union(SitesClaster a, SitesClaster b){
        SitesClaster par_a = Find(a);
        SitesClaster par_b = Find(b);
        if (par_a == par_b) return;
        if(par_a.size < par_b.size){
            par_a.root = par_b;
            par_b.size += par_a.size;
        }
        else{
            par_b.root = par_a;
            par_a.size += par_b.size;
        }
    }
}
