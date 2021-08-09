package org.example.steps.serenity;

import net.thucydides.core.annotations.Step;
import org.example.pages.LoginPage;
import org.junit.Assert;

public class LoginSteps {
    LoginPage loginPage;

    @Step
    public void clickLoginButton() {
        loginPage.clickLoginButton();
    }

    @Step
    public void inputPassword(String password) {
        loginPage.inputPassword(password);
    }

    @Step
    public void inputUser(String username) {
        loginPage.open();
        loginPage.typeUser(username);
    }

    @Step
    public void login(String username, String password) {
        inputUser(username);
        inputPassword(password);
        clickLoginButton();
    }

    @Step
    public void checkLogInError() {
        Assert.assertTrue("Error message not shown", loginPage.isErrorMessageVisible());
    }

    @Step
    public void checkYouAreLoggedOut() {
        Assert.assertTrue("Not logged out", loginPage.isTitleVisible());
    }
}
