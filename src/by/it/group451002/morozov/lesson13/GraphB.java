package by.it.group451002.morozov.lesson13;

import java.util.*;

enum Color {
	WHITE, 
	GRAY, 
	BLACK	
};
class Node {
    Character Name;
    Color color;
    List<Node> Edges;

    Node () {
        Edges = new ArrayList<Node>();
        color = Color.WHITE;
    }

    Node (Character name) {
        Edges = new ArrayList<Node>();
        color = Color.WHITE;
        Name = name;
    }

    public boolean isCycleDFS() {
        if (color == Color.GRAY)
            return false;
        else if (color == Color.BLACK)
            return true;

        boolean result = true;
        color = Color.GRAY;
        for (Node node : Edges)
            result = node.isCycleDFS();
        color = Color.BLACK;
        return result;
    }

    public void ColorDFS(List<Node> nodes) {
        if (color != Color.WHITE)
            return;
        color = Color.GRAY;
        for (Node node : Edges)
            node.ColorDFS(nodes);
        nodes.addLast(this);
        color = Color.BLACK;
    }
}

public class GraphB {
    public static int main(String[] args) {
        Map<Character, Node> map = new HashMap<>();
        List<Character> characters = new ArrayList<>();

        // Привязываем сканер к стандартному потоку ввода
        Scanner scanner = new Scanner(System.in);
        StringBuilder str = new StringBuilder(scanner.nextLine());

        while (true) {
            characters.add(str.charAt(0));
            str.delete(0, 5);
            characters.add(str.charAt(0));
            if (str.length() > 1)
                str.delete(0, 3);
            else
                break;
        }

        // Заполняем структуру с рёбрами графов
        for (int i = 0; i < characters.size(); i += 2) {
            if (!map.containsKey(characters.get(i)))
                map.put(characters.get(i), new Node());
            if (!map.containsKey(characters.get(i + 1)))
                map.put(characters.get(i + 1), new Node());

            if (!map.get(characters.get(i)).Edges.contains(map.get(characters.get(i + 1))))
                map.get(characters.get(i)).Edges.addLast(map.get(characters.get(i + 1)));
        }

        String result = "no";
        for (Node node : map.values())
            if (!node.isCycleDFS())
            {
                result = "yes";
                break;
            }
        System.out.println(result);
        return 0;
    }
}