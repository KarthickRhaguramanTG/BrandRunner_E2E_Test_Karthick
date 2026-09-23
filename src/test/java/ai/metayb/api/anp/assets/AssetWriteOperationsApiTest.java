package ai.metayb.api.anp.assets;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Assets" - ALL write operations, covering 8 Postman requests across 8
 * distinct routes: Create, Update, Update Status by ID, Delete by ID, Update Bulk
 * Edit Standard Rates by Asset ID, Update Standard Rates by Asset Standard Rate ID,
 * Bulk Upload Template Location Wise, Bulk Upload Global Rates.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "Create" directly - returned HTML instead of JSON. The
 * other 7 routes are documented on that same evidence, per the "spot-check per
 * sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Assets")
public class AssetWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/assets/create returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createAssetNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: PUT /web/assets/update returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateAssetNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: PATCH /web/assets/status/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Status by ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateStatusByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: DELETE /web/assets/delete/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete by ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void deleteByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"regression"},
            description = "BLOCKED: PUT /web/assets/bulkEditStandardRates/:assetId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Bulk Edit Standard Rates by Asset ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateBulkEditStandardRatesNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: PUT /web/assets/standardRates/:assetStandardRateId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Standard Rates by Asset Standard Rate ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateStandardRatesNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"regression"},
            description = "BLOCKED: POST /web/assets/bulk-upload-template/location-wise returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Bulk Upload Template Location Wise")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void bulkUploadTemplateLocationWiseNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/assets/bulk-upload-global-rates returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Bulk Upload Global Rates")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void bulkUploadGlobalRatesNotAutomatedDueToEnvironmentIssue() {
    }
}
