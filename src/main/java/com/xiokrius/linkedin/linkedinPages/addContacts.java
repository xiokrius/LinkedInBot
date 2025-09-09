package com.xiokrius.linkedin.linkedinPages;

import com.xiokrius.linkedin.Environment.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;


public class AddContacts extends BasePage {

    private static final Logger logger = LogManager.getLogger(AddContacts.class);

    public AddContacts(WebDriver driver) {
        super(driver);
    }

    public void locatorTest() {

        createWait(20);

        By connectButtons = By.xpath(
                "//span[@class='artdeco-button__text' and normalize-space()='Установить контакт']/ancestor::button[1]");

        // ждём, пока появится хотя бы один
        getWait().until(d -> !getDriver().findElements(connectButtons).isEmpty());

        List<WebElement> elements = getDriver().findElements(connectButtons);
        logger.info("Найдено кнопок: " + elements.size());

        for (WebElement el : elements) {
            try {
                logger.info("HTML: " + el.getAttribute("outerHTML"));
                getJS().executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                getWait().until(ExpectedConditions.elementToBeClickable(el)).click();
            } catch (Exception e) {
                logger.warn("Обычный клик не сработал, кликаю через JS: " + e.getMessage());
                getJS().executeScript("arguments[0].click();", el);
            }
        }
    }

    public void locatortest2() {

        while (true) {

            By connectButtonsOne = By.xpath(
                    "//span[@class='artdeco-button__text' and normalize-space()='Установить контакт']/ancestor::button[1]");

            getWait().until(d -> !getDriver().findElements(connectButtonsOne).isEmpty());

            // 1. Находим все кнопки "Установить контакт"
            List<WebElement> connectButtons = getDriver().findElements(
                    By.xpath(
                            "//span[@class='artdeco-button__text' and normalize-space()='Установить контакт']/ancestor::button[1]"));

            logger.info("Найдено кнопок: " + connectButtons.size());

            if (connectButtons.isEmpty()) {
                // если кнопок нет — пробуем перейти на следующую страницу
                if (!goToNextPage()) {
                    break; // если страниц больше нет — выходим
                }
                continue;
            }

            // 2. Цикл по кнопкам
            for (WebElement btn : connectButtons) {
                try {
                    getJS().executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
                    getWait().until(ExpectedConditions.elementToBeClickable(btn)).click();
                    logger.info("Кликнул по кнопке 'Установить контакт'");

                    // 3. Проверка, появилось ли модальное окно
                    try {
                        WebElement sendBtn = getWait().until(ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//span[normalize-space()='Отправить без заметки']/ancestor::button[1]")));

                        if (sendBtn.isDisplayed() && sendBtn.isEnabled()) {
                            getWait().until(ExpectedConditions.elementToBeClickable(sendBtn)).click();
                            logger.info("Нажал 'Отправить без заметки'");
                        }
                    } catch (Exception te) {

                    }

                    Thread.sleep(500); // пауза, чтобы LinkedIn не блокировал
                } catch (Exception e) {
                    logger.warn("Не удалось кликнуть по кнопке: " + e.getMessage());
                }
            }

            // 4. После прохода по кнопкам → переход на следующую страницу
            if (!goToNextPage()) {
                break;
            }
        }

    }

    public void test() {
        // Loop through pages until no next page is available

        createWait(20);

        while (true) {
            // Find all target buttons on the page
            List<WebElement> elements = getDriver().findElements(
                    By.cssSelector(".artdeco-button.artdeco-button--2.artdeco-button--secondary.ember-view"));

            logger.info("Найдено элементов: " + elements.size());

            if (elements.isEmpty()) {
                // Try to paginate if nothing is found
                if (!goToNextPage()) {
                    break;
                }
                continue;
            }

            for (WebElement element : elements) {
                try {

                    getWait().until(driver1 -> element.isDisplayed() || element.isEnabled());

                    if (element.isDisplayed()) {
                        getWait().until(ExpectedConditions.elementToBeClickable(element)).click();
                    } else {

                        getJS().executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                        getWait().until(ExpectedConditions.elementToBeClickable(element)).click();
                        // ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                    }
                } catch (Exception ignore) {

                    try {

                        getJS().executeScript("window.scrollBy(0, 300);");
                        getWait().until(ExpectedConditions.elementToBeClickable(element)).click();
                    } catch (Exception ignoredAgain) {
                        // Skip this element if still failing
                    }
                }
            }

            // After processing current list, go to next page if possible
            if (!goToNextPage()) {
                break;
            }
        }
    }

    public boolean goToNextPage() {
        try {
            WebElement nextBtn = getDriver().findElement(By.xpath("//button[@aria-label='Далее']"));
            if (nextBtn.isDisplayed() && nextBtn.isEnabled()) {
                getJS().executeScript("arguments[0].scrollIntoView({block:'center'});", nextBtn);
                getWait().until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
                Thread.sleep(1000); // ждём подгрузку
                logger.info("Перешёл на следующую страницу");
                return true;
            }
        } catch (Exception e) {
            logger.info("Кнопка 'Далее' не найдена — страниц больше нет");
        }
        return false;
    }

}
