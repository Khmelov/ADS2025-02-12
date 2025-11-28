package by.it.group410902.derzhavskaya_ludmila.lesson14;
import java.util.*;
// объединение связанных сайтов в кластеры (по ссылкам дойти до любого)
public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Создаем отображение для хранения соответствия имени сайта и его индекса
        Map<String, Integer> siteToIndex = new HashMap<>();
        // Список для хранения всех пар сайтов, которые нужно объединить
        List<int[]> pairs = new ArrayList<>();
        // Счетчик для присвоения уникальных индексов сайтам
        int indexCounter = 0;


        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }

            String[] sites = line.split("\\+");
            String site1 = sites[0];
            String site2 = sites[1];

            // Присваиваем индексы сайтам, если они еще не были добавлены
            if (!siteToIndex.containsKey(site1)) {
                siteToIndex.put(site1, indexCounter++);
            }
            if (!siteToIndex.containsKey(site2)) {
                siteToIndex.put(site2, indexCounter++);
            }

            // Сохраняем пару для последующего объединения
            int idx1 = siteToIndex.get(site1);
            int idx2 = siteToIndex.get(site2);
            pairs.add(new int[]{idx1, idx2});
        }

        int n = siteToIndex.size(); // Общее количество уникальных сайтов

        // Инициализация структуры DSU
        int[] parent = new int[n]; // родительский узел для i-го сайта
        int[] rank = new int[n];   // ранг (высота) дерева для i-го сайта
        int[] size = new int[n];   // размер компоненты, содержащей i-й сайт

        // Изначально каждый сайт - отдельная компонента
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }

        // Обрабатываем все пары сайтов для объединения компонент
        for (int[] pair : pairs) {
            int site1 = pair[0];
            int site2 = pair[1];

            // Находим корни для обоих сайтов с использованием эвристики сжатия пути
            int root1 = find(parent, site1);
            int root2 = find(parent, site2);

            // Если сайты уже находятся в одной компоненте, ничего не делаем
            if (root1 != root2) {
                // Компонента с меньшим рангом присоединяется к компоненте с большим рангом
                if (rank[root1] < rank[root2]) {
                    parent[root1] = root2;
                    size[root2] += size[root1];
                } else if (rank[root1] > rank[root2]) {
                    parent[root2] = root1;
                    size[root1] += size[root2];
                } else {
                    // Если ранги равны, выбираем произвольный корень и увеличиваем его ранг
                    parent[root2] = root1;
                    size[root1] += size[root2];
                    rank[root1]++;
                }
            }
        }

        // Собираем размеры всех компонент связности
        boolean[] visited = new boolean[n]; // Массив для отслеживания уже учтенных корней
        int[] clusterSizesArray = new int[n]; // Массив для размеров кластеров
        int clusterCount = 0;

        for (int i = 0; i < n; i++) {
            // Находим корень для каждого сайта
            int root = find(parent, i);
            if (!visited[root]) {
                // Если этот корень еще не был учтен, добавляем размер его компоненты
                clusterSizesArray[clusterCount++] = size[root];
                visited[root] = true; // Помечаем корень как учтенный
            }
        }

        // Создаем массив только с ненулевыми размерами
        int[] result = new int[clusterCount];
        for (int i = 0; i < clusterCount; i++) {
            result[i] = clusterSizesArray[i];
        }

        // сортируем по убыванию
        for (int i = 0; i < result.length - 1; i++) {
            for (int j = 0; j < result.length - i - 1; j++) {
                if (result[j] < result[j + 1]) {
                    // Меняем местами
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }

        // Выводим результат
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        scanner.close();
    }

    // Метод для нахождения корня компоненты с эвристикой сжатия пути
    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            // Рекурсивно находим корень и обновляем родителя текущего узла
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }
}