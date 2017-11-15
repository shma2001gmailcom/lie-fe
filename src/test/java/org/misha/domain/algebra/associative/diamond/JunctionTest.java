package org.misha.domain.algebra.associative.diamond;

import org.junit.Test;
import org.misha.domain.algebra.associative.Polynomial;

import java.util.Set;
import java.util.TreeSet;

import static org.misha.domain.algebra.associative.PolynomialUtils.mount;
import static org.misha.domain.algebra.associative.reduction.Reduction.doReductions;

/**
 * author: misha
 * date: 11/6/17
 * time: 12:46 PM
 *  //[[[a,b],b],b]+6[a,[a,b]]; r2=[b,[b,c]]-2b; r3=[a,[b,c]]-4a; r4=[a,c]-3b.
 *  //+ 6aab - 12aba + 6baa + abbb - 3babb + 3bbab - bbba
 *  //- 2b + bbc - 2bcb + cbb
 *  //- 4a + abc - acb - bca + cba
 *  //- 3b + ac - ca
 * - 2bba + 6caab - 12caba + 6cbaa + bbcba - 2bcbba + cabbb - 3cbabb + 3cbbab
 * [1]·[+ 2b - bbc + 2bcb - cbb]·[+ 3ab ]
 * ----------------
 * + 6bab - 2bba + 6caab - 12caba + 6cbaa - 3bbcab + bbcba + 6bcbab - 2bcbba + cabbb - 3cbabb
 *
 * + 6bab - 2bba + 6caab - 12caba + 6cbaa - 3bbcab + bbcba + 6bcbab - 2bcbba + cabbb - 3cbabb
 * [1]·[+ 4a - abc + acb + bca - cba]·[+ 3bb ]
 * ----------------
 * - 12abb + 6bab - 2bba + 6caab - 12caba + 6cbaa + 3abcbb - 3acbbb - 3bbcab + bbcba - 3bcabb + 6bcbab - 2bcbba + cabbb
 *
 * + 12abb - 6bab + 2bba - 6caab + 12caba - 6cbaa - 3abcbb + 3acbbb + 3bbcab - bbcba + 3bcabb - 6bcbab + 2bcbba - cabbb
 * [1]·[+ 3b - ac + ca]·[+ bbb ]
 * ----------------
 * + 12abb - 6bab + 2bba + 3bbbb - 6caab + 12caba - 6cbaa - 3abcbb + 2acbbb + 3bbcab - bbcba + 3bcabb - 6bcbab + 2bcbba
 *
 * + 12abb - 6bab + 2bba + 3bbbb - 6caab + 12caba - 6cbaa - 3abcbb + 2acbbb + 3bbcab - bbcba + 3bcabb - 6bcbab + 2bcbba
 [+ b ]·[+ 2b - bbc + 2bcb - cbb]·[+ 2a ]
 * ----------------
 * + 12abb - 6bab + 6bba + 3bbbb - 6caab + 12caba - 6cbaa - 3abcbb + 2acbbb - 2bbbca + 3bbcab + 3bbcba + 3bcabb - 6bcbab
 *
 * + 12abb - 6bab + 6bba + 3bbbb - 6caab + 12caba - 6cbaa - 3abcbb + 2acbbb - 2bbbca + 3bbcab + 3bbcba + 3bcabb - 6bcbab
 * [+ b ]·[+ 4a - abc + acb + bca - cba]·[+ 6b ]
 * ----------------
 * + 12abb - 30bab + 6bba + 3bbbb - 6caab + 12caba - 6cbaa - 3abcbb + 2acbbb + 6babcb - 6bacbb - 2bbbca - 3bbcab + 3bbcba + 3bcabb
 *
 * - 12abb + 30bab - 6bba - 3bbbb + 6caab - 12caba + 6cbaa + 3abcbb - 2acbbb - 6babcb + 6bacbb + 2bbbca + 3bbcab - 3bbcba - 3bcabb
 * [+ b ]·[+ 3b - ac + ca]·[+ 3bb ]
 * ----------------
 * - 12abb + 30bab - 6bba + 6bbbb + 6caab - 12caba + 6cbaa + 3abcbb - 2acbbb - 6babcb + 3bacbb + 2bbbca + 3bbcab - 3bbcba
 *
 * - 12abb + 30bab - 6bba + 6bbbb + 6caab - 12caba + 6cbaa + 3abcbb - 2acbbb - 6babcb + 3bacbb + 2bbbca + 3bbcab - 3bbcba
 * [+ bb ]·[+ 4a - abc + acb + bca - cba]·[3]
 * ----------------
 * - 12abb + 30bab - 18bba + 6bbbb + 6caab - 12caba + 6cbaa + 3abcbb - 2acbbb - 6babcb + 3bacbb + 3bbabc - 3bbacb - bbbca + 3bbcab
 *
 * - 12abb + 30bab - 18bba - 3bbbb + 6caab - 12caba + 6cbaa + 3abcbb - 2acbbb - 6babcb + 3bacbb + 3bbabc - bbbca
 * [+ bbb ]·[+ 3b - ac + ca]·[1]
 * ----------------
 * - 12abb + 30bab - 18bba + 6caab - 12caba + 6cbaa + 3abcbb - 2acbbb - 6babcb + 3bacbb + 3bbabc - bbbac
 *
 * + 12abb - 30bab + 18bba - 6caab + 12caba - 6cbaa - 3abcbb + 2acbbb + 6babcb - 3bacbb - 3bbabc + bbbac
 * [1]·[- 6aab + 12aba - 6baa - abbb + 3babb - 3bbab + bbba]·[- c ]
 * ----------------
 * + 12abb - 30bab + 18bba + 6aabc - 12abac + 6baac - 6caab + 12caba - 6cbaa + abbbc - 3abcbb + 2acbbb - 3babbc + 6babcb - 3bacbb
 *
 * + 12abb - 36bab + 18bba + 6aabc - 12abac + 6baac - 6caab + 12caba - 6cbaa + abbbc - 3abcbb + 2acbbb
 * [+ a ]·[+ 2b - bbc + 2bcb - cbb]·[+ 2b ]
 * ----------------
 * + 16abb - 36bab + 18bba + 6aabc - 12abac + 6baac - 6caab + 12caba - 6cbaa + abbbc - 2abbcb + abcbb
 *
 * + 16abb - 36bab + 18bba + 6aabc - 12abac + 6baac - 6caab + 12caba - 6cbaa + abbbc - 2abbcb + abcbb
 * [+ ab ]·[+ 2b - bbc + 2bcb - cbb]·[1]
 * ----------------
 * + 18abb - 36bab + 18bba + 6aabc - 12abac + 6baac - 6caab + 12caba - 6cbaa
 *
 * + 18abb - 36bab + 18bba + 6aabc - 12abac + 6baac - 6caab + 12caba - 6cbaa
 * [1]·[+ 4a - abc + acb + bca - cba]·[+ 6a ]
 * ----------------
 * - 24aa + 18abb - 36bab + 18bba + 6aabc - 12abac + 6abca - 6acba + 6baac - 6bcaa - 6caab + 12caba
 *
 * - 24aa + 18abb - 18bab + 6aabc - 12abac + 6abca - 6acab + 6acba + 6baac - 6baca
 * [+ ba ]·[+ 3b - ac + ca]·[6]
 * ----------------
 * - 24aa + 18abb + 6aabc - 12abac + 6abca - 6acab + 6acba
 *
 * - 24aa + 18abb + 6aabc - 12abac + 6abca - 6acab + 6acba
 * [+ a ]·[+ 4a - abc + acb + bca - cba]·[6]
 * ----------------
 * + 18abb + 6aacb - 12abac + 12abca - 6acab
 *
 * + 36abb - 12abac + 12abca
 * [+ ab ]·[+ 3b - ac + ca]·[1]
 * ----------------
 * 0
 *
 */
public class JunctionTest {
    //(r2, r1),
    @Test
    public void testJoin() throws Exception {
        Polynomial r1 = mount("+ 6aab - 12aba + 6baa + abbb - 3babb + 3bbab - bbba");
        Polynomial r2 = mount("- 2b + bbc - 2bcb + cbb");
        Polynomial r3 = mount("- 4a + abc - acb - bca + cba");
        Polynomial r4 = mount("- 3b + ac - ca");
        Polynomial junction21 = new Junction(r2, r1).join();
        System.out.println(junction21);
        Set<Polynomial> input = new TreeSet<Polynomial>();
        input.add(r1);
        input.add(r2);
        input.add(r3);
        input.add(r4);
        input.add(junction21);
        
        input = doReductions(input);
        Set<Polynomial> input1 = new TreeSet<Polynomial>();
        Polynomial j = mount("+ 18abb + 6aacb - 12abac + 12abca - 6acab");
        input1.add(j);
        input1.add(r4);
        input1 = doReductions(input1);
        for (Polynomial r : input1) {
            System.out.println("> " + r);
        }
    }
   
    //+ 6aab - 12aba + 6baa + abbb - 3babb + 3bbab - bbba
    //- 2b + bbc - 2bcb + cbb
    //- 4a + abc - acb - bca + cba
    //- 3b + ac - ca
}