package by.it.group451003.rabbimov.lesson14;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class DisJointSet<T> implements Iterable<T> {

    private class DisJointSetNode {
        public T Data;
        public int Rank;
        public DisJointSetNode Parent;
        public int Size;
        public DisJointSetNode() {
            this.Size = 1;
        }
    }

    private final Map<T, DisJointSetNode> set = new HashMap<>();
    private int count = 0;

    public int getCount() {
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return set.values().stream().map(node -> node.Data).iterator();
    }

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

    public T findSet(T member) {
        if (!set.containsKey(member)) {
            throw new IllegalArgumentException("No such set with the given member.");
        }

        return findSet(set.get(member)).Data;
    }

    DisJointSetNode findSet(DisJointSetNode node) {
        DisJointSetNode parent = node.Parent;

        if (node != parent) {
            node.Parent = findSet(node.Parent);
            return node.Parent;
        }

        return parent;
    }

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

    public boolean contains(T member) {
        return set.containsKey(member);
    }

    public boolean isConnected(T x, T y) {
        return Objects.equals(findSet(x), findSet(y));
    }

    public int getClusterSize(T member) {
        if (!set.containsKey(member)) {
            throw new IllegalArgumentException("No such set with the given member.");
        }

        return findSet(set.get(member)).Size;
    }

}