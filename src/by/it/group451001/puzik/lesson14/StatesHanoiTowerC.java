package by.it.group451001.puzik.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    // DSU with union by size and path compression
    static class DSU {
        int[] parent;
        int[] size;
        
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // path compression
            }
            return parent[x];
        }
        
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;
            
            // union by size
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
        
        int getSize(int x) {
            return size[find(x)];
        }
    }
    
    static class Towers {
        int[][] towers; // towers[0]=A, towers[1]=B, towers[2]=C
        
        Towers(int n) {
            towers = new int[3][];
            towers[0] = new int[n];
            towers[1] = new int[0];
            towers[2] = new int[0];
            for (int i = 0; i < n; i++) {
                towers[0][i] = n - i;
            }
        }
        
        Towers(Towers other) {
            towers = new int[3][];
            towers[0] = other.towers[0].clone();
            towers[1] = other.towers[1].clone();
            towers[2] = other.towers[2].clone();
        }
        
        int getMaxHeight() {
            return Math.max(Math.max(towers[0].length, towers[1].length), towers[2].length);
        }
        
        void moveDisk(int from, int to) {
            int disk = towers[from][towers[from].length - 1];
            
            // Remove from source
            int[] newFrom = new int[towers[from].length - 1];
            System.arraycopy(towers[from], 0, newFrom, 0, newFrom.length);
            towers[from] = newFrom;
            
            // Add to destination
            int[] newTo = new int[towers[to].length + 1];
            System.arraycopy(towers[to], 0, newTo, 0, towers[to].length);
            newTo[towers[to].length] = disk;
            towers[to] = newTo;
        }
    }
    
    static int stateCount = 0;
    static int[] maxHeights;
    
    static Towers hanoi(int n, int from, int to, int aux, Towers current) {
        if (n == 0) return current;
        
        // Move n-1 disks from 'from' to 'aux'
        current = hanoi(n - 1, from, aux, to, current);
        
        // Move disk n from 'from' to 'to'
        Towers next = new Towers(current);
        next.moveDisk(from, to);
        
        // Store state (excluding initial state) - store AFTER the move
        maxHeights[stateCount] = next.getMaxHeight();
        stateCount++;
        
        // Move n-1 disks from 'aux' to 'to' using the updated state
        return hanoi(n - 1, aux, to, from, next);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        if (n == 0) {
            System.out.println();
            return;
        }
        
        // Total number of moves is 2^n - 1
        int totalMoves = (1 << n) - 1;
        maxHeights = new int[totalMoves];
        stateCount = 0;
        
        Towers initial = new Towers(n);
        hanoi(n, 0, 1, 2, initial);
        
        // Group states by max height using DSU (optimized: O(n) instead of O(n^2))
        // For each max height value, keep track of the first state with that height
        // Then union all states with the same height to that first state
        DSU dsu = new DSU(stateCount);
        int[] firstStateForHeight = new int[n + 1]; // -1 means not seen yet
        for (int i = 0; i <= n; i++) {
            firstStateForHeight[i] = -1;
        }
        
        for (int i = 0; i < stateCount; i++) {
            int height = maxHeights[i];
            if (firstStateForHeight[height] == -1) {
                firstStateForHeight[height] = i; // First state with this height
            } else {
                dsu.union(i, firstStateForHeight[height]); // Union with first state of this height group
            }
        }
        
        // Collect cluster sizes (using arrays instead of Map)
        int[] clusterSizes = new int[stateCount];
        boolean[] processed = new boolean[stateCount];
        int uniqueCount = 0;
        
        for (int i = 0; i < stateCount; i++) {
            int root = dsu.find(i);
            if (!processed[root]) {
                clusterSizes[uniqueCount++] = dsu.getSize(root);
                processed[root] = true;
            }
        }

        // Sort sizes in ascending order (bubble sort)
        for (int i = 0; i < uniqueCount; i++) {
            for (int j = 0; j < uniqueCount - i - 1; j++) {
                if (clusterSizes[j] > clusterSizes[j + 1]) {
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }
            }
        }
        
        // Output sizes in ascending order (test expects ascending for this task)
        for (int i = 0; i < uniqueCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
        System.out.println();
    }
}

