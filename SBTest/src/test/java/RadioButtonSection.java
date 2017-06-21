/**
 * Created by shishlov on 6/19/2017.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

//describes radio-button section
public class RadioButtonSection {
    private WebDriver driver;
    private List<WebElement> radioButtons;
    private String sectionName;
    private String xpRadioSection = "//input[@type='radio'][@name=''][@value='']/../span[@class='radio']";
    private String xpRadioButton = "//input[@type='radio'][@name=''][@value='']/../span[@class='radio']";

    public RadioButtonSection(WebDriver driver, String sectionName)
    {
        this.driver=driver;
        this.sectionName=sectionName;
        ini(sectionName);
    }

    private String getRadioButtonXpath(String value) //gets xpath for particular radio box. Similar to this: //input[@type='radio'][@name='"+sectionName+"'][@value='"+value+"']
    {
        String xp;
        xp = new StringBuilder(xpRadioButton).insert(xpRadioButton.indexOf("[@name='")+8,sectionName).toString();
        xp = new StringBuilder(xp).insert(xp.indexOf("[@value='")+9,value).toString();
        return xp;
    }
    private String getRadioSectionXpath(String value) //gets xpath for particular radio box. Similar to this: //input[@type='radio'][@name='"+sectionName+"'][@value='"+value+"']
    {
        String xp;
        xp = new StringBuilder(xpRadioSection).insert(xpRadioSection.indexOf("[@name='")+8,sectionName).toString();
        return xp;
    }

    private void ini(String sectionName)
    {
        radioButtons = driver.findElements(By.xpath("//input[@type='radio'][@name='"+sectionName+"']"));
    }

    public void setValue(String value)
        {
        driver.findElement(By.xpath(getRadioButtonXpath(value))).click();
    }
    public String getValue() // returns a name of selected radio-button
    {
        String returnValue = new String();
        for (int i=0; i<radioButtons.size();i++) {
            if (radioButtons.get(i).isSelected())
                returnValue = radioButtons.get(i).getAttribute("value");
        }
        return returnValue;
    }
    public boolean checkIfEnabled(String value)
    {
        String attribute;
        attribute = driver.findElement(By.xpath(getRadioButtonXpath(value) + "/..")).getAttribute("class").toString();
        if (attribute.equals("filter-inactive")) return false;
            else return true;
    }
}
