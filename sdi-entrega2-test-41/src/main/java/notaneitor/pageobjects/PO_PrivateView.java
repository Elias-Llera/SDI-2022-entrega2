package notaneitor.pageobjects;

import notaneitor.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_PrivateView extends PO_NavView {

    static public void fillFormAddMark(WebDriver driver, int userOrder, String descriptionp, String scorep) {
        //Esperamos 5 segundo a que carge el DOM porque en algunos equipos falla
        SeleniumUtils.waitSeconds(driver, 5);
        //Seleccionamos el alumnos userOrder
        new Select(driver.findElement(By.id("user"))).selectByIndex(userOrder);
        //Rellenemos el campo de descripción
        WebElement description = driver.findElement(By.name("description"));
        description.clear();
        description.sendKeys(descriptionp);
        WebElement score = driver.findElement(By.name("score"));
        score.click();
        score.clear();
        score.sendKeys(scorep);
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

    public static void clickMenuOption(WebDriver driver, String id) {
        driver.findElements(By.id(id)).get(0).click();
    }

    public static void clickSubMenuOption(WebDriver driver, String idMenu, String idSubMenu) {
        driver.findElements(By.id(idMenu)).get(0).click();
        driver.findElements(By.id(idSubMenu)).get(0).click();
    }

    public static void clickOnElement(WebDriver driver, String text, int indexOfElementToClick) {
        List<WebElement> elements = checkElementBy(driver, "free", text);
        elements.get(indexOfElementToClick).click();
    }
}

