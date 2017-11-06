package org.misha.domain.algebra.associative.diamond;

import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Polynomial second = mount("- xyy + yxy + yxy - yyx");
        Polynomial junction21 = new Junction(second, first).join();
        System.out.println(junction21);
        Polynomial junction211 = new Junction(first, junction21).join();
        System.out.println(junction211);
        Polynomial junction2112 = new Junction(junction211, second).join();
        System.out.println(junction2112);
    }

    @Test
    public void test(){
        String search = "on a daily basis";
        String toMatch = "***to on a daily basis.*** to on a daily basis to on a daily basis";
        Pattern pattern = Pattern.compile(search);

        Matcher matcher = pattern.matcher(toMatch);
        int i = 0;
        }

}