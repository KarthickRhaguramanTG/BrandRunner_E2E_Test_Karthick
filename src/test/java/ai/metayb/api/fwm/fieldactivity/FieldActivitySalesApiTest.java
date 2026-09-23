package ai.metayb.api.fwm.fieldactivity;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

import static io.restassured.RestAssured.given;

/**
 * "03. FWM / Field Activity" - sales/reconciliation summary reads for a real
 * activity (id=16): Get Sales Summary, Get Distributor Sales Summary, Get User
 * Sales Performance, Get Date Wise Sku Sales Summary, Get Reconciliation Details,
 * Get Team Daily Sales Performance.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Field Activity")
public class FieldActivitySalesApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Sales Summary by ID returns sales performance for a real activity")
    @Story("Get Sales Summary by ID")
    @Description("Verified live: HTTP 200 for id=16, data.budgetId matches, data.salesPerformance present.")
    public void getSalesSummaryByIdReturnsPerformance() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/sales_summary");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 16);
        Assert.assertNotNull(response.jsonPath().get("data.salesPerformance"));
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Distributor Sales Summary by ID returns distributor sales data for a real activity")
    @Story("Get Distributor Sales Summary by ID")
    @Description("Verified live: HTTP 200 for id=16, data.budgetId matches.")
    public void getDistributorSalesSummaryByIdReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/distributor_sales_summary");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 16);
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get User Sales Performance by ID returns user-wise sales performance for a real activity")
    @Story("Get User Sales Performance by ID")
    @Description("Verified live: HTTP 200 for id=16, 'User-wise sales performance...', data.budgetId matches.")
    public void getUserSalesPerformanceByIdReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/user_sales_performance");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 16);
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Date Wise Sku Sales Summary by ID returns date-wise SKU sales for a real activity")
    @Story("Get Date Wise Sku Sales Summary by ID")
    @Description("Verified live: HTTP 200 for id=16, data.budgetId matches.")
    public void getDateWiseSkuSalesSummaryByIdReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/date_wise_sku_sales_summary");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 16);
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Reconciliation Details by ID returns reconciliation rows for a real activity")
    @Story("Get Reconciliation Details by ID")
    @Description("Verified live: HTTP 200 for id=16, 'Reconciliation details fetched successfully', data.rows present.")
    public void getReconciliationDetailsByIdReturnsRows() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/reconciliation_details");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data.rows"), "data.rows should be a list");
    }

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Team Daily Sales Performance by ID returns team-wise daily performance for a real activity")
    @Story("Get Team Daily Sales Performance by ID")
    @Description("Verified live: HTTP 200 for id=16, data.budgetId matches.")
    public void getTeamDailySalesPerformanceByIdReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/field-activity/16/team_daily_sales_performance");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.budgetId"), 16);
    }

    @Test(groups = { "regression", "negative"}, description = "Get Sales Summary by ID without authentication fails")
    @Story("Get Sales Summary by ID")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getSalesSummaryByIdWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/field-activity/16/sales_summary");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
