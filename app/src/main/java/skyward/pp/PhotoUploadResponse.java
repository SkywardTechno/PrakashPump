package skyward.pp;

public class PhotoUploadResponse {
    String isSucceed;
    String errorMessage;
    String fileName;
    String filePath;
    String FileContentType;



    public String getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(String isSucceed) {
        this.isSucceed = isSucceed;
    }

    public String getFileContentType() {
        return FileContentType;
    }

    public void setFileContentType(String fileContentType) {
        FileContentType = fileContentType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
