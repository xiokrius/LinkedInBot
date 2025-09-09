package com.xiokrius.linkedin.linkedinPages;

import java.util.List;

import com.xiokrius.linkedin.Environment.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


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

        while (true) {
            By connectButtons = By.xpath("//span[normalize-space()='Установить контакт']/ancestor::button[1]");
            List<WebElement> elements = getDriver().findElements(connectButtons);
            logger.info("Найдено кнопок: " + elements.size());

            if (elements.isEmpty()) {
                // Если кнопок нет, скроллим в конец страницы
                getJS().executeScript("window.scrollTo(0, document.body.scrollHeight);");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                // Проверяем заново
                elements = getDriver().findElements(connectButtons);
                if (elements.isEmpty()) {
                    logger.info("Больше кнопок нет на странице");
                }

            } else {
                // Скроллим к последней кнопке, чтобы подгрузились новые
                WebElement last = elements.get(elements.size() - 1);
                getJS().executeScript("arguments[0].scrollIntoView({block:'center'});", last);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            for (WebElement el : elements) {
                try {
                    logger.info("HTML: " + el.getAttribute("outerHTML"));
                    getJS().executeScript("arguments[0].scrollIntoView({block:'center'});", el);

                    // Рандомная задержка перед кликом
                    Thread.sleep(500);

                    getWait().until(ExpectedConditions.elementToBeClickable(el)).click();
                    logger.info("Кликнул по кнопке");
                } catch (Exception e) {
                    logger.warn("Обычный клик не сработал, кликаю через JS: " + e.getMessage());
                    getJS().executeScript("arguments[0].click();", el);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
            }
        }
    }
}
