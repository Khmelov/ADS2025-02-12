package by.it.group451003.galuzo.lesson14;

import java.util.*;

public class StatesHanoiTowerC {
    public static class global{
        public static int[] rods;
        public static int[] heightsArr;
        public static int indexHA;
    }

    static class DSU {
        int[] parent;
        int[] size;

        public void makeSet (int value) {
            parent[value] = value;
            size[value] = 1;
        }

        public int findSet (int value) {
            if (value == parent[value])
                return value;
            return parent[value] = findSet(parent[value]);
        }

        public void unionSets(int e1, int e2) {
            e1 = findSet(e1);
            e2 = findSet(e2);
            if (e1 != e2) {
                if (size[e1] < size[e2]) {
                    int temp = e1;
                    e1 = e2;
                    e2 = temp;
                }
                parent[e2] = e1;
                size[e1] += size[e2];
            }
        }

        public int getSize(int value) {
            return size[value];
        }

        DSU(int arrSize) {
            this.parent = new int[arrSize];
            this.size = new int[arrSize];
        }
    }

    static void towerOfHanoi(int n, char fromRod, char toRod, char extRod) {
        if (n == 0) return;
        towerOfHanoi(n - 1, fromRod, extRod, toRod);
        global.rods[fromRod - 'A'] -= 1;
        global.rods[toRod - 'A'] += 1;
        global.heightsArr[global.indexHA++] = Math.max(Math.max(global.rods[0], global.rods[1]), global.rods[2]);
        towerOfHanoi(n - 1, extRod, toRod, fromRod);
    }

    static int findHeight(int[] someArray, int value) {
        for (int i = 0; i < someArray.length; i++) {
            if (someArray[i] == value)
                return i;
        }
        return -1;
    }

    static void groupInSubtrees(DSU someDSU) {
        int height, index;
        for (int i = 0; i < global.heightsArr.length; i++) {
            height = global.heightsArr[i];
            index = findHeight(global.heightsArr, height);
            someDSU.unionSets(i, index);
        }
    }

    static void printSetSizes (int diskCount, DSU someDSU) {
        int[] dsuParents = new int[diskCount+1];
        int[] dsuTreeSizes = new int[diskCount+1];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i <= diskCount; i++)
            dsuParents[i] = findHeight(global.heightsArr, i);

        int k = 0;
        for (int parent : dsuParents)
            if (parent != -1)
                dsuTreeSizes[k++] = someDSU.getSize(someDSU.findSet(parent));

        Arrays.sort(dsuTreeSizes);
        for (int size : dsuTreeSizes)
            if (size != 0)
                sb.append(size).append(' ');

        System.out.println(sb);
    }

    public static void main(String[] args) {
        int diskCount = new Scanner(System.in).nextInt();
        int size = (int) Math.pow(2, diskCount) - 1;

        global.rods = new int[3];
        global.rods[0] = diskCount;
        global.indexHA = 0;
        global.heightsArr = new int[size];

        towerOfHanoi(diskCount, 'A', 'B', 'C');

        DSU myDSU = new DSU(size);
        for (int i = 0; i < size; i++)
            myDSU.makeSet(i);

        groupInSubtrees(myDSU);
        printSetSizes(diskCount, myDSU);
    }
}