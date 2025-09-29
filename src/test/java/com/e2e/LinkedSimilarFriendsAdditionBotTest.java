package com.e2e;

import com.xiokrius.linkedin.linkedinPages.addContactsNevad;
import com.xiokrius.linkedin.linkedinPages.login;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;

import java.time.Duration;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;


import org.openqa.selenium.JavascriptExecutor;

public class LinkedSimilarFriendsAdditionBotTest {

    String url = "https://www.linkedin.com/mynetwork/grow/";

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeClass
    public void setup() {

        ChromeOptions options = new ChromeOptions();

           if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            // для Linux (Docker, CI/CD)
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
        }

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        
        driver.manage().window().maximize();
        driver.get(url);
        
    }
    @Test(priority = 1)
    public void login() {
        login ligAp = new login(driver);
        ligAp.loginInput();
        ligAp.passWordInput();
        ligAp.okButton();
    }

    @Test(priority = 2)
    public void addContact() {
        addContactsNevad addCont = new addContactsNevad(driver);
        addCont.goToPageContacts();
        addCont.addMeFriend();
        // addCont.loca();
    }
    @AfterClass
    public void teardown() {
    driver.quit();
    }

}

