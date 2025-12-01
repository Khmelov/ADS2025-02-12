package by.it.group451004.struts.lesson14;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class DisjointSet<T> implements Iterable<T> {
    private class DisJointSetNode {
        public T data;
        public int rank;
        public DisJointSetNode parent;
        public int size;

        public DisJointSetNode() {
            this.size = 1;
        }
    }

    private final Map<T, DisJointSetNode> set = new HashMap<>();
    private int count = 0;

    public int getCount() {
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return set.values().stream().map(node -> node.data).iterator();
    }

    public void makeSet(T member) {
        if (set.containsKey(member)) {
            throw new IllegalArgumentException("A set with the given member already exists.");
        }

        DisJointSetNode newSet = new DisJointSetNode();
        newSet.data = member;
        newSet.rank = 0;
        newSet.parent = newSet;

        set.put(member, newSet);
        count++;
    }

    public T findSet(T member) {
        if (!set.containsKey(member)) {
            throw new IllegalArgumentException("No such set with the given member.");
        }

        return findSet(set.get(member)).data;
    }

    DisJointSetNode findSet(DisJointSetNode node) {
        DisJointSetNode parent = node.parent;

        if (node != parent) {
            node.parent = findSet(node.parent);
            return node.parent;
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

        if (nodeA.rank == nodeB.rank) {
            nodeB.parent = nodeA;
            nodeA.rank++;
            nodeA.size += nodeB.size;
        } else {
            if (nodeA.rank < nodeB.rank) {
                nodeA.parent = nodeB;
                nodeB.size += nodeA.size;
            } else {
                nodeB.parent = nodeA;
                nodeA.size += nodeB.size;
            }
        }
    }

    public boolean contains(T member) {
        return set.containsKey(member);
    }

    public int getClusterSize(T member) {
        if (!set.containsKey(member)) {
            throw new IllegalArgumentException("No such set with the given member.");
        }

        return findSet(set.get(member)).size;
    }

}
