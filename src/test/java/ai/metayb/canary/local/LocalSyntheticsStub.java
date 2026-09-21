package ai.metayb.canary.local;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.synthetics.StepOptions;
import software.amazon.synthetics.Synthetics;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * A minimal stand-in for the real Synthetics runtime, used only by
 * LocalCanaryRunner. The real implementation (S3 artifact upload, step
 * report generation, CloudWatch metrics) only exists inside the managed
 * Synthetics Lambda execution environment - this stub exists purely so
 * ApiHealthCanary's actual logic (config, login, all six steps, sequencing,
 * exception propagation) can be exercised locally without AWS infrastructure.
 *
 * It runs each step's Callable synchronously and logs step name, pass/fail,
 * and duration - enough to validate everything Section 10 asks for. It does
 * NOT implement step-level continueOnStepFailure from synthetics.json (a
 * failing step's exception always propagates immediately, matching this
 * project's synthetics.json which sets continueOnStepFailure to false anyway).
 *
 * Test-scope only, deliberately - this and LocalCanaryRunner must never end
 * up in the deployed Canary artifact, and src/test/java classes are never
 * included when Maven packages the main jar.
 */
class LocalSyntheticsStub implements Synthetics {

    private static final Logger logger = LogManager.getLogger(LocalSyntheticsStub.class);

    @Override
    public <T> CompletableFuture<T> executeStep(String stepName, Callable<T> task) throws Exception {
        return executeStep(stepName, task, null);
    }

    @Override
    public <T> CompletableFuture<T> executeStep(String stepName, Callable<T> task, StepOptions stepOptions) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("=== [local] step '{}' starting ===", stepName);
        try {
            T result = task.call();
            long durationMs = System.currentTimeMillis() - start;
            logger.info("=== [local] step '{}' PASSED ({} ms) ===", stepName, durationMs);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            long durationMs = System.currentTimeMillis() - start;
            logger.error("=== [local] step '{}' FAILED ({} ms): {} ===", stepName, durationMs, e.getMessage());
            CompletableFuture<T> failed = new CompletableFuture<>();
            failed.completeExceptionally(e);
            return failed;
        }
    }
}
