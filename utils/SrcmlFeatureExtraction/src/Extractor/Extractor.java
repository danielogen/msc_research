package Extractor;

import Entity.Features;
import Util.XPathProvider;
import org.w3c.dom.Node;
import javax.xml.xpath.XPath;

/*
    Used as template for extracting feature from Node
 */

public abstract class Extractor {

    private Node unit;
    private XPath xPath;

    public Extractor(Node unit) {
        this.unit = unit;
        this.xPath = XPathProvider.getInstance();
    }

    public abstract Features extract();

}
