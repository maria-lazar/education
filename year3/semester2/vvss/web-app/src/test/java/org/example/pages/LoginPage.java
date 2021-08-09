package org.example.pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

@DefaultUrl("http://localhost:8100/login")
public class LoginPage extends PageObject {
    @FindBy(css = "#username > input")
    private WebElement userInput;

    @FindBy(css = "#password > input")
    private WebElement passwordInput;

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    @FindBy(id = "title")
    private WebElement loginTitle;

    public void clickLoginButton() {
        loginButton.click();
    }

    public void inputPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void typeUser(String username) {
        userInput.sendKeys(username);
    }

    public boolean isErrorMessageVisible() {
        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        return getDriver().findElement(By.id("loginError")).getText().equals("Failed to authenticate");
    }

    public boolean isTitleVisible() {
        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        return loginTitle.getText().equals("Login");
    }
}
