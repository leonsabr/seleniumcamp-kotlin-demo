package com.seleniumcamp.demo.utils

import com.seleniumcamp.demo.allure.attachScreenshot
import com.seleniumcamp.demo.config.KConfig
import com.seleniumcamp.demo.pages.KPage
import org.openqa.selenium.WebDriver
import ru.yandex.qatools.allure.annotations.Step

class KBrowser {

    lateinit var driver: WebDriver

    fun <T : KPage> navigateTo(page: T, vararg params: String): T {
        @Step("Open {0}") fun loadPage(url: String): T {
            this.driver.get(url)
            waitForPage(page)
            attachScreenshot("$page", driver)
            return page
        }

        return loadPage(KConfig.teamcityBaseURL + String.format(page.url, *params))
    }

    fun <T : KPage> navigateTo(factory: (driver: WebDriver) -> T, vararg params: String) = navigateTo(factory(this.driver), *params)
}
