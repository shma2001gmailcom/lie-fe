package org.misha.tags;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.commons.beanutils.BeanUtils.describe;

/**
 * author: misha
 * date: 11/7/15 11:35 PM.
 */
public class LogTag extends SimpleTagSupport {
    private static final Logger log = Logger.getLogger(LogTag.class);

    @Override
    public void doTag() throws JspException, IOException {
        logHiddenDiv((PageContext) getJspContext());
    }

    private void logHiddenDiv(PageContext context) {
        PrintWriter writer;
        try {
            writer = context.getResponse().getWriter();
            writer.print("<div style=\"display: none;\">");
            final ServletRequest req = context.getRequest();
            logAttributes(req, req.getAttributeNames(), writer);
            logParameters(req, req.getParameterNames(), writer);
            logFormBean(req, writer);
            writer.print(" </div>");
        } catch (IOException ignored) {
            log.error(ignored);
        }
    }

    private void logFormBean(final ServletRequest req, final PrintWriter writer) {
        writer.print("\n---------------------FORM BEAN-------------------------------\n");
        try {
            final Map formMap = describe(req.getAttribute("org.apache.struts.taglib.html.BEAN"));
            if (formMap.isEmpty()) {
                writer.print("NONE\n");
                return;
            }
            for (final Object o : formMap.entrySet()) {
                final Entry e = (Entry) o;
                writer.print(String.format("%s=%s%n", e.getKey(), e.getValue()));
            }
        } catch (final Exception ignored) {}
        writer.print("--------------------------------------------------------------\n");
    }

    private void logParameters(
            final ServletRequest req, final Enumeration params, final PrintWriter writer
    ) {
        writer.print("\n---------------------PARAMETERS-------------------------------\n");
        if (!params.hasMoreElements()) {
            writer.print("NONE\n");
            return;
        }
        while (params.hasMoreElements()) {
            final String name = (String) params.nextElement();
            writer.print(String.format("%s=%s%n", name, StringUtils.join(req.getParameterValues(name), ";")));
        }
        writer.print("--------------------------------------------------------------\n");
    }

    private void logAttributes(
            final ServletRequest req, final Enumeration attributeNames, final PrintWriter writer
    ) {
        writer.print("\n---------------------ATTRIBUTES-------------------------------\n");
        if (!attributeNames.hasMoreElements()) {
            writer.print("NONE\n");
            return;
        }
        while (attributeNames.hasMoreElements()) {
            final String name = (String) attributeNames.nextElement();
            writer.print(String.format("%s=%s%n", name, req.getAttribute(name)));
        }
        writer.print("--------------------------------------------------------------\n");
    }
}

