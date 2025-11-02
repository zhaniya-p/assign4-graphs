package util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataGenerator {
    private final Random rnd = new Random(12345);

    public void generateAll() throws IOException {
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdir();
        ObjectMapper om = new ObjectMapper();

        genVariants(3, 6, 10, "small", om);
        genVariants(3, 10, 20, "medium", om);
        genVariants(3, 20, 50, "large", om);
    }

    private void genVariants(int count, int minN, int maxN, String prefix, ObjectMapper om) throws IOException {
        for (int i = 0; i < count; i++) {
            int n = minN + rnd.nextInt(Math.max(1, maxN - minN + 1));
            double density;
            if (i == 0) density = 0.15;
            else if (i == 1) density = 0.40;
            else density = 0.75;

            List<Map<String, Object>> edges = new ArrayList<>();
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (u == v) continue;
                    if (rnd.nextDouble() < density) {
                        Map<String, Object> e = new LinkedHashMap<>();
                        e.put("from", u);
                        e.put("to", v);
                        e.put("w", Math.round((1 + rnd.nextDouble()*9) * 100.0) / 100.0);
                        edges.add(e);
                    }
                }
            }

            // Ensure at least one cycle in some datasets: connect a small cycle randomly for some variants
            if (rnd.nextDouble() < 0.7 && n >= 3) {
                int a = rnd.nextInt(n);
                int b = (a + 1) % n;
                int c = (a + 2) % n;
                Map<String, Object> e1 = new LinkedHashMap<>();
                e1.put("from", a); e1.put("to", b); e1.put("w", 1.0);
                Map<String, Object> e2 = new LinkedHashMap<>();
                e2.put("from", b); e2.put("to", c); e2.put("w", 1.0);
                Map<String, Object> e3 = new LinkedHashMap<>();
                e3.put("from", c); e3.put("to", a); e3.put("w", 1.0);
                edges.add(e1); edges.add(e2); edges.add(e3);
            }

            Map<String, Object> out = new LinkedHashMap<>();
            out.put("n", n);
            out.put("edges", edges);
            File f = new File("data/" + prefix + "_" + i + ".json");
            om.writerWithDefaultPrettyPrinter().writeValue(f, out);
            System.out.printf("Wrote %s n=%d edges=%d%n", f.getPath(), n, edges.size());
        }
    }
}
