package ai.metayb.api.controlsettings.appsettings;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * POST /web/settings/app-settings - "01. Control Settings / App Settings / Save App Settings"
 * (5 Postman variants: Campaign tab save COMPLETE/PARTIAL, Geofencing tab save
 * ACTIVITY_WISE/GLOBALLY_MANDATORY/GLOBALLY_OPTIONAL).
 *
 * NOT AUTOMATED in this QA environment - same root cause already documented for
 * "00. Auth & Session / Switch Profile / Switch Profile": every call to this endpoint
 * returns HTTP 200 with the CloudFront/S3 SPA fallback page (Content-Type: text/html),
 * not JSON, regardless of headers or body. Verified live with:
 *   - the exact csrf token from a fresh login (ruling out a missing/stale csrf token)
 *   - a realistic, non-empty body (ruling out an empty-body artifact)
 *   - an unauthenticated call (same result - so this is not an auth-layer issue)
 * A parallel diagnostic against sibling endpoints in this SAME folder (Create Business
 * Unit, Create Designation) showed the identical symptom, while every GET endpoint
 * tested returns correct JSON. This points to a systemic environment/infra gap
 * affecting POST/PUT/DELETE routes outside "/web/auth/*" in this QA deployment,
 * not something specific to this one endpoint or fixable in test code.
 *
 * Per Step 8, left disabled rather than asserting against the CDN's error page.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - App Settings")
public class SaveAppSettingsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/settings/app-settings returns the CloudFront/S3 SPA fallback, not JSON, in QA")
    @Story("Save App Settings")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void saveAppSettingsNotAutomatedDueToEnvironmentIssue() {
        // Intentionally left unimplemented - see class Javadoc for the verified root cause.
    }
}
