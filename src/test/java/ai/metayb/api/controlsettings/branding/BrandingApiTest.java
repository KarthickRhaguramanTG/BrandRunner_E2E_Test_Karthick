package ai.metayb.api.controlsettings.branding;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "01. Control Settings / Branding" - Create Branding (POST /web/branding/list).
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause confirmed across every other folder/sub-area. Verified live with the
 * exact body the Postman collection specifies - returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Branding")
public class BrandingApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/branding/list returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Branding")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void createBrandingNotAutomatedDueToEnvironmentIssue() {
    }
}
