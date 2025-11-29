package by.it.group410902.yarmashuk.lesson14;
import java.util.*;

// Класс Disjoint Set Union (DSU)
class DSU {
    private int[] parent;
    private int[] size;
    // Используется для эвристики "объединение по размеру"

    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;   // Каждое множество изначально имеет размер 1
        }
    }

    public int find(int i) {
        if (i == parent[i]) {
            return i;
        }
        // Сжатие пути: устанавливаем parent[i] напрямую к корню его текущего родителя
        return parent[i] = find(parent[i]);
    }

    public void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {

            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ;
                size[rootJ] += size[rootI];
            } else {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    public Map<Integer, Integer> getAllSetSizes() {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) { // Если 'i' является своим собственным родителем, это корень
                result.put(i, size[i]); // Добавляем корень и его размер в карту
            }
        }
        return result;
    }
}

public class SitesB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // siteToIndex сопоставляет строковые имена сайтов с уникальными целочисленными ID (от 0 до N-1)
        Map<String, Integer> siteToIndex = new HashMap<>();
        // connections хранит пары связанных сайтов в виде строк
        List<String[]> connections = new ArrayList<>();
        int nextIndex = 0; // Счетчик для присвоения уникальных целочисленных ID

        // 1. Считываем ввод с консоли и заполняем siteToIndex и список connections
        String line;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).equals("end")) {
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                connections.add(sites);
                // Присваиваем уникальный целочисленный ID каждому новому встреченному сайту
                for (String site : sites) {
                    var a = siteToIndex.putIfAbsent(site, nextIndex++);
                    if (a != null){
                        nextIndex--;
                    }
                }
            }

        }
        scanner.close();
        // Если сайты не были введены, выводим пустую строку и завершаем работу
        if (siteToIndex.isEmpty()) {
            System.out.println("");
            return;
        }

        // 2. Инициализируем структуру DSU общим количеством уникальных сайтов
        DSU dsu = new DSU(siteToIndex.size());


        // 3. Обрабатываем все связи и выполняем операции объединения
        for (String[] connection : connections) {
            int id1 = siteToIndex.get(connection[0]);  // Получаем целочисленный ID для первого сайта

            int id2 = siteToIndex.get(connection[1]); // Получаем целочисленный ID для второго сайта

            dsu.union(id1, id2); // Объединяем множества, содержащие эти два сайта
        }

        // 4. Вычисляем размеры всех полученных кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        Map<Integer, Integer> allSetSizes = dsu.getAllSetSizes();
        for (int size : allSetSizes.values()) {
            clusterSizes.add(size);
        }

        // 5. Сортируем размеры кластеров в порядке возрастания и выводим их
        Collections.sort(clusterSizes);
        Collections.reverse(clusterSizes);

        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
        }
        System.out.println(); // Выводим новую строку в конце вывода
    }
}
