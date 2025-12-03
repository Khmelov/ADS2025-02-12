    Задание на уровень C

    Создайте class MySplayMap, который реализует интерфейс NavigableMap<Integer, String>
    и работает на основе splay-дерева
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке возрастания ключей
    Формат вывода: скобки (фигурные) и разделители
    (знак равенства и запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

                toString()
                put(Integer,String)
                remove(Integer)
                get(Integer)
                containsKey(Integer)
                containsValue(String)

                size()
                clear()
                isEmpty()

                headMap(Integer)
                tailMap(Integer)
                firstKey()
                lastKey()

                lowerKey(Integer)
                floorKey(Integer)
                ceilingKey(Integer)
                higherKey(Integer)
