package by.it.group410902.podryabinkin.lesson13;
import java.util.*;
public class GraphB {
    public static class Node{
        String value;
        ArrayList<Node> after = new ArrayList<Node>();
        int state = 0;
        public Node(String val){
            this.value = val;
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String inp = sc.nextLine().trim();
        Map<String, Node> nodes = decode(inp);
        for (Node n : nodes.values()) {
            if(n.state == 0){
                if(DFS(n)){
                    System.out.println("yes");
                    return;
                }
            }
        }
        System.out.println("no");

    }
    public static boolean DFS(Node cur){
        cur.state = 1;
        for(Node a: cur.after){
            if(a.state == 1) return true;
            if (a.state == 0 && DFS(a)) return true;
        }
        cur.state = 2;
        return false;
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
}
