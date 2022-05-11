package notaneitor.pageobjects;

import notaneitor.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_ListUsersView extends PO_NavView{

    public static void checkUsersAreListedForAdmin(WebDriver driver, int size){
        List<WebElement> userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(size, userList.size());
    }

}
