package com.example.payload;

public class UploadFileResponse {
    private String fileName;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileType, long size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
