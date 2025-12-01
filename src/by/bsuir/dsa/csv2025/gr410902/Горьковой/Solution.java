package by.bsuir.dsa.csv2025.gr410902.Горьковой;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;
public class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int k = in.nextInt();
        int n = in.nextInt();

        List<int[]> arrays = new ArrayList<>();
        int totalSize = 0;

        for (int i = 0; i < k; i++) {
            int len = in.nextInt();
            int[] a = new int[len];
            for (int j = 0; j < len; j++) {
                a[j] = in.nextInt();
            }
            arrays.add(a);
            totalSize += len;
        }

        int[] result = mergeKSortedArrays(arrays, totalSize);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(result[i]);
        }
        System.out.println(sb);
    }

    static int[] mergeKSortedArrays(List<int[]> arrays, int totalSize) {
        class Node {
            int arrayIndex;
            int indexInArray;
            int value;

            Node(int arrayIndex, int indexInArray, int value) {
                this.arrayIndex = arrayIndex;
                this.indexInArray = indexInArray;
                this.value = value;
            }
        }

        PriorityQueue<Node> heap =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.value));

        for (int i = 0; i < arrays.size(); i++) {
            int[] a = arrays.get(i);
            if (a.length > 0) {
                heap.add(new Node(i, 0, a[0]));
            }
        }

        int[] res = new int[totalSize];
        int pos = 0;

        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            res[pos++] = cur.value;

            int[] arr = arrays.get(cur.arrayIndex);
            int nextIndex = cur.indexInArray + 1;
            if (nextIndex < arr.length) {
                heap.add(new Node(cur.arrayIndex, nextIndex, arr[nextIndex]));
            }
        }

        return res;
    }

    @Test
    public void test1_simpleOneArray() {
        List<int[]> arrays = new ArrayList<>();
        arrays.add(new int[]{1, 2, 3, 4, 5});

        int[] actual = mergeKSortedArrays(arrays, 5);
        int[] expected = {1, 2, 3, 4, 5};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void test2_exampleFromStatement() {
        List<int[]> arrays = new ArrayList<>();
        arrays.add(new int[]{1, 4, 9});
        arrays.add(new int[]{2, 7});
        arrays.add(new int[]{0, 5, 6, 10});

        int[] actual = mergeKSortedArrays(arrays, 9);
        int[] expected = {0, 1, 2, 4, 5, 6, 7, 9, 10};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void test3_withNegatives() {
        List<int[]> arrays = new ArrayList<>();
        arrays.add(new int[]{-5, -1, 3});
        arrays.add(new int[]{-2, 0, 4, 8});

        int[] actual = mergeKSortedArrays(arrays, 7);
        int[] expected = {-5, -2, -1, 0, 3, 4, 8};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void test4_manyDuplicates() {
        List<int[]> arrays = new ArrayList<>();
        arrays.add(new int[]{1, 2, 2, 5});
        arrays.add(new int[]{2, 2, 3});
        arrays.add(new int[]{1, 1, 4});

        int[] actual = mergeKSortedArrays(arrays, 10);
        int[] expected = {1, 1, 1, 2, 2, 2, 2, 3, 4, 5};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void test5_someEmptyArrays() {
        List<int[]> arrays = new ArrayList<>();
        arrays.add(new int[]{});
        arrays.add(new int[]{});
        arrays.add(new int[]{5, 6, 7});

        int[] actual = mergeKSortedArrays(arrays, 3);
        int[] expected = {5, 6, 7};

        assertArrayEquals(expected, actual);
    }
}