/**
 * Created by shishlov on 6/19/2017.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// describes currency selectors
public class CurrencySelector {

    private WebDriver driver;
    private WebElement selector;
    private String strToFrom;
    public CurrencySelector(WebDriver driver, String strToFrom) {
        this.driver = driver;
        this.strToFrom=strToFrom;
        ini(strToFrom);
    }

    private void ini(String strToFrom) {
        selector = driver.findElement(By.xpath("//select[@name='converter" + strToFrom + "']/.."));
    }

    public void setValue(String curr) {
        selector.click();
        driver.findElement(By.xpath("//select[@name='converter"+strToFrom+"']/../div/div/span[normalize-space()='" + curr + "']")).click();
    }

    public String getValue()
    {
        return selector.getText();
    }
}
