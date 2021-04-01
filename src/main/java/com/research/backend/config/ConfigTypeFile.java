package com.research.backend.config;

public class ConfigTypeFile {
    private static Boolean fileType(String fileName ,String type){
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        if(type.equals(fileType)) return true;
        return false;
    }
    public static boolean isImage(String filename) {
        return  fileType(filename,".bmp") ||
                fileType(filename,".gif") ||
                fileType(filename,".jpeg") ||
                fileType(filename,".jpg") ||
                fileType(filename,".png") ||
                fileType(filename,"tif") ||
                fileType(filename,".ico");
    }

    public static boolean isPdf(String filename)
    {
        return fileType(filename, ".pdf");
    }

}
