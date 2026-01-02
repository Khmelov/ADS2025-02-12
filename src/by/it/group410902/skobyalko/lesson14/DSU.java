package by.it.group410902.skobyalko.lesson14;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

// Класс DSU (Disjoint Set Union) реализует структуру данных для объединения и поиска компонент
public class DSU<T> implements Iterable<T> {

    // Вложенный класс, представляющий узел множества
    private class DisJointSetNode {
        public T Data;
        public int Rank;
        public DisJointSetNode Parent;
        public int Size;
        public DisJointSetNode() {
            this.Size = 1;
        }
    }

    // Словарь для хранения элементов множества и их соответствующих узлов
    private final Map<T, DisJointSetNode> set = new HashMap<>();
    private int count = 0;  // Количество уникальных множеств
    public int getCount() {
        return count;
    }
    @Override
    // Метод для итерации по всем элементам множества
    public Iterator<T> iterator() {
        return set.values().stream().map(node -> node.Data).iterator();
    }

    // Создание множества для нового элемента
    public void makeSet(T member) {
        if (set.containsKey(member)) {
            throw new IllegalArgumentException("A set with the given member already exists.");
        }
        DisJointSetNode newSet = new DisJointSetNode();
        newSet.Data = member;
        newSet.Rank = 0;
        newSet.Parent = newSet;
        set.put(member, newSet);
        count++;
    }

    // Находим корень множества для элемента
    public T findSet(T member) {
        if (!set.containsKey(member)) {
            throw new IllegalArgumentException("No such set with the given member.");
        }
        return findSet(set.get(member)).Data;
    }

    // Рекурсивный метод для нахождения корня множества
    DisJointSetNode findSet(DisJointSetNode node) {
        DisJointSetNode parent = node.Parent;
        if (node != parent) {
            node.Parent = findSet(node.Parent);
            return node.Parent;
        }
        return parent;
    }

    // Объединение двух множеств
    public void union(T memberA, T memberB) {
        T rootA = findSet(memberA);
        T rootB = findSet(memberB);
        if (Objects.equals(rootA, rootB)) {
            return;
        }
        DisJointSetNode nodeA = set.get(rootA);
        DisJointSetNode nodeB = set.get(rootB);
        if (nodeA.Rank == nodeB.Rank) {
            nodeB.Parent = nodeA;
            nodeA.Rank++;
            nodeA.Size += nodeB.Size;
        } else {
            if (nodeA.Rank < nodeB.Rank) {
                nodeA.Parent = nodeB;
                nodeB.Size += nodeA.Size;
            } else {
                nodeB.Parent = nodeA;
                nodeA.Size += nodeB.Size;
            }
        }
    }

    // Проверка, существует ли элемент в множестве
    public boolean contains(T member) {
        return set.containsKey(member);
    }

    // Проверка, принадлежат ли два элемента одному множеству
    public boolean isConnected(T x, T y) {
        return Objects.equals(findSet(x), findSet(y));
    }

    // Получение размера множества, в котором находится элемент
    public int getClusterSize(T member) {
        if (!set.containsKey(member)) {
            throw new IllegalArgumentException("No such set with the given member.");
        }
        return findSet(set.get(member)).Size;
    }
}
