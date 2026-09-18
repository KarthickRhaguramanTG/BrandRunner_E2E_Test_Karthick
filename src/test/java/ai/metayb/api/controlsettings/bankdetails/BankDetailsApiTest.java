package ai.metayb.api.controlsettings.bankdetails;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "01. Control Settings / Bank Details" - Set Active Bank Account
 * (POST /web/bankdetails/set-active-bank-account), Reveal Bank Account
 * (POST /web/bankdetails/reveal-bank-account).
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause confirmed across every other folder/sub-area (every non-"/web/auth/*"
 * POST/PUT/DELETE route tested so far behaves this way). Verified live with the
 * exact bodies the Postman collection specifies - both returned HTML instead of JSON.
 *
 * Reveal Bank Account would expose real bank account details if it worked - per the
 * "create-then-cleanup" guidance for sensitive endpoints, this was verified with an
 * empty/example body only, and no attempt was made to actually retrieve real bank
 * details for any real user.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Bank Details")
public class BankDetailsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/bankdetails/set-active-bank-account returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Set Active Bank Account")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void setActiveBankAccountNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/bankdetails/reveal-bank-account returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Reveal Bank Account")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void revealBankAccountNotAutomatedDueToEnvironmentIssue() {
    }
}
