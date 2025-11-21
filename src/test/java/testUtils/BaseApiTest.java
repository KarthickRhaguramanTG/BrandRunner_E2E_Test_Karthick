//package testUtils;
//
////import ai.metayb.config.ConfigManager;
////import ai.metayb.utils.PerformanceRecorder;
//import io.qameta.allure.testng.AllureTestNg;
//import io.restassured.builder.RequestSpecBuilder;
//import io.restassured.filter.log.RequestLoggingFilter;
//import io.restassured.filter.log.ResponseLoggingFilter;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeSuite;
//import org.testng.annotations.Listeners;
//
//import java.util.concurrent.TimeUnit;
//
//import static io.restassured.RestAssured.given;
//
//@Listeners({AllureTestNg.class})
//public class BaseApiTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(BaseApiTest.class);
//
//    protected static PerformanceRecorder perfRecorder = new PerformanceRecorder();
//    protected static String baseURI;
//    protected static String authToken;
//    protected static RequestSpecification       requestSpecification, noAuthRequestSpecification;
//
//    // Optional for parallel test support
//    // protected static ThreadLocal<RequestSpecification> requestSpecThreadLocal = new ThreadLocal<>();
//
//    @BeforeSuite
//    public void globalSetup() {
//        // Allow environment override
////        String env = System.getProperty("env", "qa");
//        baseURI = ConfigManager.getApiBaseUrl();
//        logger.info("Base URI initialized for environment: {}", baseURI);
//
//        Response loginResponse = given()
//                .baseUri(baseURI)
//                .contentType("application/json")
//                .body(getLoginPayload())
//                .post("/web/auth/login");
//
//        authToken = loginResponse.jsonPath().getString("token");
//        if (authToken == null || authToken.isEmpty()) {
//            throw new RuntimeException("Login failed. Full response:\n" + loginResponse.asString());
//        }
//
//        logger.info("Auth token acquired successfully." + loginResponse.asString());
//        logger.info("Auth Token: {}", authToken);
//
//        requestSpecification = new RequestSpecBuilder()
//                .setBaseUri(baseURI)
//                .addHeader("Authorization", "Bearer " + authToken)
////                .addHeader("Content-Type", "application/json")
//                .addFilter(new RequestLoggingFilter())
//                .addFilter(new ResponseLoggingFilter())
//                .build();
//
//        noAuthRequestSpecification = new RequestSpecBuilder()
//                .setBaseUri(baseURI)
////                .addHeader("Content-Type", "application/json")
//                .addFilter(new RequestLoggingFilter())
//                .addFilter(new ResponseLoggingFilter())
//                .build();
//
////        RestAssured.requestSpecification = requestSpecification;
//
//        // Optional: for parallel execution
//        // requestSpecThreadLocal.set(requestSpecification);
//    }
//
//    @AfterMethod
//    public void recordMetrics() {
//        perfRecorder.generateReport();
//    }
//
//    protected void recordResponseTime(long startTime, String endpoint) {
//        long responseTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
//        perfRecorder.record(endpoint, responseTime);
//    }
//
//    @Contract(pure = true)
//    private @NotNull String getLoginPayload() {
////        return "{\"EMAIL\": \"admin@tolaram.com\", \"PASSWORD\": \"admin\"}";
//        return "{\"EMAIL\": \"sam@tolaram.com\", \"PASSWORD\": \"admin\"}";
//
//    }
//
//    // Optional method to get spec in thread-safe manner
//    // protected RequestSpecification getRequestSpec() {
//    //     return requestSpecThreadLocal.get();
//    // }
//}
