/**
 * Created by shishlov on 6/21/2017.
 */

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Parameter;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class TestCrossRates {

    private ChromeOptions options;
    private WebDriver driver;
    private Actions actions;
    private WebDriverWait wait;
    private ConverterPage converterPage ;

    @Parameter
    @Parameterized.Parameter
    public ConverterState stateToSet;
    @Parameterized.Parameters
    public static List<ConverterState> getData()
    {
        Reader reader = new Reader();
        reader.readStateFromXML("States.xml","Cross Rate State 2");
        return Arrays.asList(reader.readStateFromXML("States.xml","Cross Rate State 1"), reader.readStateFromXML("States.xml","Cross Rate State 2"));
    }

    @Test
    public void checkCrossRateTest()
    {

        //The Test checks Cross Rate
        options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        actions =  new Actions(driver);
        wait = new WebDriverWait(driver,10);
        Reader reader = new Reader();

        driver.navigate().to(reader.readURL("URL.xml","converter" ));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='rates-button']")));

        converterPage = new ConverterPage(driver);

        converterPage.setState(stateToSet);
        converterPage.calculate();
        Assert.assertTrue("Cross Rate CompareResult fails", converterPage.compareResult(stateToSet));

        driver.close();
        driver.quit();
    }
}
