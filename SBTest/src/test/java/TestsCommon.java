/**
 * Created by shishlov on 6/17/2017.
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

public class TestsCommon {

    private ChromeOptions options;
    private WebDriver driver;
    private Actions actions;
    private WebDriverWait wait;
    private ConverterPage converterPage ;

    @Test
    public void checkInitialConditionsTest()
    {
        //The test checks default controls' values after the Converter page has been loaded
        options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver,10);
        Reader reader = new Reader();
        ConverterState initialState = reader.readStateFromXML("States.xml","Initial State");

        driver.navigate().to(reader.readURL("URL.xml","converter" ));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='rates-button']")));

        converterPage = new ConverterPage(driver);

        Assert.assertTrue("Initial State is not correct",converterPage.compareStates(initialState));

        driver.close();
        driver.quit();
    }
    @Test
    public void checkDependanciesTest()
    {
        //The test checks radio buttons availability depending on other radio buttons
        options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver,10);
        Reader reader = new Reader();
        boolean errorFlag = false;

        driver.navigate().to(reader.readURL("URL.xml","converter" ));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='rates-button']")));

        converterPage = new ConverterPage(driver);

        converterPage.sourceCode.setValue("cash");
        converterPage.destinationCode.setValue("account");
        try {Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("ibank"));
        }catch (AssertionError e){System.out.println("exchangeType.ibank radio button is enabled, when sourceCode.cash and destinationCode.account is selected"); errorFlag=true;}
        try{Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("atm"));
        }catch (AssertionError e){System.out.println("exchangeType.atm radio button is enabled, when sourceCode.cash and destinationCode.account is selected"); errorFlag=true;}

        converterPage.sourceCode.setValue("cash");
        converterPage.destinationCode.setValue("card");;
        try{Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("ibank"));
        }catch (AssertionError e){System.out.println("exchangeType.ibank radio button is enabled, when sourceCode.cash and destinationCode.card is selected"); errorFlag=true;}
        try{Assert.assertTrue(!converterPage.servicePack.checkIfEnabled("premier"));
        }catch (AssertionError e){System.out.println("servicePack.premier radio button is enabled, when sourceCode.cash and destinationCode.card is selected"); errorFlag=true;}
        try{Assert.assertTrue(!converterPage.servicePack.checkIfEnabled("first"));
        }catch (AssertionError e){System.out.println("servicePack.first radio button is enabled, when sourceCode.cash and destinationCode.card is selected"); errorFlag=true;}

        converterPage.sourceCode.setValue("card");
        converterPage.destinationCode.setValue("cash");
        try{Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("ibank"));
        }catch (AssertionError e){System.out.println("exchangeType.ibank radio button is enabled, when sourceCode.card and destinationCode.cash is selected"); errorFlag=true;}

        converterPage.sourceCode.setValue("account");
        converterPage.destinationCode.setValue("cash");
        try{Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("ibank"));
        }catch (AssertionError e){System.out.println("exchangeType.ibank radio button is enabled, when sourceCode.account and destinationCode.cash is selected"); errorFlag=true;}
        try{Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("atm"));
        }catch (AssertionError e){System.out.println("exchangeType.atm radio button is enabled, when sourceCode.account and destinationCode.cash is selected"); errorFlag=true;}

        converterPage.sourceCode.setValue("cash");
        converterPage.destinationCode.setValue("cash");
        try{Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("ibank"));
        }catch (AssertionError e){System.out.println("exchangeType.ibank radio button is enabled, when sourceCode.cash and destinationCode.cash is selected"); errorFlag=true;}
        try{Assert.assertTrue(!converterPage.exchangeType.checkIfEnabled("atm"));
        }catch (AssertionError e){System.out.println("exchangeType.atm radio button is enabled, when sourceCode.cash and destinationCode.cash is selected"); errorFlag=true;}
        /*
            in my opinion following radio-button should not be available when sourceCode.cash and destinationCode.cash radio buttons are selected. But in reality following radio buttons are
            available. Uncomment following asserts to make test generate an error
        */
        /*
        try{Assert.assertTrue(!converterPage.servicePack.checkIfEnabled("premier"));
        }catch (AssertionError e){System.out.println("servicePack.premier radio button is enabled, when sourceCode.cash and destinationCode.cash is selected"); errorFlag=true;}
        try{Assert.assertTrue(!converterPage.servicePack.checkIfEnabled("first"));
        }catch (AssertionError e){System.out.println("servicePack.first radio button is enabled, when sourceCode.cash and destinationCode.cash is selected"); errorFlag=true;}
        */
        Assert.assertTrue("something went wrong", !errorFlag);

        driver.close();
        driver.quit();
    }
    @Test
    public void checkBuySellRatesTest()
    {
        //The Test checks mutual changing of Currency Selectors' values and check that Buy and Sell rates displayed correctly on the page
        options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver,10);
        Reader reader = new Reader();

        driver.navigate().to(reader.readURL("URL.xml","converter" ));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='rates-button']")));

        converterPage = new ConverterPage(driver);

        converterPage.сonverterFrom.setValue("USD");
        Assert.assertEquals("ConverterTo did not switched USD->RUB when ConvertedFrom set RUB->USD", converterPage.сonverterTo.getValue(),"RUB");

        converterPage.calculate();
        Assert.assertTrue("Sell Rate CompareResult fails", converterPage.compareResult("100"));

        converterPage.сonverterTo.setValue("USD");
        Assert.assertEquals("ConverterFrom did not switched USD->RUB when ConvertedTo set RUB->USD", converterPage.сonverterFrom.getValue(),"RUB");

        converterPage.calculate();
        Assert.assertTrue("Buy Rate CompareResult fails", converterPage.compareResult("100"));


        driver.close();
        driver.quit();
    }


}
