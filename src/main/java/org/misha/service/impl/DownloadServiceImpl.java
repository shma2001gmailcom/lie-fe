package org.misha.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.misha.service.DownloadService;

import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Author: mshevelin
 * Date: 6/4/14
 * Time: 12:19 PM
 */
@Named("downloadService")
public final class DownloadServiceImpl implements DownloadService {
    private static final Logger log = Logger.getLogger(DownloadService.class);
    private static final int FOUR_THOUSAND_AND_NINETY_SIX = 4096;

    @Override
    public void sendToUser(
            final HttpServletResponse response, final File file, final ServletContext application
    ) {
        final String mimeType = application.getMimeType(file.getAbsolutePath());
        response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        OutputStream out = null;
        InputStream in = null;
        try {
            out = response.getOutputStream();
            in = new FileInputStream(file);
            writeBytes(in, out);
        } catch (final Throwable e) {
            log.error(e.getMessage());
        } finally {
            close(out, in);
        }
    }

    private void writeBytes(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[FOUR_THOUSAND_AND_NINETY_SIX];
        int length;
        while ((length = in.read(buffer)) > 0) {
            try {
                out.write(buffer, 0, length);
                out.flush();
            } catch (final IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void close(final OutputStream out, final InputStream in) {
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);
    }

    @Override
    public File writeToFile(final String text, final String filePath) {
        final File file = new File(filePath);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(
                    new FileOutputStream(filePath), Charset.forName("UTF-8").newEncoder()
            );
            writer.write(text, 0, text.length());
            writer.flush();
        } catch (final Throwable e) {
            log.error(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return file;
    }

    @Override
    public String currentTime() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
    }
}
