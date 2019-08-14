import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Description;

import java.util.concurrent.TimeUnit;

class Triangle {
    private WebDriver driver;
    private String pageUrl = "https://team.cft.ru/triangle/zadanie/triangle.html?token=7c39dd71eddd4814bda3c18a08489297";
    private String isoscelesType = "Равнобедренный";
    private String equalsidedType = "Равносторонний";
    private String rectangularType = "Прямоугольный";
    private String scaleneType = "Разносторонний";
    private String notTriangleType = "Не треугольник";

    @BeforeEach
    void setUp(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to(pageUrl);
    }

    @AfterEach
    void switchOff(){
        driver.close();
    }

    @Test
    @Description("Проверка определения равнобедренного треугольника")
    void isIsoscelesTriangle(){
        String triangleType = getTriangleType(driver, "3", "3", "4");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    @Description("Проверка определения равностороннего треугольника")
    void isEqualsidedTriangle(){
        String triangleType = getTriangleType(driver, "3", "3", "3");
        Assertions.assertEquals(equalsidedType, triangleType);
    }

    @Test
    @Description("Проверка определения прямоугольного треугольника")
    void isRectangularTriangle(){
        String triangleType = getTriangleType(driver, "3", "4", "5");
        Assertions.assertEquals(rectangularType, triangleType);
    }

    @Test
    @Description("Ввод данных треугольника с разными сторонами (не прямоугольного)")
    void isScaleneTriangle(){
        String triangleType = getTriangleType(driver, "6", "4", "5");
        Assertions.assertEquals(scaleneType, triangleType);
    }

    @Test
    @Description("Ввод во все поля цифры 0")
    void allZero(){
        String triangleType = getTriangleType(driver, "0", "0", "0");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    @Description ("Ввод во все поля отрицательных чисел")
    void allNegativeNumbers(){
        String triangleType = getTriangleType(driver, "-4", "-5", "-6");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    @Description("Проверка определения “Не треугольник”, если одна сторона >= сумме двух других сторон")
    void isNotTriangle(){
        String triangleType = getTriangleType(driver, "1", "2", "5");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    @Description("Поле “Сторона А”: ввод цифры с пробелами в начале и в конце")
    void spacesBeforeAndAfterNumberA(){
        String triangleType = getTriangleType(driver, "  4  ", "4", "5");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    @Description("Поле “Сторона А”: ввод дроби")
    void fractionA(){
        String triangleType = getTriangleType(driver, "3.8", "4", "5");
        Assertions.assertEquals(scaleneType, triangleType);
    }

    @Test
    @Description("Поле “Сторона А”: ввод отрицательного числа")
    void negativeNumberA(){
        String triangleType = getTriangleType(driver, "-4", "4", "5");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    @Description("Поле “Сторона Б”: ввод цифры с пробелами в начале и в конце")
    void spacesBeforeAndAfterNumberB(){
        String triangleType = getTriangleType(driver, "5", "  4  ", "5");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    @Description("Поле “Сторона Б”: ввод дроби")
    void fractionB(){
        String triangleType = getTriangleType(driver, "5", "3.8", "5");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    @Description("Поле “Сторона Б”: ввод отрицательного числа")
    void negativeNumberB(){
        String triangleType = getTriangleType(driver, "5", "-4", "5");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    @Description("Поле “Сторона В”: ввод цифры с пробелами в начале и в конце")
    void spacesBeforeAndAfterNumberV(){
        String triangleType = getTriangleType(driver, "5", "4", "  4  ");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    @Description("Поле “Сторона В”: ввод дроби")
    void fractionV(){
        String triangleType = getTriangleType(driver, "5", "4", "3.8");
        Assertions.assertEquals(scaleneType, triangleType);
    }

    @Test
    @Description("Поле “Сторона В”: ввод отрицательного числа")
    void negativeNumberV(){
        String triangleType = getTriangleType(driver, "5", "4", "-5");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    private String getTriangleType(WebDriver driver, String sideA, String  sideB, String  sideV) {
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/table/tbody/tr[1]/td[1]/input")).sendKeys(sideA);
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/table/tbody/tr[2]/td[1]/input")).sendKeys(sideB);
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/table/tbody/tr[3]/td[1]/input")).sendKeys(sideV);

        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/table/tbody/tr[4]/td/button")).click();

        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);

        String triangleType = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/table/tbody/tr[5]/td/div")).getText();
        return triangleType;
    }
}