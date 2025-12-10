package by.bsuir.dsa.csv2025.gr410902.Ярмошук;

import org.junit.Test;
import java.util.*;
import static junit.framework.TestCase.assertEquals;

public class Solution {
    //для обходов графов в глубину
    private static Stack<String> finishOrderStack; // для хранения порядка выходя из вершин
    private static Set<String> visited;// множество посещённых вершин
    private static List<String> currentSCC;//текущая компонента сильной связности

    public static StringBuilder findComponent(String graphInput) {

        StringBuilder result = new StringBuilder();

        Map<String, List<String>> adj = new HashMap<>();//список смежности исходного графа
        Map<String, List<String>> revAdj = new HashMap<>();//список смежности транспонированного графа

        Set<String> allNodes = new HashSet<>(); //множество для хранения вершин

        String[] edges = graphInput.isEmpty() ? new String[0] : graphInput.split(", "); //массив рёбер


        //заполнение списков смежности
        for (String edge : edges) {
            String[] parts = edge.split("->"); //пары вершин
            if (parts.length < 2){

                return new StringBuilder(parts[0]);
            }

            String u = parts[0].trim(); //trim для удаления пробелов
            String v = parts[1].trim();

            allNodes.add(u); //добавление вершин в множество
            allNodes.add(v);

            //добавление вершин в списки смежности
            adj.computeIfAbsent(u, k -> new ArrayList<>());
            adj.computeIfAbsent(v, k -> new ArrayList<>());
            revAdj.computeIfAbsent(u, k -> new ArrayList<>());
            revAdj.computeIfAbsent(v, k -> new ArrayList<>());
            adj.get(u).add(v);
            revAdj.get(v).add(u); //для транспонированного графа
        }

        // --- алгоритм Косарайю ---
        // первый поиск в глубину на графе, чтобы получить порядок завершения
        visited = new HashSet<>();//посещённые вершины
        finishOrderStack = new Stack<>();//для запоминания порядка выходя из вершин

        //сортируем список вершин
        List<String> sortedNodes = new ArrayList<>(allNodes);
        Collections.sort(sortedNodes);

        for (String node : sortedNodes) {
            if (!visited.contains(node)) { //если данную вершину не посещали
                dfs1(node, adj); //обходим граф в глубину начиная с данной вершины
            }
        }

        // второй обход в глубину на транспонированном графе, чтобы найти компоненты сильной связности SCC
        visited.clear(); // сбрасываем посещенные узлы
        List<List<String>> sccs = new ArrayList<>(); //список компонент сильной связности

        while (!finishOrderStack.isEmpty()) {//достаём из стека вершины начиная с последней посещённой при первом обходе
            String node = finishOrderStack.pop();
            if (!visited.contains(node)) {
                currentSCC = new ArrayList<>();
                dfs2(node, revAdj);//второй обход в глубину на транспонированном графе
                Collections.sort(currentSCC); //сортируем узлы внутри компоненты связности лексикографически
                sccs.add(currentSCC);
            }
        }
        // вывод компонент сильной связности в топологическом порядке
        for (List<String> scc : sccs) {
            for (String node : scc) {
                result.append(node);
            }
            result.append("\n");
        }
        result.setLength(result.length() - 1);//удаляем последний /n

        return result;

    }

    // рекурсивная функция для первого обхода в глубину
    private static void dfs1(String u, Map<String, List<String>> adj) {
        visited.add(u);//добавляем вершину в множество посещённых вершин

        for (String v : adj.get(u)) {//перебираем вершины смежные данной
            if (!visited.contains(v)) {
                dfs1(v, adj);//рекурсивно запускаем обход в глубину для смежной вершины
            }
        }
        finishOrderStack.push(u); //добавляем вершину в стек после завершения её обхода
    }

    // рекурсивная функция для второго обхода в глубину (транспонированного графа)
    private static void dfs2(String u, Map<String, List<String>> revAdj) {
        visited.add(u);//добавляем вершину в множество посещённых вершин
        currentSCC.add(u);//добавляем вершину в текущую компоненту связности

        for (String v : revAdj.get(u)) {//перебираем вершины смежные данной в транспонированном графе
            if (!visited.contains(v)) {
                dfs2(v, revAdj);
            }
        }
    }
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String graphInput = scanner.nextLine();
        StringBuilder result = findComponent(graphInput);
        System.out.println(result);

    }


    @Test
    public void test1() {
        assertEquals("123",findComponent("123").toString());
    }
    @Test
    public void test2() {
        assertEquals("1",findComponent("1->1").toString() );
    }
    @Test
    public void test3() {
        assertEquals("123\n45\n67",findComponent("1->3, 3->2, 2->1, 4->5, 5->4, 2->4, 2->5, 5->7, 7->6, 6->7, 3->6").toString() );
    }
    @Test
    public void test4() {
        assertEquals("123456",findComponent("1->2, 2->3, 2->4, 3->1, 4->5, 4->6, 5->2, 5->6, 6->3").toString() );
    }
    @Test
    public void test5() {
        assertEquals("8\n256\n1347",findComponent("1->3, 2->5, 3->7, 4->3, 5->6, 6->2, 7->1, 7->4, 8->2").toString() );
    }
    @Test
    public void test6() {
        assertEquals("8\n14567\n3",findComponent("5->3, 1->5, 4->7, 7->3, 5->6, 6->1, 7->1, 6->4, 8->7").toString() );
    }
    @Test
    public void test7() {
        assertEquals("8\n6\n12347\n5",findComponent("1->2, 2->3, 2->4, 1->3, 2->5, 3->7, 4->3, 7->1, 6->4, 8->7, 6->2, 8->1").toString() );
    }

}