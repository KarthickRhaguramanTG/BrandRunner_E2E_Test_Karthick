package ai.metayb.api.fwm.reports;

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
 * "03. FWM / Reports - Sales" - all 3 Postman requests, all GET.
 *
 * workflowId=508 is a real, pre-existing workflow used throughout this folder.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Reports - Sales")
public class ReportsSalesApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Activity Lists returns standard activities with sales workflow details")
    @Story("Get Activity Lists")
    @Description("Verified live: HTTP 200, data.details is a non-empty array with standardActivityId/workflowDetails.")
    public void getActivityListsReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/sales-report/activity-lists");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.details").isEmpty(), "data.details should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get View Sales Info by Workflow ID returns sales report details for a real workflow")
    @Story("Get View Sales Info by Workflow ID")
    @Description("Verified live: HTTP 200 for workflowId=508, 'Sales report details fetched successfully', data present.")
    public void getViewSalesInfoByWorkflowIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/sales-report/view-sales-info/508");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Sales report details fetched successfully");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get View Report by Workflow ID / Stock Invoice ID is denied for a user without sales-report access")
    @Story("Get View Report by Workflow ID / Stock Invoice ID")
    @Description("Verified live: HTTP 400, message 'Access denied: You do not have permission to access this " +
            "sales report.' for workflowId=508, stockInvoiceId=1 - a real authorization finding (same class as " +
            "Reports - Bank Details), not a bug. No positive scenario is automated since no differently-" +
            "permissioned test account is available.")
    public void getViewReportIsDeniedForUnauthorizedUser() {
        Response response = given().spec(requestSpecification).get("/web/sales-report/view-report/508/1");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Access denied: You do not have permission to access this sales report.");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Activity Lists without authentication fails")
    @Story("Get Activity Lists")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getActivityListsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/sales-report/activity-lists");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
