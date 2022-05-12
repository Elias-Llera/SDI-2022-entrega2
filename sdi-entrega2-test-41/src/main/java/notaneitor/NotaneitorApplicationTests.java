package notaneitor;

import notaneitor.pageobjects.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
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
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
    static String Geckodriver = "C:\\Users\\danie\\Downloads\\geckodriver-v0.30.0-win64\\geckodriver-v0.30.0-win64.exe";

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


    //[Prueba1] Registro de Usuario con datos válidos
    @Test
    @Order(1)
    public void PR01() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "uo273649@uniovi.es", "José", "Pérez", "77777", "77777");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        String checkText = "Bienvenido a nuestra red social";
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

        Assertions.assertEquals(checkText , result.getAttribute("validationMessage"));
    }

    //[Prueba2B] Registro de Usuario con datos inválidos: nombre vacío.
    @Test
    @Order(3)
    public void PR02B() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "uo273823@uniovi.es", "", "Pérez", "77777", "77777");
        String checkText = "Rellene este campo.";
        WebElement result = driver.findElement(By.name("name"));

        Assertions.assertEquals(checkText , result.getAttribute("validationMessage"));
    }

    //[Prueba2C] Registro de Usuario con datos inválidos: apellido vacío.
    @Test
    @Order(4)
    public void PR02C() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "uo273823@uniovi.es", "José", "", "77777", "77777");

        String checkText = "Rellene este campo.";
        WebElement result = driver.findElement(By.name("surname"));

        Assertions.assertEquals(checkText , result.getAttribute("validationMessage"));
    }

    //[Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
    @Test
    @Order(5)
    public void PR03() {
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        PO_SignUpView.fillForm(driver, "uo273823@uniovi.es", "José", "Pérez", "11111", "66666");

        String checkText = "Las contraseñas no coinciden ";
        List<WebElement> result = PO_SignUpView.checkElementBy(driver, "text",checkText );
    }

    //[Prueba4] Registro de Usuario con datos inválidos (email existente).
    @Test
    @Order(6)
    public void PR04() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Utilizamos un mail de un usuario ya existente
        PO_SignUpView.fillForm(driver,  "user01@email.com", "José", "Pérez", "77777", "77777");
        String checkText = "El email ya existe";
        List<WebElement> result = PO_SignUpView.checkElementBy(driver, "text",checkText );
        //Comprobamos el error de repeated email.
        Assertions.assertEquals(checkText , result.get(0).getText());
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
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text",checkText );
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
        List<WebElement> result = PO_LoginView.checkElementByKey(driver, "user.list.users", PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("user.list.users", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText , result.get(0).getText());

        //Nos desconectamos
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        //Comprobamos que se redirige a la página de inicio de sesión
        result = PO_LoginView.checkElementByKey(driver, "nav.login.message", PO_Properties.getSPANISH() );
        checkText = PO_HomeView.getP().getString("nav.login.message", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText , result.get(0).getText());
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

    //P14. Loguearse como profesor y Agregar Nota A2.
    //P14. Esta prueba podría encapsularse mejor ...
    @Test
    @Order(16)
    public void PR14() {
        //Vamos al formulario de login.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
            PO_LoginView.fillLoginForm(driver, "99999993D", "123456");
        //Cmmprobamos que entramos en la pagina privada del Profesor
        PO_View.checkElementBy(driver, "text", "99999993D");

        //Pinchamos en la opción de menu de Notas: //li[contains(@id, 'marks-menu')]/a
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//li[contains(@id, 'marks-menu')]/a");
        elements.get(0).click();

        //Esperamos a aparezca la opción de añadir nota: //a[contains(@href, 'mark/add')]
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'mark/add')]");
        //Pinchamos en agregar Nota.
        elements.get(0).click();

        //Ahora vamos a rellenar la nota. //option[contains(@value, '4')]
        String checkText = "Nota Nueva 3";
        PO_PrivateView.fillFormAddMark(driver, 3, checkText, "8");
        //Esperamos a que se muestren los enlaces de paginación la lista de notas
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        //Nos vamos a la última página
        elements.get(3).click();
        //Comprobamos que aparece la nota en la pagina
        elements = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, elements.get(0).getText());

        //Ahora nos desconectamos comprobamas que aparece el menu de registrarse
        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

   /* //PRN. Loguearse como profesor, vamos a la ultima página y Eliminamos la Nota Nueva 1.
    //PRN. Ver la lista de Notas.
    @Test
    @Order(17)
    public void PR15() {
        //Vamos al formulario de logueo.
        //PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //PO_LoginView.fillLoginForm(driver, "99999993D", "123456");

        //Comprobamos que entramos en la pagina privada del Profesor
        //PO_View.checkElementBy(driver, "text", "99999993D");
        //Pinchamos en la opción de menu de Notas: //li[contains(@id, 'marks-menu')]/a
    }*/

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
        //Nos logeamos con usuario estandar
        PO_LoginView.login(driver, "user01@email.com", "user01");
        //Vamos a la vista /user/list
        PO_PrivateView.clickSubMenuOption(driver, "userDropdown", "userList");

        //Realizamos una busqueda con el campo vacio
        PO_UserListView.executeSearch(driver, "");

        //Presionamos el boton de ultima pagina
        List<WebElement> lastPageBtn = SeleniumUtils.waitLoadElementsBy(driver, "text", "Última",
                PO_View.getTimeout());
        lastPageBtn.get(0).click();

        //Comprobamos que la página es la 3
        List<WebElement> pageNumbers = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        Assertions.assertEquals(4, pageNumbers.size());
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
        //Nos logeamos como administradores
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Vamos a la vista /user/list
        PO_PrivateView.clickSubMenuOption(driver, "userDropdown", "userList");

        //Realizamos una busqueda con un campo que no nos proporcione ningun resultado
        PO_UserListView.executeSearch(driver, "wefmeifmoiwef");

        //Comprobamos que la página es la 1
        List<WebElement> pageNumbers = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        Assertions.assertEquals(3, pageNumbers.size());
        Assertions.assertEquals("1", pageNumbers.get(1).getText());

        //Y que hay 5 usuarios
        boolean notFound = PO_HomeView.checkInvisibilityOfElement(driver, "free", "//tbody/tr");

        //Comprobamos no esta visible este elemento
        Assertions.assertEquals(true, notFound);
    }


    //[Prueba18] Hacer una búsqueda con un texto específico y comprobar que se muestra la página que
    //corresponde, con la lista de usuarios en los que el texto especificado sea parte de su nombre, apellidos o de su email.
    @Test
    @Order(18)
    public void PR18() {
        //Nos logeamos como usuario
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Vamos a la vista /user/list
        PO_PrivateView.clickSubMenuOption(driver, "userDropdown", "userList");

        //Realizamos una busqueda con el campo "01"
        String searchText = "01";
        PO_UserListView.executeSearch(driver, searchText);

        //Comprobamos que los resultados son los esperados
        PO_UserListView.checkElementBy(driver, "text", "User010");
        PO_UserListView.checkElementBy(driver, "text", "User011");
        PO_UserListView.checkElementBy(driver, "text", "User012");
        PO_UserListView.checkElementBy(driver, "text", "User013");
        PO_UserListView.checkElementBy(driver, "text", "User014");

        //Y que hay 5 usuarios
        List<WebElement> userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());

        //Comprobamos que estamos en la página 1
        List<WebElement> pageNumbers = SeleniumUtils.waitLoadElementsBy(driver, "class", "page-link",
                PO_View.getTimeout());
        Assertions.assertEquals(4, pageNumbers.size());
        Assertions.assertEquals("1", pageNumbers.get(1).getText());
        Assertions.assertEquals("2", pageNumbers.get(2).getText());
    }

    //[Prueba21] Mostrar el listado de invitaciones de amistad recibidas.
    // Comprobar con un listado que contenga varias invitaciones recibidas.
    @Test
    @Order(21)
    public void PR21() {
        //Iniciamos sesión como usuario estándar
        PO_LoginView.login(driver, "user03@email.com", "user03");

        //Vamos a la vista /friendInvitations/list
        PO_PrivateView.clickSubMenuOption(driver, "friendshipsDropdown", "invitationsMenu");

        //Comprobamos que estamos en la vista friend invitation list
        List<WebElement> result = PO_LoginView.checkElementByKey(driver, "friendInvitation.list.info", PO_Properties.getSPANISH() );
        String checkText = PO_HomeView.getP().getString("friendInvitation.list.info", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText , result.get(0).getText());

        //Comprobamos que hay solicitudes de amistad
        List<WebElement> invList = SeleniumUtils.waitLoadElementsBy(driver, "free", "/h3", PO_View.getTimeout());
        Assertions.assertTrue( invList.size() == 3 );
    }

    // [Prueba23] Mostrar el listado de amigos de un usuario.
    // Comprobar que el listado contiene los amigos que deben ser
    @Test
    @Order(23)
    public void  PR23(){
        //Log in con user
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Ir a la página de listado de amigos
        PO_PrivateView.clickSubMenuOption(driver, "friendshipsDropdown", "friendsMenu");

        //Comprobamos que estamos en la página de listado de amigos
        List<WebElement> result = PO_LoginView.checkElementByKey(driver, "friends.list.info", PO_Properties.getSPANISH() );
        String checkText = PO_HomeView.getP().getString("friends.list.info", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText , result.get(0).getText());

        //Comprobamos que nos salen todos los amigos del usuario
        List<WebElement> friendList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertTrue( friendList.size() == 2 );
    }

    // [Prueba26] Mostrar el listado de publicaciones de un usuario y comprobar
    // que se muestran todas las que existen para dicho usuario.
    @Test
    @Order(28)
    public void PR26(){
        //Log in con user con 3 publicaciones
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Ir a la página de publicaciones propias
        PO_PrivateView.clickSubMenuOption(driver, "postsDropdown", "myPostsMenu");

        //Comprobamos que estamos en la página de listado de publicaciones
        List<WebElement> result = PO_LoginView.checkElementByKey(driver, "posts.list.info", PO_Properties.getSPANISH() );
        String checkText = PO_HomeView.getP().getString("posts.list.info", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText , result.get(0).getText());

        //Comprobamos que hay 3 publicaciones
        List<WebElement> posts = driver.findElements(By.className("card"));;
        Assertions.assertEquals(3, posts.size());
    }

    // [Prueba27] Mostrar el listado de publicaciones de un usuario amigo y comprobar
    // que se muestran todas las que existen para dicho - usuario.
    @Test
    @Order(27)
    public void PR27(){
        //Log in con user
        PO_LoginView.login(driver, "user02@email.com", "user02");
        //Ir a la pagina de listar amigos
        PO_PrivateView.clickSubMenuOption(driver, "friendshipsDropdown", "friendsMenu");

        //Hacemos clic en un amigo con 3 publicaciones
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'btn btn-primary')]");
        elements.get(0).click();

        //Comprobamos que estamos en la página de listado de publicaciones
        List<WebElement> result = PO_LoginView.checkElementByKey(driver, "posts.list.info", PO_Properties.getSPANISH() );
        String checkText = PO_HomeView.getP().getString("posts.list.info", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText , result.get(0).getText());


        //Comprobamos que hay 3 publicaciones
        List<WebElement> posts = driver.findElements(By.className("card"));;
        Assertions.assertEquals(3, posts.size());
    }

    // [Prueba28] Utilizando un acceso vía URL u otra alternativa,
    // tratar de listar las publicaciones de un usuario que no sea amigo
    // del usuario identificado en sesión. Comprobar que el sistema da un error de autorización.
    @Test
    @Order(28)
    public void PR28(){
        //Log in con user
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Intentamos acceder mediante URL a un usuario que no es amigo
        driver.navigate().to(URL+"/post/list/user04@email.com");

        //Comprobamos que estamos en la página de error
        String checkText = PO_View.getP().getString("error.accessDenied.message", PO_Properties.getSPANISH());
        List<WebElement> result = PO_View.checkElementByKey(driver, "error.accessDenied.message", PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    //[Prueba35] Acceder a la lista de amigos de un usuario, y realizar un filtrado para encontrar a un amigo
    //concreto, el nombre a buscar debe coincidir con el de un amigo.
    @Test
    @Order(38)
    public void PR35() {
        initDB();
        // Vamos al inicio de sesión del cliente de la API
        driver.navigate().to(URL + "/apiclient/client.html?w=login");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Lista de amigos";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        List<WebElement> invList =driver.findElements(By.className("card"));
        Assertions.assertEquals( 3, invList.size());
    }

    //[Prueba36] Acceder a la lista de mensajes de un amigo, la lista debe contener al menos tres mensajes.
    @Test
    @Order(39)
    public void PR36() {
        initDB();
        // Vamos al inicio de sesión del cliente de la API
        driver.navigate().to(URL + "/apiclient/client.html?w=login");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Lista de amigos";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        List<WebElement> invList =driver.findElements(By.className("card"));
        Assertions.assertEquals( 3, invList.size());
    }

    //[Prueba37] Acceder a la lista de mensajes de un amigo y crear un nuevo mensaje. Validar que el mensaje
    //aparece en la lista de mensajes
    @Test
    @Order(40)
    public void PR37() {
        initDB();
        // Vamos al inicio de sesión del cliente de la API
        driver.navigate().to(URL + "/apiclient/client.html?w=login");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la página privada de usuario
        String checkText = "Lista de amigos";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        List<WebElement> invList =driver.findElements(By.className("card"));
        Assertions.assertEquals( 3, invList.size());
    }

}
