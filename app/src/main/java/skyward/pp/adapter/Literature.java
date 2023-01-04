package skyward.pp.adapter;

/**
 * Created by ANDROID 1 on 06-10-2016.
 */
public class Literature {

    String LiteratureName = null;
    String LiteratureID = null;
    String LiteraturePath = null;
    String ProductID = null;
    String FilePath = null;
    String FileName = null;

    public Literature(String literatureName, String literatureID, String literaturePath, String productID, String filePath, String fileName) {
        LiteratureName = literatureName;
        LiteratureID = literatureID;
        LiteraturePath = literaturePath;
        ProductID = productID;
        FilePath = filePath;
        FileName = fileName;
    }


    public String getLiteratureName() {
        return LiteratureName;
    }

    public void setLiteratureName(String literatureName) {
        LiteratureName = literatureName;
    }

    public String getLiteratureID() {
        return LiteratureID;
    }

    public void setLiteratureID(String literatureID) {
        LiteratureID = literatureID;
    }

    public String getLiteraturePath() {
        return LiteraturePath;
    }

    public void setLiteraturePath(String literaturePath) {
        LiteraturePath = literaturePath;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
