package org.misha.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Author: mshevelin
 * Date: 6/4/14
 * Time: 12:12 PM
 */
public interface DownloadService {

    void sendToUser(HttpServletResponse response, File file, ServletContext application);

    File writeToFile(String text, String filePath);

    String currentTime();
}
