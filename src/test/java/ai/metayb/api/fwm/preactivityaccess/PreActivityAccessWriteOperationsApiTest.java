package ai.metayb.api.fwm.preactivityaccess;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Pre-Activity Access" - write operations, covering 5 Postman requests
 * across 2 distinct routes:
 *   - Toggle Initiation Form: "Enable", "Disable" (POST /web/pre-activity/updateInitiationform) - 2
 *   - Update Mobile Permissions: "View + Create", "All permissions", "View only" (POST /web/pre-activity/updatePermissions) - 3
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "Toggle Initiation Form" (Enable) directly - returned HTML
 * instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Pre-Activity Access")
public class PreActivityAccessWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/pre-activity/updateInitiationform (2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Toggle Initiation Form")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void toggleInitiationFormNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/pre-activity/updatePermissions (3 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Mobile Permissions")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateMobilePermissionsNotAutomatedDueToEnvironmentIssue() {
    }
}
