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
 * "03. FWM / Reports - Bank Details" - all 3 Postman requests, all GET.
 *
 * All 3 are automated as NEGATIVE-ONLY (permission-denied): the logged-in QA account
 * (Super Admin) is verified live to lack the USER_BANK_DETAILS_REPORT permission, so
 * every call returns HTTP 400 "Permission denied: USER_BANK_DETAILS_REPORT" - a real
 * authorization finding (Section 7's "user without required permission" scenario),
 * not a bug. No positive scenario is automated since no differently-permissioned
 * test account is available in this environment.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Reports - Bank Details")
public class ReportsBankDetailsApiTest extends BaseApiTest {

    @Test(groups = { "regression", "negative"}, description = "Get Report Details is denied for a user without the bank-details-report permission")
    @Story("Get Report Details")
    @Description("Verified live: HTTP 400, message 'Permission denied: USER_BANK_DETAILS_REPORT'.")
    public void getReportDetailsIsDeniedForUnauthorizedUser() {
        Response response = given().spec(requestSpecification).get("/web/bank-details-report/report-details?page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Permission denied: USER_BANK_DETAILS_REPORT");
    }

    @Test(groups = { "regression", "negative"}, description = "Get User Listing is denied for a user without the bank-details-report permission")
    @Story("Get User Listing")
    @Description("Verified live: HTTP 400, message 'Permission denied: USER_BANK_DETAILS_REPORT' - same as Get Report Details.")
    public void getUserListingIsDeniedForUnauthorizedUser() {
        Response response = given().spec(requestSpecification).get("/web/bank-details-report/user-listing");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Permission denied: USER_BANK_DETAILS_REPORT");
    }

    @Test(groups = { "regression", "negative"}, description = "Get User Bank Details is denied for a user without the bank-details-report permission")
    @Story("Get User Bank Details")
    @Description("Verified live: HTTP 400, message 'Permission denied: USER_BANK_DETAILS_REPORT' - same as Get Report Details.")
    public void getUserBankDetailsIsDeniedForUnauthorizedUser() {
        Response response = given().spec(requestSpecification).get("/web/bank-details-report/user-bank-details");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Permission denied: USER_BANK_DETAILS_REPORT");
    }
}
