package com.seleniumcamp.demo;

import com.seleniumcamp.demo.allure.KAllureTestRunner;
import com.seleniumcamp.demo.config.KConfig;
import com.seleniumcamp.demo.pages.teamcity.KTeamCityBuildConfigurationPage;
import com.seleniumcamp.demo.pages.teamcity.KTeamCityLoginPage;
import com.seleniumcamp.demo.rules.KBrowserHandlerRule;
import com.seleniumcamp.demo.rules.KLoggingRule;
import com.seleniumcamp.demo.utils.KBrowser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import ru.yandex.qatools.allure.annotations.Title;

import static com.seleniumcamp.demo.config.KConstantsKt.ADMIN;
import static com.seleniumcamp.demo.config.KConstantsKt.BUILD_CONFIGURATION_ID;
import static com.seleniumcamp.demo.config.KConstantsKt.BUILD_STATUS;

@RunWith(KAllureTestRunner.class)
@Title("Test in Java, Other Stuff in Kotlin")
public class JavaKotlinTest {

    private final KBrowser browser = new KBrowser();

    @Rule
    public RuleChain rules = RuleChain.emptyRuleChain()
            .around(new KBrowserHandlerRule(browser))
            .around(new KLoggingRule(browser, false));

    @Title("Verify build run")
    @Test
    public void verifyBuildRun() {
        browser.navigateTo(KTeamCityLoginPage::new)
                .login(ADMIN, ADMIN)
                .waitForBuildAgent();
        final int expectedBuildNumber = browser.navigateTo(KTeamCityBuildConfigurationPage::new, BUILD_CONFIGURATION_ID)
                .getLatestBuildNumber() + 1;
        browser.navigateTo(KTeamCityBuildConfigurationPage::new, BUILD_CONFIGURATION_ID)
                .runNewBuild()
                .waitForRunningBuildToFinish()
                .verifyLatestBuild(expectedBuildNumber, BUILD_STATUS,
                        String.format("%s/viewLog.html?buildId=%d&tab=buildResultsDiv&buildTypeId=%s",
                                KConfig.INSTANCE.getTeamcityBaseURL(), expectedBuildNumber, BUILD_CONFIGURATION_ID));
    }
}
