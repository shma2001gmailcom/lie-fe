package org.misha.utils.copy;

import javax.annotation.Nonnull;
import java.io.InputStream;

import static java.lang.System.arraycopy;

/**
 * Author: mshevelin
 * Date: 3/25/14
 * Time: 4:22 PM
 * ByteArrayInputStream implementation that does not synchronize methods.
 */

final class FastByteInputStream extends InputStream {
    private static final int INT = 255;
    private final byte[] bytes;
    private final int count;// buffer size limit
    private int position;// number of bytes have been read from the buffer

    FastByteInputStream(final byte[] buffer, final int limit) {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        bytes = buffer;
        count = limit;
    }

    @Override
    public final int available() {
        return count - position;
    }

    @Override
    public final int read() {
        return position < count ? bytes[position++] & INT : -1;
    }

    @Override
    public final int read(@Nonnull final byte[] b, final int offset, int length) {
        if (position >= count) {
            return -1;
        }
        if (position + length > count) {
            length = count - position;
        }
        arraycopy(bytes, position, b, offset, length);
        position += length;
        return length;
    }

    @Override
    public final long skip(long n) {
        if (position + n > count) {
            n = (long)count - (long)position;
        }
        if (n < 0) {
            return 0;
        }
        position += n;
        return n;
    }
}



