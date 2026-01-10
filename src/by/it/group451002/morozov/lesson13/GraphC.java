package by.it.group451002.morozov.lesson13;

import java.util.*;

public class GraphC {
    public static int main(String[] args) {
        Map<Character, Node> map = new HashMap<>();
        Map<Character, Node> reversedMap = new HashMap<>();
        List<Character> characters = new ArrayList<>();

        // Привязываем сканер к стандартному потоку ввода
        Scanner scanner = new Scanner(System.in);
        StringBuilder str = new StringBuilder(scanner.nextLine());

        while (true) {
            characters.add(str.charAt(0));
            str.delete(0, 3);
            characters.add(str.charAt(0));
            if (str.length() > 1)
                str.delete(0, 3);
            else
                break;
        }

        // Заполняем структуру с рёбрами графов
        for (int i = 0; i < characters.size(); i += 2) {
            if (!map.containsKey(characters.get(i)))
                map.put(characters.get(i), new Node(characters.get(i)));
            if (!map.containsKey(characters.get(i + 1)))
                map.put(characters.get(i + 1), new Node(characters.get(i + 1)));

            if (!reversedMap.containsKey(characters.get(i)))
                reversedMap.put(characters.get(i), new Node(characters.get(i)));
            if (!reversedMap.containsKey(characters.get(i + 1)))
                reversedMap.put(characters.get(i + 1), new Node(characters.get(i + 1)));

            if (!map.get(characters.get(i)).Edges.contains(map.get(characters.get(i + 1))))
                map.get(characters.get(i)).Edges.addLast(map.get(characters.get(i + 1)));

            if (!reversedMap.get(characters.get(i + 1)).Edges.contains(reversedMap.get(characters.get(i))))
                reversedMap.get(characters.get(i + 1)).Edges.addLast(reversedMap.get(characters.get(i)));
        }

        List<Node> topologicSort = new ArrayList<>();
        List<Node> tempList = new ArrayList<>();
        for (Node node : reversedMap.values())
        {
            tempList.clear();
            node.ColorDFS(tempList);
            for (Node n : tempList)
                topologicSort.addFirst(n);
        }

        List<List<Character>> result = new ArrayList<>();
        for (Node node : topologicSort)
        {
            tempList.clear();
            map.get(node.Name).ColorDFS(tempList);
            if (!tempList.isEmpty()) {
                result.addFirst(new ArrayList<>());
                for (Node n : tempList)
                    result.getFirst().add(n.Name);
                Collections.sort(result.getFirst());
            }
        }

        for (List<Character> list : result){
            for (Character ch : list)
                System.out.print(ch);
            System.out.println();
        }
        return 0;
    }
}