package org.misha.performance;

import org.apache.log4j.xml.DOMConfigurator;
import org.misha.algebra.parser.Parser;

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
            s = s + "+ " + ad(i) + " ";
        }
        new Parser(s).parse();
    }

    private static String ad(final int times) {
        String s = "a";
        for (int i = 0; i < times; ++i) {
            s = "[" + s + ", " + "b" + "]";
        }
        return s;
    }
}
