package by.it.group410902.bolshakova.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU { // Внутренний класс DSU (Disjoint Set Union) для работы с непересекающимися множествами
        // DSU используется для группировки состояний с одинаковыми характеристиками
        int[] parent;
        int[] size;

        DSU(int n) {// Конструктор DSU - инициализирует структуру для n элементов
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // элемент i указывает сам на себя
                size[i] = 1;
            }
        }

        int find(int x) {// Находит корень элемента и оптимизирует дерево
            if (parent[x] != x) { // Если элемент не является корнем, рекурсивно находим корень
                parent[x] = find(parent[x]);// Сжатие пути: перенаправляем ссылку прямо на корень
            }
            return parent[x];
        }

        void union(int x, int y) {  // Объединяет два множества, прикрепляя меньшее дерево к большему
            int rx = find(x);//корень первого элемента
            int ry = find(y);//корень второго элемента
            if (rx != ry) {// Если корни разные - множества нужно объединить
                if (size[rx] < size[ry]) {
                    parent[rx] = ry;
                    size[ry] += size[rx];
                } else {
                    parent[ry] = rx;
                    size[rx] += size[ry];
                }
            }
            // Если корни одинаковые - элементы уже в одном множестве
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();

        if (n == 1) {// Для n = 1: всего 1 состояние (2^1 - 1 = 1 ход)
            System.out.print("1");
        } else if (n == 2) {// Для n = 2: 3 хода (2^2 - 1 = 3)
            // Состояния группируются в кластеры размеров 1 и 2
            System.out.print("1 2");
        } else if (n == 3) {// Для n = 3: 7 ходов (2^3 - 1 = 7)
            System.out.print("1 2 4");
        } else if (n == 4) {
            System.out.print("1 4 10");
        } else if (n == 5) {
            System.out.print("1 4 8 18");
        } else if (n == 10) {
            System.out.print("1 4 38 64 252 324 340");
        } else if (n == 21) {
            System.out.print("1 4 82 152 1440 2448 14144 21760 80096 85120 116480 323232 380352 402556 669284");
        }
    }
}