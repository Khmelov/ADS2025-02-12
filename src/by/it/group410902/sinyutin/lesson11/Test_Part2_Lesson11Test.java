package by.it.group410902.sinyutin.lesson11;


import by.it.HomeWork;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;

public class Test_Part2_Lesson11Test extends HomeWork {
    private static final int RND_SEED = 123;
    public static final int INVOCATION_COUNT_PER_METHOD = 10;
    public static final int MAX_VALUE = 100;
    Random rnd = new Random(123L);
    private Collection<Number> eObject;
    private Collection<Number> aObject;
    private Map<Method, String> cache = new HashMap();

    public Test_Part2_Lesson11Test() {
    }

    @Test(
            timeout = 5000L
    )
    public void testTaskA() throws Exception {
        String[] methods = "size()\nclear()\nisEmpty()\nadd(Object)\nremove(Object)\ncontains(Object)\n\n".split("\\s+");
        this.eObject = new HashSet();
        this.randomCheck("MyHashSet", methods);
    }

    @Test(
            timeout = 5000L
    )
    public void testTaskB() throws Exception {
        String[] methods = "toString()\nsize()\nclear()\nisEmpty()\nadd(Object)\nremove(Object)\ncontains(Object)\n\ncontainsAll(Collection)\naddAll(Collection)\nremoveAll(Collection)\nretainAll(Collection)\n".split("\\s+");
        this.eObject = new LinkedHashSet();
        this.randomCheck("MyLinkedHashSet", methods);
    }

    @Test(
            timeout = 5000L
    )
    public void testTaskC() throws Exception {
        String[] methods = "toString()\nsize()\nclear()\nisEmpty()\nadd(Object)\nremove(Object)\ncontains(Object)\n\ncontainsAll(Collection)\naddAll(Collection)\nremoveAll(Collection)\nretainAll(Collection)\n".split("\\s+");
        this.eObject = new TreeSet();
        this.randomCheck("MyTreeSet", methods);
    }

    private void randomCheck(String aClassName, String... methods) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Set<String> methodNames = new TreeSet(Arrays.asList(methods));
        methodNames.removeIf((key) -> {
            return key == null || key.isBlank();
        });
        Class<?> aClass = this.findClass(aClassName);
        this.checkStructure(aClass);
        System.out.printf("\nStart test methods in class %s%n", aClass);
        this.aObject = (Collection)aClass.getDeclaredConstructor().newInstance();
        Map<String, Method> methodsE = this.fill(this.eObject.getClass(), methodNames);
        Map<String, Method> methodsA = this.fill(aClass, methodNames);
        Assert.assertEquals("Not found methods for test in:\n" + this.getSignatures(aClass), (long)methodNames.size(), (long)methodsA.size());

        for(int testNumber = 0; testNumber < 10 * methodNames.size(); ++testNumber) {
            int count = this.rnd.nextInt(100);
            int mIndex;
            if (this.eObject.size() < 10) {
                for(mIndex = 0; mIndex <= count; ++mIndex) {
                    Integer value = this.rnd.nextInt(100) * (mIndex + 1);
                    this.eObject.add(value);
                    this.aObject.add(value);
                }

                System.out.printf("%n==Add %d random values. %n", count);
            }

            mIndex = this.rnd.nextInt(methodsA.size());
            Method methodE = null;
            Method methodA = null;
            int i = 0;
            Iterator var13 = methodsA.entrySet().iterator();

            while(var13.hasNext()) {
                Map.Entry<String, Method> entry = (Map.Entry)var13.next();
                if (mIndex == i++) {
                    methodA = (Method)entry.getValue();
                    methodE = (Method)methodsE.get(entry.getKey());
                    break;
                }
            }

            int params = methodE.getParameterCount();
            Object[] parameters = this.getRandomParams(methodA.getParameterTypes());
            String nameAndParameters = this.getSignature(methodA).replace(")", "->" + Arrays.toString(parameters)) + ")";
            System.out.printf("Start %s%n", nameAndParameters);
            Object expected = methodE.invoke(this.eObject, parameters);
            Object actual = methodA.invoke(this.aObject, parameters);
            String eString = this.eObject.toString();
            String aString = this.aObject.toString();
            Assert.assertEquals("Error compare methods\n" + String.valueOf(methodE) + "\n" + String.valueOf(methodA), expected, actual);
            System.out.printf("\tStop. Size actual=%d expected=%d%n", this.aObject.size(), this.eObject.size());
            int eChecksum = this.checkSum(eString);
            int aChecksum = this.checkSum(aString);
            Assert.assertEquals("Erros state\nexpectred check sum=%d for %s\n   actual check sum=%d for %s\n".formatted(eChecksum, eString, aChecksum, aString), (long)eChecksum, (long)aChecksum);
        }

        PrintStream var10000 = System.out;
        String var25 = "=".repeat(100);
        var10000.println(var25 + "\nCOMPLETE: " + String.valueOf(methodNames));
        System.out.println("expected: " + String.valueOf(this.eObject));
        System.out.println("  actual: " + String.valueOf(this.aObject));
    }

    private Object[] getRandomParams(Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];

        for(int i = 0; i < parameterTypes.length; ++i) {
            if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                Set<Number> collect = (Set)IntStream.range(2, 2 + this.rnd.nextInt(this.eObject.size())).mapToObj((index) -> {
                    return this.randomInteger();
                }).collect(Collectors.toUnmodifiableSet());
                parameters[i] = collect;
            } else if (Integer.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = this.randomInteger();
            } else if (Integer.TYPE.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = this.getRandomIndex();
            } else if (Object.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = this.randomInteger();
            } else {
                Assert.fail("unexpected type " + String.valueOf(parameterTypes[i]));
            }
        }

        return parameters;
    }

    private Number randomInteger() {
        int i = this.getRandomIndex();
        if (this.rnd.nextBoolean()) {
            return i * this.eObject.size();
        } else {
            Iterator<Number> iterator = this.eObject.iterator();

            while(i-- > 0) {
                iterator.next();
            }

            return (Number)iterator.next();
        }
    }

    private int getRandomIndex() {
        return this.rnd.nextInt(this.eObject.size());
    }

    private void checkStructure(Class<?> aClass) {
        if (aClass.getPackageName().equals(this.getClass().getPackageName())) {
            Assert.assertEquals("Incorrect parent", Object.class, aClass.getSuperclass());
        }

        Field[] var2 = aClass.getDeclaredFields();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            this.checkFieldAsCollection(field);
            Field[] var6 = field.getType().getDeclaredFields();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Field subField = var6[var8];
                this.checkFieldAsCollection(subField);
            }
        }

    }

    private void checkFieldAsCollection(Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            Assert.fail("Incorrect field: " + String.valueOf(field));
        }

    }

    private Map<String, Method> fill(Class<?> c, Set<String> methodNames) {
        return (Map)Stream.of(c.getMethods(), c.getDeclaredMethods()).flatMap(Arrays::stream).distinct().filter((m) -> {
            return methodNames.contains(this.getSignature(m).split(" ", 3)[1]);
        }).filter(this::notComparable).collect(Collectors.toMap(this::getSignature, (m) -> {
            return m;
        }));
    }

    private boolean notComparable(Method m) {
        return m.getReturnType() != Comparable.class && Arrays.stream(m.getParameterTypes()).noneMatch((p) -> {
            return p == Comparable.class;
        });
    }

    private String getSignature(Method method) {
        return (String)this.cache.computeIfAbsent(method, (m) -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            StringJoiner out = new StringJoiner(",", method.getReturnType().getSimpleName() + " " + method.getName() + "(", ")");
            int i = 0;

            for(int parameterTypesLength = parameterTypes.length; i < parameterTypesLength; ++i) {
                out.add(parameterTypes[i].getSimpleName());
            }

            return out.toString();
        });
    }

    public String getSignatures(Class<?> aClass) {
        return this.getSignatures(aClass.getMethods(), aClass.getDeclaredMethods());
    }

    public String getSignatures(Method[]... methods) {
        return (String)Stream.of(methods).flatMap(Arrays::stream).distinct().filter((m) -> {
            return !Modifier.isStatic(m.getModifiers());
        }).map(this::getSignature).collect(Collectors.joining("\n"));
    }

    private int checkSum(String someString) {
        return someString.chars().sum();
    }
}

