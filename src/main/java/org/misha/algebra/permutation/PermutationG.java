/*
 * Copyright (c) 2014. Misha's property, all rights reserved.
 */

package org.misha.algebra.permutation;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: mshevelin
 * Date: 11/28/14
 * Time: 11:01 AM
 * <p/>
 * This class represents the notion of permutation of objects of the type T;
 * the multiplication is implemented as follows: if permutation x maps A to B and
 * permutation y maps B to C then permutation x.times(y) maps A to C (permutation
 * acts from right hand side).
 */

@SuppressWarnings("ClassWithTooManyMethods")
@Immutable //in the case if T is immutable
public class PermutationG<T extends Comparable<T>> implements Comparable<PermutationG<T>> {
    final Set<T> set;
    private final int degree;
    private final HashMap<T, T> raw;

    private PermutationG(final T[] objects) {
        degree = objects.length;
        raw = new HashMap<T, T>(degree);
        set = new TreeSet<T>();
    }

    private PermutationG(final Collection<T> objects) {
        degree = objects.size();
        raw = new HashMap<T, T>(degree);
        set = new TreeSet<T>();
    }

    /**
     * static generation method
     *
     * @param objects an array of permuted objects
     * @param <S>     type of objects permuted
     * @return new instance
     */
    public static <S extends Comparable<S>> PermutationG<S> create(final S[] objects) {
        final PermutationG<S> p = new PermutationG<S>(objects);
        Collections.addAll(p.set, objects);
        int i = 0;
        for (final S t : p.set) {
            p.raw.put(t, objects[i]);
            ++i;
        }
        return p;
    }

    /**
     * another static generation method
     *
     * @param objects a collection of permuted objects
     * @param <S>    the type of objects permuted
     * @return new instance
     */
    public static <S extends Comparable<S>> PermutationG<S> create(
            final Collection<S> objects
    ) {
        final PermutationG<S> p = new PermutationG<S>(objects);
        p.set.addAll(objects);
        final Iterator<S> it = objects.iterator();
        for (final S t : p.set) {
            p.raw.put(t, it.next());
        }
        return p;
    }

    private boolean valid(final PermutationG<T> another) {
        return set.containsAll(another.set) && another.set.containsAll(set);
    }

    /**
     * can violate object's state
     *
     * @param key key
     * @return value at key
     */
    public T getAt(final T key) {
        return raw.get(key);
    }

    /**
     * can violate object's state
     *
     * @param another another permutation
     * @return product of this and another
     */
    public PermutationG<T> times(final PermutationG<T> another) {
        if (!valid(another)) {
            throw new IllegalArgumentException("another permutation must be on same set.");
        }
        final PermutationG<T> p = new PermutationG<T>(set);
        for (final T key : set) {
            p.raw.put(key, another.getAt(getAt(key)));
        }
        return p;
    }

    public PermutationG<T> reverse() {
        final PermutationG<T> p = new PermutationG<T>(set);
        for (final T key : set) {
            p.raw.put(getAt(key), key);
        }
        return p;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PermutationG that = (PermutationG) o;
        return raw.equals(that.raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    @Override
    public String toString() {
        return "\n" + asList().toString();
    }

    @Override
    public int compareTo(final PermutationG<T> o) {
        if (!valid(o)) {
            throw new IllegalArgumentException("another permutation must be on same set.");
        }
        for (final T key : raw.keySet()) {
            final int compare = getAt(key).compareTo(o.getAt(key));
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    private T maxValue() {
        T result = null;
        for (final T t : set) {
            result = t;
        }
        return result;
    }

    private T minValue() {
        final Iterator<T> it = set.iterator();
        if (it.hasNext()) {
            return set.iterator().next();
        }
        throw new IllegalStateException("set iterator have to has next");
    }

    private PermutationG<T> shift() {
        final List<T> orig = asList();
        final int size = orig.size();
        if (size > 0) {
            return PermutationG.create(orig.subList(1, size));
        }
        throw new IllegalStateException("can't shift list of size <= 0");
    }

    public int size() {
        return raw.size();
    }

    private List<T> asList() {
        final List<T> list = new ArrayList<T>();
        for (final T key : set) {
            list.add(raw.get(key));
        }
        return list;
    }

    private Collection<PermutationG<T>> sequence() {
        final List<PermutationG<T>> result = new ArrayList<PermutationG<T>>();
        if (degree == 2) {
            return makeForDegreeTwo(result);
        }
        final PermutationG<T> shifted = shift();
        final Collection<T> pattern = new ArrayList<T>(degree);
        makeFirstBlock(result, shifted, pattern);
        final int bound = result.size();
        replicate(result, bound);
        return Collections.unmodifiableCollection(result);
    }

    public static <S extends Comparable<S>> Collection<PermutationG<S>> sequence(
            final PermutationG<S> permutation
    ) {
        final PermutationG<S> id = create(permutation.set);
        return id.sequence();
    }

    private void replicate(final List<PermutationG<T>> result, final int bound) {
        for (int j = 0; j < bound; ++j) {
            for (int i = 1; i < degree; ++i) {
                result.add((PermutationG.create(swap(result.get(j).asList(), 0, i))));
            }
        }
    }

    private void makeFirstBlock(
            final Collection<PermutationG<T>> result, final PermutationG<T> shifted,
            final Collection<T> pattern
    ) {
        for (final PermutationG<T> partial : shifted.sequence()) {
            pattern.add(minValue());
            for (final T key : partial.set) {
                pattern.add(partial.raw.get(key));
            }
            final PermutationG<T> toAdd = PermutationG.create(pattern);
            result.add(toAdd);
            pattern.clear();
        }
    }

    private Collection<PermutationG<T>> makeForDegreeTwo(final Collection<PermutationG<T>> result) {
        Collection<T> tmp = new ArrayList<T>();
        tmp.add(minValue());
        tmp.add(maxValue());
        final PermutationG<T> id = PermutationG.create(tmp);
        result.add(id);
        tmp = swap(tmp, 0, 1);
        final PermutationG<T> transposition = PermutationG.create(tmp);
        result.add(transposition);
        return result;
    }

    private Collection<T> swap(final Collection<T> list, final int i, final int j) {
        final List<T> result = new ArrayList<T>();
        result.addAll(list);
        Collections.swap(result, i, j);
        return result;
    }

    public static PermutationG<Integer> parse(final CharSequence s) {
        final Pattern pattern = Pattern.compile("(\\(\\d)([\\d\\\\,| ]*)(\\d\\))");
        final Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            final String middle = matcher.group(2).trim();
            final String[] middles = StringUtils.split(middle, ",");
            final int length = middles.length;
            final Integer[] integers = new Integer[length + 2];
            integers[0] = Integer.parseInt(StringUtils.split(matcher.group(1).trim(), "\\(")[0]);
            for (int i = 1; i < length + 1; ++i) {
                integers[i] = Integer.parseInt(middles[i - 1].trim());
            }
            integers[length + 1] = Integer.parseInt(StringUtils.split(matcher.group(3).trim(), ")")[0]);
            return create(integers);
        }
        return null;
    }
}
