package by.bsuir.dsa.csv2025.gr410902.Плехова;

import java.util.*;
import java.io.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.*;

//  Класс с решением
class Solution {

    static class Event implements Comparable<Event> {
        int x;
        int type; // 1 = начало, -1 = конец

        Event(int x, int type) {
            this.x = x;
            this.type = type;
        }

        @Override
        public int compareTo(Event o) {
            if (this.x != o.x) return Integer.compare(this.x, o.x);
            return Integer.compare(o.type, this.type); // начало перед концом
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            System.out.println(0);
            return;
        }
        int n = sc.nextInt();

        List<Event> events = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int l = sc.nextInt();
            int r = sc.nextInt();
            events.add(new Event(l, 1));
            events.add(new Event(r, -1));
        }

        Collections.sort(events);

        long result = 0;
        int active = 0;
        int prevX = 0;

        for (Event e : events) {
            if (active > 0) {
                result += e.x - prevX;
            }
            active += e.type;
            prevX = e.x;
        }

        System.out.println(result);
    }
}

