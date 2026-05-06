/**
 * Practical23B.java
 * CSC211 (2026) - Term 2 Practical 3B
 *
 * Ternary heap stored in a String[] array starting at index 1.
 * Each node i has children at indices 3i-1, 3i, and 3i+1.
 *
 * Reference: Lecture notes on d-ary heaps; index formula derived from
 * standard binary heap formula generalised to branching factor d=3.
 */
public class Practical23B {

    // ---------------------------------------------------------------
    // Ternary heap index helpers (1-based)
    // For a node at index i:
    //   first child  : 3i - 1
    //   second child : 3i
    //   third child  : 3i + 1
    // ---------------------------------------------------------------

    /**
     * Question 1
     * Prints each node together with its children (up to three).
     * Output format: "Node X -> A, B, C"
     * Only children that exist within the array are printed.
     *
     * @param heap String array (1-based); heap[0] is ignored.
     */
    public static void print(String[] heap) {
        if (heap == null || heap.length <= 1) {
            return; // nothing to print
        }

        int n = heap.length - 1; // number of elements (indices 1..n)

        for (int i = 1; i <= n; i++) {
            // Skip slots that are null or empty
            if (heap[i] == null || heap[i].isEmpty()) {
                continue;
            }

            int c1 = 3 * i - 1; // first child index
            int c2 = 3 * i;     // second child index
            int c3 = 3 * i + 1; // third child index

            // Build the children string only for children that exist
            StringBuilder children = new StringBuilder();
            if (c1 <= n && heap[c1] != null) {
                if (children.length() > 0) children.append(", ");
                children.append(heap[c1]);
            }
            if (c2 <= n && heap[c2] != null) {
                if (children.length() > 0) children.append(", ");
                children.append(heap[c2]);
            }
            if (c3 <= n && heap[c3] != null) {
                if (children.length() > 0) children.append(", ");
                children.append(heap[c3]);
            }

            // Only print nodes that have at least one child
            if (children.length() > 0) {
                System.out.println("Node " + heap[i] + " -> " + children);
            }
        }
    }

    /**
     * Question 2
     * Validates whether the array represents a valid ternary min-heap
     * or a valid ternary max-heap.
     *
     * A min-heap requires every parent to be <= all of its children.
     * A max-heap requires every parent to be >= all of its children.
     * Lexicographic (String.compareTo) ordering is used throughout.
     *
     * @param heap String array (1-based); heap[0] is ignored.
     * @return true if the array is a valid min- or max-heap, false otherwise.
     */
    public static boolean validate(String[] heap) {
        if (heap == null || heap.length <= 2) {
            // 0 or 1 element is trivially a valid heap
            return true;
        }

        int n = heap.length - 1; // number of elements

        // Determine heap type by examining the root and its first valid child
        // We need to find the first parent-child pair to decide min vs max.
        Boolean isMin = null; // null = undecided yet

        for (int i = 1; i <= n; i++) {
            if (heap[i] == null || heap[i].isEmpty()) continue;

            int[] childIndices = { 3 * i - 1, 3 * i, 3 * i + 1 };

            for (int ci : childIndices) {
                if (ci > n || heap[ci] == null || heap[ci].isEmpty()) continue;

                int cmp = heap[i].compareTo(heap[ci]);

                if (isMin == null) {
                    // First non-equal pair determines the heap type
                    if (cmp < 0) {
                        isMin = true;  // parent < child → min-heap
                    } else if (cmp > 0) {
                        isMin = false; // parent > child → max-heap
                    }
                    // cmp == 0 (duplicates): keep looking to determine type
                }
            }

            if (isMin != null) break; // type determined; stop scanning for type
        }

        // If still undecided (all equal or single element), it's valid as both
        if (isMin == null) return true;

        // Now verify the entire heap against the determined type
        for (int i = 1; i <= n; i++) {
            if (heap[i] == null || heap[i].isEmpty()) continue;

            int[] childIndices = { 3 * i - 1, 3 * i, 3 * i + 1 };

            for (int ci : childIndices) {
                if (ci > n || heap[ci] == null || heap[ci].isEmpty()) continue;

                int cmp = heap[i].compareTo(heap[ci]);

                if (isMin && cmp > 0) {
                    // Min-heap violated: parent > child
                    return false;
                }
                if (!isMin && cmp < 0) {
                    // Max-heap violated: parent < child
                    return false;
                }
            }
        }

        return true;
    }

    // ---------------------------------------------------------------
    // main: test cases
    // ---------------------------------------------------------------
    public static void main(String[] args) {

        System.out.println("=== Test 1: Valid min-heap (strings) ===");
        //        apple
        //      /   |   \
        //   apply  art  banana
        //  / | \
        // cat dog egg
        String[] minHeap = { null, "apple", "apply", "art", "banana", "cat", "dog", "egg" };
        print(minHeap);
        System.out.println("Valid: " + validate(minHeap)); // expected: true

        System.out.println();
        System.out.println("=== Test 2: Valid max-heap (strings) ===");
        String[] maxHeap = { null, "zebra", "yellow", "xray", "wolf", "van", "urn", "top" };
        print(maxHeap);
        System.out.println("Valid: " + validate(maxHeap)); // expected: true

        System.out.println();
        System.out.println("=== Test 3: Invalid heap ===");
        // "apple" as root but child "aardvark" < "apple" (min violation if max intended)
        // and "zebra" > "apple" which breaks min-heap
        String[] invalid = { null, "apple", "zebra", "ant", "banana" };
        print(invalid);
        System.out.println("Valid: " + validate(invalid)); // expected: false

        System.out.println();
        System.out.println("=== Test 4: Single element ===");
        String[] single = { null, "hello" };
        print(single);
        System.out.println("Valid: " + validate(single)); // expected: true

        System.out.println();
        System.out.println("=== Test 5: All duplicates ===");
        String[] dupes = { null, "same", "same", "same", "same" };
        print(dupes);
        System.out.println("Valid: " + validate(dupes)); // expected: true

        System.out.println();
        System.out.println("=== Test 6: Null/empty array ===");
        System.out.println("Valid (null): " + validate(null));          // expected: true
        System.out.println("Valid (empty): " + validate(new String[0])); // expected: true
        System.out.println("Valid (only index-0): " + validate(new String[]{ null })); // expected: true

        System.out.println();
        System.out.println("=== Test 7: Last node has fewer than 3 children ===");
        //        a
        //      / | \
        //     b  c  d
        //    /
        //   e           <- only one child of node b
        String[] partial = { null, "a", "b", "c", "d", "e" };
        print(partial);
        System.out.println("Valid: " + validate(partial)); // expected: true
    }
}
