package tests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class TestBase {
    protected WebDriver driver;
    protected String baseUrl;
    protected List<String> groupPaths;

    @BeforeEach
    public void setUp() {
        loadTestData();
        initDriver();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loadTestData() {
        Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("test_data.json"));
        JsonObject data = gson.fromJson(reader, JsonObject.class);

        baseUrl = data.get("baseUrl").getAsString();
        groupPaths = Arrays.asList(gson.fromJson(data.get("groups"), String[].class));
    }

    private void initDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox":
                FirefoxOptions ffOptions = new FirefoxOptions();
                driver = new FirefoxDriver(ffOptions);
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().window().setSize(new Dimension(1920, 1080));
    }

    protected String getGroupUrl(int index) {
        return baseUrl + groupPaths.get(index);
    }
}
