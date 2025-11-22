package by.it.group451003.kharkevich.lesson13;

import java.util.Scanner;

public class GraphB {

    // Массивы для отслеживания состояния вершин при обходе
    static boolean[] visited; // visited[i] = была ли посещена вершина i
    static boolean[] stack;   // stack[i] = находится ли вершина i в текущем пути обхода

    // Метод для нахождения максимального номера вершины в графе
    static int getMax(int[] startVertex, int[] endVertex) {
        int size = Integer.MIN_VALUE; // Начинаем с самого маленького числа
        for (int i = 0; i < startVertex.length; i++) {
            if (startVertex[i] > size)
                size = startVertex[i]; // Обновляем максимум если нашли большую стартовую вершину
            if (endVertex[i] > size)
                size = endVertex[i];   // Обновляем максимум если нашли большую конечную вершину
        }
        return size; // Возвращаем номер самой большой вершины
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // Читаем всю строку и разбиваем по запятым на отдельные ребра
        String[] arr = scan.nextLine().split(",");

        // Массивы для хранения начальных и конечных вершин каждого ребра
        int[] startVertex = new int[arr.length];
        int[] endVertex = new int[arr.length];

        // Обрабатываем каждое ребро
        for (int i = 0; i < arr.length; i++) {
            // Убираем пробелы и разбиваем ребро на части (например: "1 -> 2" -> ["1", "->", "2"])
            String[] current = arr[i].trim().split(" ");
            startVertex[i] = Integer.parseInt(current[0]); // Первый элемент - начальная вершина
            endVertex[i] = Integer.parseInt(current[current.length - 1]); // Последний элемент - конечная вершина
        }

        // Создаем граф. Размер = максимальная вершина + 1 (т.к. вершины нумеруются с 0)
        GraphUtil graph = new GraphUtil(getMax(startVertex, endVertex) + 1);

        // Добавляем все ребра в граф
        for (int i = 0; i < arr.length; i++)
            graph.addOrientedEdge(startVertex[i], endVertex[i]);

        // Проверяем есть ли циклы и выводим результат
        System.out.println(isCyclic(graph) ? "yes" : "no");
        scan.close();
    }

    // Основной метод проверки наличия циклов в графе
    public static boolean isCyclic(GraphUtil graph) {
        // Инициализируем массивы для отслеживания состояния вершин
        visited = new boolean[graph.vertexCount]; // Все вершины сначала не посещены
        stack = new boolean[graph.vertexCount];   // Ни одна вершина не в текущем пути

        // Проверяем каждую вершину графа
        for (int i = 0; i < graph.vertexCount; i++) {
            // Если вершина еще не посещена и при ее обходе найден цикл
            if (!visited[i] && isCyclicUtil(graph, i))
                return true; // Найден цикл - возвращаем true
        }

        return false; // Циклов не найдено - возвращаем false
    }

    // Вспомогательный рекурсивный метод для поиска циклов из конкретной вершины
    static boolean isCyclicUtil(GraphUtil graph, int vertex) {
        // Если текущая вершина еще не посещена
        if (!visited[vertex]) {
            visited[vertex] = true; // Помечаем вершину как посещенную
            stack[vertex] = true;   // Добавляем вершину в текущий путь обхода

            // Проверяем всех соседей текущей вершины
            for (int neighbor : graph.getNeighbors(vertex)) {
                // Если сосед не посещен и при его обходе найден цикл
                if (!visited[neighbor] && isCyclicUtil(graph, neighbor))
                    return true; // Найден цикл
                // Если сосед уже находится в текущем пути обхода - это цикл!
                if (stack[neighbor])
                    return true; // Найден цикл
            }
        }

        // Убираем вершину из текущего пути обхода (возвращаемся назад по рекурсии)
        stack[vertex] = false;
        return false; // Цикл не найден
    }
}