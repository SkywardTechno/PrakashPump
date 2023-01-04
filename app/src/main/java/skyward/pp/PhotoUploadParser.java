package skyward.pp;

import android.content.Context;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PhotoUploadParser extends DefaultHandler {
    Context context;
    PhotoUploadResponse resp;
    boolean currentElement = false;
    String currentValue = "";

    public PhotoUploadParser(Context context) {
        super();
        this.context = context;
        this.resp = new PhotoUploadResponse();
    }

    public PhotoUploadResponse getResp() {
        return resp;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = true;
        currentValue = "";
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("IsSucceed")) {
            resp.setIsSucceed(currentValue);
        } else if (localName.equals("ErrorMessage")) {
            resp.setErrorMessage(currentValue);
        } else if (localName.equals("FileName")) {
            resp.setFileName(currentValue);
        } else if (localName.equals("FilePath")) {
            resp.setFilePath(currentValue);
        }else if (localName.equals("FileContentType")) {
            resp.setFileContentType(currentValue);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }
    }
}
