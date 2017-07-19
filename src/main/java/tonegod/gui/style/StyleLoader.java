package tonegod.gui.style;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author rockfire
 */
public class StyleLoader implements AssetLoader {

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        InputStream file = assetInfo.openStream();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException pce) {
            throw new IOException("Failed to configure XML parse.", pce);
        } catch (SAXException se) {
            throw new IOException("Failed to parse XML document.", se);
        } finally {
            file.close();
        }
    }
}
