package org.misha.controller;

import org.apache.log4j.Logger;
import org.misha.service.DownloadService;
import org.misha.views.PolynomialObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Author: mshevelin
 * Date: 6/3/14
 * Time: 5:19 PM
 */
@Controller
@RequestMapping("/")
final class DownloadResultController {
    private static final Logger log = Logger.getLogger(DownloadResultController.class);
    private final ServletContext application;
    private final DownloadService downloadService;
    @Value("#{applicationProperties['temp.folder']}")
    private String tempFolder;

    @Autowired
    public DownloadResultController(ServletContext application,
                                    @Named("downloadService") DownloadService downloadService
    ) {
        this.application = application;
        this.downloadService = downloadService;
    }
    @SuppressWarnings("unused declaration")
    @RequestMapping(value = "/download", method = RequestMethod.POST)//*-result.jsp/form/@action
    private String saveResult(
            final HttpServletResponse response,
            @ModelAttribute("polynomialObject") final PolynomialObject polynomialObject
    ) {
        final String text = polynomialObject.getValue();
        String filePath = application.getRealPath(tempFolder);
        log.debug("Path to file: " + filePath);
        filePath += "/" + polynomialObject.getName() + downloadService.currentTime() + ".txt";
        final File file = downloadService.writeToFile(text, filePath);
        log.debug("save result");
        downloadService.sendToUser(response, file, application);
        return "download";
    }
}
