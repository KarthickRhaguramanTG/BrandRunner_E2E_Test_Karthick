package ai.metayb.api.masterdata.products;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "05. Master Data / Products" - ALL write operations, covering 8 Postman requests
 * across 8 distinct routes: Create Product, Update Product, Edit Price by Product
 * ID, Edit UOM Price by Product ID, Bulk, Measurement Price by Product ID,
 * Distributors By Brand, Distributor Stocks By Brand.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause confirmed across every folder worked on so far (00/01/02/03). Verified
 * live for "Create Product", "Update Product", "Edit Price by Product ID", "Bulk",
 * and "Distributors By Brand" directly - all returned HTML instead of JSON. The
 * remaining routes are documented on that same evidence.
 */
@Epic("BrandRunners Web API")
@Feature("Master Data - Products")
public class ProductsWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/product returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Product")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createProductNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PUT /web/product/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Product")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void updateProductNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/product/:productId/edit-price returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Edit Price by Product ID")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void editPriceByProductIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/product/:productId/edit-uom-price returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Edit UOM Price by Product ID")
    @Description("Blocked by environment - same route family. Not executed.")
    public void editUomPriceByProductIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/product/bulk returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Bulk")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void bulkNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/product/:productId/measurement-price returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Measurement Price by Product ID")
    @Description("Blocked by environment - same route family. Not executed.")
    public void measurementPriceByProductIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/product/distributors-by-brand returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Distributors By Brand")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void distributorsByBrandNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/product/distributor-stocks-by-brand returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Distributor Stocks By Brand")
    @Description("Blocked by environment - same route family. Not executed.")
    public void distributorStocksByBrandNotAutomatedDueToEnvironmentIssue() {
    }
}
