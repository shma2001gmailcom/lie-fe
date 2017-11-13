package org.misha.domain.algebra.associative.diamond;

import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.misha.domain.algebra.associative.PolynomialUtils.mount;

/**
 * author: misha
 * date: 11/6/17
 * time: 12:46 PM
 */
public class JunctionTest {

    @Test
    public void testJoin() throws Exception {
        Polynomial first = mount("+ xxy - xyx - xyx + yxx");
        Polynomial second = mount("+ xyy - yxy - yxy + yyx");
        Polynomial junction21 = new Junction(second, first).join();
        assertTrue(junction21.equals(mount("- xyyx + yxxy")));
    }
}