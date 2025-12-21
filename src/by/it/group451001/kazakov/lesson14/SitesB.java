package by.it.group451001.kazakov.lesson14;

import java.util.*;

// Класс Disjoint Set Union (DSU) для объединения непересекающихся множеств
// с эвристиками: сжатие пути и объединение по размеру
class DSU {
    private int[] parent; // массив родительских элементов
    private int[] size;   // массив размеров множеств для эвристики объединения по размеру

    // Конструктор инициализирует DSU для n элементов
    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;  // каждый элемент изначально является корнем своего множества
            size[i] = 1;    // каждое множество изначально имеет размер 1
        }
    }

    // Метод find с эвристикой сжатия пути
    public int find(int i) {
        if (i == parent[i]) {
            return i; // если элемент является корнем, возвращаем его
        }
        // Рекурсивно находим корень и сжимаем путь для ускорения будущих операций
        return parent[i] = find(parent[i]);
    }

    // Метод union для объединения двух множеств с эвристикой объединения по размеру
    public void union(int i, int j) {
        int rootI = find(i); // находим корень первого множества
        int rootJ = find(j); // находим корень второго множества

        if (rootI != rootJ) {
            // Объединяем меньшее множество с большим для балансировки
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ;        // меньшее множество присоединяется к большему
                size[rootJ] += size[rootI];   // обновляем размер большего множества
            } else {
                parent[rootJ] = rootI;        // меньшее множество присоединяется к большему
                size[rootI] += size[rootJ];   // обновляем размер большего множества
            }
        }
    }

    // Метод для получения размеров всех множеств (кластеров)
    public Map<Integer, Integer> getAllSetSizes() {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) { // если элемент является корнем своего множества
                result.put(i, size[i]); // добавляем корень и размер множества в карту
            }
        }
        return result;
    }
}

public class SitesB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Map для сопоставления имен сайтов с уникальными числовыми идентификаторами
        Map<String, Integer> siteToIndex = new HashMap<>();
        // Список для хранения пар связанных сайтов
        List<String[]> connections = new ArrayList<>();
        int nextIndex = 0; // счетчик для генерации уникальных идентификаторов

        // Чтение входных данных: пары связанных сайтов до строки "end"
        String line;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).equals("end")) {
            String[] sites = line.split("\\+"); // разделяем строку по символу '+'
            if (sites.length == 2) {
                connections.add(sites); // сохраняем пару связанных сайтов
                // Добавляем сайты в map с уникальными идентификаторами
                for (String site : sites) {
                    // putIfAbsent добавляет сайт только если его еще нет в map
                    var existingIndex = siteToIndex.putIfAbsent(site, nextIndex);
                    if (existingIndex == null) {
                        nextIndex++; // увеличиваем счетчик только для новых сайтов
                    }
                }
            }
        }
        scanner.close();

        // Если не было введено сайтов, выводим пустую строку
        if (siteToIndex.isEmpty()) {
            System.out.println("");
            return;
        }

        // Инициализация DSU с количеством уникальных сайтов
        DSU dsu = new DSU(siteToIndex.size());

        // Обработка всех связей: объединение сайтов в кластеры
        for (String[] connection : connections) {
            int id1 = siteToIndex.get(connection[0]); // получаем ID первого сайта
            int id2 = siteToIndex.get(connection[1]); // получаем ID второго сайта
            dsu.union(id1, id2); // объединяем сайты в один кластер
        }

        // Сбор информации о размерах полученных кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        Map<Integer, Integer> allSetSizes = dsu.getAllSetSizes();
        for (int size : allSetSizes.values()) {
            clusterSizes.add(size); // добавляем размеры всех кластеров в список
        }

        // Сортировка размеров кластеров по убыванию
        Collections.sort(clusterSizes);
        Collections.reverse(clusterSizes);

        // Вывод результатов: размеры кластеров через пробел
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println(); // завершаем вывод переводом строки
    }
}