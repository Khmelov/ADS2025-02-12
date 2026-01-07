package by.it.group451002.koltsov.lesson14;

import java.util.*;

class Site {
    String name;
    Integer ID;
    Site parent;

    Site(String name, Integer ID) {
        this.name = name;
        parent = this;
    }
}

public class DSUtaskB {
    HashMap<String, Site> sites;
    HashMap<String, Integer> contentSizes;

    DSUtaskB() {
        sites = new HashMap<>();
        contentSizes = new HashMap<>();
    }

    public void add(String name) {
        if (!sites.containsKey(name))
            sites.put(name, new Site(name, sites.size() + 1));
        contentSizes.put(name, 1);
    }

    public Site find(String name) {
        List<Site> pathSites = new ArrayList<>();
        Site tempSite = sites.get(name);
        while (tempSite.parent != tempSite) {
            pathSites.add(tempSite);
            tempSite = tempSite.parent;
        }
        for (Site site : pathSites)
            site.parent = tempSite;
        return tempSite;
    }

    public void union(String name1, String name2) {
        if (contentSizes.get(find(name1).name) >= contentSizes.get(find(name2).name)) {
            find(name2).parent = find(name1);
            contentSizes.put(name1, contentSizes.get(name1) + 1);
        }
        else {
            find(name1).parent = find(name2);
            contentSizes.put(name2, contentSizes.get(name2) + 1);
        }
    }

    public List<Integer> getUnionsSizes() {
        HashMap<String, Integer> sizes = new HashMap<>();
        for (Site site : sites.values()) {
            String key = find(site.name).name;
            if (sizes.containsKey(key)) {
                sizes.put(key, sizes.get(key) + 1);
            }
            else
                sizes.put(key, 1);
        }
        List<Integer> result = new ArrayList<>(sizes.values());
        Collections.sort(result);
        return result;
    }
}
