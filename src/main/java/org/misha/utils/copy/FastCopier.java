package org.misha.utils.copy;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Author: mshevelin
 * Date: 3/25/14
 * Time: 4:27 PM
 * <p/>
 * A cloning tool.
 */
@SuppressWarnings("deprecation")
@Deprecated
public final class FastCopier {
    private static final Logger log = Logger.getLogger(FastCopier.class);

    private FastCopier() {
    }

    @SuppressWarnings("unchecked cast")
    public static <T extends Serializable> T copy(final T orig) {
        T copy = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        final FastByteOutputStream os;
        try {
            os = new FastByteOutputStream();
            out = new ObjectOutputStream(os);
            out.writeObject(orig);
            in = new ObjectInputStream(os.getInputStream());
            copy = (T) in.readObject();
        } catch (final Throwable e) {
            log.error(e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                } catch (IOException e) {
                    //
                }
            }
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
        return copy;
    }
}


