package org.misha.domain.algebra.associative.reduction;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.misha.domain.algebra.associative.PolynomialUtils.mount;

/**
 * author: misha
 * date: 13.11.17
 */
public class ReducerTest {
    @Test
    public void reduceOnce() throws Exception {
        PolynomialSet set = new PolynomialSet();
        set.addAll(mount("- x"), mount("-xy + xyxy"));
        Reducer reducer = new Reducer(set);
        reducer.reduceOnce();
        System.out.println(set.toString());
        reducer.reduceOnce();
        System.out.println(set.toString());
        reducer.reduceOnce();
        System.out.println(set.toString());
        reducer.reduceOnce();
        System.out.println(set.toString());
    }
}