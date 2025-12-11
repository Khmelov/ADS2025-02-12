package by.it.group410902.podryabinkin.lesson14;


import java.util.*;

public class PointsA {
    public class PointClaster{
        double x;
        double y;
        double z;
        PointClaster root = null;
        ArrayList<PointClaster> childrens;
        public PointClaster(double x, double y, double z){
            this.x = x; this.y = y; this.z = z;
            childrens = new ArrayList<>();
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PointsA solver = new PointsA();

        double D = sc.nextDouble();
        int N = sc.nextInt();
        ArrayList<PointClaster> points = new ArrayList<>();


        for (int i = 0; i < N; i++) {
            double x = sc.nextDouble(); double y = sc.nextDouble(); double z = sc.nextDouble();
            points.add(solver.new PointClaster(x, y, z));
        }

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                PointClaster a = points.get(i);
                PointClaster b = points.get(j);
                double dx = a.x - b.x; double dy = a.y - b.y; double dz = a.z - b.z;
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < D) {
                    solver.Union(a, b);
                }
            }
        }

        Map<PointClaster, Integer> clusterSizes = new HashMap<>();
        for (PointClaster p : points) {
            PointClaster root = solver.Find(p);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // собираем размеры и сортируем
        ArrayList<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // выводим размеры кластеров
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
    public PointClaster Find(PointClaster a){
        if(a.root == null) return a;
        else  return  a.root;
    }
    public void Union(PointClaster a, PointClaster b) {
        PointClaster par_a = Find(a);
        PointClaster par_b = Find(b);
        if (par_a == par_b) return;


        if (par_a.childrens.size() < par_b.childrens.size()) {
            PointClaster temp = par_a;
            par_a = par_b;
            par_b = temp;
        }

        for (PointClaster c : par_b.childrens) {
            c.root = par_a;
            par_a.childrens.add(c);
        }
        par_b.root = par_a;
        par_a.childrens.add(par_b);
        par_b.childrens.clear();
    }
    /*public void Union( PointClaster a, PointClaster b){
        if(a.root != null){
            if(b.root != null){
                PointClaster parent;
                PointClaster prev_parent;
                if(b.root.childrens.size() > a.root.childrens.size()){
                    parent = b.root;
                    prev_parent = a.root;

                }
                else{
                    parent = a.root;
                    prev_parent = b.root;
                }
                for(int i = 0; i < prev_parent.childrens.size(); i++){
                    parent.childrens.add(prev_parent.childrens.get(i));
                    prev_parent.childrens.get(i).root = parent;
                }
                prev_parent.childrens.clear();
                prev_parent.root = parent;
                parent.childrens.add(prev_parent);
            }
            else if(b.childrens.size() != 0){
                PointClaster parent;
                PointClaster prev_parent;
                if(a.root.childrens.size() > b.childrens.size()){
                    parent = a.root;
                    prev_parent = b;
                }
                else {
                    parent = b;
                    prev_parent = a.root;
                }
                for(int i = 0; i < prev_parent.childrens.size(); i++){
                    parent.childrens.add(prev_parent.childrens.get(i));
                    prev_parent.childrens.get(i).root = parent;
                }
                prev_parent.childrens.clear();
                prev_parent.root = parent;
                parent.childrens.add(prev_parent);
            }
            else{
                b.root = a.root;
                a.root.childrens.add(b);
            }
        }
        else if(b.root != null){
            if(a.childrens.size() != 0){
                PointClaster parent;
                PointClaster prev_parent;
                if(b.root.childrens.size() > a.childrens.size()){
                    parent = b.root;
                    prev_parent = a;

                }
                else{
                    parent = a;
                    prev_parent = b.root;
                }
                for(int i = 0; i < prev_parent.childrens.size(); i++){
                    parent.childrens.add(prev_parent.childrens.get(i));
                    prev_parent.childrens.get(i).root = parent;
                }
                prev_parent.childrens.clear();
                prev_parent.root = parent;
                parent.childrens.add(prev_parent);
            }
            else{
                a.root = b.root;
                b.root.childrens.add(a);
            }
        }
        else{
            PointClaster parent;
            PointClaster prev_parent;
            if(b.childrens.size() > a.childrens.size()){
                parent = b;
                prev_parent = a;

            }
            else{
                parent = a;
                prev_parent = b;
            }
            for(int i = 0; i < prev_parent.childrens.size(); i++){
                parent.childrens.add(prev_parent.childrens.get(i));
                prev_parent.childrens.get(i).root = parent;
            }
            prev_parent.childrens.clear();
            prev_parent.root = parent;
            parent.childrens.add(prev_parent);
        }
    }*/
}
