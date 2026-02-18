public class practic {
    static long countOn3 = 0;
    static long  countOn2A = 0;

    static long countOn2B = 0;
    static long countOn = 0;

    static class Counter{
        private long value = 0;

        public void increment() {
            value++;
        }

        public long getValue()
        {
            return value;
        }
    }




    public static int mcsOn2A(int[] X) {
        Counter counter = new Counter();
        int maxSoFar = 0;

        for (int low = 0; low < X.length; low++) {
            maxSoFar = Math.max(maxSoFar, maxSubarrayStartingAt(X, low, counter));
        }
        countOn2A = counter.getValue();
        return maxSoFar;
    }

    private static int maxSubarrayStartingAt(int[] X, int start, Counter counter) {
        int maxSum = 0;
        int currentSum = 0;

        for (int i = start; i < X.length; i++) {
            currentSum += X[i];
            counter.increment();
            maxSum = Math.max(maxSum, currentSum);
        }
        return maxSum;
    }


        }



