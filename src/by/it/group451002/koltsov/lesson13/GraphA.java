package by.it.group451002.koltsov.lesson13;

import java.util.*;

public class GraphA {
    public static int main(String[] args) {
        // Для начала необходимо хотя бы считать вхоодную строку и преобразовать её в какую-либо структуру, на которой было бы удобно производить сортировку.
        // Эта структура может быть, например, HashMap списков рёбер (по сути просто имя (ключ) к узлу в Map)
        // Соответственно ребро A -> B будет представлено как строка B, содержащаяся в списке в Map с ключём A
        Map<Character, List<Character>> map = new HashMap<>();
        List<Character> characters = new ArrayList<>();

        // Привязываем сканеи к стандартному потоку ввода
        Scanner scanner = new Scanner(System.in);
        StringBuilder str = new StringBuilder(scanner.nextLine());

        // массив доступных для посещеня узлов
        TreeSet<Character> possibleNodes = new TreeSet<>();
        TreeSet<Character> visitedNodes = new TreeSet<>();

        while (true) {
            Character ch = str.charAt(0);
            possibleNodes.add(ch);

            characters.add(ch);
            str.delete(0, 5);

            ch = str.charAt(0);
            possibleNodes.add(ch);

            characters.add(str.charAt(0));
            if (str.length() > 1)
                str.delete(0, 3);
            else
                break;
        }

        // Заполняем структуру с рёбрами графов
        for (int i = 0; i < characters.size(); i += 2) {
            if (!map.containsKey(characters.get(i)))
                map.put(characters.get(i), new ArrayList<>());
            if (!map.containsKey(characters.get(i + 1)))
                map.put(characters.get(i + 1), new ArrayList<>());

            possibleNodes.remove(characters.get(i + 1));
            if (!map.get(characters.get(i)).contains(characters.get(i + 1)))
                map.get(characters.get(i)).addLast(characters.get(i + 1));
        }

        StringBuilder result = new StringBuilder();

        // Пока ещё можно посетить какую-нибудь вершину
        while (!possibleNodes.isEmpty()) {
            Character ch = possibleNodes.pollFirst();
            result.append(ch);
            result.append(" ");
            visitedNodes.add(ch);
            for (Character c : map.get(ch)) {
                if (!visitedNodes.contains(c))
                    possibleNodes.add(c);
            }
        }

        System.out.println(result);
        return 0;
    }
}
