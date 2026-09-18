package ai.metayb.api.anp.activities;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Activities" - ALL write operations, covering 27 Postman requests across
 * 4 distinct routes:
 *   - Update Status by ID: "Activate", "Deactivate" (PATCH /web/activity/:id/status) - 2 variants
 *   - Section Fields: product/branding/assets/man_power/property/vendor/rent/currency/
 *     text/number/dropdown/mobile_number/attachment/date_picker/vehicle
 *     (POST /web/activity/section-fields) - 15 variants
 *   - Activity Details: basic-new/basic-existing/campaign-existing/approval-existing
 *     (POST /web/activity/activity-details) - 4 variants
 *   - Create Activity: 6 variants (POST /web/activity/create-activity)
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause confirmed across every folder/sub-area worked on so far. Verified live for
 * "Update Status by ID" (Activate, activityId=16) and "Section Fields" (product)
 * directly - both returned HTML instead of JSON. The other 2 routes are documented on
 * that same evidence, per the "spot-check per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Activities")
public class ActivityWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: PATCH /web/activity/:id/status (Activate/Deactivate) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Status by ID")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void updateStatusByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/activity/section-fields (14 field-type variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Section Fields")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void sectionFieldsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/activity/activity-details (4 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Activity Details")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void activityDetailsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/activity/create-activity (6 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Activity")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void createActivityNotAutomatedDueToEnvironmentIssue() {
    }
}
