package ai.metayb.api.controlsettings.businessunits;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "01. Control Settings / Business Units" - write requests: Create Business Unit
 * (POST /web/business-unit), Update Business Unit (PUT /web/business-unit/:id),
 * Delete Business Unit (DELETE /web/business-unit/:id).
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause confirmed across every other folder/sub-area. Verified live with a
 * realistic body and safe non-existent ids (999999) for Update/Delete - every call
 * returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Business Units")
public class BusinessUnitWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/business-unit returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Business Unit")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void createBusinessUnitNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: PUT /web/business-unit/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Business Unit")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void updateBusinessUnitNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: DELETE /web/business-unit/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Business Unit")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void deleteBusinessUnitNotAutomatedDueToEnvironmentIssue() {
    }
}
