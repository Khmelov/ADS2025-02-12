package by.bsuir.dsa.csv2025.gr451002.Ешманский;

import java.util.Scanner;

public class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            int n = scanner.nextInt();
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = scanner.nextInt();
            }

            Solution treap = new Solution(array);

            int q = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < q; i++) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    i--;
                    continue;
                }
                String[] parts = line.split("\\s+");
                String cmd = parts[0];

                switch (cmd) {
                    case "INSERT":
                        int insPos = Integer.parseInt(parts[1]);
                        int insVal = Integer.parseInt(parts[2]);
                        treap.insert(insPos, insVal);
                        break;
                    case "DELETE":
                        int delPos = Integer.parseInt(parts[1]);
                        treap.delete(delPos);
                        break;
                    case "UPDATE":
                        int updPos = Integer.parseInt(parts[1]);
                        int updVal = Integer.parseInt(parts[2]);
                        treap.update(updPos, updVal);
                        break;
                    case "GET":
                        int getPos = Integer.parseInt(parts[1]);
                        System.out.println(treap.get(getPos));
                        break;
                    default:
                        System.out.println("Unknown command: " + cmd);
                }
            }


            scanner.close();
        }
    }