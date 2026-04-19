package com.radkovich.multithrededserver;

public enum ContentType {
    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JS(".js", "application/javascript"),
    JSON(".json", "application/json"),
    PNG(".png", "image/png"),
    JPG(".jpg", "image/jpeg"),
    JPEG(".jpeg", "image/jpeg"),
    TXT(".txt", "text/plain");

    private final String extension;
    private final String contentType;

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getContentType(){
        return contentType;
    }

    public static String fromFileName(String fileName) {
        for (ContentType contentType : ContentType.values()) {
            if(fileName.endsWith(contentType.extension)){
                return contentType.contentType;
            }
        }
        return "Unknown content type!";
    }
}
