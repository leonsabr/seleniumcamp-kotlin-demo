package com.seleniumcamp.demo

import com.seleniumcamp.demo.allure.KAllureTestRunner
import com.seleniumcamp.demo.config.ADMIN
import com.seleniumcamp.demo.config.BUILD_CONFIGURATION_ID
import com.seleniumcamp.demo.config.BUILD_STATUS
import com.seleniumcamp.demo.config.KConfig
import com.seleniumcamp.demo.pages.teamcity.KTeamCityBuildConfigurationPage
import com.seleniumcamp.demo.pages.teamcity.KTeamCityLoginPage
import com.seleniumcamp.demo.rules.KBrowserHandlerRule
import com.seleniumcamp.demo.rules.KLoggingRule
import com.seleniumcamp.demo.utils.KBrowser
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import ru.yandex.qatools.allure.annotations.Title

@RunWith(KAllureTestRunner::class)
@Title("Test in Kotlin, Other Stuff in Kotlin")
class KotlinKotlinTest {

    private val browser = KBrowser()

    @Rule @JvmField
    val rules: RuleChain = RuleChain.emptyRuleChain()
            .around(KBrowserHandlerRule(browser))
            .around(KLoggingRule(browser))

    @Test fun `Verify build run`() {
        browser.navigateTo(::KTeamCityLoginPage)
                .login(ADMIN, ADMIN)
                .waitForBuildAgent()
        val expectedBuildNumber = browser.navigateTo(::KTeamCityBuildConfigurationPage, BUILD_CONFIGURATION_ID)
                .getLatestBuildNumber() + 1
        browser.navigateTo(::KTeamCityBuildConfigurationPage, BUILD_CONFIGURATION_ID)
                .runNewBuild()
                .waitForRunningBuildToFinish()
                .verifyLatestBuild(expectedBuildNumber, BUILD_STATUS,
                        "${KConfig.teamcityBaseURL}/viewLog.html?buildId=$expectedBuildNumber&tab=buildResultsDiv&buildTypeId=$BUILD_CONFIGURATION_ID")
    }
}
