package by.it.a_khmelev.lesson10;


import by.it.HomeWork;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings("all")

//поставьте курсор на следующую строку и нажмите Ctrl+Shift+F10
//для корректной сборки теста добавьте библиотеку init.jar в проект (она находится в корне)
public class Test_Part2_Lesson10Test extends HomeWork {

    private static final int RND_SEED = 123;
    public static final int INVOCATION_COUNT_PER_METHOD = 10;
    public static final int MAX_VALUE = 100;
    Random rnd = new Random(RND_SEED);
    private Collection<Number> eObject;
    private Collection<Number> aObject;

    private Map<Method, String> cache = new HashMap<>();

    @Test(timeout = 5000)
    public void testTaskA() throws Exception {
        String[] methods = """
                toString()
                size()
                              
                add(Object)
                addFirst(Object)
                addLast(Object)
                                
                element()
                getFirst()
                getLast()
                                
                poll()
                pollFirst()
                pollLast()
                """.split("\\s+");
        eObject = new ArrayDeque<>();
        randomCheck("MyArrayDeque", methods);
    }

    @Test(timeout = 5000)
    public void testTaskB() throws Exception {
        String[] methods = """
                toString()
                add(Object)
                remove(int)
                remove(Object)
                size()
                                
                addFirst(Object)
                addLast(Object)
                                
                element()
                getFirst()
                getLast()
                                
                poll()
                pollFirst()
                pollLast()
                """.split("\\s+");
        eObject = new LinkedList<>();
        randomCheck("MyLinkedList", methods);
    }

    @Test(timeout = 5000)
    public void testTaskC() throws Exception {
        String[] methods = """
                toString()
                size()
                clear()
                add(Object)
                remove()
                contains(Object)
                                
                offer(Object)
                poll()
                peek()
                element()
                isEmpty()
                                
                containsAll(Collection)
                addAll(Collection)
                removeAll(Collection)
                retainAll(Collection)
                """.split("\\s+");
        eObject = new PriorityQueue<>();
        randomCheck("MyPriorityQueue", methods);

        // Подсказка! Вывод образцовой коллекции должен быть абсолютно идентичен вашей.
        // Все методы имеют жестко заданное поведение, а уровень C не удается пройти скорее всего
        // из-за того, что методы removeAll(Collection) и retainAll(Collection) вами сделаны наивно
        // и имеют скорость O(n log n). Сторонним эффектом такого решения будет отличие в порядке элементов.
        // Корректно написанные removeAll и retainAll должны работать за O(n), считая, что операции contains
        // в переданной коллекции работают за O(1). See https://en.wikipedia.org/wiki/Heapsort
    }

    private void randomCheck(String aClassName, String... methods) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Set<String> methodNames = new TreeSet<>(Arrays.asList(methods));
        methodNames.removeIf(key -> key == null || key.isBlank());
        Class<?> aClass = findClass(aClassName);
        checkStructure(aClass);
        System.out.printf("\nStart test methods in class %s%n", aClass);
        aObject = (Collection<Number>) aClass.getDeclaredConstructor().newInstance();

        Map<String, Method> methodsA = fill(aClass, methodNames);
        Map<String, Method> methodsE = fill(eObject.getClass(), methodNames);

        assertEquals("Not found methods for test in:\n" + getSignatures(aClass), methodNames.size(), methodsA.size());

        for (int testNumber = 0; testNumber < INVOCATION_COUNT_PER_METHOD * methodNames.size(); testNumber++) {
            int count = rnd.nextInt(INVOCATION_COUNT_PER_METHOD * 10);
            if (eObject.size() < 10) {
                for (int i = 0; i <= count; i++) {
                    Integer value = rnd.nextInt(MAX_VALUE) * (i + 1);
                    aObject.add(value);
                    eObject.add(value);
                }
                System.out.printf("%n==Add %d random values. %n", count);
            }
            int mIndex = rnd.nextInt(methodsA.size());
            Method methodE = null;
            Method methodA = null;
            int i = 0;
            for (Map.Entry<String, Method> entry : methodsA.entrySet()) {
                if (mIndex == i++) {
                    methodA = entry.getValue();
                    methodE = methodsE.get(entry.getKey());
                    break;
                }
            }
            int params = methodE.getParameterCount();
            Object[] parameters = getRandomParams(methodA.getParameterTypes());
            System.out.printf("Start %s. Parameters=%s%n", getSignature(methodA), Arrays.toString(parameters));
            Object expected = methodE.invoke(eObject, parameters);
            Object actual = methodA.invoke(aObject, parameters);
            String eString = eObject.toString();
            String aString = aObject.toString();
            assertEquals("Error compare methods\n" + methodE + "\n" + methodA, expected, actual);
            assertEquals("Erros state after\n" + methodE + "\n" + methodA, eString, aString);
            System.out.printf("Size actual=%d expected=%d%n", aObject.size(), eObject.size());
        }
        System.out.println("=".repeat(100) + "\nCOMPLETE: " + methodNames);
        System.out.println("expected: " + eObject);
        System.out.println("  actual: " + aObject);
    }

    private Object[] getRandomParams(Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                Set<Number> collect = IntStream
                        .range(2, 2 + rnd.nextInt(eObject.size()))
                        .mapToObj(index -> randomInteger())
                        .collect(Collectors.toUnmodifiableSet());
                parameters[i] = collect;
            } else if (Integer.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = randomInteger();
            } else if (int.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = getRandomIndex();
            } else if (Object.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = randomInteger();
            } else {
                fail("unexpected type " + parameterTypes[i]);
            }
        }
        return parameters;
    }

    private Number randomInteger() {
        int i = getRandomIndex();
        if (rnd.nextBoolean()) {
            return i * eObject.size();
        }
        Iterator<Number> iterator = eObject.iterator();
        while (i-- > 0) {
            iterator.next();
        }
        return iterator.next();
    }

    private int getRandomIndex() {
        return rnd.nextInt(eObject.size());
    }


    private void checkStructure(Class<?> aClass) {
        if (aClass.getPackageName().equals(this.getClass().getPackageName())) {
            assertEquals("Incorrect parent", Object.class, aClass.getSuperclass());
        }
        for (Field field : aClass.getDeclaredFields()) {
            checkFieldAsCollection(field);
            for (Field subField : field.getType().getDeclaredFields()) {
                checkFieldAsCollection(subField);
            }
        }
    }

    private void checkFieldAsCollection(Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            fail("Incorrect field: " + field);
        }
    }


    private Map<String, Method> fill(Class<?> c, Set<String> methodNames) {
        return Stream.of(c.getMethods(), c.getDeclaredMethods())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(m -> methodNames.contains(getSignature(m).split(" ", 3)[1]))
                .filter(this::notComparable)
                .collect(Collectors.toMap(this::getSignature, m -> m));
    }


    private boolean notComparable(Method m) {
        return m.getReturnType() != Comparable.class &&
               Arrays.stream(m.getParameterTypes())
                       .noneMatch(p -> p == Comparable.class);
    }

    private String getSignature(Method method) {
        return cache.computeIfAbsent(method, m -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            StringJoiner out = new StringJoiner(
                    ",",
                    method.getReturnType().getSimpleName() + " " + method.getName() + "(",
                    ")"
            );
            for (int i = 0, parameterTypesLength = parameterTypes.length; i < parameterTypesLength; i++) {
                out.add(parameterTypes[i].getSimpleName());
            }
            return out.toString();
        });
    }


    public String getSignatures(Class<?> aClass) {
        return getSignatures(aClass.getMethods(), aClass.getDeclaredMethods());
    }

    public String getSignatures(Method[]... methods) {
        return Stream.of(methods)
                .flatMap(Arrays::stream)
                .distinct()
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .map(this::getSignature)
                .collect(Collectors.joining("\n"));
    }
}