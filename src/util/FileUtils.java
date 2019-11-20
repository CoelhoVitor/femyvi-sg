/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import model.FileMessage;

public class FileUtils {
    
    public static String getOriginPathWithoutServerNum(String originPath, String fileType) {
        return originPath.replaceFirst("[_][1][.][^.]+$", "." + fileType);
    }

    public static String getFilenameWithoutExtension(String filename) {
        return filename.replaceFirst("[.][^.]+$", "");
    }

    public static String getFilenameWithoutServerNum(String filename) {
        return filename.replaceFirst("[_][1]+$", "");
    }

}
