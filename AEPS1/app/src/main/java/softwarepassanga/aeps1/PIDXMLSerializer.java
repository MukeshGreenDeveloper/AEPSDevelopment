package softwarepassanga.aeps1;

import org.json.JSONException;
import org.json.XML;

public class PIDXMLSerializer {
    public static Object getJsonFromXml(String str) {
        try {
            return XML.toJSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
