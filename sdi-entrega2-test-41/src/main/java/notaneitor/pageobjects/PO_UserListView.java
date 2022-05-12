package notaneitor.pageobjects;

import notaneitor.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_UserListView extends PO_NavView {

    /**
     * Receives a list of positions of the rows and mark them
     * @param driver
     * @param positions
     */
    public static void markCheckboxes(WebDriver driver, int[] positions) {
        List<WebElement> list = checkElementBy(driver, "id", "cbDelete");
        for (int position : positions) {
            list.get(position).click();
        }
    }

    /**
     * Executes the serach by the searchText given by parameter
     *
     * @param driver
     * @param searchText
     */
    public static void executeSearch(WebDriver driver, String searchText) {
        //CLickamos en la opción de registro y esperamos a que se cargue el enlace de Registro.
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "id", "inputSearch",
                getTimeout());
        //Tiene que haber un sólo elemento.
        Assertions.assertEquals(1, elements.size());
        //Ahora lo clickamos
        elements.get(0).sendKeys(searchText);
        //A continuacion, apretamos el boton de busqueda
        elements = checkElementBy(driver, "id", "btnSearch");
        //Tiene que haber un sólo elemento.
        Assertions.assertEquals(1, elements.size());
        //Ahora lo clickamos
        elements.get(0).click();
    }
}
