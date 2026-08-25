package testUtils;

import ai.metayb.config.ConfigManager;
import ai.metayb.ui.core.DataReader;
import ai.metayb.utils.PerformanceRecorder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

@Listeners({AllureTestNg.class})
public class BaseApiTest {

    private static final Logger logger = LogManager.getLogger(BaseApiTest.class);

    protected static final PerformanceRecorder perfRecorder = new PerformanceRecorder();
    protected static String baseURI;
    protected static String authToken;
    protected static RequestSpecification requestSpecification;
    protected static RequestSpecification noAuthRequestSpecification;

    @BeforeSuite
    public void globalSetup() throws Exception {
        baseURI = ConfigManager.getApiBaseUrl();
        logger.info("Base URI initialized for environment: {}", baseURI);

        Response loginResponse = given()
                .baseUri(baseURI)
                .contentType("application/json")
                .body(getLoginPayload())
                .post("/web/auth/login");

        authToken = loginResponse.jsonPath().getString("token");
        if (authToken == null || authToken.isEmpty()) {
            throw new RuntimeException("Login failed. Full response:\n" + loginResponse.asString());
        }
        logger.info("Auth token acquired successfully.");

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .addHeader("Authorization", "Bearer " + authToken)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        noAuthRequestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    @AfterMethod
    public void recordMetrics() {
        perfRecorder.generateReport();
    }

    protected void recordResponseTime(long startTime, String endpoint) {
        long responseTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        perfRecorder.record(endpoint, responseTime);
    }

    private String getLoginPayload() throws Exception {
        DataReader credentials = new DataReader();
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("EMAIL", credentials.apiEmail);
        payload.put("PASSWORD", credentials.apiPassword);
        return new ObjectMapper().writeValueAsString(payload);
    }
}
