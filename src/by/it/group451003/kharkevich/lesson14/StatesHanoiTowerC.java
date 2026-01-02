package by.it.group451003.kharkevich.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static class DSU {
        int[] parent;  // Массив для хранения родительских элементов
        int[] size;    // Массив для хранения размеров множеств

        DSU(int size) {
            parent = new int[size];  // Инициализация массива родителей
            this.size = new int[size];  // Инициализация массива размеров
        }

        void makeSet(int v) {
            parent[v] = v;
            size[v] = 1;
        }

        int size(int v) {  // Получение размера множества, содержащего v
            return size[findSet(v)];  // Возвращаем размер корневого элемента
        }

        int findSet(int v) {  // Поиск корневого элемента с сжатием пути
            if (v == parent[v])  // Если v - корень
                return v;  // Возвращаем v
            return parent[v] = findSet(parent[v]);  // Рекурсивный поиск со сжатием пути
        }

        void unionSets(int a, int b) {
            a = findSet(a);
            b = findSet(b);
            if (a != b) {  // Если множества разные
                if (size[a] < size[b]) {  // Если первое множество меньше
                    int temp = a;  // Меняем местами a и b
                    a = b;
                    b = temp;
                }
                parent[b] = a;  // Подвешиваем меньшее множество к большему
                size[a] += size[b];  // Обновляем размер объединенного множества
            }
        }
    }

    static int[] carryingOver(int N, int step, int k) {
        int axisX = 0, axisY, axisZ;  // Определение осей (X всегда 0 - начальный стержень)
        axisY = (N % 2 == 0) ? 1 : 2;  // Для четных N ось Y = 1, для нечетных = 2
        axisZ = (N % 2 == 0) ? 2 : 1;  // Для четных N ось Z = 2, для нечетных = 1

        int[] result = new int[3];  // Массив для хранения изменений высот
        int t = (step / (1 << (k - 1)) - 1) / 2;  // Вычисление номера перемещения

        int[] mapping = (k % 2 != 0) ? new int[]{axisX, axisY, axisZ} : new int[]{axisX, axisZ, axisY};
        int from = mapping[t % 3];  // Определение исходного стержня
        int to = mapping[(t + 1) % 3];  // Определение целевого стержня

        result[from] = -1;  // Уменьшение высоты на исходном стержне
        result[to] = 1;     // Увеличение высоты на целевом стержне
        return result;      // Возврат массива изменений
    }

    static int countBits(int num) {  // Подсчет младших нулевых битов
        int count = 0;  // Счетчик нулевых битов
        while (num % 2 == 0) {  // Пока младший бит равен 0
            count++;     // Увеличиваем счетчик
            num /= 2;    // Сдвигаем число вправо (делим на 2)
        }
        return count;    // Возвращаем количество нулевых битов
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();  // Чтение количества дисков
        int max_size = (1 << N) - 1;  // Вычисление общего количества шагов (2^N - 1)

        int[] steps_heights = new int[N];  // Массив для первых шагов достижения высот
        for (int i = 0; i < N; i++)
            steps_heights[i] = -1;  // Инициализация массива значением -1

        DSU dsu = new DSU(max_size);  // Создание DSU для всех шагов

        int[] heights = new int[3];  // Массив текущих высот на трех стержнях
        heights[0] = N;  // Начальная высота на первом стержне (все диски)

        for (int i = 0; i < max_size; i++) {  // Моделирование всех шагов
            int step = i + 1;  // Текущий шаг (нумерация с 1)

            int[] delta = (step % 2 != 0)  // Определение типа перемещения
                    ? carryingOver(N, step, 1)  // Нечетный шаг - перемещение маленького диска
                    : carryingOver(N, step, countBits(step) + 1);  // Четный шаг - другого диска

            for (int j = 0; j < 3; j++)  // Обновление высот всех стержней
                heights[j] += delta[j];

            int max = Math.max(heights[0], Math.max(heights[1], heights[2]));  // Максимальная высота
            dsu.makeSet(i);  // Создание множества для текущего шага

            int maxHeightIndex = max - 1;  // Индекс в массиве (высота-1)

            if (steps_heights[maxHeightIndex] == -1)  // Если высота встречается впервые
                steps_heights[maxHeightIndex] = i;  // Запоминаем первый шаг с этой высотой
            else  // Если высота уже встречалась
                dsu.unionSets(steps_heights[maxHeightIndex], i);  // Объединяем шаги
        }

        int[] sizes = new int[N];  // Массив для хранения размеров компонент
        for (int i = 0; i < N; i++)  // Для каждой возможной высоты
            if (steps_heights[i] != -1)  // Если высота достигалась
                sizes[i] = dsu.size(steps_heights[i]);  // Сохраняем размер компоненты

        StringBuilder sb = new StringBuilder();  // Построитель строк для результата
        for (int i = 0; i < N; i++) {  // Сортировка выбором по убыванию
            int max = i;  // Индекс максимального элемента
            for (int j = i + 1; j < N; j++)  // Поиск максимального в оставшейся части
                if (sizes[max] < sizes[j])
                    max = j;
            if (sizes[max] == 0)  // Если найден нулевой размер - завершаем
                break;
            int temp = sizes[max];  // Обмен местами текущего и максимального
            sizes[max] = sizes[i];
            sizes[i] = temp;
            sb.insert(0, sizes[i] + " ");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }
}