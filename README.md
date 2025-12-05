# TIXR Web Bug Report — Header Logo Unclickable Due to Overlapping `links-wrapper` Div Element

**Issue Type:** Bug  
**Severity:** High  
**Priority:** P1  
**Environment:**  
- Web (Desktop)  
- Chrome / Firefox / Safari  
- Affects all viewport sizes

---

## 🐞 Summary
The Tixr header logo link (`<a class="logo">`) is not clickable because it is fully covered by the `<div class="links-wrapper">` element.
This overlapping causes `links-wrapper` to intercept pointer events, preventing navigation back to the homepage.

---

## 📌 Steps to Reproduce
1. Open any tixr.com/groups/xxxxxx page that has a `<div class="links-wrapper">` element, such as: https://www.tixr.com/groups/zamnafestival, https://www.tixr.com/groups/sjearthquakes, and https://www.tixr.com/groups/newcitygas.
2. Hover the mouse over the TIXR(tm) logo in the top-left corner where the carousel(s) begin. 
3. Attempt to click the logo (nothing happens).  
4. Open DevTools → Console.
5. Run the following code to detect the topmost element at the logo’s center:

    ```js
    const logo = document.querySelector('a.logo');
    const rect = logo.getBoundingClientRect();
    document.elementFromPoint(rect.left + rect.width/2, rect.top + rect.height/2);
    ```

6. Observe that the returned element is:

    ```
    <div class="links-wrapper">
    ```

   instead of:

    ```
    <a class="logo">
    ```

---

## ✔ Expected Behavior
- The logo should be fully clickable.  
- Clicking it should navigate to `https://www.tixr.com/`.  
- No content should overlap or block the header area.

---

## ❌ Actual Behavior
- The logo is visually present but not clickable.  
- Clicks are intercepted by an overlapping `links-wrapper` element.  
- The browser’s hit-testing confirms `links-wrapper` is the top element.

---

## 🔍 Root Cause Analysis
The `.links-wrapper` element extends upward into the header region, overlapping the clickable bounds of `.logo`.

Possible causes:
- Incorrect spacing (margin/padding pushing upward)
- A stacking context issue (z-index too high)
- Positioning rules causing vertical overlap
- Misaligned container boundaries

This results in click interception, making the logo non-functional.

---

## 🧭 Impact Assessment
| Area | Impact | Notes |
|------|--------|-------|
| Navigation | **High** | Homepage link is blocked |
| User Experience | **High** | Logo is a standard, expected nav anchor |
| Brand Trust | Medium | Logo appears broken |
| SEO / Engagement | Medium | Reduced homepage traffic |

This qualifies as a **P1 production issue**.

---

## 🧪 Technical Evidence

### Selenium Hit Test
```java
JavascriptExecutor js = (JavascriptExecutor) driver;
WebElement topEl = (WebElement) js.executeScript(
    "return document.elementFromPoint(arguments[0], arguments[1]);",
    centerX, centerY
);
System.out.println(topEl.getAttribute("class"));
