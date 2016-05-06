package com.thoughtworks.lean.impls;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;

/**
 * Created by qmxie on 5/5/16.
 */
public class UserTeamViewTest {
    WebDriver driver = null;
    private String teamName;

    private WebElement getE(String cssSelector){
        return driver.findElement(By.cssSelector(cssSelector));
    }

    @Given("^我以admin登陆$")
    public void login_as_admin() {
        driver = new ChromeDriver();
        driver.navigate().to("http://121.42.193.129:9900/login");
        this.getE("[name=username]").sendKeys("admin@localhost");
        this.getE("[name=password]").sendKeys("admin");
        this.getE("div .login--field.right").submit();
        assertTrue(driver.getTitle().contains("DeliFlow"));
    }


    @Given("^如果我在某团队里面$")
    public void there_should_be_more_than_one_team() throws Throwable {
        int teams = this.getE("ul.card--flow").findElements(By.cssSelector("li")).size() - 1;
        assertTrue(teams > 0);
    }

    @When("^我点击某团队时$")
    public void click_the_first_team() throws Throwable {
        teamName = this.getE("ul.card--flow li a").getText();
        this.getE("ul.card--flow li a").click();
        WebDriverWait wait = new WebDriverWait(driver,2);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.top_nav__list-item a")));
    }

    @Then("^我会进到该团队的页面$")
    public void enter_team_view() throws Throwable {
        assertEquals(teamName, this.getE("h3.header__title").getText());
    }

    @When("^我点击了 新的团队 按钮$")
    public void creat_new_team() throws Throwable {
        this.getE("#add-team").click();
    }

    @Then("^我能看到新建团队的页面$")
    public void see_create_team_view() throws Throwable {
        assertNotNull(this.getE("#add-team div input"));
    }

}
