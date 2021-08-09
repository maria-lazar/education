package org.example.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

public class AddPage extends PageObject {
    @FindBy(css = "#name > input")
    private WebElement nameInput;

    @FindBy(id = "saveBtn")
    private WebElement saveButton;

    @FindBy(id = "title")
    private WebElement addTitle;

    public void typeName(String name) {
        nameInput.sendKeys(name);
    }

    public void clickSaveButton() {
        saveButton.click();
    }

    public boolean isTitleVisible() {
        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        return addTitle.getText().equals("Add");
    }
}
