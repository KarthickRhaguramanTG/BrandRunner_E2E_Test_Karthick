package ai.metayb.api.auth_session.profile;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * POST /web/profile/switch - "00. Auth & Session / Switch Profile / Switch Profile".
 *
 * NOT AUTOMATED in this QA environment. Verified live with every header/body
 * combination from the Postman request (valid csrf + valid targetUserId, missing
 * csrf, non-existent targetUserId, missing targetUserId): every call returns HTTP
 * 200 with an HTML SPA page (Content-Type: text/html, Server: AmazonS3, "x-cache:
 * Error from cloudfront"), not a JSON API response. That is CloudFront serving its
 * static-site fallback for what looks like an origin/routing error on this specific
 * path - an environment/infrastructure gap, not a framework or test-code issue
 * (the sibling endpoints in the same folder - Get My Roles, List Switchable Users,
 * Get Current Session - all return correct JSON from the same base URL).
 *
 * Per Step 8's explicit instruction ("If a negative scenario cannot be safely
 * determined, document it instead of inventing"), this is left disabled rather than
 * asserting against what would effectively be a test of the CDN's error page. Once
 * this route is confirmed reachable in QA, remove @Test(enabled = false) and add
 * real assertions against the actual JSON contract.
 */
@Epic("BrandRunners Web API")
@Feature("Profile")
public class SwitchProfileApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: /web/profile/switch returns the CloudFront/S3 SPA fallback, not JSON, in QA")
    @Story("Switch Profile")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void switchProfileNotAutomatedDueToEnvironmentIssue() {
        // Intentionally left unimplemented - see class Javadoc for the verified root cause.
    }
}
