package ai.metayb.api.masterdata.vehicles;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "05. Master Data / Vehicles" - ALL write operations, covering 4 Postman requests
 * across 3 distinct routes:
 *   - Update Status by ID: "Activate", "Deactivate" (PATCH /web/vehicle/:id/status) - 2 variants
 *   - Create (POST /web/vehicle/create)
 *   - Update by ID (PUT /web/vehicle/update/:id)
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause. Verified live for all 3 routes directly - all returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("Master Data - Vehicles")
public class VehiclesWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PATCH /web/vehicle/:id/status (Activate/Deactivate, 2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Status by ID")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void updateStatusByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/vehicle/create returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PUT /web/vehicle/update/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update by ID")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void updateByIdNotAutomatedDueToEnvironmentIssue() {
    }
}
