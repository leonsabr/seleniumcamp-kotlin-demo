package com.seleniumcamp.demo;

import com.seleniumcamp.demo.allure.JAllureTestRunner;
import com.seleniumcamp.demo.config.JConfig;
import com.seleniumcamp.demo.pages.teamcity.JTeamCityBuildConfigurationPage;
import com.seleniumcamp.demo.pages.teamcity.JTeamCityLoginPage;
import com.seleniumcamp.demo.rules.JBrowserHandlerRule;
import com.seleniumcamp.demo.rules.JLoggingRule;
import com.seleniumcamp.demo.utils.JBrowser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import ru.yandex.qatools.allure.annotations.Title;

import static com.seleniumcamp.demo.config.JConstants.ADMIN;
import static com.seleniumcamp.demo.config.JConstants.BUILD_CONFIGURATION_ID;
import static com.seleniumcamp.demo.config.JConstants.BUILD_STATUS;

@RunWith(JAllureTestRunner.class)
@Title("Test in Java, Other Stuff in Java")
public class JavaJavaTest {

    private final JBrowser browser = new JBrowser();

    @Rule
    public RuleChain rules = RuleChain.emptyRuleChain()
            .around(new JBrowserHandlerRule(browser))
            .around(new JLoggingRule(browser));

    @Title("Verify build run")
    @Test
    public void verifyBuildRun() {
        browser.navigateTo(JTeamCityLoginPage.class)
                .login(ADMIN, ADMIN)
                .waitForBuildAgent();
        final int expectedBuildNumber = browser.navigateTo(JTeamCityBuildConfigurationPage.class, BUILD_CONFIGURATION_ID)
                .getLatestBuildNumber() + 1;
        browser.navigateTo(JTeamCityBuildConfigurationPage.class, BUILD_CONFIGURATION_ID)
                .runNewBuild()
                .waitForRunningBuildToFinish()
                .verifyLatestBuild(expectedBuildNumber, BUILD_STATUS,
                        String.format("%s/viewLog.html?buildId=%d&tab=buildResultsDiv&buildTypeId=%s",
                                JConfig.INSTANCE.getTeamcityBaseURL(), expectedBuildNumber, BUILD_CONFIGURATION_ID));
    }
}
