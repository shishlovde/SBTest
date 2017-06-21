/**
 * Created by shishlov on 6/20/2017.
 */

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;

//reads for xml data file and stres in ConverterState class
public class Reader {


    public ConverterState readStateFromXML(String file, String stateName) {


        String xpathStr = "//ConverterState[@name='"+stateName+"']/";
        ConverterState state = new ConverterState();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(classLoader.getResource(file).getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            XPathExpression expr = xpath.compile(xpathStr+"Amount");
            state.amount = expr.evaluate(doc, XPathConstants.STRING).toString();

            expr = xpath.compile(xpathStr+"ConverterFrom");
            state.converterFrom = expr.evaluate(doc, XPathConstants.STRING).toString();

            expr = xpath.compile(xpathStr+"ConverterTo");
            state.converterTo = expr.evaluate(doc, XPathConstants.STRING).toString();

            expr = xpath.compile(xpathStr+"SourceCode");
            state.sourceCode = expr.evaluate(doc, XPathConstants.STRING).toString();

            expr = xpath.compile(xpathStr+"DestinationCode");
            state.destinationCode = expr.evaluate(doc, XPathConstants.STRING).toString();

            expr = xpath.compile(xpathStr+"ExchangeType");
            state.exchangeType = expr.evaluate(doc, XPathConstants.STRING).toString();

            expr = xpath.compile(xpathStr+"ServicePack");
            state.servicePack = expr.evaluate(doc, XPathConstants.STRING).toString();

            expr = xpath.compile(xpathStr+"ConverterDateSelect");
            state.converterDateSelect = expr.evaluate(doc, XPathConstants.STRING).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    public String readURL (String file, String urlName)
    {
        String xpathStr = "//url[@name='"+urlName+"']";
        String resultURL = new String();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(classLoader.getResource(file).getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            XPathExpression expr = xpath.compile(xpathStr);
            resultURL = expr.evaluate(doc, XPathConstants.STRING).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultURL;
    }
}
