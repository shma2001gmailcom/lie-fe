package org.misha.utils.copy;

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
public final class FastCopier {
    private static final Logger log = Logger.getLogger(FastCopier.class);

    private FastCopier() {
    }

    @SuppressWarnings("unchecked cast")
    public static <T extends Serializable> T copy(final T orig) throws IOException {
        try (FastByteOutputStream os = new FastByteOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(os);
             ObjectInputStream in = new ObjectInputStream(os.getInputStream())
        ) {
            out.writeObject(orig);
            return  (T) in.readObject();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}


