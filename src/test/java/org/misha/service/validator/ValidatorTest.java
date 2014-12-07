package org.misha.service.validator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Author: mshevelin
 * Date: 6/2/14
 * Time: 3:38 PM
 */

public class ValidatorTest {

    @Test
    public void testIsLieMonomialValid() throws Exception {
        String valid = "108[Z, [u, v]]";
        assertEquals(Validator.isLieMonomialValid(valid), true);
        valid = "- 108[[Z, [u, v]],[a,b]]";
        assertEquals(Validator.isLieMonomialValid(valid), true);
        String invalid = "-[ab,c]";
        assertEquals(Validator.isLieMonomialValid(invalid), false);
        invalid = "-[a, b]]";
        assertEquals(Validator.isLieMonomialValid(invalid), false);
        invalid = "-3][";
        assertEquals(Validator.isLieMonomialValid(invalid), false);
        invalid = "-3[[  ,b ], [a, b]]";
        assertFalse(Validator.isLieMonomialValid(invalid));
        invalid = "-3[[b  , ], [a, b]]";
        assertFalse(Validator.isLieMonomialValid(invalid));
    }
}
