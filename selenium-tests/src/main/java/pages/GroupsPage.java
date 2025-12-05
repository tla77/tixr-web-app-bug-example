package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class GroupsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By LINKS_WRAPPER = By.cssSelector("div.links-wrapper");
    private static final By HOME_LINK = By.cssSelector("div.home-link.hide-mobile");

    public GroupsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void load(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.presenceOfElementLocated(HOME_LINK));
    }

    public boolean hasLinksWrapper() {
        List<WebElement> elements = driver.findElements(LINKS_WRAPPER);
        return !elements.isEmpty();
    }

    public boolean hasHomeLink() {
        List<WebElement> elements = driver.findElements(HOME_LINK);
        return !elements.isEmpty();
    }

    public Rectangle getLinksWrapperRect() {
        return driver.findElement(LINKS_WRAPPER).getRect();
    }

    public Rectangle getHomeLinkRect() {
        return driver.findElement(HOME_LINK).getRect();
    }

    public boolean elementsOverlap(Rectangle rect1, Rectangle rect2) {
        return !(rect1.x + rect1.width <= rect2.x ||
                rect2.x + rect2.width <= rect1.x ||
                rect1.y + rect1.height <= rect2.y ||
                rect2.y + rect2.height <= rect1.y);
    }

    public boolean isHomeLinkClickable() {
        WebElement homeLink = driver.findElement(HOME_LINK);
        Rectangle rect = homeLink.getRect();
        int centerX = rect.x + rect.width / 2;
        int centerY = rect.y + rect.height / 2;

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement elementAtPoint = (WebElement) js.executeScript(
                "return document.elementFromPoint(arguments[0], arguments[1]);",
                centerX, centerY);

        if (elementAtPoint == null) {
            return false;
        }

        Boolean isHomeLink = (Boolean) js.executeScript(
                "return arguments[0].closest('.home-link') !== null;",
                elementAtPoint);

        return Boolean.TRUE.equals(isHomeLink);
    }
}
