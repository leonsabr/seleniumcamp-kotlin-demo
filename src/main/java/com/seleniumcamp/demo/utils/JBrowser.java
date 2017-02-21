package com.seleniumcamp.demo.utils;

import com.seleniumcamp.demo.config.JConfig;
import com.seleniumcamp.demo.pages.JPage;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Step;

import static com.seleniumcamp.demo.allure.JAttachments.attachScreenshot;
import static com.seleniumcamp.demo.utils.JWaiters.waitForPage;

public class JBrowser {

    private WebDriver driver;

    public void setDriver(final WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public <T extends JPage> T navigateTo(final T page, final String... params) {
        return loadPage(page, JConfig.INSTANCE.getTeamcityBaseURL() + String.format(page.getUrl(), (Object[]) params));
    }

    @Step("Open {1}")
    private <T extends JPage> T loadPage(final T page, final String url) {
        driver.get(url);
        waitForPage(page);
        attachScreenshot(page.toString(), driver);
        return page;
    }
}
