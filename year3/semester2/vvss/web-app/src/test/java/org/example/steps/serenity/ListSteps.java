package org.example.steps.serenity;

import net.thucydides.core.annotations.Step;
import org.example.pages.ListPage;
import org.junit.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ListSteps {
    ListPage listPage;

    @Step
    public void checkYouAreLoggedIn() {
        Assert.assertTrue("You are not logged in", listPage.isTitleVisible());
    }

    @Step
    public void checkAddedItem(String name) {
        assertThat(listPage.getItems(), hasItem(name));
    }

    @Step
    public void checkDeletedItem(String name) {
        assertThat(listPage.getItems(), not(hasItem(name)));
    }

    @Step
    public void navigateToAddPage() {
        clickAddButton();
    }

    @Step
    public void delete(String name) {
        clickDeleteButton(name);
    }

    @Step
    public void clickAddButton() {
        listPage.clickAddButton();
    }

    @Step
    public void clickDeleteButton(String name) {
        listPage.clickDeleteButtonOf(name);
    }

    @Step
    public void logout() {
        clickLogoutButton();
    }

    @Step
    public void clickLogoutButton() {
        listPage.clickLogoutButton();
    }
}
