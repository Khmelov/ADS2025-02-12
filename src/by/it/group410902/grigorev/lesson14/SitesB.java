package by.it.group410902.grigorev.lesson14;

import java.util.*;
import java.util.stream.Collectors;
public class SitesB {
    public static void main(String[] args) {

        List<Set<String>> dsu = new ArrayList<>();

        Set<String> links = new HashSet<>();

        try (Scanner scanner = new Scanner(System.in)) {

            String line;
            while (!(line = scanner.nextLine()).equals("end")) {
                links.add(line);
                String[] sites = line.split("\\+");

                Set<String> set = new HashSet<>(Arrays.asList(sites));
                dsu.add(set);
            }
        }

        for (int i = 0; i < dsu.size(); i++) {

            for (Set<String> set : dsu) {
                boolean union = false;
                label:
                if (dsu.get(i) != set) {
                    for (String site1 : dsu.get(i)) {
                        for (String site2 : set) {
                            if (!site1.equals(site2) && checkLink(links, site1, site2)) {
                                union = true;
                                break label;
                            }
                        }
                    }
                }
                if (union) {
                    dsu.get(i).addAll(set);
                    set.clear();
                    i = 0;
                }
            }
        }

        dsu.removeIf(Set::isEmpty);

        String output = dsu.stream()
                .map(Set::size)
                .sorted((n, m) -> m - n)
                .map(String::valueOf)
                .collect(Collectors.joining(" "))
                .trim();

        System.out.println(output);
    }



    private static boolean checkLink(Set<String> links, String site1, String site2) {
        return links.contains(String.format("%s+%s", site1, site2)) ||
                links.contains(String.format("%s+%s", site2, site1));
    }
}

