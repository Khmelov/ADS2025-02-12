package by.it.group451002.dirko.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SitesB {
    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        ArrayList<HashSet<String>> sitesSets = new ArrayList<>();
        while (true) {
            String s = r.readLine();
            if (Objects.equals(s, "end")) break;
            String[] strSites = s.split("\\+");
            sitesSets.add(new HashSet<String>());
            sitesSets.getLast().add(strSites[0]);
            sitesSets.getLast().add(strSites[1]);
        }

        for (int i = 0; i < sitesSets.size(); i++) {
            if (sitesSets.get(i).isEmpty()) continue;
            for (int j = 0; j < sitesSets.size(); j++) {
                if (i == j) break;
                for (String sites : sitesSets.get(j))
                    if (sitesSets.get(i).contains(sites)) {
                        sitesSets.get(i).addAll(sitesSets.get(j));
                        sitesSets.get(j).clear();
                        break;
                    }
            }
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (HashSet<String> siteSet: sitesSets) {
            if (!siteSet.isEmpty())
                result.add(siteSet.size());
        }
        result.sort(Collections.reverseOrder());

        for (Integer size: result) {
            System.out.printf(size + " ");
        }

    }
}
