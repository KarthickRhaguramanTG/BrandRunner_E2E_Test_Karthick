package ai.metayb.api.masterdata.products;

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
 * "05. Master Data / Products" - read requests: List Products, Get Details by ID,
 * Get Fabric Connection Check.
 *
 * id=1770 ("Product five") is a real, pre-existing product in this QA tenant,
 * discovered via List Products, not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("Master Data - Products")
public class ProductsApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "List Products returns existing products")
    @Story("List Products")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, businessUnitId, name, productCode}.")
    public void listProductsReturnsProducts() {
        Response response = given().spec(requestSpecification).get("/web/product");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "smoke", "positive"}, description = "Get Details by ID returns a real product's details")
    @Story("Get Details by ID")
    @Description("Verified live: HTTP 200 for id=1770, data.name/data.productCode present.")
    public void getDetailsByIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/product/1770/details");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.id"), 1770);
        Assert.assertNotNull(response.jsonPath().getString("data.name"));
    }

    @Test(groups = { "regression", "negative"}, description = "Get Details by ID with a non-existent id fails")
    @Story("Get Details by ID")
    @Description("Verified live: HTTP 400, message 'Product not found'.")
    public void getDetailsByIdWithNonExistentIdFails() {
        Response response = given().spec(requestSpecification).get("/web/product/999999/details");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Product not found");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Fabric Connection Check returns the fabric integration status")
    @Story("Get Fabric Connection Check")
    @Description("Verified live: HTTP 200, 'Connection status fetched successfully', data.type/data.connection present.")
    public void getFabricConnectionCheckReturnsStatus() {
        Response response = given().spec(requestSpecification).get("/web/product/fabric-connection-check");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("data.type"));
        Assert.assertNotNull(response.jsonPath().get("data.connection"));
    }

    @Test(groups = { "regression", "negative"}, description = "List Products without authentication fails")
    @Story("List Products")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listProductsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/product");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
