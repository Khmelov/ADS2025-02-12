package by.it.group451002.koltsov.lesson14;

import java.util.*;

class Node {
    Integer X;
    Integer Y;
    Integer Z;

    Node parent;
    Integer ID;

    Node (Integer X, Integer Y, Integer Z, Integer ID)
    {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        parent = this;
        this.ID = ID;
    }
}

public class DSUtaskA {
    HashMap<Integer, Node> elems;
    Integer distance;
    HashMap<Integer, Integer> rank;

    DSUtaskA(Integer dist) {
        distance = dist;
        elems = new HashMap<>();
        rank = new HashMap<>();
    }

    public void add(Integer X, Integer Y, Integer Z) {
        elems.put(elems.size() + 1, new Node(X, Y, Z, elems.size() + 1));
        rank.put(elems.size(), 0);

        for (Node n : elems.values())
            if (Math.sqrt(Math.pow(X - n.X, 2) + Math.pow(Y - n.Y, 2) + Math.pow(Z - n.Z, 2)) < distance) {
                union(elems.size(), n.ID);
            }
    }

    public Node find(Integer ID) {
        Node tempNode = elems.get(ID);
        while (tempNode.parent != tempNode) {
            tempNode = tempNode.parent;
        }
        return tempNode;
    }

    public void union(Integer ID1, Integer ID2) {
        // Если отцы одинаковые, то есть оба узла уже в одном сете, возвращаемся
        if (find(ID2) == find(ID1))
            return;

        if (rank.get(find(ID1).ID) >= rank.get(find(ID2).ID)) {
            // Прикрепляем более короткое поддерево к более длинному (тогда длина более длинного поддерева не изменится)
            find(ID2).parent = find(ID1);

            if (Objects.equals(rank.get(find(ID1).ID), rank.get(find(ID2).ID))) {
                rank.put(find(ID1).ID, rank.get(find(ID1).ID) + 1);
            }
        }
        else {
            find(ID1).parent = find(ID2);
        }
    }

    public List<Integer> getUnionsSizes() {
        HashMap<Integer, Integer> sizes = new HashMap<>();
        for (Node node : elems.values()) {
            Integer key = find(node.ID).ID;
            if (sizes.containsKey(key)) {
                sizes.put(key, sizes.get(key) + 1);
            }
            else
                sizes.put(key, 1);
        }
        List<Integer> result = new ArrayList<>(sizes.values());
        Collections.sort(result);
        return result;
    }
}
