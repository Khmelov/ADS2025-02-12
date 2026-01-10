package by.it.group451002.spitsyna.lesson14;

import java.util.*;

public class SitesB {
    private static List<int[]> pairs;
    private static Map<String, Integer> siteIndex;
    private static int siteAmount;
    private static int[] parent;
    private static int[] size;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        pairs = new ArrayList<>();
        siteIndex = new HashMap<>();

        String lineInput;
        int index = 0;

        while(!(lineInput = scanner.nextLine()).equals("end")){
            String[] parseInput = lineInput.split("\\+");

            for (String site : parseInput){
                if (!siteIndex.containsKey(site))
                    siteIndex.put(site, index++);
            }

            //заполняем список пар(сайты представлены в виде индексов)
            int[] newPair = new int[2];
            newPair[0] = siteIndex.get(parseInput[0]);
            newPair[1] = siteIndex.get(parseInput[1]);
            pairs.add(newPair);
        }

        siteAmount = siteIndex.size();
        size = new int[siteAmount];
        Arrays.fill(size, 1);
        parent = new int[siteAmount];

        //заполняем родительский массив
        for (int i = 0; i < siteAmount; i++)
            parent[i] = i;

        for (int i = 0; i < pairs.size(); i++){
            int siteInd1 = pairs.get(i)[0];
            int siteInd2 = pairs.get(i)[1];
            union(siteInd1, siteInd2);
        }

        int[] clusterSize = new int[siteAmount];

        for (int i = 0; i < siteAmount; i++){
            clusterSize[find(i)]++;
        }

        Arrays.sort(clusterSize);

        StringBuilder resStr = new StringBuilder();
        for (int i = siteAmount-1; i >= 0; i--){
            if (clusterSize[i] != 0){
                resStr.append(clusterSize[i]);
                resStr.append(" ");
            }
        }
        resStr.delete(resStr.length()-1, resStr.length());
        System.out.println(resStr);
    }

    public static int find(int elem){
        if (parent[elem] != elem)
            parent[elem] = find(parent[elem]);
        return parent[elem];
    }

    public static void union(int elem1, int elem2){
        int root1 = find(elem1);
        int root2 = find(elem2);

        if (root1 != root2){
            //меньший кластер входит в больший
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
