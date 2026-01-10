package by.it.group451002.spitsyna.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        HashMap<String, List<String>> graph = new HashMap<>();
        HashMap<String, Integer> indegree = new HashMap<>(); // хеш-таблица, которая хранит количество ребер, которые входят в эту вершину
        String[] parseStr = input.split(",");

        //создание графа
        for (int i = 0; i < parseStr.length; i++){
            parseStr[i] = parseStr[i].trim();
            String strSrc = parseStr[i].substring(0, parseStr[i].indexOf(" "));
            String strDest = parseStr[i].substring((parseStr[i].lastIndexOf(" ")+1));

            //проверяем, есть ли уже элемент с таким ключом в хеш-таблице графа
            if (!graph.containsKey(strSrc)) {
                graph.put(strSrc, new ArrayList<>());
                if (!indegree.containsKey(strSrc))
                    indegree.put(strSrc, 0);
            }
            if (!graph.get(strSrc).contains(strDest)){
                graph.get(strSrc).add(strDest);

                if (!indegree.containsKey(strDest))
                    indegree.put(strDest, 1);
                else
                    indegree.put(strDest, indegree.get(strDest)+1);
            }

        }

        System.out.println(topologicalSort(graph, indegree));
    }

    //Топологическая сортировка по алгоритму Кана
    public static String topologicalSort(HashMap<String, List<String>> lgraph, HashMap<String, Integer> lindegree){
        Queue<String> queue = new PriorityQueue<>();
        StringBuilder resStr = new StringBuilder();

        //считаем истоки
        for (String vert : lindegree.keySet()){
            if (lindegree.get(vert) == 0)
                queue.add(vert);
        }

        while (!queue.isEmpty()){
            String currVert = queue.poll();
            //Все смежные вершины текущей вершины
            List<String> adjVert = lgraph.get(currVert);

            if (adjVert != null) {
                //"удаляем" текущую вершину из графа
                for (int i = 0; i < adjVert.size(); i++) {
                    String currAdjVert = adjVert.get(i);
                    lindegree.put(currAdjVert, lindegree.get(currAdjVert) - 1);

                    if (lindegree.get(currAdjVert) == 0)
                        queue.add(currAdjVert);
                }
            }

            resStr.append(currVert);
            resStr.append(" ");

        }

        //resStr.delete(resStr.length()-1,resStr.length());
        return resStr.toString();
    }
}


