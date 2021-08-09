package org.example.features.search;


import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.example.steps.serenity.AddSteps;
import org.example.steps.serenity.ListSteps;
import org.example.steps.serenity.LoginSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class AddDeleteTest {

    @Managed
    private WebDriver webdriver;
    @Steps
    private LoginSteps loginSteps;
    @Steps
    private ListSteps listSteps;
    @Steps
    private AddSteps addSteps;

    @Test
    public void addDeleteTestSuccessful() {
        loginSteps.login("a", "p");
        listSteps.checkYouAreLoggedIn();

        listSteps.navigateToAddPage();
        addSteps.checkAddPage();
        addSteps.add("b");
        listSteps.checkAddedItem("b");

        listSteps.delete("b");
        listSteps.checkDeletedItem("b");

        listSteps.logout();
        loginSteps.checkYouAreLoggedOut();
    }
}
