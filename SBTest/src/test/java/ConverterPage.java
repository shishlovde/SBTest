/**
 * Created by shishlov on 6/19/2017.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

//This class keeps Converter Page WebElements and methods to work with them
public class ConverterPage {

    private WebDriver driver;
    private WebDriverWait wait;
    //public WebElement amount;
    public CurrencySelector сonverterFrom;
    public CurrencySelector сonverterTo;
    public RadioButtonSection sourceCode;
    public RadioButtonSection destinationCode;
    public RadioButtonSection exchangeType;
    public RadioButtonSection servicePack;
    public RadioButtonSection converterDateSelect;
    public WebElement rateBuy;
    public WebElement rateSell;
    public WebElement rateBuyOdd;
    public WebElement rateSellOdd;
    public WebElement calculateBtn;

    //private String xpAmount = "//input[@placeholder='Сумма']";
    private String xpRateBuy = "//tr[@class='rates-current__table-row']/td[@class='rates-current__table-cell rates-current__table-cell_column_buy']/span/span[@class='rates-current__rate-value']";
    private String xpRateSell = "//tr[@class='rates-current__table-row']/td[@class='rates-current__table-cell rates-current__table-cell_column_sell']/span/span[@class='rates-current__rate-value']";
    private String xpRateBuyOdd = "//tr[@class='rates-current__table-row rates-current__table-row_odd']/td[@class='rates-current__table-cell rates-current__table-cell_column_buy']/span/span[@class='rates-current__rate-value']";
    private String xpRateSellOdd = "//tr[@class='rates-current__table-row rates-current__table-row_odd']/td[@class='rates-current__table-cell rates-current__table-cell_column_sell']/span/span[@class='rates-current__rate-value']";
    private String xpCalculateBtn;
    private String xpTotalPane = "//span[@class='rates-converter-result__total-to']";

    private WebElement uiAmount(){ return driver.findElement(By.xpath("//input[@placeholder='Сумма']"));} //Attempt to regenerate WebElement on every call, but decided to keep WebElements as class members



    public ConverterPage (WebDriver driver) {
        this.driver=driver;
        ini();
    }


   private void ini()
    {
        wait = new WebDriverWait(driver, 10);
        //amount = driver.findElement(By.xpath("xpAmount")); //Amount to change field
        сonverterFrom = new CurrencySelector(driver,"From"); //converterFrom selector
        сonverterTo = new CurrencySelector(driver,"To"); //converterTo selector
        sourceCode = new RadioButtonSection(driver, "sourceCode"); //sourceCode radio-buttons group
        destinationCode = new RadioButtonSection(driver, "destinationCode"); //destinationCode radio-buttons group
        exchangeType = new RadioButtonSection(driver, "exchangeType"); //exchangeType radio-buttons group
        servicePack = new RadioButtonSection(driver, "servicePack"); // servicePack radio-buttons group
        converterDateSelect = new RadioButtonSection(driver, "converterDateSelect"); //converterDateSelect radio-buttons group
        rateBuy = driver.findElement(By.xpath(xpRateBuy)); // Buy rate field. In case of complex rate it is the bottom row
        rateSell = driver.findElement(By.xpath(xpRateSell)); //Sell rate field. In case fo complex rate it is the bottom row
        calculateBtn = driver.findElement(By.xpath("//button[@class='rates-button']")); // Calculate button
        /*if (сonverterFrom.getValue()!="RUB" && сonverterTo.getValue()!="RUB")
        {
            rateBuyOdd = driver.findElement(By.xpath(xpRateBuyOdd)); // Buy rate filed from the upper row in case of complex rate
            rateSellOdd = driver.findElement(By.xpath(xpRateBuyOdd)); // Sell rate field from the upper row in case of complex rate
        }*/
    }
    private WebElement getWE (String xpath) //gets WebElement by xpath string
    {
        return driver.findElement(By.xpath(xpath));
    }

    public void setAmount(String value)
    {
        uiAmount().clear();
        uiAmount().sendKeys(value);
    }
    public String getAmount() //Gets a value from the Amount field changes "," -> "." and removes spaces
    {
        return uiAmount().getAttribute("value").replace(",",".").replace(" ","");
    }

    public BigDecimal getRateBuy()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpRateBuy)));
        String strRate = driver.findElement(By.xpath(xpRateBuy)).getText().replace(",",".");
        BigDecimal rate = new BigDecimal(strRate);
        if (сonverterFrom.getValue().equals("JPY")) rate = rate.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
        return rate;
    }

    public BigDecimal getRateSell()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpRateSell)));
        String strRate = driver.findElement(By.xpath(xpRateSell)).getText().replace(",",".");
        BigDecimal rate = new BigDecimal(strRate);
        if (сonverterTo.getValue().equals("JPY")) rate = rate.divide(new BigDecimal(100),4,RoundingMode.HALF_UP);
        return rate;
    }

    public BigDecimal getRateBuyOdd()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpRateBuyOdd)));
        String strRate = driver.findElement(By.xpath(xpRateBuyOdd)).getText().replace(",",".");
        BigDecimal rate = new BigDecimal(strRate);
        if (сonverterFrom.getValue().equals("JPY")) rate = rate.divide(new BigDecimal(100),4,RoundingMode.HALF_UP);
        return rate;

    }
    public BigDecimal getRateSellOdd()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpRateSellOdd)));
        String strRate = driver.findElement(By.xpath(xpRateSellOdd)).getText().replace(",",".");
        BigDecimal rate = new BigDecimal(strRate);
        if (сonverterFrom.getValue().equals("JPY")) rate = rate.divide(new BigDecimal(100),4,RoundingMode.HALF_UP);
        return rate;
    }

    public BigDecimal getRate() //computes a straight rate that should be multiplies with an amount to get a result
    {
        BigDecimal rate = new BigDecimal(1);
        if (сonverterFrom.getValue().equals("RUB")) rate = new BigDecimal(1).divide(getRateSell(), 10, BigDecimal.ROUND_HALF_UP);
        if (сonverterTo.getValue().equals("RUB")) rate =getRateBuy();
        if (!сonverterFrom.getValue().equals("RUB") && !сonverterTo.getValue().equals("RUB")) rate = getRateBuyOdd().divide(getRateSell(),10,BigDecimal.ROUND_HALF_UP);
        return rate;
    }

    public void setState (ConverterState stateToSet) // sets conditions form the calculations (amount, currency selectors and radio-buttons)
    {
        setAmount(String.valueOf(stateToSet.amount));
        сonverterFrom.setValue(stateToSet.converterFrom);
        сonverterTo.setValue(stateToSet.converterTo);
        sourceCode.setValue(stateToSet.sourceCode);
        destinationCode.setValue(stateToSet.destinationCode);
        exchangeType.setValue(stateToSet.exchangeType);
        servicePack.setValue(stateToSet.servicePack);
        converterDateSelect.setValue(stateToSet.converterDateSelect);
    }

    public void calculate()
    {
        calculateBtn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpTotalPane)));
    }

    public boolean compareStates (ConverterState stateToCompare) //compares the current State with the given one
    {
        if (getAmount().equals(stateToCompare.amount)
                && сonverterFrom.getValue().equals(stateToCompare.converterFrom)
                && сonverterTo.getValue().equals(stateToCompare.converterTo)
                && sourceCode.getValue().equals(stateToCompare.sourceCode)
                && destinationCode.getValue().equals(stateToCompare.destinationCode)
                && exchangeType.getValue().equals(stateToCompare.exchangeType)
                && servicePack.getValue().equals(stateToCompare.servicePack)
                && converterDateSelect.getValue().equals(stateToCompare.converterDateSelect))
            return true;
        else return false;
    }

    public boolean compareResult (ConverterState stateToCompare) //compares
    {
        return __compareResult(stateToCompare.amount);
    }
    public boolean compareResult (String amountToCompare) //compares
    {
        return __compareResult(amountToCompare);
    }

    private boolean __compareResult (String amountToCompare) //compares
    {
        BigDecimal calcResult = new BigDecimal(amountToCompare.replace(",",".")).multiply(getRate()).setScale(2, RoundingMode.HALF_UP); //gets amount from xml, converts to BigDecimal and multiplies by Rate
        BigDecimal actualResult = new BigDecimal(driver.findElement(By.xpath(xpTotalPane)).getText().replaceAll("[^0-9,]", "").replace(",",".")); //gets amount from the page, keeps only numbers and comma and converts to BigDecimal

        if (calcResult.compareTo(actualResult)==0) return true;
            else return false;
    }
}

