/*
 * Copyright (c) 2015. Misha's property, all rights reserved.
 */

package org.misha.performance;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.misha.domain.algebra.lie.endomorphism.Endo;
import org.misha.domain.algebra.lie.polynomial.Polynomial;
import org.misha.domain.algebra.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Author: mshevelin
 * Date: 8/27/14
 * Time: 4:26 PM
 */

public class LongCommutator {
    private static final Logger log = Logger.getLogger(LongCommutator.class);
    final Endo commutator = new Parser().parseEndo(
            "(+ a - 2[[b, a], b] - 4[[[c, b], b], b]; + b; + c - 2[[c, b], b] + 2[[[b, a], b], b] + 4[[[[c," +
                    " b], b], b], b])"
    );
    final Endo elementary = new Parser().parseEndo("(+a ; + b - [a, c]; + c)");
    final Endo reversedCommutator = new Parser().parseEndo(
            "(+ a + 2[[b, a], b] + 4[[[c, b], b], b] - 4[[[[b, a], b], b], b]; + b; + c + 2[[c, b], " +
                    "b] - 2[[[b, a], b], b])"
    );
    final Endo reversedElementary = new Parser().parseEndo("(+a ; + b + [a, c]; + c)");

    public void testLongCommutator() {
        Endo endo;
        DOMConfigurator.configure("./src/main/resources/log4j.xml");
        log.debug(
                "A^{-1} = (+a ; + b ; + c - [a, b]) * " +
                        "(+ a - 2[b, c]; + b; + c) * " +
                        "(+ a ; + b ; + c + [a, b]) * " +
                        "(+ a + 2[b, c]; +b; +c) =\n"
        );
        log.debug(
                "= (+ a - 2[[b, a], b] - 4[[[c, b], b], b]; " +
                        "+ b; " +
                        "+ c - 2[[c, b], b] + 2[[[b, a], b], b] + 4[[[[c, b], b], b], b])"
        );
        log.debug("B^{-1} = (+a ; + b - [a, c]; + c)");
        endo = commutator.times(elementary);
        log.debug("A^{-1} * B^{-1}=\n" + endo);
        endo = endo.times(reversedCommutator);
        log.debug("A^{-1} * B^{-1} * A=\n" + endo);
        endo = endo.times(reversedElementary);
        log.debug("A^{-1} * B^{-1} * A * B=\n" + endo);
    }

    public void testLongJacobi() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("./long-comm.txt"));
            final String line = sc.nextLine();
            final String[] polynomials = line.split("\\(|\\)")[1].split(";");
            log.debug(polynomials[0].trim());
            log.debug(polynomials[1].trim());
            log.debug(polynomials[2].trim());
            final Polynomial first = Polynomial.mount(polynomials[0].trim());
            //final Polynomial second = Polynomial.mount("+ b + 4[[c, b], [b, a]] - 4[[[b, a], b],
            // c] - 2[[[b, a], b], [b, a]] + 2[[[c, a], b], b] + 4[[[c, b], b], [[b, a], b]] - 2[[[[b, a],
            // a], b], b] - 4[[[[b, a], b], b], [[b, a], b]] - 4[[[[c, a], a], c], [c, a]] + 2[[[[c, a],
            // a], [c, a]], [[c, a], a]] - 4[[[[c, a], c], [c, a]], [[c, a], c]] - 16[[[[c, a], c], [c,
            // a]], [[[c, a], a], [c, a]]] - 4[[[[c, b], b], b], c] - 8[[[[c, b], b], b], [[c, b],
            // b]] + 8[[[[c, b], b], b], [[[b, a], b], b]] + 4[[[[[b, a], b], b], b], c] + 8[[[[[b, a], b],
            // b], b], [[c, b], b]] - 8[[[[[b, a], b], b], b], [[[b, a], b], b]] + 2[[[[[c, a], a], a], [c,
            // a]], [c, a]] + 12[[[[[c, a], a], [c, a]], [c, a]], [[c, a], c]] + 4[[[[[c, a], a], [c, a]],
            // [c, a]], [[[c, a], a], [c, a]]] - 4[[[[[c, a], c], c], [c, a]], [c, a]] - 4[[[[[c, a], c],
            // [c, a]], [c, a]], [[c, a], a]] + 8[[[[[c, a], c], [c, a]], [c, a]], [[[c, a], c], [c,
            // a]]] - 8[[[[[c, a], c], [c, a]], [c, a]], [[[[c, a], a], [c, a]], [c, a]]] + 4[[[[[[c, a],
            // a], c], [c, a]], [c, a]], [c, a]] - 8[[[[[[c, a], a], [c, a]], [c, a]], [c, a]], [[[c, a],
            // c], [c, a]]] + 8[[[[[[c, a], a], [c, a]], [c, a]], [c, a]], [[[[c, a], a], [c, a]], [c,
            // a]]] ");
            //final Polynomial third = Polynomial.mount(polynomials[2].trim());
            log.debug("mounted");
            log.debug(first.fox().toString());
            //log.debug(second.fox().toString());
            //log.debug(third.fox().toString());
        } catch (final FileNotFoundException e) {
            log.error(e.getMessage());
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    public static void main(final String[] args) throws CloneNotSupportedException {
        final LongCommutator longCommutator = new LongCommutator();
        longCommutator.testLongCommutator();
        longCommutator.testLongJacobi();
    }
}
