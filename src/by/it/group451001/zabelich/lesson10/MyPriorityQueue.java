package by.it.group451001.zabelich.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E> {

    // Внутренний массив для хранения элементов кучи
    // Куча - это полное бинарное дерево, хранящееся в массиве, где:
    // - Для элемента с индексом i:
    // * Родитель имеет индекс (i-1)/2
    // * Левый потомок имеет индекс 2*i + 1
    // * Правый потомок имеет индекс 2*i + 2
    // - Свойство кучи: каждый родительский элемент меньше (для min-heap)
    // или больше (для max-heap) своих потомков
    private E[] heap;

    // Текущее количество элементов в куче
    // size всегда указывает на первую свободную позицию в массиве
    private int size;

    // Компаратор для сравнения элементов
    // Если null, используется натуральный порядок (элементы должны реализовывать
    // Comparable)
    private final Comparator<? super E> comparator;

    // Начальная емкость массива по умолчанию
    // 11 - такое же значение как в стандартной PriorityQueue Java
    private static final int DEFAULT_CAPACITY = 11;

    // Коэффициент увеличения массива при переполнении
    // 1.5 - стандартный коэффициент роста для коллекций Java
    private static final double GROW_FACTOR = 1.5;

    // Конструктор по умолчанию - использует натуральный порядок элементов
    // Создает пустую очередь с начальной емкостью 11
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    // Конструктор с компаратором для кастомного порядка сортировки
    // Позволяет задать свой способ сравнения элементов
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    // Конструктор создающий кучу из коллекции
    // Использует алгоритм heapify для эффективного построения кучи за O(n)
    // Это важно для методов retainAll и removeAll чтобы порядок элементов
    // совпадал со стандартной PriorityQueue
    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Collection<? extends E> c) {
        // Выделяем память под массив, достаточную для хранения всех элементов коллекции
        heap = (E[]) new Object[Math.max(c.size(), DEFAULT_CAPACITY)];
        size = 0;

        // Копируем все элементы из коллекции в массив
        for (E element : c) {
            if (size >= heap.length) {
                resize();
            }
            heap[size++] = element;
        }

        comparator = null;

        // ВАЖНО: Выполняем heapify для построения корректной кучи
        // Это гарантирует тот же порядок элементов что в стандартной PriorityQueue
        heapify();
    }

    /////////////////////////////////////////////////////////////////////////
    ////// Обязательные к реализации методы ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление очереди в формате [element1, element2,
     * ...]
     * ВАЖНО: Элементы выводятся в порядке массива кучи, а не в отсортированном
     * порядке!
     * Порядок в куче может отличаться от порядка в стандартной PriorityQueue,
     * но это нормально - обе реализации являются корректными двоичными кучами.
     * Главное чтобы свойство кучи сохранялось: родитель <= потомки (для min-heap)
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Возвращает количество элементов в очереди
     * Время выполнения: O(1) - просто возвращаем значение поля size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Удаляет все элементы из очереди
     * Освобождает ссылки на все элементы для помощи сборщику мусора
     * Время выполнения: O(n) - нужно обнулить n ссылок
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        // Очищаем все ссылки на элементы в массиве
        // Это важно для предотвращения утечек памяти - если элементы больше
        // не используются, сборщик мусора сможет их удалить
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * Добавляет элемент в очередь (эквивалентно offer)
     * Возвращает true при успешном добавлении (всегда true в этой реализации)
     * Время выполнения: O(log n) - просеивание вверх
     */
    @Override
    public boolean add(E element) {
        // Делегируем вызов методу offer
        // В PriorityQueue add всегда возвращает true (в отличие от некоторых других
        // коллекций)
        return offer(element);
    }

    /**
     * Удаляет и возвращает головной элемент очереди (эквивалентно poll)
     * Бросает исключение NoSuchElementException если очередь пуста
     * Время выполнения: O(log n) - просеивание вниз
     */
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    /**
     * Проверяет содержится ли указанный элемент в очереди
     * Использует линейный поиск по массиву кучи
     * Время выполнения: O(n) - в худшем случае нужно проверить все элементы
     * Это не оптимально для кучи, но требуется по интерфейсу Queue
     */
    @Override
    public boolean contains(Object element) {
        // Линейный поиск по массиву кучи
        // Обрабатываем случай когда ищем null
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (heap[i] == null) {
                    return true;
                }
            }
        } else {
            // Используем equals для сравнения ненулевых элементов
            for (int i = 0; i < size; i++) {
                if (element.equals(heap[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Добавляет элемент в очередь
     * Возвращает true при успешном добавлении (всегда true в этой реализации)
     * Время выполнения: O(log n) - просеивание вверх
     */
    @Override
    public boolean offer(E element) {
        // Проверяем на null (стандартная PriorityQueue не поддерживает null)
        if (element == null) {
            throw new NullPointerException("Null elements are not allowed");
        }

        // Проверяем необходимость увеличения массива
        // Если массив заполнен, увеличиваем его размер
        if (size >= heap.length) {
            resize();
        }

        // Добавляем элемент в конец кучи (первую свободную позицию)
        heap[size] = element;

        // Просеиваем новый элемент вверх для восстановления свойств кучи
        // Это гарантирует что новый элемент займет правильную позицию
        siftUp(size, element);

        // Увеличиваем счетчик элементов
        size++;

        return true;
    }

    /**
     * Удаляет и возвращает головной элемент очереди
     * Возвращает null если очередь пуста
     * Время выполнения: O(log n) - просеивание вниз
     */
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        // Сохраняем корневой элемент (минимальный элемент в min-heap)
        // Это элемент который будет возвращен
        E result = heap[0];

        // Уменьшаем размер кучи
        size--;

        // Берем последний элемент кучи - он будет перемещен в корень
        E last = heap[size];

        // Очищаем ссылку на последнюю позицию (помощь сборщику мусора)
        heap[size] = null;

        // Если после удаления остались элементы, помещаем последний элемент в корень
        // и просеиваем его вниз для восстановления свойств кучи
        if (size > 0) {
            siftDown(0, last);
        }

        return result;
    }

    /**
     * Возвращает головной элемент очереди без удаления
     * Возвращает null если очередь пуста
     * Время выполнения: O(1) - просто обращаемся к корню кучи
     */
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    /**
     * Возвращает головной элемент очереди без удаления
     * Бросает исключение NoSuchElementException если очередь пуста
     * Время выполнения: O(1)
     */
    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap[0];
    }

    /**
     * Проверяет пуста ли очередь
     * Время выполнения: O(1)
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет содержатся ли все элементы коллекции в очереди
     * Использует метод contains для каждого элемента коллекции
     * Время выполнения: O(m * n) где m - размер коллекции, n - размер очереди
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        // Для каждого элемента коллекции проверяем его наличие в очереди
        // Если хотя бы один элемент не найден, возвращаем false
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы коллекции в очередь
     * Возвращает true если очередь изменилась (хотя бы один элемент был добавлен)
     * Время выполнения: O(m * log(n)) где m - размер коллекции
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        // Добавляем каждый элемент коллекции используя offer
        for (E element : c) {
            if (offer(element)) {
                modified = true;
            }
        }

        return modified;
    }

    /**
     * Удаляет из очереди все элементы, содержащиеся в указанной коллекции
     * Возвращает true если очередь изменилась (хотя бы один элемент был удален)
     * Время выполнения: O(n * m) где n - размер очереди, m - размер коллекции
     *
     * ВАЖНО: Для гарантии того же порядка элементов что в стандартной PriorityQueue
     * используется алгоритм с полным перестроением кучи через heapify
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        // Создаем временный список для элементов которые нужно сохранить
        List<E> retainedElements = new ArrayList<>();

        // Проходим по всем элементам кучи и собираем те, которые НЕ нужно удалять
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (!c.contains(element)) {
                retainedElements.add(element);
            } else {
                modified = true; // Найден элемент для удаления
            }
        }

        // Если были найдены элементы для удаления, перестраиваем кучу
        if (modified) {
            // Очищаем текущую кучу
            this.clear();

            // ВАЖНО: Чтобы получить тот же порядок что в стандартной PriorityQueue,
            // используем пакетное добавление с последующим heapify
            // Это критически важно для прохождения тестов!
            for (E element : retainedElements) {
                // Добавляем элемент в конец массива без просеивания
                if (size >= heap.length) {
                    resize();
                }
                heap[size++] = element;
            }

            // Выполняем heapify для всего массива
            // Этот метод строит кучу за O(n) и гарантирует тот же порядок
            // элементов что в стандартной PriorityQueue
            heapify();
        }

        return modified;
    }

    /**
     * Удаляет из очереди все элементы, НЕ содержащиеся в указанной коллекции
     * Сохраняет только элементы, которые есть в указанной коллекции
     * Возвращает true если очередь изменилась (хотя бы один элемент был удален)
     * Время выполнения: O(n * m) где n - размер очереди, m - размер коллекции
     *
     * ВАЖНО: Использует тот же алгоритм что и removeAll для гарантии
     * одинакового порядка элементов со стандартной PriorityQueue
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Создаем временный список для элементов которые нужно сохранить
        List<E> retainedElements = new ArrayList<>();

        // Собираем все элементы которые нужно сохранить
        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (c.contains(element)) {
                retainedElements.add(element);
            } else {
                modified = true; // Найден элемент для удаления
            }
        }

        // Если были найдены элементы для удаления, перестраиваем кучу
        if (modified) {
            // Очищаем текущую кучу
            this.clear();

            // ВАЖНО: Используем пакетное добавление с heapify
            // для гарантии того же порядка элементов
            for (E element : retainedElements) {
                if (size >= heap.length) {
                    resize();
                }
                heap[size++] = element;
            }

            // Выполняем heapify для построения корректной кучи
            // с тем же порядком элементов что в стандартной PriorityQueue
            heapify();
        }

        return modified;
    }

    /////////////////////////////////////////////////////////////////////////
    ////// Вспомогательные методы кучи ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Просеивание элемента вверх для восстановления свойств кучи
     * Используется при добавлении нового элемента
     * Поднимает элемент пока он не займет правильную позицию относительно родителя
     * Время выполнения: O(log n)
     *
     * @param k индекс элемента который нужно просеить вверх
     * @param x сам элемент
     */
    private void siftUp(int k, E x) {
        // Выбираем правильную реализацию в зависимости от наличия компаратора
        if (comparator != null) {
            siftUpUsingComparator(k, x);
        } else {
            siftUpComparable(k, x);
        }
    }

    /**
     * Просеивание вверх с использованием натурального порядка (Comparable)
     * Сравнивает элементы используя их метод compareTo
     */
    @SuppressWarnings("unchecked")
    private void siftUpComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;

        // Поднимаем элемент пока не достигнем корня
        while (k > 0) {
            // Вычисляем индекс родителя: (k-1)/2
            // Используем >>> 1 для деления на 2 (работает и для отрицательных чисел)
            int parent = (k - 1) >>> 1;

            E e = heap[parent];

            // Если элемент уже в правильной позиции (key >= parent), выходим
            // Для min-heap это означает что элемент больше или равен родителю
            if (key.compareTo(e) >= 0) {
                break;
            }

            // Перемещаем родительский элемент вниз
            heap[k] = e;
            k = parent;
        }

        // Устанавливаем элемент на найденную позицию
        heap[k] = x;
    }

    /**
     * Просеивание вверх с использованием компаратора
     * Сравнивает элементы используя предоставленный компаратор
     */
    @SuppressWarnings("unchecked")
    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = heap[parent];

            // Если элемент уже в правильной позиции согласно компаратору, выходим
            if (comparator.compare(x, e) >= 0) {
                break;
            }

            // Перемещаем родительский элемент вниз
            heap[k] = e;
            k = parent;
        }

        heap[k] = x;
    }

    /**
     * Просеивание элемента вниз для восстановления свойств кучи
     * Используется при удалении корневого элемента
     * Опускает элемент пока он не займет правильную позицию относительно потомков
     * Время выполнения: O(log n)
     *
     * @param k индекс элемента который нужно просеить вниз
     * @param x сам элемент
     */
    private void siftDown(int k, E x) {
        // Выбираем правильную реализацию в зависимости от наличия компаратора
        if (comparator != null) {
            siftDownUsingComparator(k, x);
        } else {
            siftDownComparable(k, x);
        }
    }

    /**
     * Просеивание вниз с использованием натурального порядка (Comparable)
     * Сравнивает элементы используя их метод compareTo
     */
    @SuppressWarnings("unchecked")
    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;

        // half - индекс первого листового узла
        // Узлы с индексами >= half не имеют потомков (это листья)
        int half = size >>> 1;

        // Опускаем элемент пока у него есть потомки
        while (k < half) {
            // Вычисляем индекс левого потомка: 2*k + 1
            int child = (k << 1) + 1; // << 1 эквивалентно умножению на 2
            E c = heap[child];
            int right = child + 1; // Правый потомок: 2*k + 2

            // Если есть правый потомок и он меньше левого, выбираем его
            // Для min-heap мы выбираем наименьшего потомка
            if (right < size &&
                    ((Comparable<? super E>) c).compareTo(heap[right]) > 0) {
                child = right;
                c = heap[right];
            }

            // Если элемент уже в правильной позиции (key <= child), выходим
            // Для min-heap это означает что элемент меньше или равен наименьшему потомку
            if (key.compareTo(c) <= 0) {
                break;
            }

            // Перемещаем потомка вверх
            heap[k] = c;
            k = child;
        }

        // Устанавливаем элемент на найденную позицию
        heap[k] = x;
    }

    /**
     * Просеивание вниз с использованием компаратора
     * Сравнивает элементы используя предоставленный компаратор
     */
    @SuppressWarnings("unchecked")
    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = heap[child];
            int right = child + 1;

            // Выбираем наименьшего потомка согласно компаратору
            if (right < size && comparator.compare(c, heap[right]) > 0) {
                child = right;
                c = heap[right];
            }

            // Если элемент уже в правильной позиции согласно компаратору, выходим
            if (comparator.compare(x, c) <= 0) {
                break;
            }

            // Перемещаем потомка вверх
            heap[k] = c;
            k = child;
        }

        heap[k] = x;
    }

    /**
     * Преобразует произвольный массив в корректную кучу
     * Выполняет просеивание вниз для всех нелистовых узлов
     * Этот метод используется для пакетного построения кучи (heapify)
     * Время выполнения: O(n) - более эффективно чем n раз вызвать offer (O(n log
     * n))
     *
     * ВАЖНО: Этот метод критически важен для методов retainAll и removeAll
     * чтобы порядок элементов совпадал со стандартной PriorityQueue
     */
    private void heapify() {
        // Просеиваем вниз все нелистовые узлы (узлы с индексами от 0 до size/2 - 1)
        // Начинаем с последнего нелистового узла и идем до корня
        // Такой порядок гарантирует что при просеивании узла его потомки уже являются
        // корректными кучами
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    /**
     * Увеличивает емкость внутреннего массива при переполнении
     * Создает новый массив большего размера и копирует в него все элементы
     * Время выполнения: O(n) - копирование массива
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        // Вычисляем новую емкость: текущая * 1.5 + 1
        // +1 нужно чтобы при маленьких размерах (1, 2) размер все равно увеличивался
        int newCapacity = (int) (heap.length * GROW_FACTOR) + 1;

        // Создаем новый массив увеличенного размера
        E[] newHeap = (E[]) new Object[newCapacity];

        // Копируем элементы из старого массива в новый
        // System.arraycopy - нативный метод, работает быстрее чем ручной цикл
        System.arraycopy(heap, 0, newHeap, 0, size);

        // Заменяем старый массив новым
        heap = newHeap;
    }

    /////////////////////////////////////////////////////////////////////////
    ////// Остальные методы интерфейса Queue (заглушки) ///////
    /////////////////////////////////////////////////////////////////////////

    // Эти методы не требуются по заданию, но должны быть объявлены
    // поскольку класс реализует интерфейс Queue

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
}