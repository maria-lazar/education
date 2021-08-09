package org.example.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ListPage extends PageObject {
    @FindBy(id = "title")
    private WebElement titleText;

    @FindBy(id = "addBtn")
    private WebElement addButton;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    public void clickAddButton() {
        addButton.click();
    }

    public void clickLogoutButton() {
        logoutButton.click();
    }

    public boolean isTitleVisible() {
        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        return titleText.getText().equals("Pet Adoption");
    }

    public void clickDeleteButtonOf(String name) {
        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        WebElementFacade itemList = find(By.id("itemList"));
        WebElement deleteButton = itemList.findElements(By.className("pet-div")).stream()
                .filter(p -> p.findElement(By.className("pet-name")).getText().equals(name))
                .map(p -> p.findElement(By.className("deleteBtn")))
                .findFirst()
                .get();
        deleteButton.click();
    }

    public List<String> getItems() {
        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        WebElementFacade itemList = find(By.id("itemList"));
        return itemList.findElements(By.className("pet-name")).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
