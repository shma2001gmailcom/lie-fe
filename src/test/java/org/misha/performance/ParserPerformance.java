package org.misha.performance;

import org.apache.log4j.xml.DOMConfigurator;
import org.misha.domain.algebra.parser.Parser;

import java.text.MessageFormat;

/**
 * Author: mshevelin
 * Date: 5/16/14
 * Time: 4:29 PM
 */

final class ParserPerformance {
    private ParserPerformance() {
    }

    public static void main(final String[] args) {
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        String s = "";
        for (int i = 0; i < 3 * 10; ++i) {
            s = MessageFormat.format("{0}+ {1} ", s, ad(i));
        }
        new Parser(s).parse();
    }

    private static String ad(final int times) {
        String s = "a";
        for (int i = 0; i < times; ++i) {
            s = new StringBuilder().append("[").append(s).append(", ").append("b").append("]").toString();
        }
        return s;
    }
}
