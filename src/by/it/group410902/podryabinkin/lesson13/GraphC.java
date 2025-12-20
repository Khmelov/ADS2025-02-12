package by.it.group410902.podryabinkin.lesson13;

import java.util.*;

public class GraphC {

    public static class Node{
        String value;
        ArrayList<Node> after = new ArrayList<Node>();
        ArrayList<Node> newafter = new ArrayList<Node>();
        int state = 0;
        public Node(String val){
            this.value = val;
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String inp = sc.nextLine().trim();

        Map<String, Node> nodes = decode(inp);

        ArrayList<Node> all = new ArrayList<>(nodes.values());
        ArrayList<Node> stack = new ArrayList<>();


        for(Node n : all) if (n.state == 0) DFS(n, stack);



        reverse(all);


        for(Node n : all) n.state = 0;


        ArrayList<ArrayList<Node>> cc = new ArrayList<>();

        for (int i = stack.size() - 1; i >= 0; i--) {
            Node v = stack.get(i);
            if (v.state == 0) {
                ArrayList<Node> comp = new ArrayList<>();
                DFS2(v, comp);
                cc.add(comp);
            }
        }


        for(ArrayList<Node> comp : cc){
            comp.sort(Comparator.comparing(p -> p.value));
            for(Node n : comp) System.out.print(n.value);
            System.out.println();
        }
    }

    public static ArrayList<Node> DFS(Node cur, ArrayList<Node> stack){
        cur.state = 1;
        for(Node a: cur.after){
            if (a.state == 0 ) DFS(a, stack);
        }
        cur.state = 2;
        stack.add(cur);
        return stack;
    }
    public static Map<String, Node> decode(String inp){
        String s = "";
        boolean first = true;
        String a = "";
        String b = "";
        Map<String, Node> nodes = new HashMap<>();
        for(int i = 0; i < inp.length(); i++){
            if(inp.charAt(i) == ' ' || inp.charAt(i) == ',') continue;
            if(inp.charAt(i) == '-' || inp.charAt(i) == '>') continue;
            if(first){
                a = inp.charAt(i) + "";
                first = false;
            }
            else{
                b = inp.charAt(i) + "";
                first = true;
                Node a1 = nodes.computeIfAbsent(a, k -> new Node(k));
                Node b1 = nodes.computeIfAbsent(b, k -> new Node(k));
                a1.after.add(b1);
                a = "";
                b = "";
            }
        }
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue().value);
        }

        return nodes;
    }

    public static void reverse(ArrayList<Node> nodes){
        for(Node a : nodes){
            a.state = 0;
        }
        for(Node a : nodes){
            a.state = 1;
            for(Node b: a.after){
                b.newafter.add(a);
            }
        }
    }
    public static ArrayList<Node> DFS2(Node cur, ArrayList<Node> stack){
        cur.state = 1;
        for(Node a: cur.newafter){
            if (a.state == 0 ) DFS2(a, stack);
        }
        cur.state = 2;
        stack.add(cur);
        return stack;
    }
}
