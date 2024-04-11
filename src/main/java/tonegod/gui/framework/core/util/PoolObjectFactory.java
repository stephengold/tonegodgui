package tonegod.gui.framework.core.util;

/**
 *
 * @author t0neg0d
 * @param <T> type of object the pool contains
 */
public interface PoolObjectFactory<T> {
    T newPoolObject();
}
