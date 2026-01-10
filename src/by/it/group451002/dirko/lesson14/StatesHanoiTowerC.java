package by.it.group451002.dirko.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class StatesHanoiTowerC {

    public static void updateStates(ArrayList<Stack<Integer>> towers, int[] states) {
        int ind;
        if (towers.get(0).size() > towers.get(1).size()) {
            if (towers.get(0).size() > towers.get(2).size())
                ind = 0;
            else ind = 2;
        }
        else {
            if (towers.get(1).size() > towers.get(2).size())
                ind = 1;
            else ind = 2;
        }
        states[towers.get(ind).size() - 1]++;
    }

    public static void solveHanoi(ArrayList<Stack<Integer>> towers, int[] states, int N) {
        if (N % 2 == 0) {
            while (true) {
                swap(towers, 0, 1, states);
                if (towers.get(1).size() == N || towers.get(2).size() == N) break;

                swap(towers, 0, 2, states);
                if (towers.get(1).size() == N || towers.get(2).size() == N) break;

                swap(towers, 1, 2, states);
                if (towers.get(1).size() == N || towers.get(2).size() == N) break;
            }
        }
        else {
            while (true) {
                swap(towers, 0, 2, states);
                if (towers.get(1).size() == N || towers.get(2).size() == N) break;

                swap(towers, 0, 1, states);
                if (towers.get(1).size() == N || towers.get(2).size() == N) break;

                swap(towers, 1, 2, states);
                if (towers.get(1).size() == N || towers.get(2).size() == N) break;
            }
        }
    }

    public static void swap(ArrayList<Stack<Integer>> towers, int indFirst, int indSecond, int[] states) {
        if (towers.get(indFirst).empty()) {
            if (towers.get(indSecond).empty()) return;
            towers.get(indFirst).push(towers.get(indSecond).pop());
        }
        else if (towers.get(indSecond).empty() || towers.get(indFirst).peek() < towers.get(indSecond).peek())
            towers.get(indSecond).push(towers.get(indFirst).pop());
        else towers.get(indFirst).push(towers.get(indSecond).pop());
        updateStates(towers, states);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String s = r.readLine();
        final int N = Integer.parseInt(s);

        int[] states = new int[N];
        ArrayList<Stack<Integer>> towers = new ArrayList<>();
        towers.add(new Stack<>());
        towers.add(new Stack<>());
        towers.add(new Stack<>());
        for (int i = N; i > 0; i--)
            towers.getFirst().push(i);

        solveHanoi(towers, states, N);

        Arrays.sort(states);
        for (int i = 0; i < N; i++)
            System.out.print(states[i] + " ");
    }
}
