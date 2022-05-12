package notaneitor;

import notaneitor.pageobjects.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import notaneitor.util.SeleniumUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    //RUTAS ELIAS
    //static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\gekodriver\\geckodriver-v0.30.0-win64.exe";

    //RUTAS DE OSCAR
    //static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\Users\\oscar\\OneDrive\\Desktop\\SDI\\LAB\\sesion06\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";

    // RUTAS SITOO
    //static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "D:\\Descargas\\geckodriver-v0.31.0-win64\\geckodriver.exe";

    // RUTAS ÁNGEL
    static String PathFirefox = "/usr/bin/firefox";
    static String Geckodriver = "/usr/local/bin/geckodriver";
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
        driver.quit();
    }

    //Antes de cada prueba se navega al URL home de la aplicación
    @BeforeEach
    public void setUp() {
        initDB();
        driver.navigate().to(URL);
    }

    private void initDB(){
        driver.navigate().to(URL + "/initbd");
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        //driver.manage().deleteAllCookies();
        //driver.close();
    }

    //[Prueba1] Registro de Usuario con datos válidos
    @Test
    @Order(1)
    public void PR01() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "uo273649@uniovi.es", "José", "Pérez", "77777", "77777");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        String checkText = "Nuevo usuario registrado";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);

        Assertions.assertEquals(checkText, result.get(0).getText());
        //Send a delete request to remove the user

    }

    //[Prueba2A] Registro de Usuario con datos inválidos: email vacío.
    @Test
    @Order(2)
    public void PR02A() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "", "José", "Pérez", "77777", "77777");

        String checkText = "Rellene este campo.";
        WebElement result = driver.findElement(By.name("email"));

        Assertions.assertEquals(checkText, result.getAttribute("validationMessage"));
    }

    //[Prueba2B] Registro de Usuario con datos inválidos: nombre vacío.
    @Test
    @Order(3)
    public void PR02B() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "uo273823@uniovi.es", "", "Pérez", "77777", "77777");
        String checkText = "Rellene este campo.";
        WebElement result = driver.findElement(By.name("name"));

        Assertions.assertEquals(checkText, result.getAttribute("validationMessage"));
    }

    //[Prueba2C] Registro de Usuario con datos inválidos: apellido vacío.
    @Test
    @Order(4)
    public void PR02C() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "uo273823@uniovi.es", "José", "", "77777", "77777");

        String checkText = "Rellene este campo.";
        WebElement result = driver.findElement(By.name("surname"));

        Assertions.assertEquals(checkText, result.getAttribute("validationMessage"));
    }

    //[Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
    @Test
    @Order(5)
    public void PR03() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "uo273823@uniovi.es", "José", "Pérez", "11111", "66666");

        String checkText = "Las contraseñas no coinciden ";
        List<WebElement> result = PO_SignUpView.checkElementBy(driver, "text", checkText);
    }

    //[Prueba4] Registro de Usuario con datos inválidos (email existente).
    @Test
    @Order(6)
    public void PR04() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Utilizamos un mail de un usuario ya existente
        PO_SignUpView.fillForm(driver, "user01@email.com", "José", "Pérez", "77777", "77777");
        String checkText = "El email ya existe";
        List<WebElement> result = PO_SignUpView.checkElementBy(driver, "text", checkText);
        //Comprobamos el error de repeated email.
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //Prueba5] Inicio de sesión con datos válidos (administrador)
    @Test
    @Order(7)
    public void PR05() {
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Comprobamos que entramos en la página privada del administrador
        String checkText = "Usuarios";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba6] Inicio de sesión con datos válidos (usuario estándar)
    @Test
    @Order(8)
    public void PR06() {
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Listado de usuarios";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba7] Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos)
    @Test
    @Order(9)
    public void PR07() {
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "", "");
        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Identifícate";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba8] Inicio de sesión con datos válidos (usuario estándar, email existente, pero contraseña incorrecta)
    @Test
    @Order(10)
    public void PR08() {
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "i");
        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Email o password incorrecto";
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

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
        PO_ListUsersView.checkUsersAreListedForAdmin(driver, 16);
    }


    //[Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza y dicho usuario desaparece.
    @Test
    @Order(14)
    public void PR12() {

        //Iniciamos sesión como administradores
        PO_LoginView.logAsAdmin(driver);

        //Vamos a la vista /admin/list
        PO_PrivateView.clickSubMenuOption(driver, "usersDropdown", "adminList");

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
        PO_PrivateView.clickSubMenuOption(driver, "usersDropdown", "adminList");

        //Marcamos la última checkbox
        int position = PO_UserListView.checkElementBy(driver, "id", "cbDelete").size() - 1;
        PO_UserListView.markCheckboxes(driver, new int[]{position});

        //Pulsamos el botón de borrar
        List<WebElement> languageButton = SeleniumUtils.waitLoadElementsBy(driver, "id", "btnDelete", PO_View.getTimeout());
        languageButton.get(0).click();

        // Comprobamos que se muestra correctamente. Debe haber uno menos porque lo acabamos de eliminar
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "user15");
        Assertions.assertTrue(notFound);
        PO_ListUsersView.checkUsersAreListedForAdmin(driver, 15);
    }

    //[Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos usuarios desaparecen.
    @Test
    @Order(16)
    public void PR14() {

        //Iniciamos sesión como administradores
        PO_LoginView.logAsAdmin(driver);

        //Vamos a la vista /admin/list
        PO_PrivateView.clickSubMenuOption(driver, "usersDropdown", "adminList");

        //Marcamos las posiciones 3, 7 y 9: por ejemplo
        PO_UserListView.markCheckboxes(driver, new int[]{3, 7, 9});

        //Pulsamos el botón de borrar
        List<WebElement> languageButton = SeleniumUtils.waitLoadElementsBy(driver, "id", "btnDelete", PO_View.getTimeout());
        languageButton.get(0).click();

        //Comprobamos que el número de usuarios es el correcto
        PO_ListUsersView.checkUsersAreListedForAdmin(driver, 13);

        //Comprobamos que User04, User08 y User10 desaparecieron
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "user04");
        Assertions.assertTrue(notFound);
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "user08");
        Assertions.assertTrue(notFound);
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "user10");
        Assertions.assertTrue(notFound);
    }

    //[Prueba15] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema,
    //excepto el propio usuario y aquellos que sean Administradores
    @Test
    @Order(17)
    public void PR15() {
        //Vamos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //PÁGINA 1 - Comprobamos que todos los usuarios salvo user01
        List<WebElement> userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());

        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "user01");
        boolean notFound2 = PO_HomeView.checkInvisibilityOfElement(driver, "text", "admin");
        Assertions.assertTrue(notFound || notFound2);

        //PÁGINA 2 - Vamos a la siguiente página y comprobamos que hay 5 elementos y que no existe user01 ni admin
        //Presionamos el boton de ultima pagina
        List<WebElement> pageBtns = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        pageBtns.get(1).click();

        userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "user01");
        notFound2 = PO_HomeView.checkInvisibilityOfElement(driver, "text", "admin");
        Assertions.assertTrue(notFound || notFound2);

        //PÁGINA 3 - Vamos a la última página y comprobamos que nuevamente hay 5 elementos y no está el admin
        pageBtns = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        pageBtns.get(2).click();

        userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(4, userList.size());
        notFound = PO_HomeView.checkInvisibilityOfElement(driver, "text", "user01");
        notFound2 = PO_HomeView.checkInvisibilityOfElement(driver, "text", "admin");
        Assertions.assertTrue(notFound || notFound2);

    }

    //[Prueba16] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
    //corresponde con el listado usuarios existentes en el sistema.
    @Test
    @Order(18)
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
    @Order(19)
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
    @Order(20)
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

    // [Prueba19] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario.
    // Comprobar que la solicitud de amistad aparece en el listado de invitaciones (punto siguiente).
    @Test
    @Order(21)
    public void PR19() {
        //Iniciamos sesión como usuario estándar
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Ya en el listado de usuarios, enviamos una invitación al usuario 4
        List<WebElement> sendInviteButtons = SeleniumUtils.waitLoadElementsBy(driver, "text", "Enviar invitación", PO_View.getTimeout());
        sendInviteButtons.get(3).click();

        // Sale el mensaje informativo
        List<WebElement> message = SeleniumUtils.waitLoadElementsBy(driver, "text", "Invitación enviada", PO_View.getTimeout());
        Assertions.assertTrue(message.size() == 1);
    }

    // [Prueba20] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario
    // al que ya le habíamos enviado la invitación previamente. No debería dejarnos enviar la invitación,
    // se podría ocultar el botón de enviar invitación o notificar que ya había sido enviada previamente.
    @Test
    @Order(22)
    public void PR20() {
        //Iniciamos sesión como usuario estándar
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Intentamos enviarle una invitación al user 01, salta error
        List<WebElement> sendInviteButtons = SeleniumUtils.waitLoadElementsBy(driver, "text", "Enviar invitación", PO_View.getTimeout());
        sendInviteButtons.get(0).click();

        // Sale el mensaje de error
        List<WebElement> message = SeleniumUtils.waitLoadElementsBy(driver, "text", "No puedes enviar una invitación a este usuario", PO_View.getTimeout());
        Assertions.assertTrue(message.size() == 1);
    }

    //[Prueba21] Mostrar el listado de invitaciones de amistad recibidas.
    // Comprobar con un listado que contenga varias invitaciones recibidas.
    @Test
    @Order(23)
    public void PR21() {
        //Iniciamos sesión como usuario estándar
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user03@email.com", "user03");

        //Vamos a la vista /friendInvitations/list
        PO_PrivateView.clickSubMenuOption(driver, "friendshipsDropdown", "invitationsMenu");

        //Comprobamos que hay solicitudes de amistad
        List<WebElement> invList =driver.findElements(By.className("card"));
        Assertions.assertEquals( 3, invList.size());
    }

    // [Prueba22] Sobre el listado de invitaciones recibidas. Hacer clic en el botón/enlace de una de ellas
    // y comprobar que dicha solicitud desaparece del listado de invitaciones.
    @Test
    @Order(24)
    public void PR22() {
        //Iniciamos sesión como usuario estándar
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        //Vamos a la vista /friendInvitations/list
        PO_PrivateView.clickSubMenuOption(driver, "friendshipsDropdown", "invitationsMenu");

        // Aceptamos la invitación, la invitación desaparece
        List<WebElement> acceptButtons = SeleniumUtils.waitLoadElementsBy(driver, "text", "Aceptar", PO_View.getTimeout());
        acceptButtons.get(0).click();

        List<WebElement> invitations = driver.findElements(By.className("card"));
        Assertions.assertTrue(invitations.size() == 0);
    }

    // [Prueba23] Mostrar el listado de amigos de un usuario.
    // Comprobar que el listado contiene los amigos que deben ser
    @Test
    @Order(25)
    public void  PR23(){
        //Log in con user
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Ir a la página de listado de amigos
        PO_PrivateView.clickSubMenuOption(driver, "friendshipsDropdown", "friendsMenu");

        //Comprobamos que nos salen todos los amigos del usuario
        List<WebElement> friendList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals( 2, friendList.size() );
    }

    // [Prueba24] Ir al formulario crear publicaciones, rellenarla con datos válidos y pulsar el botón Submit.
    // Comprobar que la publicación sale en el listado de publicaciones de dicho usuario.
    @Test
    @Order(26)
    public void  PR24(){
        //Log in con user sin publicaciones
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        // Vamos a la vista de nueva publicación
        PO_PrivateView.clickSubMenuOption(driver, "postsDropdown", "newPostMenu");

        // Creamos una publicación
        PO_NewPostView.fillPostForm(driver, "Publicación de prueba", "Contenido de publicación de prueba");

        // Comprobamos que aparece la publicación
        List<WebElement> posts = driver.findElements(By.className("card"));
        Assertions.assertEquals(1, posts.size());
    }

    // [Prueba25] Ir al formulario de crear publicaciones, rellenarla con datos inválidos (campo título vacío)
    // y pulsar el botón Submit. Comprobar que se muestra el mensaje de campo obligatorio.
    @Test
    @Order(27)
    public void  PR25(){
        //Log in con user
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        // Vamos a la vista de nueva publicación
        PO_PrivateView.clickSubMenuOption(driver, "postsDropdown", "newPostMenu");

        // Creamos una publicación
        PO_NewPostView.fillPostForm(driver, "", "Contenido de publicación de prueba");

        // Comprobamos que sale el mensaje de error
        List<WebElement> message = SeleniumUtils.waitLoadElementsBy(driver, "text", "El título es obligatorio.", PO_View.getTimeout());
        Assertions.assertTrue(message.size() == 1);
    }

    // [Prueba26] Mostrar el listado de publicaciones de un usuario y comprobar
    // que se muestran todas las que existen para dicho usuario.
    @Test
    @Order(28)
    public void PR26(){
        //Log in con user con 3 publicaciones
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Ir a la página de publicaciones propias
        PO_PrivateView.clickSubMenuOption(driver, "postsDropdown", "myPostsMenu");

        //Comprobamos que hay 3 publicaciones
        List<WebElement> posts = driver.findElements(By.className("card"));;
        Assertions.assertEquals(3, posts.size());
    }

    // [Prueba27] Mostrar el listado de publicaciones de un usuario amigo y comprobar
    // que se muestran todas las que existen para dicho - usuario.
    @Test
    @Order(29)
    public void PR27(){
        //Log in con user
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        //Ir a la pagina de listar amigos
        PO_PrivateView.clickSubMenuOption(driver, "friendshipsDropdown", "friendsMenu");

        //Hacemos clic en un amigo con 3 publicaciones
        WebElement buttonPosts = SeleniumUtils
                .waitLoadElementsBy(driver, "class", "btn", PO_View.getTimeout()).stream().findFirst()
                .orElse(null);
        buttonPosts.click();

        //Comprobamos que hay 3 publicaciones
        List<WebElement> posts =driver.findElements(By.className("card"));;
        Assertions.assertEquals(3, posts.size());
    }

    // [Prueba28] Utilizando un acceso vía URL u otra alternativa,
    // tratar de listar las publicaciones de un usuario que no sea amigo
    // del usuario identificado en sesión. Comprobar que el sistema da un error de autorización.
    @Test
    @Order(30)
    public void PR28(){
        //Log in con user
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Intentamos acceder mediante URL a un usuario que no es amigo
        driver.navigate().to(URL+"/posts/user05@email.com");

        //Comprobamos que estamos en la página de error
        List<WebElement> result = SeleniumUtils.waitLoadElementsBy(driver, "text", "Acceso no autorizado", PO_View.getTimeout());
        Assertions.assertTrue(result.size()==1);

        // Aquí reset a la db
        initDB();
        driver.navigate().to(URL);
    }

    //[Prueba29] Intentar acceder sin estar autenticado a la opción de listado de usuarios. Se deberá volver al
    //formulario de login.
    @Test
    @Order(31)
    public void PR29() {
        // Intentamos acceder - sin estar autenticados - a una opción del menú para usuarios autenticados: listado de usuarios
        driver.navigate().to(URL + "/users/list");

        // Comprobamos que nos ha devuelto al formulario de login
        String checkText = "Identificación de usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba30] Intentar acceder sin estar autenticado a la opción de listado de invitaciones de amistad recibida
    //de un usuario estándar. Se deberá volver al formulario de login.
    @Test
    @Order(33)
    public void PR30() {
        // Intentamos acceder - sin estar autenticados - a una opción del menú para usuarios autenticados: listado de peticiones de amistad
        driver.navigate().to(URL + "/friendships/invitations");

        // Comprobamos que nos ha devuelto al formulario de login
        String checkText = "Identificación de usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba31] Intentar acceder estando autenticado como usuario standard a la lista de amigos de otro
    //usuario. Se deberá mostrar un mensaje de acción indebida.
    // TODO: Check wtf this is.
    @Test
    @Order(34)
    public void PR31() {
        //Iniciamos sesión como un usuario normal
        //Vamos al formulario de inicio de sesión.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Intentamos acceder - autenticados como usuario estándar - a una opción del menú para usuarios autenticados: eliminar usuarios
        driver.navigate().to(URL + "/users/admin/list");

        // Comprobamos que nos ha devuelto al formulario de login
        String checkText = "Acceso prohibido";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba32] Inicio de sesión con datos válidos.
    @Test
    @Order(35)
    public void PR32() {
        // Vamos al inicio de sesión del cliente de la API
        driver.navigate().to(URL + "/apiclient/client.html?w=login");

        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Lista de amigos";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba33] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
    @Test
    @Order(36)
    public void PR33() {
        // Vamos al inicio de sesión del cliente de la API
        driver.navigate().to(URL + "/apiclient/client.html?w=login");

        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "unhacker@email.com", "hackeadoBrooo");

        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Usuario con email unhacker@email.com no ha sido encontrado. O la contraseña es incorrecta";
        List<WebElement> result = PO_View.checkElementBy(driver, "id", "login-error");
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba34] Acceder a la lista de amigos de un usuario, que al menos tenga tres amigos.
    @Test
    @Order(37)
    public void PR34() {
        initDB();
        PO_LoginView.logInApi(driver, URL, "user01@email.com", "user01");

        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Lista de amigos";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        // Tenemos que esperar a que carguen todos los elementos de la tabla, ya que vienen de la DB
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.and(
                ExpectedConditions.elementToBeClickable(By.id("user02@email.com")),
                ExpectedConditions.elementToBeClickable(By.id("user03@email.com")),
                ExpectedConditions.elementToBeClickable(By.id("user06@email.com"))
        ));

        // Realizamos el proceso de obtención del nombre del amigo dado un email y esperamos a que cargue la entrada en concreto
        WebElement friendTable = driver.findElement(By.id("friendsTableBody"));
        List<WebElement> friendList = friendTable.findElements(By.tagName("tr"));
        Assertions.assertEquals( 3, friendList.size());
    }

    //[Prueba35] Acceder a la lista de amigos de un usuario, y realizar un filtrado para encontrar a un amigo
    // en concreto: el nombre a buscar debe coincidir con el de un amigo.
    @Test
    @Order(38)
    public void PR35() {
        initDB();
        PO_LoginView.logInApi(driver, URL, "user01@email.com", "user01"); // sabemos los amigos que tiene este usuario

        // Establecemos el email y el nombre del amigo que queremos buscar
        String friendEmail = "user02@email.com"; // sabemos que este es un amigo
        String friendName = "user02"; // y este es su nombre

        // Realizamos el proceso de obtención del nombre del amigo dado un email y esperamos a que cargue la entrada en concreto
        WebElement friendRow = SeleniumUtils.waitLoadElementsBy(
                driver,
                "id",
                friendEmail,
                PO_View.getTimeout()
        ).get(0);

        // el segundo td es el de nombre
        WebElement nameCell = friendRow.findElements(By.tagName("td")).get(1);

        // Comprobamos que el nombre es efectivamente el del amigo que estábamos buscando :)
        Assertions.assertEquals(friendName, nameCell.getText()); // deben coincidir
    }

    //[Prueba36] Acceder a la lista de mensajes de un amigo, la lista debe contener al menos tres mensajes.
    @Test
    @Order(39)
    public void PR36() {
        initDB();
        PO_LoginView.logInApi(driver, URL, "user01@email.com", "user01");

        // Ahora mismo estamos en la lista de amigos, y queremos acceder al chat de un amigo en concreto
        WebElement friendChat = SeleniumUtils.waitLoadElementsBy(
                driver,
                "id",
                "chat-user02",
                PO_View.getTimeout()
        ).get(0);
        friendChat.click(); // pulsamos el enlace que nos llevará a la lista de mensajes con ese amigo

        // Debemos comprobar que hay más de tres mensajes con ese amigo
        List<WebElement> invList = driver.findElements(By.tagName("tr")); // en caso de que haya más de tres filas en la tabla...
        Assertions.assertTrue( invList.size() >= 3); // sabemos que hay más de tres mensajes
    }

    //[Prueba37] Acceder a la lista de mensajes de un amigo y crear un nuevo mensaje. Validar que el mensaje
    // aparece en la lista de mensajes
    @Test
    @Order(40)
    public void PR37() {
        initDB();
        PO_LoginView.logInApi(driver, URL, "user01@email.com", "user01");

        // Escribimos algún tipo de mensaje que queremos mandar :)
        String message = "Esto es un mensaje de prueba. Estamos comprobando que todo funciona bien!";

        // Ahora mismo estamos en la lista de amigos, y queremos acceder al chat de un amigo en concreto
        WebElement friendChat = SeleniumUtils.waitLoadElementsBy(
                driver,
                "id",
                "chat-user02",
                PO_View.getTimeout()
        ).get(0);
        friendChat.click(); // pulsamos el enlace que nos llevará a la lista de mensajes con ese amigo

        // Tenemos que crear un nuevo mensaje, para ello accedemos al input de tipo texto que se encuentra en la parte inferior
        WebElement writeMessageInput = driver.findElement(By.id("message"));
        writeMessageInput.click(); // hacemos click en el input para escribir el mensaje
        writeMessageInput.clear(); // borramos el input: por si estaba escrito algo sin darnos cuenta
        writeMessageInput.sendKeys(message); // escribimos el mensaje en concreto

        // Y ahora pulsamos el botón de enviar y comprobaremos que se ha enviado
        WebElement sendButton = driver.findElement(By.id("button-message"));
        sendButton.click(); // enviamos el mensaje

        List<WebElement> result = PO_View.checkElementBy(driver, "text", message); // buscamos el mensaje en la página
        // No nos importa lo que haya más allá del guion: puede ser recibido, leído o lo que sea, eso es parte de otro test
        Assertions.assertEquals(message, result.get(0).getText().split("-")[0].trim()); // en caso de que esté el mensaje, las cosas habrán ido como debían
    }

    //[Prueba38] Identificarse en la aplicación y enviar un mensaje a un amigo. Validar que el mensaje enviado
    //aparece en el chat. Identificarse después con el usuario que recibió el mensaje y validar que tiene un
    //mensaje sin leer. Entrar en el chat y comprobar que el mensaje pasa a tener el estado leído.

    //[Prueba39] Identificarse en la aplicación y enviar tres mensajes a un amigo. Validar que los mensajes
    //enviados aparecen en el chat. Identificarse después con el usuario que recibido el mensaje y validar que el
    //número de mensajes sin leer aparece en la propia lista de amigos
}
