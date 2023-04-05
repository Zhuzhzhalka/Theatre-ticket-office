package com.example.theatreoffice;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

public class TheatreOfficeSeleniumTests {
    String URL = "http://localhost:8080/";
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/home/fedor/Projects/Java/geckodriver");
        FirefoxOptions options = new FirefoxOptions();
        driver = new FirefoxDriver(options);
        driver.manage().window().setSize(new Dimension(1200, 1000));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        Duration duration = Duration.parse("PT10S");
        wait = new WebDriverWait(driver, duration);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    @Test(priority = 0)
    public void testHomeAndPerformanceDetails() {
        driver.get(URL);

        Assert.assertEquals(driver.getTitle(), "Театральная касса");
        Assert.assertEquals(driver.findElement(By.cssSelector("h1")).getText(), "Театральная касса");
        Assert.assertEquals(driver.findElement(By.cssSelector("a[href='/theatres']")).getText(), "Театры");
        Assert.assertEquals(driver.findElement(By.cssSelector("a[href='/performances']")).getText(), "Представления");

        WebElement button = driver.findElement(By.xpath("//a[@href='/performances/4']"));
        button.click();
        wait.until(stalenessOf(button));
        Assert.assertEquals(driver.getTitle(), "Представление");
        Assert.assertEquals(driver.findElement(By.cssSelector("h3")).getText(), "Анна Каренина");
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(), 'длительность')]")).getText(), "длительность: 02:40");
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(), 'жанр')]")).getText(), "жанр: Мюзикл");
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(), 'рейтинг')]")).getText(), "рейтинг: 9.6");
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(), 'Режиссер')]")).getText(), "Режиссер: Безруков Сергей");
        Assert.assertEquals(driver.findElement(By.xpath("//a[@href='/performances/4/edit']")).getText(), "Редактировать");
        Assert.assertEquals(driver.findElement(By.xpath("//form[@action='/performances/4/remove']")).getText(), "Удалить");
        Assert.assertEquals(driver.findElement(By.xpath("//a[@href='/performances/4/add_schedule']")).getText(), "Добавить расписание");
        Assert.assertEquals(driver.findElement(By.xpath("//a[@href='/performances/4/8']")).getText(), "Билеты");
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(), 'актер')]")).getText(), "Помидоров Андрей -- актер");
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(), 'Театр')]")).getText(), "Театр: Красноярский театр кукол; дата: 19:00 25-10-2020");

    }
    @Test(priority = 1)
    public void testTheatres() {
        driver.get(URL);

        WebElement button = driver.findElement(By.xpath("//a[@href='/theatres']"));
        button.click();
        wait.until(stalenessOf(button));

        Assert.assertEquals(driver.getTitle(), "Театры");
        WebElement sortElement = driver.findElement(By.xpath("//form[@action='/theatres']"));
        List<WebElement> allSortChildElements = sortElement.findElements(By.xpath("*"));
        WebElement sortDivChildElement = allSortChildElements.get(1);
        allSortChildElements = sortDivChildElement.findElements(By.xpath("*"));
        WebElement perfTitleSearchElement = allSortChildElements.get(0);

        String titleSearched = "Война и мир";
        perfTitleSearchElement.sendKeys(titleSearched);

        sortElement = driver.findElement(By.xpath("//form[@action='/theatres']"));
        allSortChildElements = sortElement.findElements(By.xpath("*"));
        sortDivChildElement = allSortChildElements.get(2);
        allSortChildElements = sortDivChildElement.findElements(By.xpath("*"));

        WebElement submitBtn = allSortChildElements.get(0);
        submitBtn.submit();
        wait.until(stalenessOf(submitBtn));

        List<WebElement> theatres = driver.findElements(By.name("theatres"));
        Assert.assertEquals(theatres.size(), 1);
        WebElement theatreElement = theatres.get(0);
        Assert.assertEquals(theatreElement.findElement(By.cssSelector("h3")).getText(), "Молодежный театр");
    }

    @Test(priority = 2)
    public void testPerformances() {
        driver.get(URL);

        WebElement button = driver.findElement(By.xpath("//a[@href='/performances']"));
        button.click();
        wait.until(stalenessOf(button));

        Assert.assertEquals(driver.getTitle(), "Представления");

        WebElement sortElement = driver.findElement(By.xpath("//form[@action='/performances']"));
        List<WebElement> allSortChildElements = sortElement.findElements(By.xpath("*"));
        WebElement sortDivChildElement = allSortChildElements.get(0);
        allSortChildElements = sortDivChildElement.findElements(By.xpath("*"));
        WebElement genreSortElement = allSortChildElements.get(1);
        Select drpGenre = new Select(genreSortElement);
        drpGenre.selectByVisibleText("Комедия");

        allSortChildElements = sortElement.findElements(By.xpath("*"));
        sortDivChildElement = allSortChildElements.get(2);
        allSortChildElements = sortDivChildElement.findElements(By.xpath("*"));
        WebElement submitBtn = allSortChildElements.get(0);
        submitBtn.submit();
        wait.until(stalenessOf(submitBtn));

        List<WebElement> divPerformanceElements = driver.findElements(By.name("perfs"));
        Assert.assertEquals(divPerformanceElements.size(), 1);
        WebElement comedyPerformance = divPerformanceElements.get(0);
        Assert.assertEquals(comedyPerformance.findElement(By.cssSelector("h3")).getText(), "Свадьба");
        Assert.assertEquals(comedyPerformance.findElement(By.cssSelector("a[href='/performances/9']")).getText(), "Детали");
    }

    @Test(priority = 3)
    public void testTickets() {
        driver.get(URL);

        WebElement button = driver.findElement(By.xpath("//a[@href='/performances/4']"));
        button.click();

        List<WebElement> divElements = driver.findElements(By.name("schedule"));
        Assert.assertEquals(divElements.size(), 1);
        WebElement scheduleElement = divElements.get(0);
        WebElement ticketsBtn = scheduleElement.findElement(By.cssSelector("a[href='/performances/4/8']"));
        ticketsBtn.click();
        wait.until(stalenessOf(ticketsBtn));

        Assert.assertEquals(driver.getTitle(), "Билеты");
        WebElement buyTicketsBtn = driver.findElement(By.cssSelector("a[href='/performances/4/8/add']"));
        buyTicketsBtn.click();

        String testFirstName = "Тестов";
        String testLastName = "Тест";
        String testSeat = "1";

        List<WebElement> inputFields = driver.findElements(By.cssSelector("input"));
        inputFields.get(0).sendKeys(testFirstName);
        inputFields.get(1).sendKeys(testLastName);
        inputFields.get(2).sendKeys(testSeat);

        WebElement submitBtn = driver.findElement(By.cssSelector("button"));
        submitBtn.click();

        List<WebElement> tickets = driver.findElements(By.name("tickets"));
        Assert.assertEquals(tickets.size(), 1);
        WebElement ticket = tickets.get(0);
        List<WebElement> ticketInfo = ticket.findElements(By.cssSelector("p"));
        Assert.assertEquals(ticketInfo.size(), 3);
        Assert.assertEquals(ticketInfo.get(0).getText(), "Фамилия: Тестов");
        Assert.assertEquals(ticketInfo.get(1).getText(), "Имя: Тест");
        Assert.assertEquals(ticketInfo.get(2).getText(), "Место: 1");

        scheduleElement = driver.findElement(By.name("schedule"));
        Assert.assertEquals(scheduleElement.findElement(By.xpath("//p[contains(text(), 'Партер')]")).getText(), "Партер: 99/100 (100 руб.)");
    }

    @Test(priority = 4)
    public void testEditTheatre() {
        driver.get(URL);

        WebElement button = driver.findElement(By.xpath("//a[@href='/theatres']"));
        button.click();
        wait.until(stalenessOf(button));

        List<WebElement> allDivElements = driver.findElements(By.name("theatres"));
        WebElement theatreElement = allDivElements.get(0);
        button = theatreElement.findElement(By.cssSelector("a"));
        button.click();
        wait.until(stalenessOf(button));

        Assert.assertEquals(driver.getTitle(), "Театр");

        WebElement theatreInfo = driver.findElements(By.cssSelector("div")).get(2);
        button = theatreInfo.findElement(By.cssSelector("a"));
        button.click();
        wait.until(stalenessOf(button));

        Assert.assertEquals(driver.getTitle(), "Редактирование театра");

        List<WebElement> inputFields = driver.findElements(By.cssSelector("input"));
        String testTheatreName = "Новый";
        String testGroundSeats = "123";
        String testBalconySeats = "456";
        String testMezzanineSeats = "78";
        String testBuilding = "45";
        String testStreet = "Новая";
        String testTown = "Новгород";
        String testCountry = "Россия";
        inputFields.get(0).clear();
        inputFields.get(1).clear();
        inputFields.get(2).clear();
        inputFields.get(3).clear();
        inputFields.get(4).clear();
        inputFields.get(5).clear();
        inputFields.get(6).clear();
        inputFields.get(7).clear();
        inputFields.get(0).sendKeys(testTheatreName);
        inputFields.get(1).sendKeys(testGroundSeats);
        inputFields.get(2).sendKeys(testBalconySeats);
        inputFields.get(3).sendKeys(testMezzanineSeats);
        inputFields.get(4).sendKeys(testBuilding);
        inputFields.get(5).sendKeys(testStreet);
        inputFields.get(6).sendKeys(testTown);
        inputFields.get(7).sendKeys(testCountry);

        WebElement submitBtn = driver.findElement(By.cssSelector("button"));
        submitBtn.submit();
        wait.until(stalenessOf(submitBtn));

        Assert.assertEquals(driver.getTitle(), "Театр");

        theatreInfo = driver.findElements(By.cssSelector("div")).get(2);

        Assert.assertEquals(driver.findElement(By.cssSelector("h3")).getText(), "Новый");
        Assert.assertEquals(theatreInfo.findElement(By.xpath("//p[contains(text(), 'дом')]")).getText(), "дом: 45");
        Assert.assertEquals(theatreInfo.findElement(By.xpath("//p[contains(text(), 'улица')]")).getText(), "улица: Новая");
        Assert.assertEquals(theatreInfo.findElement(By.xpath("//p[contains(text(), 'город')]")).getText(), "город: Новгород");
        Assert.assertEquals(theatreInfo.findElement(By.xpath("//p[contains(text(), 'страна')]")).getText(), "страна: Россия");
        Assert.assertEquals(theatreInfo.findElement(By.xpath("//p[contains(text(), 'Партер')]")).getText(), "Партер: 123");
        Assert.assertEquals(theatreInfo.findElement(By.xpath("//p[contains(text(), 'Балкон')]")).getText(), "Балкон: 456");
        Assert.assertEquals(theatreInfo.findElement(By.xpath("//p[contains(text(), 'Бельэтаж')]")).getText(), "Бельэтаж: 78");
    }

    @Test(priority = 5)
    public void testEditPerformance() {
        driver.get(URL);

        WebElement button = driver.findElement(By.xpath("//a[@href='/performances']"));
        button.click();
        wait.until(stalenessOf(button));

        List<WebElement> allDivElements = driver.findElements(By.name("perfs"));
        WebElement theatreElement = allDivElements.get(0);
        button = theatreElement.findElement(By.cssSelector("a"));
        button.click();
        wait.until(stalenessOf(button));

        Assert.assertEquals(driver.getTitle(), "Представление");

        WebElement perfInfo = driver.findElements(By.cssSelector("div")).get(2);
        button = perfInfo.findElement(By.name("editBtn"));
        button.click();
        wait.until(stalenessOf(button));

        Assert.assertEquals(driver.getTitle(), "Редактирование представления");

        List<WebElement> inputFields = driver.findElements(By.cssSelector("input"));
        String testPerfTitle = "Тесто";
        String testDuration = "01:01";
        String testGenre = "Комедия";
        String testRating = "9.9";
        String testDirFN = "Тестов";
        String testDirLN = "Тест";
        inputFields.get(0).clear();
        inputFields.get(1).clear();
        inputFields.get(2).clear();
        inputFields.get(3).clear();
        inputFields.get(4).clear();
        inputFields.get(5).clear();
        inputFields.get(0).sendKeys(testPerfTitle);
        inputFields.get(1).sendKeys(testDuration);
        inputFields.get(2).sendKeys(testGenre);
        inputFields.get(3).sendKeys(testRating);
        inputFields.get(4).sendKeys(testDirFN);
        inputFields.get(5).sendKeys(testDirLN);

        WebElement submitBtn = driver.findElement(By.cssSelector("button"));
        submitBtn.submit();
        wait.until(stalenessOf(submitBtn));

        perfInfo = driver.findElements(By.cssSelector("div")).get(2);

        Assert.assertEquals(perfInfo.findElement(By.cssSelector("h3")).getText(), "Тесто");
        Assert.assertEquals(perfInfo.findElement(By.xpath("//p[contains(text(), 'длительность')]")).getText(), "длительность: 01:01");
        Assert.assertEquals(perfInfo.findElement(By.xpath("//p[contains(text(), 'жанр')]")).getText(), "жанр: Комедия");
        Assert.assertEquals(perfInfo.findElement(By.xpath("//p[contains(text(), 'рейтинг')]")).getText(), "рейтинг: 9.9");
        Assert.assertEquals(perfInfo.findElement(By.xpath("//p[contains(text(), 'Режиссер')]")).getText(), "Режиссер: Тестов Тест");
    }

    @AfterClass
    public void quit() {
        driver.quit();
    }
}
