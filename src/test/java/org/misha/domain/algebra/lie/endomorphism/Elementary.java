package org.misha.domain.algebra.lie.endomorphism;

import org.misha.domain.algebra.lie.polynomial.Polynomial;
import org.misha.domain.algebra.lie.polynomial.monomial.Monomial;
import org.misha.domain.algebra.lie.polynomial.monomial.MonomialUtils;
import org.misha.domain.algebra.parser.Parser;

import java.util.ArrayList;
import java.util.Random;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.misha.domain.algebra.lie.polynomial.Polynomial.mount;

/**
 * author: misha
 * created: 2/23/17.
 */
public class Elementary {
    public static final Endo id = Parser.parseEndo("(+a; +b; +c)");
    private static final Monomial a = MonomialUtils.monomial("a");
    private static final Monomial b = MonomialUtils.monomial("b");
    private static final Monomial c = MonomialUtils.monomial("c");

    private static Endo create(int i) {
        switch (i % 3) {
            case 0:
                return getForA();
            case 1:
                return getForB();
        }
        return getForC();
    }

    private static Endo getForA() {
        Endo result = new Endo();
        result.mapTo(a, mount("+ a " + getTerm("[b, c]") /*+ getTerm("[[b, c], c]")*/));
        result.mapTo(b, mount("+ b"));
        result.mapTo(c, mount("+ c"));
        return result;
    }

    private static Endo getForB() {
        Endo result = new Endo();
        result.mapTo(a, mount("+ a"));
        result.mapTo(b, mount("+ b " + getTerm("[a, c]") /*+ getTerm("[[a, c], c]")*/));
        result.mapTo(c, mount("+ c"));
        return result;
    }

    private static String getTerm(String monomial) {
        int scalar = scalar();
        switch (scalar) {
            case 0:
                return EMPTY;
            case 1:
                return "+" + monomial;
            case -1:
                return "-" + monomial;

        }
        return (scalar > 0 ? "+" : EMPTY) + scalar + monomial;
    }

    private static Endo getForC() {
        Endo result = new Endo();
        result.mapTo(a, mount("+ a"));
        result.mapTo(b, mount("+ b "));
        result.mapTo(c, mount("+ c" + getTerm("[a, b]") /*+ getTerm("[[a, b], b]")*/));
        return result;
    }

    private static int sign() {
        final int value = new Random().nextInt();
        switch (value % 3) {
            case 1:
                return 1;
            case 2:
                return -1;
        }
        return 0;
    }

    private static int scalar() {
        return new Random().nextInt(4) * sign();
    }

    private static Endo reverse(Endo e) {
        Endo result = new Endo();
        int i = 0;
        for (Tuple.Pair<Monomial, Polynomial> pair : e) {
            Polynomial value = pair.getValue();
            if (value.deg() > 1) {
                ++i;
                if (i > 1) {
                    throw new IllegalArgumentException("can not reverse this endomorphism: " + e);
                } else {
                    Monomial argument = pair.getArgument();
                    Polynomial p = value.clone().plus(argument.times(-1)).times(-1);
                    result.mapTo(argument, p.plus(argument));
                }
            } else {
                result.mapTo(pair.getArgument(), pair.getValue());
            }
        }
        return result;
    }

    public static void main(String... args) throws CloneNotSupportedException {
        ArrayList<Endo> list = new ArrayList<Endo>();

        for (int i = 0; i < 8; ++i) {
            Endo e = create(i);
            if (!e.equals(id)) {
                list.add(e);
            }
        }
        Endo reversed = id;
        int size = list.size();
        for (int i = 0; i < size; ++i) {
            Endo e = list.get(size - 1 - i);
            Endo reverse = reverse(e);
            System.out.println(e + "  " + reverse);
            reversed = reversed.times(reverse);
        }
        Endo product = id;
        for (Endo e : list) {
            product = product.times(e);
        }
        System.out.println(product + " " + reversed);
        System.out.println("\n" + product.times(reversed));
    }
}

