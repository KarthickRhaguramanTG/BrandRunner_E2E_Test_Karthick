package ai.metayb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformanceRecorder {
    private final Map<String, List<Long>> metrics = new HashMap<>();

    public void record(String endpoint, long responseTime) {
        metrics.computeIfAbsent(endpoint, k -> new ArrayList<>()).add(responseTime);
    }

    public void generateReport() {
        System.out.println("\n=== Performance Report ===");
        metrics.forEach((endpoint, times) -> {
            double avg = times.stream().mapToLong(Long::longValue).average().orElse(0);
            long min = times.stream().mapToLong(Long::longValue).min().orElse(0);
            long max = times.stream().mapToLong(Long::longValue).max().orElse(0);

            System.out.printf("%s - Avg: %.2fms, Min: %dms, Max: %dms, Samples: %d%n",
                    endpoint, avg, min, max, times.size());
        });
    }

    public void clear() {
        metrics.clear();
    }
}