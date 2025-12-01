package by.bsuir.dsa.csv2025.gr410902.Шахов;

import org.junit.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class Solution {

    static class Task {
        String name;
        int priority;

        public Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return name + " " + priority;
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
    // ---------------- Динамический массив ----------------
    static class DynamicArray implements Iterable<Task> {
        private Task[] arr = new Task[4];
        private int size = 0;

        public void add(Task t) {
            if (size == arr.length) {
                Task[] newArr = new Task[arr.length * 2];
                System.arraycopy(arr, 0, newArr, 0, arr.length);
                arr = newArr;
            }
            arr[size++] = t;
        }

        public Task get(int index) {
            return arr[index];
        }

        public int size() { return size; }

        @Override
        public Iterator<Task> iterator() {
            return new Iterator<Task>() {
                int idx = 0;
                public boolean hasNext() { return idx < size; }
                public Task next() { return arr[idx++]; }
            };
        }
    }

    // ---------------- Max-Heap -------------------
    static class MaxHeap {
        private Task[] heap = new Task[4];
        private int size = 0;

        private void ensure() {
            if (size == heap.length) {
                Task[] n = new Task[heap.length * 2];
                System.arraycopy(heap, 0, n, 0, heap.length);
                heap = n;
            }
        }

        public void add(Task t) {
            ensure();
            heap[size] = t;
            siftUp(size);
            size++;
        }

        private void siftUp(int i) {
            while (i > 0) {
                int p = (i - 1) / 2;
                if (heap[i].priority > heap[p].priority) {
                    Task tmp = heap[i];
                    heap[i] = heap[p];
                    heap[p] = tmp;
                    i = p;
                } else break;
            }
        }

        public Task extractMax() {
            if (size == 0) return null;
            Task top = heap[0];
            heap[0] = heap[--size];
            siftDown(0);
            return top;
        }

        private void siftDown(int i) {
            while (true) {
                int l = 2*i + 1;
                int r = 2*i + 2;
                int max = i;

                if (l < size && heap[l].priority > heap[max].priority) max = l;
                if (r < size && heap[r].priority > heap[max].priority) max = r;

                if (max != i) {
                    Task t = heap[i];
                    heap[i] = heap[max];
                    heap[max] = t;
                    i = max;
                } else break;
            }
        }
    }

    private DynamicArray tasks = new DynamicArray();
    private MaxHeap heap = new MaxHeap();

    public void addTask(String name, int pr) {
        Task t = new Task(name, pr);
        tasks.add(t);
        heap.add(t);
    }

    // -----------------------------------------------------

    public Map.Entry<String, Integer> getNextTask() {
        Task t = heap.extractMax();
        if (t == null) return null;
        return new AbstractMap.SimpleEntry<>(t.name, t.priority);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!
    // -----------------------------------------------------

    public Iterable<Task> allTasks() {
        return tasks;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Solution tm = new Solution();

        // Читаем количество задач
        int n = scanner.nextInt();
        scanner.nextLine(); // переходим на следующую строку

        // Читаем и добавляем задачи
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            String name = parts[0];
            int priority = Integer.parseInt(parts[1]);
            tm.addTask(name, priority);
        }

        // Извлекаем и выводим задачи в порядке приоритета
        Map.Entry<String, Integer> task;
        while ((task = tm.getNextTask()) != null) {
            System.out.println(task.getKey() + " " + task.getValue());
        }

        scanner.close();
    }

    @Test
    public void checkA() throws Exception {

        Solution tm = new Solution();

        tm.addTask("Сделать_отчёт", 2);
        tm.addTask("Исправить_баг", 5);
        tm.addTask("Ответить_клиенту", 3);
        tm.addTask("Созвон_с_командой", 4);

        String[] expected = {
                "Исправить_баг 5",
                "Созвон_с_командой 4",
                "Ответить_клиенту 3",
                "Сделать_отчёт 2"
        };

        boolean ok = true;
        for (String exp : expected) {
            Map.Entry<String,Integer> p = tm.getNextTask();
            ok &= (p.getKey() + " " + p.getValue()).equals(exp);
        }

        assertTrue("Test A failed", ok);
    }
}
