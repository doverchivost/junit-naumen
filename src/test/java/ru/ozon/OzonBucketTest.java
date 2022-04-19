package ru.ozon;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class OzonBucketTest {

    private WebDriver driver;
    //private String itemName;

    @Before
    public void createDriver() {
        System.setProperty("webdriver.gecko.driver","geckodriver/geckodriver.exe");

        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void addItemToCart() {
        //добавляем кружку в корзину
        String itemAdded = addElementToCart("кружка");
        //переходим в коризну
        driver.get("https://www.ozon.ru/cart");
        //находим текст Корзина*, берем от туда цифру (кол-во элементов в корзине)
        WebElement cart = driver.findElement(By.className("a2l"));
        long itemsAmount = Long.parseLong(cart.getText());
        //название первого элемента в корзине
        String actualItem = driver.findElement(By.cssSelector("span.de1:nth-child(1) > span:nth-child(1)"))
                .getText();
        Assert.assertNotEquals("В корзине нет товаров", 0, itemsAmount);
        Assert.assertEquals("В корзине несколько товаров", 1, itemsAmount);
        Assert.assertEquals("В корзину добавлен другой товар", actualItem, itemAdded);
    }

    @Test
    public void deleteItem() {
        //переходим в корзину
        driver.get("https://www.ozon.ru/cart");
        //есди корзина пуста добавим в нее товар
        if (driver.findElement(By.className("ka9")).getText().contains("Корзина пуста")) {
            addElementToCart("чайная ложка");
            driver.get("https://www.ozon.ru/cart");
        }
        //нажмем "удалить" в строке первого элемента в коризне
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div/div[3]/div[4]/div[1]/div[1]/div/div[2]/div[2]/div[1]/div/div/div/div[2]/div/div[2]/div/div[2]/button/span/span"))
                .click();
        //подтвердим удаление во всплывшем окне, нажав "Удалить"
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div/div/section/div[3]/div/button/span/span"))
                .click();
        //Убедимся, что появился текст "Корзина пуста"
        String cartText = driver.findElement(By.className("ka9")).getText();
        Assert.assertEquals("В корзине остались элементы", cartText, "Корзина пуста");
    }

    @After
    public void closeDriver() {
        driver.quit();
    }

    public String addElementToCart(String name) {
        //переходим на главную страницу
        driver.get("https://www.ozon.ru/");
        //в поисковике ищем название товара
        driver.findElement(By.cssSelector("input.tv2:nth-child(1)"))
                .sendKeys(name);
        //нажимаем на поиск
        driver.findElement(By.className("v2t")).click();
        //переходим в каточку певрого товара в списке найденных товаров товаров
        driver.findElements(By.className("tile-hover-target")).get(0).click();
        String itemName = driver.findElement(By.className("yk1")).getText();
        //нажимаем на "Добавить в крозину"
        driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div[4]/div[3]/div[3]/div/div[15]/div[1]/div/div/div/div[1]/button"))
                .click();
        return itemName;
    }
}
