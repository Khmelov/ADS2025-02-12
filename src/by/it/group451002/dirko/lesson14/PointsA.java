package by.it.group451002.dirko.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PointsA {
    private static class Point {
        public int x, y, z, index;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static int getFreeElem(int[] indClasters) {
        for (int i = 0; i < indClasters.length; i++)
            if (indClasters[i] == -1)
                return i;
        return -1;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String s = r.readLine();
        String[] numsStr = s.split(" ");
        int maxDistance = Integer.parseInt(numsStr[0]);
        int pointsCount = Integer.parseInt(numsStr[1]);

        Point[] points = new Point[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            s = r.readLine();
            numsStr = s.split(" ");
            points[i] = new Point(Integer.parseInt(numsStr[0]), Integer.parseInt(numsStr[1]), Integer.parseInt(numsStr[2]));
            points[i].index = i;
        }

        double[][] distants = new double[pointsCount][pointsCount];
        for (int i = 0; i < pointsCount; i++)
            for (int j = i + 1; j < pointsCount; j++) {
                distants[i][j] = Math.sqrt(Math.pow(points[i].x - points[j].x, 2) + Math.pow(points[i].y - points[j].y, 2) +
                        Math.pow(points[i].z - points[j].z, 2));
                distants[j][i] = distants[i][j];
            }

        ArrayList<ArrayList<Point>> clasters = new ArrayList<>();
        ArrayList<Point> claster = new ArrayList<>();
        int[] indClasters = new int[pointsCount]; Arrays.fill(indClasters, -1);

        int firstInd = 0;
        claster.add(points[firstInd]);
        indClasters[firstInd] = 0;
        clasters.add(claster);
        for (int i = 0; i < clasters.size(); i++) {
            for (int j = 0; j < claster.size(); j++) {
                Point tempPoint = clasters.get(i).get(j);
                if (indClasters[tempPoint.index] == -1) continue;
                for (int k = 0; k < points.length; k++) {
                    if (indClasters[k] == -1 && Math.abs(distants[tempPoint.index][k]) < maxDistance) {
                        clasters.get(i).add(points[k]);
                        indClasters[k] = i;
                    }
                }
            }
            int newIndex = getFreeElem(indClasters);
            if (newIndex == -1) break;
            claster = new ArrayList<>();
            claster.add(points[newIndex]);
            indClasters[newIndex] = clasters.size();
            clasters.add(claster);
        }

        Integer[] sizes = new Integer[clasters.size()];
        for (int i = 0; i < clasters.size(); i++)
            sizes[i] = clasters.get(i).size();
        Arrays.sort(sizes, Collections.reverseOrder());
        for (int i = 0; i < sizes.length; i++)
            System.out.print(sizes[i] + " ");
    }
}
