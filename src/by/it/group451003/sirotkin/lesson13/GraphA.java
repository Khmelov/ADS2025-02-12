    package by.it.group451003.sirotkin.lesson13;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Map;
    import java.util.PriorityQueue;
    import java.util.Scanner;

    public class GraphA {
        public GraphA() {
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            Map<String, List<String>> graph = new HashMap();
            Map<String, Integer> inDegree = new HashMap();
            String[] edges = input.split(", ");
            String[] var6 = edges;
            int var7 = edges.length;

            int i;
            for(i = 0; i < var7; ++i) {
                String edge = var6[i];
                String[] parts = edge.split(" -> ");
                String from = parts[0];
                String to = parts[1];
                graph.putIfAbsent(from, new ArrayList());
                ((List)graph.get(from)).add(to);
                inDegree.putIfAbsent(from, 0);
                inDegree.putIfAbsent(to, 0);
                inDegree.put(to, (Integer)inDegree.get(to) + 1);
            }

            PriorityQueue<String> queue = new PriorityQueue();
            Iterator var14 = inDegree.keySet().iterator();

            String current;
            while(var14.hasNext()) {
                current = (String)var14.next();
                if ((Integer)inDegree.get(current) == 0) {
                    queue.offer(current);
                }
            }

            List<String> result = new ArrayList();

            while(true) {
                do {
                    if (queue.isEmpty()) {
                        for(i = 0; i < result.size(); ++i) {
                            System.out.print((String)result.get(i));
                            if (i < result.size() - 1) {
                                System.out.print(" ");
                            }
                        }

                        return;
                    }

                    current = (String)queue.poll();
                    result.add(current);
                } while(!graph.containsKey(current));

                Iterator var17 = ((List)graph.get(current)).iterator();

                while(var17.hasNext()) {
                    String neighbor = (String)var17.next();
                    inDegree.put(neighbor, (Integer)inDegree.get(neighbor) - 1);
                    if ((Integer)inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }
    }
