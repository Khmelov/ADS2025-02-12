package by.it.group410902.skobyalko.lesson02;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
/// //
public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      // сколько предметов
        int W = input.nextInt();      // вместимость рюкзака
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // сортировка по убыванию удельной стоимости
        Arrays.sort(items);

        double result = 0;
        int weightLeft = W;

        for (Item item : items) {
            if (weightLeft == 0)
                break;

            if (item.weight <= weightLeft) {
                result += item.cost;
                weightLeft -= item.weight;
            } else {
                double fraction = (double) weightLeft / item.weight;
                result += item.cost * fraction;
                weightLeft = 0;
            }
        }

        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        double getCostPerWeight() {
            return (double) cost / weight;
        }

        @Override
        public int compareTo(Item o) {
            return Double.compare(o.getCostPerWeight(), this.getCostPerWeight()); // по убыванию
        }

        @Override
        public String toString() {
            return "Item{cost=" + cost + ", weight=" + weight + ", value=" + getCostPerWeight() + '}';
        }
    }
}
