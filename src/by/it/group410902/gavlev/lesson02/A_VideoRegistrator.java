package by.it.group410902.gavlev.lesson02;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class A_VideoRegistrator {

        public static void main(String[] args) {
            A_VideoRegistrator instance = new A_VideoRegistrator();
            double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
            List<Double> starts = instance.calcStartTimes(events, 1);
            System.out.println(starts);
        }

        List<Double> calcStartTimes(double[] events, double workDuration) {
            List<Double> result = new ArrayList<>();
            Arrays.sort(events);
            int i = 0;
            while (i < events.length) {
                double start = events[i];
                result.add(start);
                double end = start + workDuration;
                while (i < events.length && events[i] <= end) {
                    i++;
                }
            }
            return result;
        }
    }