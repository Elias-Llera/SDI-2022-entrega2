package notaneitor;

import notaneitor.pageobjects.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import notaneitor.util.SeleniumUtils;

import java.util.List;

//Ordenamos las pruebas por la anotación @Order de cada método
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotaneitorApplicationTests {
    //Para MACOSX
    //static String PathFirefox = "/Applications/Firefox 2.app/Contents/MacOS/firefox-bin";
    //static String Geckodriver = "/Users/delacal/selenium/geckodriver-v0.30.0-macos";
    //Para Windows
    //static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";    //Común a Windows y a MACOSX
    //Común a Windows y a MACOSX
    //static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Users\\danie\\Downloads\\geckodriver-v0.30.0-win64\\geckodriver-v0.30.0-win64.exe";

    //RUTAS DE OSCAR
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\Users\\oscar\\OneDrive\\Desktop\\SDI\\LAB\\sesion06\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";

    static final String URL = "http://localhost:3000";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {

    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        //Cerramos el navegador al finalizar las pruebas
        //driver.quit();
    }

    //Antes de cada prueba se navega al URL home de la aplicación
    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        //driver.manage().deleteAllCookies();
        //driver.close();
    }

//    @Test
//    @Order(1)
//    void PR01A() {
//        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
//    }
//
//    @Test
//    @Order(2)
//    void PR01B() {
//        List<WebElement> welcomeMessageElement = PO_HomeView.getWelcomeMessageText(driver, PO_Properties.getSPANISH());
//        Assertions.assertEquals(welcomeMessageElement.get(0).getText(), PO_HomeView.getP().getString("welcome.message", PO_Properties.getSPANISH()));
//    }
//
//    //PR02. OPción de navegación. Pinchar en el enlace Registro en la página home
//    @Test
//    @Order(3)
//    public void PR02() {
//        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
//    }
//
//    //PR03. OPción de navegación. Pinchar en el enlace Identificate en la página home
//    @Test
//    @Order(4)
//    public void PR03() {
//        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
//    }
//
//    //PR04. Opción de navegación. Cambio de idioma de Español a Ingles y vuelta a Español
//    @Test
//    @Order(5)
//    public void PR04() {
//        PO_HomeView.checkChangeLanguage(driver, "btnSpanish", "btnEnglish", PO_Properties.getSPANISH(), PO_Properties.getENGLISH());
//
//
//    }
//
//    //PR05. Prueba del formulario de registro. registro con datos correctos
//    @Test
//    @Order(6)
//    public void PR05() {
//        //Vamos al formulario de registro
//        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
//        //Rellenamos el formulario.
//        PO_SignUpView.fillForm(driver, "77777778A", "Josefo", "Perez", "77777", "77777");
//        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
//        String checkText = "Notas del usuario";
//        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
//        Assertions.assertEquals(checkText, result.get(0).getText());
//    }
//
//    //PR06a. Prueba del formulario de registro. DNI repetido en la BD
//    // Propiedad: Error.signup.dni.duplicate
//    @Test
//    @Order(7)
//    public void PR06A() {
//        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
//        PO_SignUpView.fillForm(driver, "99999990A", "Josefo", "Perez", "77777", "77777");
//        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.dni.duplicate", PO_Properties.getSPANISH());
//        //Comprobamos el error de DNI repetido.
//        String checkText = PO_HomeView.getP().getString("Error.signup.dni.duplicate", PO_Properties.getSPANISH());
//        Assertions.assertEquals(checkText, result.get(0).getText());
//    }
//
//    //PR06B. Prueba del formulario de registro. Nombre corto.
//    // Propiedad: Error.signup.dni.length
//    @Test
//    @Order(8)
//    public void PR06B() {
//        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
//        PO_SignUpView.fillForm(driver, "99999990B", "Jose", "Perez", "77777", "77777");
//        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.name.length", PO_Properties.getSPANISH());
//        //Comprobamos el error de Nombre corto de nombre corto .
//        String checkText = PO_HomeView.getP().getString("Error.signup.name.length", PO_Properties.getSPANISH());
//        Assertions.assertEquals(checkText, result.get(0).getText());
//    }
//
//    //PR09. Loguearse con exito desde el ROl de Usuario, 99999990D, 123456
//    @Test
//    @Order(9)
//    public void PR07() {
//        //Vamos al formulario de logueo.
//        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
//        //Rellenamos el formulario
//        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
//        //COmprobamos que entramos en la pagina privada de Alumno
//        String checkText = "Notas del usuario";
//        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
//        Assertions.assertEquals(checkText, result.get(0).getText());
//    }
//
//    @Test
//    @Order(10)
//    public void PR08() {
//    }
//
//    @Test
//    @Order(11)
//    public void PR09() {
//    }
//
//    @Test
//    @Order(12)
//    public void PR10() {
//    }
//
//    @Test
//    @Order(13)
//    public void PR11() {
//    }
//
//    //PR12. Loguearse, comprobar que se visualizan 4 filas de notas y desconectarse usando el rol de
//    @Test
//    @Order(14)
//    public void PR12() {
//        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
//        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
//        String checkText = "Notas del usuario";
//        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
//
//        //Contamos el número de filas de notas
//        List<WebElement> markList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
//        Assertions.assertEquals(4, markList.size());
//
//        //Ahora nos desconectamos comprobamas que aparece el menu de registrarse
//        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
//        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
//    }
//
//    //PR13. Loguearse como estudiante y ver los detalles de la nota con Descripcion = Nota A2.
//    @Test
//    @Order(15)
//    public void PR13() {
//        //Comprobamos que entramos en la pagina privada de Alumno
//        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
//        PO_LoginView.fillLoginForm(driver, "99999990A", "123456");
//        String checkText = "Notas del usuario";
//
//        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
//
//        //SeleniumUtils.esperarSegundos(driver, 1);
//        //Contamos las notas
//        By enlace = By.xpath("//td[contains(text(), 'Nota A2')]/following-sibling::*[2]");
//        driver.findElement(enlace).click();
//        //Esperamos por la ventana de detalle
//
//        checkText = "Detalles de la nota";
//        result = PO_View.checkElementBy(driver, "text", checkText);
//        Assertions.assertEquals(checkText, result.get(0).getText());
//
//        //Ahora nos desconectamos comprobamas que aparece el menu de registrarse
//        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
//        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
//    }

    //[Prueba9] Hacer clic en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
    //sesión (Login).
    @Test
    @Order(11)
    public void PR09() {
        //Iniciamos sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Comprobamos que hemos iniciado sesión correctamente
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Listado de usuarios");
        Assertions.assertEquals("Listado de usuarios", result.get(0).getText());

        //Nos desconectamos
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        //Comprobamos que se redirige a la página de inicio de sesión
        result = PO_LoginView.checkElementBy(driver, "text", "Identificación de usuario");
        Assertions.assertEquals("Identificación de usuario", result.get(0).getText());
    }

    //[Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado
    @Test
    @Order(12)
    public void PR10() {
        //Vamos al formulario login
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Comprobamos la invisibilidad del botón logout
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "@href", "logout");

        //Comprobamos que no está visible este elemento
        Assertions.assertTrue(notFound);
    }

    //[Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema
    @Test
    @Order(13)
    public void PR11() {
        //Iniciamos sesión como administradores
        PO_LoginView.logAsAdmin(driver);

        //Vamos a la vistado de listado de administrador users/admin/list
        PO_PrivateView.clickSubMenuOption(driver, "usersDropdown", "adminList");

        // Comprobamos que se muestra correctamente
        PO_ListUsersView.checkUsersAreListedForAdmin(driver, 17);
    }


    //[Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza y dicho usuario desaparece.
    @Test
    @Order(14)
    public void PR12() {
        //Iniciamos sesión como administradores
        PO_LoginView.logAsAdmin(driver);

        //Marcamos la primera checkbox
        PO_UserListView.markCheckboxes(driver, new int[]{0});

        //Pulsamos el botón de borrar
        List<WebElement> languageButton = SeleniumUtils.waitLoadElementsBy(driver, "id", "btnDelete", PO_View.getTimeout());
        languageButton.get(0).click();

        // Comprobamos que se muestra correctamente. Debe haber uno menos porque lo acabamos de eliminar
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User0");
        Assertions.assertTrue(notFound);
        PO_ListUsersView.checkUsersAreListedForAdmin(driver, 15);
    }
    //[Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza y dicho usuario desaparece.
    @Test
    @Order(15)
    public void PR13() {
        //Iniciamos sesión como administradores
        PO_LoginView.logAsAdmin(driver);
        //Vamos a la vista /admin/list
        PO_PrivateView.clickSubMenuOption(driver, "admins-menu", "adminList");

        //Marcamos la última checkbox
        int position = PO_UserListView.checkElementBy(driver, "id", "cbDelete").size() - 1;
        PO_UserListView.markCheckboxes(driver, new int[]{position});

        //Pulsamos el botón de borrar
        PO_UserListView.clickOption(driver, "Eliminar", "id", "btnDelete");

        // Comprobamos que se muestra correctamente. Debe haber uno menos porque lo acabamos de eliminar
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User0");
        Assertions.assertTrue(notFound);
        PO_ListUsersView.checkUsersAreListedForAdmin(driver, 15);
    }

    //[Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos usuarios desaparecen.
    @Test
    @Order(14)
    public void PR14() {
        //Iniciamos sesión como administradores
        PO_LoginView.logAsAdmin(driver);

        //Vamos a la vista /admin/list
        PO_PrivateView.clickSubMenuOption(driver, "admins-menu", "adminList");

        //Marcamos las posiciones 3, 7 y 9: por ejemplo
        PO_UserListView.markCheckboxes(driver, new int[]{3, 7, 9});
        //Pulsamos el botón de borrar
        //Pulsamos el botón de borrar
        PO_UserListView.clickOption(driver, "Eliminar", "id", "btnDelete");

        //Comprobamos que el número de usuarios es el correcto
        PO_ListUsersView.checkUsersAreListedForAdmin(driver, 13);

        //Comprobamos que User04, User08 y User10 desaparecieron
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User04");
        Assertions.assertTrue(notFound);
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User08");
        Assertions.assertTrue(notFound);
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User10");
        Assertions.assertTrue(notFound);
    }

    //[Prueba15] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema,
    //excepto el propio usuario y aquellos que sean Administradores
    @Test
    @Order(15)
    public void PR15() {
        //Vamos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //PÁGINA 1 - Comprobamos que todos los usuarios salvo user01
        List<WebElement> userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());

        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User01");
        boolean notFound2 = PO_HomeView.checkInvisibilityOfElement(driver, "text", "admin");
        Assertions.assertTrue(notFound || notFound2);

        //PÁGINA 2 - Vamos a la siguiente página y comprobamos que hay 5 elementos y que no existe user01 ni admin
        PO_PrivateView.clickOnElement(driver, "//a[contains(@class, 'page-link')]", 2);
        userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User01");
        notFound2 = PO_HomeView.checkInvisibilityOfElement(driver, "text", "admin");
        Assertions.assertTrue(notFound || notFound2);

        //PÁGINA 3 - Vamos a la última página y comprobamos que nuevamente hay 5 elementos y no está el admin
        PO_PrivateView.clickOnElement(driver, "//a[contains(@class, 'page-link')]", 2);
        userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "User01");
        notFound2 = PO_HomeView.checkInvisibilityOfElement(driver, "text", "admin");
        Assertions.assertTrue(notFound || notFound2);

    }

    //[Prueba16] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
    //corresponde con el listado usuarios existentes en el sistema.
    @Test
    @Order(16)
    public void PR16() {
        //Vamos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Realizamos una busqueda con el campo vacio
        PO_UserListView.executeSearch(driver, "");

        //Presionamos el boton de ultima pagina
        List<WebElement> lastPageBtn = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        lastPageBtn.get(2).click();

        //Comprobamos que la página es la 3
        List<WebElement> pageNumbers = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        Assertions.assertEquals(3, pageNumbers.size());
        Assertions.assertEquals("2", pageNumbers.get(1).getText());
        Assertions.assertEquals("3", pageNumbers.get(2).getText());

        //Y que hay 5 usuarios
        List<WebElement> userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(4, userList.size());
    }

    //[Prueba17] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
    //muestra la página que corresponde, con la lista de usuarios vacía.
    @Test
    @Order(17)
    public void PR17() {
        //Vamos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Realizamos una busqueda con un campo que no nos proporcione ningun resultado
        PO_UserListView.executeSearch(driver, "wefmeiffsdfsdfdsfsdfsdfsdfdsfdsfdsmoiwef");

        //Comprobamos que no hay paginas
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "free", "page-link");
        //Comprobamos no esta visible este elemento
        Assertions.assertEquals(true, notFound);

        //Y que hay 0 usuarios
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "free", "//tbody/tr");
        //Comprobamos no esta visible este elemento
        Assertions.assertEquals(true, notFound);
    }


    //[Prueba18] Hacer una búsqueda con un texto específico y comprobar que se muestra la página que
    //corresponde, con la lista de usuarios en los que el texto especificado sea parte de su nombre, apellidos o de su email.
    @Test
    @Order(18)
    public void PR18() {
        //Vamos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Realizamos una busqueda con el campo vacio
        PO_UserListView.executeSearch(driver, "");

        //Realizamos una busqueda con el campo "1"
        PO_UserListView.executeSearch(driver, "1");

        //Comprobamos que los resultados son los esperados
        PO_UserListView.checkElementBy(driver, "text", "user01");
        PO_UserListView.checkElementBy(driver, "text", "user10");
        PO_UserListView.checkElementBy(driver, "text", "user11");
        PO_UserListView.checkElementBy(driver, "text", "user12");
        PO_UserListView.checkElementBy(driver, "text", "user13");

        //Y que hay 5 usuarios
        List<WebElement> userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());

        //Comprobamos que estamos en la página 1
        List<WebElement> pageNumbers = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        Assertions.assertEquals(2, pageNumbers.size());
        Assertions.assertEquals("1", pageNumbers.get(0).getText());
        Assertions.assertEquals("2", pageNumbers.get(1).getText());

        //Vamos a la segunda pagina
        List<WebElement> lastPageBtn = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        lastPageBtn.get(1).click();

        //Comprobamos que los resultados son los esperados
        PO_UserListView.checkElementBy(driver, "text", "user14");
        PO_UserListView.checkElementBy(driver, "text", "user15");
    }

}
