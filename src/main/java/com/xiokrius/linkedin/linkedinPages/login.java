package com.xiokrius.linkedin.linkedinPages;

import com.xiokrius.linkedin.ConfigManager;
import com.xiokrius.linkedin.Environment.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class login extends BasePage {


    private String login = ConfigManager.getProperty("loginInLink");
    private String pass = ConfigManager.getProperty("passInLink");

    public login(WebDriver driver) {
        super(driver);
    }

    public void loginInput() {

        WebElement loginInpuElement = waitAndGetClickableElement(By.id("username"));

        loginInpuElement.sendKeys(login);

    }

    public void passWordInput() {
        WebElement passElement = waitAndGetClickableElement(By.id("password"));

        passElement.sendKeys(pass);
    }

    public void okButton() {

        WebElement okButton = waitAndGetClickableElement(By.xpath("//button[@data-litms-control-urn='login-submit']"));

        okButton.click();
    }

}

