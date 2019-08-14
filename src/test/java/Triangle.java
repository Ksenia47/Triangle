import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    void isIsoscelesTriangle(){
        String triangleType = getTriangleType(driver, "3", "3", "4");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    void isEqualsidedTriangle(){
        String triangleType = getTriangleType(driver, "3", "3", "3");
        Assertions.assertEquals(equalsidedType, triangleType);
    }

    @Test
    void isRectangularTriangle(){
        String triangleType = getTriangleType(driver, "3", "4", "5");
        Assertions.assertEquals(rectangularType, triangleType);
    }

    @Test
    void isScaleneTriangle(){
        String triangleType = getTriangleType(driver, "6", "4", "5");
        Assertions.assertEquals(scaleneType, triangleType);
    }

    @Test
    void allZero(){
        String triangleType = getTriangleType(driver, "0", "0", "0");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    void allNegativeNumbers(){
        String triangleType = getTriangleType(driver, "-4", "-5", "-6");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    void isNotTriangle(){
        String triangleType = getTriangleType(driver, "1", "2", "5");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    void spacesBeforeAndAfterNumberA(){
        String triangleType = getTriangleType(driver, "  4  ", "4", "5");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    void fractionA(){
        String triangleType = getTriangleType(driver, "3.8", "4", "5");
        Assertions.assertEquals(scaleneType, triangleType);
    }

    @Test
    void negativeNumberA(){
        String triangleType = getTriangleType(driver, "-4", "4", "5");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    void spacesBeforeAndAfterNumberB(){
        String triangleType = getTriangleType(driver, "5", "  4  ", "5");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    void sractionB(){
        String triangleType = getTriangleType(driver, "5", "3.8", "5");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    void negativeNumberB(){
        String triangleType = getTriangleType(driver, "5", "-4", "5");
        Assertions.assertEquals(notTriangleType, triangleType);
    }

    @Test
    void spacesBeforeAndAfterNumberV(){
        String triangleType = getTriangleType(driver, "5", "4", "  4  ");
        Assertions.assertEquals(isoscelesType, triangleType);
    }

    @Test
    void fractionV(){
        String triangleType = getTriangleType(driver, "5", "4", "3.8");
        Assertions.assertEquals(scaleneType, triangleType);
    }

    @Test
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