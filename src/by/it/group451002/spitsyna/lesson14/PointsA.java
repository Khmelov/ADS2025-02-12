package by.it.group451002.spitsyna.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class PointsA {
    private static int[][] pointsArr;
    private static int[] parent;
    private static int pointsAmount;
    private static int maxDist;
    private static int[] size;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] parseInput = input.split(" ");
        maxDist = Integer.parseInt(parseInput[0]);
        pointsAmount = Integer.parseInt(parseInput[1]);

        pointsArr = new int[pointsAmount][3];
        parent = new int[pointsAmount];
        size = new int[pointsAmount];
        Arrays.fill(size, 1);

        for (int i = 0; i < pointsAmount; i++){
            String currCoord = scanner.nextLine();
            parseInput = currCoord.split(" ");
            parent[i] = i;
            pointsArr[i][0] = Integer.parseInt(parseInput[0]);
            pointsArr[i][1] = Integer.parseInt(parseInput[1]);
            pointsArr[i][2] = Integer.parseInt(parseInput[2]);
        }

        //перебираем все точки
        for (int i = 0; i < pointsAmount; i++){
            for (int j = i+1; j < pointsAmount; j++){
                // Евклидово расстояние (сравниваем квадраты, чтобы избежать sqrt)
                int dx = pointsArr[j][0] - pointsArr[i][0];
                int dy = pointsArr[j][1] - pointsArr[i][1];
                int dz = pointsArr[j][2] - pointsArr[i][2];
                int dist = dx*dx + dy*dy + dz*dz;

                if (dist < maxDist * maxDist)
                    union(i, j);
            }
        }

        int[] clusterSizes = new int[pointsAmount];

        for (int i = 0; i < pointsAmount; i++)
            clusterSizes[find(i)]++;

        Arrays.sort(clusterSizes);
        StringBuilder resStr = new StringBuilder();
        for (int i = pointsAmount-1; i >= 0; i--){
            if (clusterSizes[i] != 0) {
                resStr.append(clusterSizes[i]);
                resStr.append(" ");
            }
        }
        resStr.delete(resStr.length()-1, resStr.length());
        System.out.println(resStr);
    }

    public static int find(int elem){
        if (parent[elem] != elem){
            parent[elem] = find(parent[elem]); //сжатие пути, теперь каждый элемент будем прямо указывать на корень
        }
        return parent[elem];
    }

    public static void union(int elem1, int elem2){
        int root1 = find(elem1);
        int root2 = find(elem2);

        if (root1 != root2){
            //меньший по размеру кластер попадает в больший по размеру кластер
            if (size[root1] < size[root2]){
                parent[root1] = root2;
                size[root2] += size[root1];
            }
            else {
                parent[root2] = root1;
                size[root1] += size[root2];
            }
        }
    }
}
