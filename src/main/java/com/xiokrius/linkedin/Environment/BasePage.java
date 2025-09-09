package com.xiokrius.linkedin.Environment;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;



public abstract class BasePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public BasePage(WebDriver driver) {

        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    // Для доступа к драйверу

    protected WebDriver getDriver() {
        return this.driver;
    }

    protected WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }
        return wait;
    }

    protected JavascriptExecutor getJS() {
        return this.js;
    }

    // Для перехода по урлу

    protected void openUrl(String url) {
        driver.get(url);
        System.out.println("Открыт URL: " + url);
        System.out.println("Текущий URL в браузере: " + driver.getCurrentUrl());

    }

    protected void switchToIframe() {
        WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("designer-client-frame")));
        driver.switchTo().frame(iframe);
        System.out.println("Перешли в фрейм.");

    }

    protected void returnToMainContent() {
        driver.switchTo().defaultContent();
        System.out.println("Возвращаемся в основной контент страницы.");
    }

    protected void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        System.out.println("скроллим до : " + element);
    }

    protected void clickJSToElement(WebElement element) {

        js.executeScript("arguments[0].click();", element);
        System.out.println("клик выполнен." + element);
    }

    // Метод для заполнения значения через JavaScript
    protected void fillSelectWithJS(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1];", element, value);
        System.out.println("Заполнили поле: " + value);
    }

    // Метод для выбора значения в select
    protected void selectDropdownByValue(WebElement selectElement, String value) {
        Select select = new Select(selectElement);
        select.selectByValue(value);
        System.out.println("Выбрали значение в select: " + value);
    }

    protected void scrollToElementHorizontally(WebElement scrollContainer, WebElement targetElement) {
        js.executeScript(
                "const container = arguments[0];" +
                        "const target = arguments[1];" +
                        "const containerWidth = container.offsetWidth;" +
                        "const targetLeft = target.getBoundingClientRect().left;" +
                        "const containerLeft = container.getBoundingClientRect().left;" +
                        "const targetOffset = targetLeft - containerLeft;" +
                        "const scrollAmount = targetOffset - containerWidth / 2 + target.offsetWidth / 2;"
                        +
                        "container.scrollLeft += scrollAmount;",
                scrollContainer, targetElement);

        System.out.println("нашли скроллер " + scrollContainer + "сделал" + targetElement);
    }

    protected void setInputValue(WebElement element, String value) {
        js.executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));",
                element, value);
        System.out.println("Внес в поле " + element + "значение" + value);
    }

    protected void setInputJS(WebElement element, String value) {
        js.executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('keydown', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('keyup', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                element, value);
    }

    protected void fillAndActivateSelect(WebElement select, String value) {
        // 1. Заполняем через JS
        fillSelectWithJS(select, value);

        // 2. Имитируем полную последовательность действий пользователя
        js.executeScript("""
                const select = arguments[0];
                select.dispatchEvent(new Event('focus'));
                select.dispatchEvent(new Event('change', {bubbles: true}));
                select.dispatchEvent(new Event('blur'));
                """, select);

        // 3. Даём время на обработку
        try {
            System.out.println("ес");
        } catch (Exception e) {
            System.out.println("ес1");
        }

    }

    protected void checkLink(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int responseCode = connection.getResponseCode();
            System.out.println(url + " → " + responseCode);
        } catch (Exception e) {
            System.out.println(url + " → Ошибка: " + e.getMessage());
        }
    }

    protected WebElement waitAndGetPresentElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            System.out.println("Нашли элемент: " + locator);
            return element;
        } catch (Exception e) {
            System.out.println("Элемент не найден: " + locator);
            throw new RuntimeException("Не удалось найти элемент " + locator + " за указанное время", e);
        }
    }

    protected WebElement waitAndGetClickableElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            System.out.println("Нашли элемент: " + locator);
            return element;
        } catch (Exception e) {
            System.out.println("Элемент не найден: " + locator);
            throw new RuntimeException("Не удалось найти элемент " + locator + " за указанное время", e);
        }
    }

    protected WebElement waitAndGetVisibleElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element;
        } catch (Exception e) {
            System.out.println("Элемент не найден: " + locator);
            throw new RuntimeException("Не удалось найти элемент " + locator + " за указанное время", e);
        }
    }

    protected WebElement driverGetFindElement(By locator) {
        return driver.findElement((locator));
    }

    protected WebDriverWait createWait(int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    public void inIframe(Runnable action) {
        switchToIframe();
        action.run();
        returnToMainContent();
    }

}
