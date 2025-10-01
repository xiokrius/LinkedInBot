package com.xiokrius.linkedin.linkedinPages;

import java.time.Duration;
import java.util.List;

import com.xiokrius.linkedin.Environment.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class addContactsNevad extends BasePage {

    private static final Logger logger = LogManager.getLogger(AddContacts.class);

    public addContactsNevad(WebDriver driver) {

        super(driver);
    }

    public void goToPageContacts() {

        WebElement people = waitAndGetClickableElement(By.xpath(
                "//button[@aria-label='Показать все рекомендации – люди, которых вы можете знать, с учетом ваших недавних действий']"));

        people.click();

    }

    public void addMeFriend() {
        createWait(20);

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));

        // Основной контейнер с кнопками
        WebElement activeContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@data-sdui-screen='com.linkedin.sdui.flagshipnav.mynetwork.CohortSeeAll']")));

        // Локатор кнопки "Установить контакт"
        By connectButtons = By.xpath("//span[normalize-space()='Установить контакт']/ancestor::button[1]");

        while (true) {
            // Ищем все доступные кнопки
            List<WebElement> elements = activeContainer.findElements(connectButtons);
            logger.info("Найдено кнопок: " + elements.size());

            if (elements.isEmpty()) {
                getJS().executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", activeContainer);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {
                }

                // После скролла проверяем ещё раз
                elements = activeContainer.findElements(connectButtons);
                if (elements.isEmpty()) {
                    logger.info("Больше кнопок нет на странице, выходим.");
                    break; // завершаем цикл
                }
            }

            // кликаем по первой
            while (!elements.isEmpty()) {
                try {
                    WebElement el = activeContainer.findElements(connectButtons).get(0);

                    logger.info("HTML: " + el.getAttribute("outerHTML"));
                    getJS().executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                    Thread.sleep(300);

                    wait.until(ExpectedConditions.elementToBeClickable(el)).click();
                    logger.info("Кликнул по кнопке");
                } catch (StaleElementReferenceException e) {
                    logger.warn("Элемент устарел, пробую снова...");
                } catch (Exception e) {
                    logger.warn("Обычный клик не сработал, пробую через JS: " + e.getMessage());
                    try {
                        WebElement el = activeContainer.findElements(connectButtons).get(0);
                        getJS().executeScript("arguments[0].click();", el);
                    } catch (Exception ex) {
                        logger.warn("JS-клик тоже не сработал: " + ex.getMessage());
                    }
                }

                try {
                    Thread.sleep(500); // задержка
                } catch (InterruptedException ignored) {
                }

                // Обновляем список кнопок после клика
                elements = activeContainer.findElements(connectButtons);
            }

            // скроллим вниз для загрузки новых
            getJS().executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", activeContainer);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
