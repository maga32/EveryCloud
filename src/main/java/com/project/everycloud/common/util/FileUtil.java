package com.project.everycloud.common.util;

import org.springframework.stereotype.Component;

@Component
public class FileUtil {

    /**
     * Replace WINDOWS path to MAC/LINUX/UNIX path processing
     *
     * @param path
     * @return path.replaceAll("\\\\", "/")
     */
    public static String macPath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * Replace MAC/LINUX/UNIX path to WINDOWS path processing
     *
     * @param path
     * @return path.replaceAll("/", "\\\\")
     */
    public static String winPath(String path) {
        return path.replaceAll("/", "\\\\");
    }

    /**
     * If String doesn't end with '/', add '/' end of the String
     *
     * @param str String
     * @return String
     */
    public static String addSlash(String str) {
        return str.endsWith("/") ? str : str + "/";
    }

    /**
     * If String doesn't end with '/', add '/' end of the String
     *
     * @param str String
     * @return String
     */
    public static String removeSlash(String str) {
        return str.endsWith("/") ? str : str.substring(0, str.length()-1);
    }

}
