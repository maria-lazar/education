package org.example.features.search;


import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.example.steps.serenity.ListSteps;
import org.example.steps.serenity.LoginSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/loginData.csv")
public class LoginTestSuccessful {

    @Managed
    private WebDriver webdriver;
    @Steps
    private LoginSteps loginSteps;
    @Steps
    private ListSteps listSteps;
    private String username, password;

    @Test
    public void loginTestSuccessful() {
        loginSteps.login(username, password);
        listSteps.checkYouAreLoggedIn();
    }
}
