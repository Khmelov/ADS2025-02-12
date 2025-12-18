package by.it.group451002.koltsov.lesson14;

import javax.management.ObjectName;
import java.util.*;

class Step {
    Integer maxHeight = 0;
    Integer ID;
    Step parent;

    Step(List<List<Integer>> towers, Integer ID)
    {
        for (List<Integer> list : towers)
            if (list.size() > maxHeight)
                maxHeight = list.size();
        this.ID = ID;
        parent = this;
    }
}

public class DSUtaskC {
    HashMap<Integer, Step> steps;
    HashMap<Integer, Integer> contentSizes;

    DSUtaskC() {
        steps = new HashMap<>();
        contentSizes = new HashMap<>();
    }

    public void add(List<List<Integer>> towers) {
        steps.put(steps.size() + 1, new Step(towers, steps.size() + 1));
        contentSizes.put(steps.size(), 1);
    }

    public Step find(Integer ID) {
        List<Step> pathSites = new ArrayList<>();
        Step tempStep = steps.get(ID);
        while (tempStep.parent != tempStep) {
            pathSites.add(tempStep);
            tempStep = tempStep.parent;
        }
        for (Step step : pathSites)
            step.parent = tempStep;
        return tempStep;
    }

    public void union(Integer ID1, Integer ID2) {
        if (contentSizes.get(find(ID1).ID) >= contentSizes.get(find(ID2).ID)) {
            find(ID2).parent = find(ID1);
            contentSizes.put(ID1, contentSizes.get(ID1) + 1);
        }
        else {
            find(ID1).parent = find(ID2);
            contentSizes.put(ID2, contentSizes.get(ID2) + 1);
        }
    }

    public List<Integer> getUnionsSizes() {
        HashMap<Integer, Integer> sizes = new HashMap<>();
        for (Step step : steps.values()) {
            Integer key = find(step.ID).ID;
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

    public void unionAll() {
        HashMap<Integer, Step> roots = new HashMap<>();
        for (Step step : steps.values())
            if (roots.containsKey(step.maxHeight))
                union(roots.get(step.maxHeight).ID, step.ID);
            else
                roots.put(step.maxHeight, step);
    }

}