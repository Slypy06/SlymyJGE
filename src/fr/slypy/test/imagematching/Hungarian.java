package fr.slypy.test.imagematching;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

public class Hungarian {

    // Optimal assignment using Hungarian algorithm
    public static Map<Vector2f, Vector2f> optimalAssignment(BufferedImage source, BufferedImage target, float spatialWeight) {
        Pixel[] srcPixels = ImageMatcher.extractPixels(source);
        Pixel[] tgtPixels = ImageMatcher.extractPixels(target);
        int w = source.getWidth();
        int n = tgtPixels.length;
        int m = srcPixels.length;

        if (n != m) throw new IllegalArgumentException("Target cannot be different size than source");

        // Cost matrix (target rows × source cols)
        int[][] cost = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cost[i][j] = ImageMatcher.heuristic(srcPixels[j], tgtPixels[i], spatialWeight);
            }
        }

        int[] assignment = hungarian(cost);
        
        Map<Vector2f, Vector2f> map = new HashMap<>();
        for (int i = 0; i < assignment.length; i++) {
            int tgtX = i % w, tgtY = i / w;
            int srcIndex = assignment[i];
            int srcX = srcIndex % w, srcY = srcIndex / w;
            map.put(new Vector2f(srcX, srcY), new Vector2f(tgtX, tgtY));
        }
        return map;
        
    }

    // Classic Hungarian implementation (minimization)
    static int[] hungarian(int[][] cost) {
        int n = cost.length, m = cost[0].length;
        int[] u = new int[n + 1], v = new int[m + 1], p = new int[m + 1], way = new int[m + 1];

        for (int i = 1; i <= n; i++) {
            p[0] = i;
            int j0 = 0;
            int[] minv = new int[m + 1];
            boolean[] used = new boolean[m + 1];
            Arrays.fill(minv, Integer.MAX_VALUE);
            do {
                used[j0] = true;
                int i0 = p[j0], delta = Integer.MAX_VALUE, j1 = 0;
                for (int j = 1; j <= m; j++) {
                    if (!used[j]) {
                        int cur = cost[i0 - 1][j - 1] - u[i0] - v[j];
                        if (cur < minv[j]) {
                            minv[j] = cur;
                            way[j] = j0;
                        }
                        if (minv[j] < delta) {
                            delta = minv[j];
                            j1 = j;
                        }
                    }
                }
                for (int j = 0; j <= m; j++) {
                    if (used[j]) { u[p[j]] += delta; v[j] -= delta; }
                    else { minv[j] -= delta; }
                }
                j0 = j1;
            } while (p[j0] != 0);
            do {
                int j1 = way[j0];
                p[j0] = p[j1];
                j0 = j1;
            } while (j0 != 0);
        }

        int[] assignment = new int[n];
        for (int j = 1; j <= m; j++) {
            if (p[j] != 0) assignment[p[j] - 1] = j - 1;
        }
        return assignment;
    }
}