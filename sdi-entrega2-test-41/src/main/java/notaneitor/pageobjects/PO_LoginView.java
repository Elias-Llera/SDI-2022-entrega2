package notaneitor.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_LoginView extends PO_NavView {

	static public void fillLoginForm(WebDriver driver, String emailp, String passwordp) {
		WebElement email = driver.findElement(By.name("email"));
		email.click();
		email.clear();
		email.sendKeys(emailp);
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys(passwordp);
		//Pulsar el boton de Alta.
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}

	public static void login(WebDriver driver, String username, String passwordp) {
		// Vamos al formulario de inicio de sesión
		driver.findElements(By.id("loginButton")).get(0).click();
		//Rellenamos el formulario
		PO_LoginView.fillLoginForm(driver, username, passwordp);
		//Comprobamos que entramos en la página privada de alumno
		String checkText = PO_View.getP().getString("user.list.users", PO_Properties.getSPANISH());
		List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());

	}

	public static void logAsAdmin(WebDriver driver) {
		//Vamos al formulario login
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		//Rellenamos el formulario
		PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

		//Comprobamos que se logeo correctamente
		List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Listado de usuarios");
		Assertions.assertEquals("Listado de usuarios", result.get(0).getText());
	}

	public static void logout(WebDriver driver) {
		driver.findElements(By.id("logout")).get(0).click();
	}


	public static void logInApi(WebDriver driver, String URL, String email, String password) {
		// Vamos al inicio de sesión del cliente de la API
		driver.navigate().to(URL + "/apiclient/client.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
	}

}
