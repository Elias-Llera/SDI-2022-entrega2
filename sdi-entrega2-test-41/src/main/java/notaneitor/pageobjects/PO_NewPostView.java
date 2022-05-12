package notaneitor.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_NewPostView extends PO_NavView{
    public static void fillPostForm(WebDriver driver, String title, String content) {
        WebElement titleElement = driver.findElement(By.name("title"));
        titleElement.click();
        titleElement.clear();
        titleElement.sendKeys(title);
        WebElement contentElement = driver.findElement(By.name("content"));
        contentElement.click();
        contentElement.clear();
        contentElement.sendKeys(content);
        //Pulsar el boton de Alta.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }
}
