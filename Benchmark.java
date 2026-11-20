import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Benchmark {
    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Path.of("data.txt"));
        int[] input = new int[lines.size()];
        for (int i = 0; i < input.length; i++) {
            input[i] = Integer.parseInt(lines.get(i));
        }
        int[] expected = input.clone();
        Arrays.sort(expected);

        // Warm up the JVM before recording any measurements.
        for (int i = 0; i < 30; i++) {
            measure(input, expected, 7);
            measure(input, expected, 10);
        }

        int rounds = 11;
        long[] times7 = new long[rounds];
        long[] times10 = new long[rounds];
        System.out.println("round,cutoff_7_us,cutoff_10_us");
        for (int i = 0; i < rounds; i++) {
            // Alternate which cutoff runs first.
            if (i % 2 == 0) {
                times7[i] = measure(input, expected, 7);
                times10[i] = measure(input, expected, 10);
            } else {
                times10[i] = measure(input, expected, 10);
                times7[i] = measure(input, expected, 7);
            }
            System.out.printf(java.util.Locale.ROOT, "%d,%.3f,%.3f%n",
                    i + 1, times7[i] / 1000.0, times10[i] / 1000.0);
        }

        Arrays.sort(times7);
        Arrays.sort(times10);
        System.out.printf(java.util.Locale.ROOT, "median,%.3f,%.3f%n",
                times7[rounds / 2] / 1000.0, times10[rounds / 2] / 1000.0);
    }

    private static long measure(int[] input, int[] expected, int cutoff) {
        int[] copy = input.clone();
        long start = System.nanoTime();
        MergeSort.sort(copy, cutoff);
        long elapsed = System.nanoTime() - start;
        if (!Arrays.equals(copy, expected)) {
            throw new AssertionError("Wrong result for cutoff " + cutoff);
        }
        return elapsed;
    }
}


