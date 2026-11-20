public final class MergeSort {
    private MergeSort() {}

    public static void sort(int[] values, int cutoff) {
        if (cutoff < 1) throw new IllegalArgumentException("cutoff must be positive");
        sort(values, new int[values.length], 0, values.length, cutoff);
    }

    private static void sort(int[] values, int[] auxiliary, int lo, int hi, int cutoff) {
        if (hi - lo <= cutoff) {
            for (int i = lo + 1; i < hi; i++) {
                int value = values[i];
                int j = i;
                while (j > lo && value < values[j - 1]) {
                    values[j] = values[j - 1];
                    j--;
                }
                values[j] = value;
            }
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(values, auxiliary, lo, mid, cutoff);
        sort(values, auxiliary, mid, hi, cutoff);
        System.arraycopy(values, lo, auxiliary, lo, hi - lo);
        int left = lo;
        int right = mid;
        for (int i = lo; i < hi; i++) {
            if (left == mid || (right < hi && auxiliary[right] < auxiliary[left])) {
                values[i] = auxiliary[right++];
            } else {
                values[i] = auxiliary[left++];
            }
        }
    }
}
