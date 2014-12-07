package org.misha.utils.copy;

import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.System.arraycopy;

/**
 * Author: mshevelin
 * Date: 3/25/14
 * Time: 4:20 PM
 * <p/>
 * FastByteOutputStream implementation that doesn't synchronize methods
 * and doesn't copy the data trough toByteArray().
 */

final class FastByteOutputStream extends OutputStream {
    private static final int INT = 1024;
    private byte[] bytes;
    private int size;

    FastByteOutputStream() {
        this(5 * INT);
    }

    private FastByteOutputStream(final int initSize) {
        size = 0;
        bytes = new byte[initSize];
    }

    private void checkBufferThenWrite(final int size) {
        if (size > bytes.length) {
            final byte[] old = bytes;
            bytes = new byte[Math.max(size, 2 * bytes.length)];
            arraycopy(old, 0, bytes, 0, old.length);
        }
    }

    @Override
    public final void write(final byte[] b) {
        checkBufferThenWrite(size + b.length);
        arraycopy(b, 0, bytes, size, b.length);
        size += b.length;
    }

    @Override
    public final void write(final byte[] b, final int offset, final int length) {
        checkBufferThenWrite(size + length);
        arraycopy(b, offset, bytes, size, length);
        size += length;
    }

    @Override
    public final void write(final int b) {
        checkBufferThenWrite(size + 1);
        bytes[size++] = (byte) b;
    }

    public InputStream getInputStream() {
        return new FastByteInputStream(bytes, size);
    }
}


