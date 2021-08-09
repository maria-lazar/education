package org.example.steps.serenity;

import net.thucydides.core.annotations.Step;
import org.example.pages.AddPage;
import org.example.pages.LoginPage;
import org.junit.Assert;

public class AddSteps {
    AddPage addPage;

    @Step
    public void clickSaveButton() {
        addPage.clickSaveButton();
    }

    @Step
    public void inputName(String name) {
        addPage.typeName(name);
    }

    @Step
    public void add(String name) {
        inputName(name);
        clickSaveButton();
    }

    @Step
    public void checkAddPage() {
        Assert.assertTrue("Title not visible", addPage.isTitleVisible());
    }
}
