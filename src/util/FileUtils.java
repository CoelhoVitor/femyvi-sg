
package util;

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
