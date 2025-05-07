package by.it.group451004.rak.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;
import java.util.HashMap;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
//        int n = 55555;
        int m = 1000;
//        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
//        n *= 2;
//        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
//        n *= 2;
//        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
//        n *= 2;
//        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
//        n *= 2;
//        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
//        n *= 2;
//        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());


        BigInteger nBig = BigInteger.valueOf(999999999);
        System.out.printf("fasterMatrixC(%d)=%d \n\t time=%d \n\n", nBig, fibo.fasterMatrixC(nBig, m), fibo.time());
        nBig = nBig.multiply(nBig);
        nBig = nBig.multiply(nBig);
        nBig = nBig.multiply(nBig);
        nBig = nBig.multiply(nBig);
        nBig = nBig.multiply(nBig);
        nBig = nBig.multiply(nBig);
        System.out.printf("fasterMatrixC(%d)=%d \n\t time=%d \n\n", nBig, fibo.fasterMatrixC(nBig, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (n < 2) return n;

        //

        long resBeforeBefore = 0; // F(0)
        long resBefore = 1;       // F(1)
        long result = 1;          // F(2), начнем с i = 2

        for (long i = 2; i <= n; i++) {
            result = (resBefore + resBeforeBefore) % m;
            resBeforeBefore = resBefore;
            resBefore = result;
        }
    return result;
    }

    long fasterMatrixC(long n, int m) {
        if (n < 2) return n;

        long[][] baseMatrix = {{1, 1}, {1, 0}};
        HashMap<Long, long[][]> matrixMap = new HashMap<>();
        matrixMap.put(1L, baseMatrix);

        while (!matrixMap.containsKey(n)) {
            long first = getMaxKey(matrixMap, n - 1);
            long second = getMaxKey(matrixMap, n - first);

            long[][] mult = multiplyMatrices(matrixMap.get(first), matrixMap.get(second), m);
            matrixMap.put(first + second, mult);
        }

        return matrixMap.get(n)[0][1]; // F(n)
    }

    long getMaxKey(HashMap<Long, long[][]> matrixMap, long limit) {
        if (matrixMap.containsKey(limit)) return limit;

        long base = 1;
        while (matrixMap.containsKey(base) && base < limit) {
            base *= 2;
        }
        base /= 2;

        long tail = base / 2;
        while (tail > 0) {
            if (matrixMap.containsKey(base + tail) && base + tail < limit) {
                base += tail;
            }
            tail /= 2;
        }

        return base;
    }

    long[][] multiplyMatrices(long[][] A, long[][] B, int m) {
        long[][] result = new long[2][2];

        result[0][0] = (A[0][0] * B[0][0] % m + A[0][1] * B[1][0] % m) % m;
        result[0][1] = (A[0][0] * B[0][1] % m + A[0][1] * B[1][1] % m) % m;
        result[1][0] = (A[1][0] * B[0][0] % m + A[1][1] * B[1][0] % m) % m;
        result[1][1] = (A[1][0] * B[0][1] % m + A[1][1] * B[1][1] % m) % m;

        return result;
    }

    BigInteger fasterMatrixC(BigInteger n, int m) {
        if (n.compareTo(BigInteger.valueOf(2)) < 0)
            return n;

        BigInteger[][] baseMatrix = {
                {BigInteger.ONE, BigInteger.ONE},
                {BigInteger.ONE, BigInteger.ZERO}
        };

        HashMap<BigInteger, BigInteger[][]> matrixMap = new HashMap<>();
        matrixMap.put(BigInteger.ONE, baseMatrix);

        while (!matrixMap.containsKey(n)) {
            BigInteger first = getMaxKey(matrixMap, n.subtract(BigInteger.ONE));
            BigInteger second = getMaxKey(matrixMap, n.subtract(first));

            BigInteger[][] mult = multiplyMatrices(matrixMap.get(first), matrixMap.get(second), m);
            matrixMap.put(first.add(second), mult);
        }

        return matrixMap.get(n)[0][1]; // F(n)
    }

    BigInteger getMaxKey(HashMap<BigInteger, BigInteger[][]> matrixMap, BigInteger limit) {
        if (matrixMap.containsKey(limit)) return limit;

        BigInteger base = BigInteger.ONE;
        while (matrixMap.containsKey(base) && base.compareTo(limit) < 0) {
            base = base.shiftLeft(1); // *= 2
        }
        base = base.shiftRight(1); // /= 2

        BigInteger tail = base.shiftRight(1);
        while (tail.compareTo(BigInteger.ZERO) > 0) {
            BigInteger sum = base.add(tail);
            if (matrixMap.containsKey(sum) && sum.compareTo(limit) < 0) {
                base = sum;
            }
            tail = tail.shiftRight(1);
        }

        return base;
    }

    BigInteger[][] multiplyMatrices(BigInteger[][] A, BigInteger[][] B, int m) {
        BigInteger M = BigInteger.valueOf(m);
        BigInteger[][] result = new BigInteger[2][2];

        result[0][0] = A[0][0].multiply(B[0][0]).mod(M)
                .add(A[0][1].multiply(B[1][0]).mod(M)).mod(M);
        result[0][1] = A[0][0].multiply(B[0][1]).mod(M)
                .add(A[0][1].multiply(B[1][1]).mod(M)).mod(M);
        result[1][0] = A[1][0].multiply(B[0][0]).mod(M)
                .add(A[1][1].multiply(B[1][0]).mod(M)).mod(M);
        result[1][1] = A[1][0].multiply(B[0][1]).mod(M)
                .add(A[1][1].multiply(B[1][1]).mod(M)).mod(M);

        return result;
    }


//    long fasterC(long n, int m) {
//        if (n < 2) return n;
//        BigInteger result = BigInteger.ONE; //n=2
//        BigInteger resBefore = BigInteger.ONE; //n=1
//        BigInteger resBeforeBefore = BigInteger.ZERO; //n=0
//        for (int i = 1; i < n; i++) {
//            result = resBefore.add(resBeforeBefore);
//            resBeforeBefore = resBefore;
//            resBefore = result;
//        }
//        return result.mod(BigInteger.valueOf(m)).longValue();
//    }

//    long fasterMatrixC(long n, int m) { //логарифмическое время (было бы, но произведение матриц всё хуёвит)
//        if (n < 2) return n;
//        // |1 1| ^ n = |F_n+1 F_n|
//        // |1 0|       |F_n F_n-1|
//        BigInteger[][] matrixOfFib = {{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE,BigInteger.ZERO}};
//        HashMap<Long, BigInteger[][]> mapOfFibMatrices = new HashMap<>();
//        mapOfFibMatrices.put(1L, matrixOfFib);
//        while (!mapOfFibMatrices.containsKey(n)) {
//            long firstMultiplier = getMaxKey(mapOfFibMatrices,n - 1);
//            long secondMultiplier = getMaxKey(mapOfFibMatrices,n - firstMultiplier);
//
//            mapOfFibMatrices.put(firstMultiplier + secondMultiplier,
//                    MultiplyMatrices(mapOfFibMatrices.get(firstMultiplier), mapOfFibMatrices.get(secondMultiplier)));
//        }
//        return mapOfFibMatrices.get(n)[0][1].mod(BigInteger.valueOf(m)).longValue();
//    }
//
//    long getMaxKey(HashMap<Long, BigInteger[][]> mapOfFibMatrices, long limit) { //сложность log n (2 log_2 n)
//        if (mapOfFibMatrices.containsKey(limit)) { return limit; }
//        long base = 1;
//        while (mapOfFibMatrices.containsKey(base) && (base < limit)) {
//            base *= 2;
//        }
//        base /= 2;
//        long tail = base / 2;
//        while (tail > 0) {
//            if (mapOfFibMatrices.containsKey(base + tail)) {
//                base += tail;
//            }
//            tail /= 2;
//        }
//        return base;
//    }
//
//    BigInteger[][] MultiplyMatrices(BigInteger[][] A, BigInteger[][] B){ //тут плохо, квадратичная сложность
//        BigInteger[][] MultipliedMatrix = new BigInteger[2][2];
//        MultipliedMatrix[0][0] = A[0][0].multiply(B[0][0]).add(A[0][1].multiply(B[1][0]));
//        MultipliedMatrix[0][1] = A[0][0].multiply(B[0][1]).add(A[0][1].multiply(B[1][1]));
//        MultipliedMatrix[1][0] = A[1][0].multiply(B[0][0]).add(A[1][1].multiply(B[1][0]));
//        MultipliedMatrix[1][1] = A[1][0].multiply(B[0][1]).add(A[1][1].multiply(B[1][1]));
//        return MultipliedMatrix;
//    }
}

