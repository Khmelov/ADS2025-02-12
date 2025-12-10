package by.it.group410901.kovalevich.lesson14;

import java.util.Scanner;
import java.util.Arrays;

public class StatesHanoiTowerC {
    private static Tower towerA, towerB, towerC;
    private static int[] maxHeights;
    private static int stepCounter;

    //класс, представляющий башню
    static class Tower {
        private int[] disks;
        private int top; //индекс верхнего элемента

        public Tower(int capacity) {
            disks = new int[capacity];
            top = -1;
        }

        //добавить диск на вершину башни
        public void push(int disk) {
            disks[++top] = disk;
        }

        //снять диск с вершины башни
        public int pop() {
            return disks[top--];
        }

        //получить текущую высоту башни
        public int size() {
            return top + 1;
        }
    }

    static class DSU {
        private int[] parent;
        private int[] sz; //sz[i] = размер поддерева с корнем i

        public DSU(int n) {
            parent = new int[n];
            sz = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; //каждый элемент - свой собственный родитель
                sz[i] = 1;//изначальный размер каждого множества - 1
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            //напрямую связываем элемент с корнем
            return parent[i] = find(parent[i]);
        }

        //объединяет два множества, в которых находятся i и j
        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                //присоединяем меньшее дерево к большему
                if (sz[rootI] < sz[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                sz[rootI] += sz[rootJ];
            }
        }
    }

    public static void solveHanoi(int n, Tower source, Tower destination, Tower auxiliary) {
        //n - кол-во дисков для перемещения
        //source - исходный стержень
        //destination - целевой стержень
        //auxiliary - вспомогательный стержень

        if (n == 0) {
            return;
        }

        //переместить n-1 дисков с исходного стержня на вспомогательный
        solveHanoi(n - 1, source, auxiliary, destination);

        //переместить самый большой диск (n) с исходного на целевой
        destination.push(source.pop());

        int hA = towerA.size();
        int hB = towerB.size();
        int hC = towerC.size();
        maxHeights[stepCounter] = Math.max(hA, Math.max(hB, hC));
        stepCounter++;

        //переместить n-1 дисков со вспомогательного стержня на целевой
        solveHanoi(n - 1, auxiliary, destination, source);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();

        //общее количество ходов в задаче (2^N) - 1
        int totalMoves = (1 << n) - 1;

        towerA = new Tower(n);
        towerB = new Tower(n);
        towerC = new Tower(n);

        //все диски на стержне A
        for (int i = n; i > 0; i--) {
            towerA.push(i);
        }

        maxHeights = new int[totalMoves];
        stepCounter = 0;

        //заполнение массива maxHeights
        solveHanoi(n, towerA, towerB, towerC);

        DSU dsu = new DSU(totalMoves);

        int[] representatives = new int[n + 1];
        Arrays.fill(representatives, -1);

        //группировка шагов с одинаковой максимальной высотой
        for (int i = 0; i < totalMoves; i++) {
            int height = maxHeights[i];
            if (representatives[height] == -1) {
                representatives[height] = i;
            } else {
                dsu.union(representatives[height], i);
            }
        }

        //сбор размеров уникальных поддеревьев
        int[] tempSizes = new int[totalMoves];
        int uniqueSetsCount = 0;
        boolean[] visitedRoots = new boolean[totalMoves];

        for (int i = 0; i < totalMoves; i++) {
            int root = dsu.find(i);
            if (!visitedRoots[root]) {
                tempSizes[uniqueSetsCount++] = dsu.sz[root];
                visitedRoots[root] = true;
            }
        }

        int[] finalSizes = new int[uniqueSetsCount];
        for (int i = 0; i < uniqueSetsCount; i++) {
            finalSizes[i] = tempSizes[i];
        }

        Arrays.sort(finalSizes);

        for (int i = 0; i < finalSizes.length; i++) {
            System.out.print(finalSizes[i] + (i == finalSizes.length - 1 ? "" : " "));
        }
        System.out.println();
    }
}
