package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OderingCardTest {

    private ChromeDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        //System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe");
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    //Заполняем все поля. Заявка оформляется успешно
    @Test
    public void shouldSendForm() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+21236554523");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expectedText, actualText, "Текст сообщения не совпадает");
    }

    //Заполняем только поле фамилия и имя
    @Test
    public void enterNameTest() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("button")).click();
        driver.findElement(By.cssSelector(".input_invalid")).findElement(By.cssSelector(".input__sub"));

        // String actualText = (driver.findElement(By.cssSelector(".input_invalid")).findElement(By.cssSelector(".input__sub"))).getText().trim();
        String actualText = (driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
        String expectedText = "Поле обязательно для заполнения";

        assertEquals(expectedText, actualText, "Текст сообщения не совпадает");
    }

    //Отправляем пустую форму
    @Test
    public void emptyFormTest() {
        driver.findElement(By.cssSelector("button")).click();

        String actualText = driver.findElement(By.cssSelector(".input__sub")).getText().trim();
        String expectedText = "Поле обязательно для заполнения";

        assertEquals(expectedText, actualText, "Текст сообщения не совпадает");
    }

    //вводим неверное имя - на латынеце
    @Test
    public void enterNameLatTest() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Xenia");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+21236554523");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        //String actualText = (driver.findElement(By.cssSelector("[data-test-id='name']")).findElement(By.cssSelector(".input__sub"))).getText().trim();
        String actualText = (driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expectedText, actualText, "Текст сообщения не совпадает");
    }

    //вводим телефон, где меньше 11 цифр
    @Test
    public void enterTelNot11Test() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Ксения Хомякова");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+212365");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        //String actualText = (driver.findElement(By.cssSelector("[data-test-id='phone']")).findElement(By.cssSelector(".input__sub"))).getText().trim();
        String actualText = (driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expectedText, actualText, "Текст сообщения не совпадает");
    }

    //вводим всё, кроме галочки согласия
    @Test
    public void enterNotCheckboxBoxTest() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Ксения Хомякова");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+89565448789");
        driver.findElement(By.cssSelector("button")).click();

        boolean actual = driver.findElement(By.cssSelector(".input_invalid")).isDisplayed();
        boolean expected = true;

        assertEquals(expected, actual, "Не совпадает");
    }

    //не вводим имя, а  телефон вводим и ставим галочку
    @Test
    public void enterNotName() {
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+21236554523");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button")).click();

        //String actualText = (driver.findElement(By.cssSelector("[data-test-id='name']")).findElement(By.cssSelector(".input__sub"))).getText().trim();
        String actualText = (driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
        String expectedText = "Поле обязательно для заполнения";

        assertEquals(expectedText, actualText, "Текст сообщения не совпадает");
    }


}
