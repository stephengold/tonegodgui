package tonegod.gui.core.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author t0neg0d
 */
public class XMLHelper {
        /**
         * A private constructor to inhibit instantiation of this class.
         */
        private XMLHelper() {
        }

	public static String getNodeText(Node parentNode, String nodeName) {
		Element fstElmnt = (Element) parentNode;
		NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(nodeName);
		Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		NodeList fstNm = fstNmElmnt.getChildNodes();
		String ret = fstNm.item(0).getNodeValue();
		return ret;
	}

	public static String getNodeText(Node node) {
		NodeList fstNm = node.getChildNodes();
		String ret = fstNm.item(0).getNodeValue();
		return ret;
	}

	public static String getNodeAttributeValue(Node node, String attributeName) {
		return ((Element)node).getAttribute(attributeName);
	}
}
