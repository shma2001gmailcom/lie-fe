package org.misha.tags;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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

    @Override
    public void doTag() throws JspException, IOException {
        final PageContext context = (PageContext) getJspContext();
        final ServletRequest req = context.getRequest();
        final ServletResponse response = context.getResponse();
        final Enumeration attributeNames = req.getAttributeNames();
        final Enumeration params = req.getParameterNames();
        final PrintWriter writer = response.getWriter();
        logHiddenDiv(req, attributeNames, params, writer);
    }

    private void logHiddenDiv(
            final ServletRequest req, final Enumeration attributeNames, final Enumeration params,
            final PrintWriter writer
    ) {
        writer.print("<div style=\"display: none;\">");
        logAttributes(req, attributeNames, writer);
        logParameters(req, params, writer);
        logFormBean(req, writer);
        writer.print(" </div>");
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
        } catch (final Exception e) {
            //e.printStackTrace();
        }
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

