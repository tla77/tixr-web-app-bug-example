package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Rectangle;
import pages.GroupsPage;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Test suite for the links-wrapper overlapping home-link bug.
 *
 * Bug: On /groups pages, when a div.links-wrapper element exists,
 * it completely overlaps the div.home-link.hide-mobile element
 * containing the company logo link to homepage.
 */
public class OverlapBugTest extends TestBase {

    // ========== Tests that FAIL when bug exists, PASS when fixed ==========

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Verify home-link is not overlapped by links-wrapper")
    public void testHomeLinkNotOverlappedByLinksWrapper(int groupIndex) {
        String url = getGroupUrl(groupIndex);
        GroupsPage page = new GroupsPage(driver);
        page.load(url);

        assumeTrue(page.hasLinksWrapper(), "Page does not have links-wrapper element");
        assumeTrue(page.hasHomeLink(), "Page does not have home-link element");

        Rectangle linksWrapperRect = page.getLinksWrapperRect();
        Rectangle homeLinkRect = page.getHomeLinkRect();

        boolean overlap = page.elementsOverlap(linksWrapperRect, homeLinkRect);

        assertFalse(overlap,
                String.format("BUG: links-wrapper overlaps home-link on %s%n" +
                        "links-wrapper: %s%nhome-link: %s",
                        url, rectToString(linksWrapperRect), rectToString(homeLinkRect)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Verify home-link is clickable (not obscured)")
    public void testHomeLinkIsClickable(int groupIndex) {
        String url = getGroupUrl(groupIndex);
        GroupsPage page = new GroupsPage(driver);
        page.load(url);

        assumeTrue(page.hasHomeLink(), "Page does not have home-link element");

        boolean isClickable = page.isHomeLinkClickable();

        assertTrue(isClickable,
                String.format("BUG: home-link is not clickable (obscured by another element) on %s", url));
    }

    // ========== Tests that PASS when bug exists, FAIL when fixed ==========

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Bug reproduction: Confirm links-wrapper overlaps home-link")
    public void testBugPresentLinksWrapperOverlapsHomeLink(int groupIndex) {
        String url = getGroupUrl(groupIndex);
        GroupsPage page = new GroupsPage(driver);
        page.load(url);

        assumeTrue(page.hasLinksWrapper(), "Page does not have links-wrapper element");
        assumeTrue(page.hasHomeLink(), "Page does not have home-link element");

        Rectangle linksWrapperRect = page.getLinksWrapperRect();
        Rectangle homeLinkRect = page.getHomeLinkRect();

        boolean overlap = page.elementsOverlap(linksWrapperRect, homeLinkRect);

        assertTrue(overlap,
                String.format("Bug not reproduced: elements do not overlap on %s (bug may already be fixed)", url));
    }

    private String rectToString(Rectangle rect) {
        return String.format("[x=%d, y=%d, width=%d, height=%d]",
                rect.x, rect.y, rect.width, rect.height);
    }
}
