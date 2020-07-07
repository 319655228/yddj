package com.hckj.yddxst.bean;

public class BakerInfo {
    private String filePath;
    private String fileText;

    public BakerInfo(String filePath, String fileText) {
        this.filePath = filePath;
        this.fileText = fileText;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileText() {
        return fileText;
    }

    public void setFileText(String fileText) {
        this.fileText = fileText;
    }
}
