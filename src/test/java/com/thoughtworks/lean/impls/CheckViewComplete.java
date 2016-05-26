package com.thoughtworks.lean.impls;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.thoughtworks.lean.utils.SharedDriver;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Map;

import static com.thoughtworks.lean.utils.ConstantCssSelector.VIEW_TITLE;
import static org.junit.Assert.*;

/**
 * Created by qmxie on 5/26/16.
 */
public class CheckViewComplete {
    private WebDriver driver;
    private Map<String, String> sideBarTitileToPageId = Maps.newHashMap();

    public CheckViewComplete() {
        driver = new SharedDriver();
        this.sideBarTitileToPageId.put("流水线","#pipeline-dashboard");
        this.sideBarTitileToPageId.put("流水线模版","#pipeline-templates");
        this.sideBarTitileToPageId.put("构建监控","#pipeline-monitor");
    }

    private void WaitForPresence(int seconds, String cssselector) {

        new WebDriverWait(driver, seconds)
                .until(ExpectedConditions.presenceOfElementLocated(this.CssSelect(cssselector)));
    }

    private void WaitForTextToBe(int seconds, String cssselector, String text) {
        new WebDriverWait(driver, seconds)
                .until(ExpectedConditions.textToBe(this.CssSelect(cssselector), text));
    }

    private By CssSelect(String cssselector) {
        return By.cssSelector(cssselector);
    }

    private WebElement getElement(String cssSelector) {
        return driver.findElement(By.cssSelector(cssSelector));
    }

    private List<WebElement> getElements(String cssSelector) {
        return driver.findElements(By.cssSelector(cssSelector));
    }

    private void navigateTo(String path){
        driver.navigate().to(path);
    }


    @Given("^login as admin$")
    public void loginAsAdmin() {
        driver.navigate().to("http://deliflow-server:9900/login");
        driver.findElement(By.cssSelector("input[name=username]")).sendKeys("admin@localhost");
        driver.findElement(By.cssSelector("input[name=password]")).sendKeys("admin");
        driver.findElement(By.cssSelector("div .login--field.right")).submit();
        assertTrue(driver.getTitle().contains("DeliFlow"));
    }

    @Then("^There should be more than (\\d+) of \\[(.*)\\]$")
    public void checkComponentNumberMoreThan(int number, String cssSelector) {
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector(cssSelector), number));
    }

    @Then("^There should be (\\d+) of \\[(.*)\\]$")
    public void checkComponentNumberEquals(int number, String cssSelector) {
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.numberOfElementsToBe(
                        By.cssSelector(cssSelector), number));
    }

    @Then("^choose team (\\S*)$")
    public void chooseTeam(String teamName) throws Throwable {
        this.getElement("ul.card--flow li a[title=\""+ teamName +"\"]").click();
        this.WaitForTextToBe(5, VIEW_TITLE, teamName);
    }


    @When("^click sidebar with title (\\S*)$")
    public void clickSidebar(String sideBarTitle) throws Throwable {
        String pageCssSelector = this.sideBarTitileToPageId.get(sideBarTitle);
        this.getElement(".subnav__list-item a[title='" + sideBarTitle + "']").click();
        this.WaitForPresence(10, pageCssSelector);
    }

    @Then("^All \\[(.*)\\] should not be empty$")
    public void ContentShouldNotBeEmpty(String cssSelector) throws Throwable {
        for (WebElement ele: this.getElements(cssSelector)){
            assertFalse(Strings.isNullOrEmpty(ele.getText()));
        }
    }

    @When("^click pipeline (\\S+)$")
    public void choosePipeline(String pipelineName) throws Throwable {
        this.getElement(".pipeline-group__item_title[title='"+pipelineName+"']>a").click();
        this.WaitForTextToBe(15, VIEW_TITLE, pipelineName);
    }

    @Then("^Click every navTabs should nav to proper view$")
    public void allClickShouldNavToPoperView() throws Throwable {
        for (WebElement ele: this.getElements(".nav__tabs li a")){
            String tabName = ele.getText();
            ele.click();
            WaitForTextToBe(5, ".nav__tabs .active a", tabName);
            assertNotNull(this.getElement(VIEW_TITLE));
        }
    }
}
