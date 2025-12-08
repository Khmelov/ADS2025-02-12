package by.it.group410902.podryabinkin.lesson13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.*;


public class GraphA {

    /*
Мы считаем для каждой вершины количество входящих рёбер (inDegree).
Помещаем в приоритетную очередь все вершины без входящих рёбер.
Извлекаем их в порядке лексикографическом (PriorityQueue делает это автоматически).
После удаления вершины уменьшаем inDegree её потомков.
Если у потомка стало 0 входящих рёбер — добавляем его в очередь
     */
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            String inp = sc.nextLine().trim();

            String outs = "";
            //Сдаюсь и делаю через Map
            Map<String, Node1> nodes = new HashMap<>();
            String[] pares = inp.split(",");
            for (String conn : pares) {

                int i = 0;
                String from = "";
                String to = "";
                boolean first_part = true;
                while(i < conn.length()){
                    if(first_part){
                        if(conn.charAt(i) == '-'){
                            first_part = false;
                            from += outs;
                            outs = "";
                            i += 2;


                        }
                        else if(conn.charAt(i) != ' '){
                            outs += conn.charAt(i);
                            i++;
                        }
                        else i++;
                    }
                    else {
                        if(conn.charAt(i) == ',' || i == conn.length() - 1){
                            if(i == conn.length() - 1) outs += conn.charAt(i);
                            to += outs;
                            outs = "";
                            i++;

                        }
                        else if(conn.charAt(i) != ' '){
                            outs += conn.charAt(i);
                            i++;
                        }
                        else i++;
                    }

                }


                Node1 a = nodes.computeIfAbsent(from, k -> new Node1(k));
                Node1 b = nodes.computeIfAbsent(to, k -> new Node1(k));

                a.after.add(b);
                b.inDegree++;
            }

            // Очередь вершин без входящих рёбер — лексикографическая
            PriorityQueue<Node1> q = new PriorityQueue<>(Comparator.comparing(n -> n.value));
            for (Node1 n : nodes.values()) {
                if (n.inDegree == 0) q.add(n);
            }

            List<String> result = new ArrayList<>();
            while (!q.isEmpty()) {
                Node1 cur = q.poll();
                result.add(cur.value);
                for (Node1 next : cur.after) {
                    next.inDegree--;
                    if (next.inDegree == 0) q.add(next);
                }
            }

            // Вывод
            System.out.println(String.join(" ", result));
        }
    }





/*
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        String inp = sc.nextLine().trim();



        String outs = "";
        ArrayList<Node1> All = new ArrayList<Node1>();

        Node1 n1 = new Node1();
        Node1 n2 = new Node1();

        //Загрузка графа
        for(int i = 0; i < inp.length(); i++){
            if(inp.charAt(i) != ' ' && inp.charAt(i) != ',' && inp.charAt(i) != '-' && inp.charAt(i) != '>'){
                outs += inp.charAt(i);
            }
            if(inp.charAt(i) == '-'){
                n1 = new Node1();
                n1.value = outs;
                outs = "";
                i++;
            }
            if(inp.charAt(i) == ',' || i+1 == inp.length()){
                n2 = new Node1();
                n2.value = outs;
                outs = "";
                Node1 in1 = null, in2 = null;
                for(int j = 0; j < All.size() && (in1 == null || in2 == null); j++){
                    if (All.get(j).value.equals(n1.value)){
                        in1 = All.get(j);
                    }
                    if (All.get(j).value.equals(n2.value)){
                        in2 = All.get(j);
                    }
                }
                if(in1 == null){
                    in1 = n1;
                    All.add(n1);
                }
                if(in2 == null){
                    in2 = n2;
                    All.add(n2);
                }
                in1.after.add(in2);
                in2.prev.add(in1);
            }
        }
        ArrayList<Node1> posl = obhod(All);
        String outp = "";
        for(int i = posl.size() - 1; i >= 0 ; i--){
            outp += posl.get(i).value;
            if(i > 0) outp += " ";
        }
        System.out.println(outp);

    }
    private static ArrayList<Node1> obhod(ArrayList<Node1> arr){
        //отсортируем для лексикографичности
        arr.sort(Comparator.comparing(p -> p.value));
        ArrayList<Node1> posl = new ArrayList<Node1>();
        for( Node1 a : arr){
            a.escaped = false;
            a.after.sort(Comparator.comparing(p -> p.value));
        }

        for(int i = 0; i < arr.size(); i++){
            if(!arr.get(i).escaped){
                posl.addAll(obhod_dop(arr.get(i)));
            }
        }

        return posl;
    }
    private static ArrayList<Node1> obhod_dop(Node1 cur){
        ArrayList<Node1> posl = new ArrayList<Node1>();
        if(cur.escaped) return posl;
        cur.escaped = true;
        for(Node1 a : cur.after){
           posl.addAll(obhod_dop(a));
        }
        posl.add(cur);
        return posl;
    }
    */

