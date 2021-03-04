package Util;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.Iterator;

/**
 * Created by arifn on 7/31/2017.
 */
public class XPathProvider {

    private static XPath xPath = null;

    public static XPath getInstance(){

        if(xPath == null) {
            XPath xPath = XPathFactory.newInstance().newXPath();

            xPath.setNamespaceContext(new NamespaceContext() {
                public String getNamespaceURI(String prefix) {
                    if (prefix == null) throw new NullPointerException("Null prefix");
                    else if ("srcml".equals(prefix)) return "http://www.srcML.org/srcML/src";
                    else if ("xml".equals(prefix)) return XMLConstants.XML_NS_URI;
                    return XMLConstants.NULL_NS_URI;
                }

                // This method isn't necessary for XPath processing.
                public String getPrefix(String uri) {
                    throw new UnsupportedOperationException();
                }

                // This method isn't necessary for XPath processing either.
                public Iterator getPrefixes(String uri) {
                    throw new UnsupportedOperationException();
                }
            });
            return xPath;
        }
        else{
            return xPath;
        }
    }
}
