package notaneitor.pageobjects;

import notaneitor.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PO_APIClientView {
    public static void login(WebDriver driver, String URL, String email, String password) {
        // Vamos al inicio de sesión del cliente de la API
        driver.navigate().to(URL + "/apiclient/client.html?w=login");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, email, password);
    }
    public static void openChat(WebDriver driver, String URL, String email, String password, String friendName) {
        PO_APIClientView.login(driver, URL, email, password);

        // Ahora mismo estamos en la lista de amigos, y queremos acceder al chat de un amigo en concreto
        WebElement friendChat = SeleniumUtils.waitLoadElementsBy(
                driver,
                "id",
                "chat-" + friendName,
                PO_View.getTimeout()
        ).get(0);
        friendChat.click(); // pulsamos el enlace que nos llevará a la lista de mensajes con ese amigo
    }
    public static void sendMessage(WebDriver driver, String message) {
        // Tenemos que crear un nuevo mensaje, para ello accedemos al input de tipo texto que se encuentra en la parte inferior
        WebElement writeMessageInput = driver.findElement(By.id("message"));
        writeMessageInput.click(); // hacemos click en el input para escribir el mensaje
        writeMessageInput.clear(); // borramos el input: por si estaba escrito algo sin darnos cuenta
        writeMessageInput.sendKeys(message); // escribimos el mensaje en concreto

        // Y ahora pulsamos el botón de enviar y comprobaremos que se ha enviado
        WebElement sendButton = driver.findElement(By.id("button-message"));
        sendButton.click(); // enviamos el mensaje
    }
}
