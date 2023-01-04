package skyward.pp.adapter;

/**
 * Created by ANDROID 1 on 27-09-2016.
 */
public class MyVideos {

    String ID = null;
    String FileName = null;
    String FilePath= null;
String ImageName=null;
    String ImagePath=null;

    public MyVideos(String ID, String fileName, String filePath, String imageName, String imagePath) {
        this.ID = ID;
        FileName = fileName;
        FilePath = filePath;
        ImageName = imageName;
        ImagePath = imagePath;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }
}
