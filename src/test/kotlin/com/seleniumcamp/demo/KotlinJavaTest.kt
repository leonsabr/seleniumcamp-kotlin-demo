package com.seleniumcamp.demo

import com.seleniumcamp.demo.allure.JAllureTestRunner
import com.seleniumcamp.demo.config.JConfig
import com.seleniumcamp.demo.config.JConstants.ADMIN
import com.seleniumcamp.demo.config.JConstants.BUILD_CONFIGURATION_ID
import com.seleniumcamp.demo.config.JConstants.BUILD_STATUS
import com.seleniumcamp.demo.pages.teamcity.JTeamCityBuildConfigurationPage
import com.seleniumcamp.demo.pages.teamcity.JTeamCityLoginPage
import com.seleniumcamp.demo.rules.JBrowserHandlerRule
import com.seleniumcamp.demo.rules.JLoggingRule
import com.seleniumcamp.demo.utils.JBrowser
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import ru.yandex.qatools.allure.annotations.Title

@RunWith(JAllureTestRunner::class)
@Title("Test in Kotlin, Other Stuff in Java")
class KotlinJavaTest {

    private val browser = JBrowser()

    @Rule @JvmField
    val rules: RuleChain = RuleChain.emptyRuleChain()
            .around(JBrowserHandlerRule(browser))
            .around(JLoggingRule(browser))

    @Test fun `Verify build run`() {
        browser.navigateTo(JTeamCityLoginPage(browser.driver))
                .login(ADMIN, ADMIN)
                .waitForBuildAgent()
        val expectedBuildNumber = browser.navigateTo(JTeamCityBuildConfigurationPage(browser.driver), BUILD_CONFIGURATION_ID)
                .getLatestBuildNumber() + 1
        browser.navigateTo(JTeamCityBuildConfigurationPage(browser.driver), BUILD_CONFIGURATION_ID)
                .runNewBuild()
                .waitForRunningBuildToFinish()
                .verifyLatestBuild(expectedBuildNumber, BUILD_STATUS,
                        "${JConfig.INSTANCE.teamcityBaseURL}/viewLog.html?buildId=$expectedBuildNumber&tab=buildResultsDiv&buildTypeId=$BUILD_CONFIGURATION_ID")
    }
}
