package tonegod.gui.framework.core.util;

import com.jme3.util.SafeArrayList;

/**
 *
 * @author t0neg0d
 */
public class Pool<T> {
	SafeArrayList<PoolResource> pool = new SafeArrayList<>(PoolResource.class);
	PoolObjectFactory<T> factory;
	
	/**
	 * Creates a Pool of initial capacity initSize using the PoolObjectFactory provided by the user.
	 * @param factory The PoolObjectFactory used to create new instances of the T
	 * @param initSize The initial size of the Pool
	 */
	public Pool(PoolObjectFactory<T> factory, int initSize) {
		this.factory = factory;
		for (int i = 0; i < initSize; i++) {
			pool.add(new PoolResource<>(factory.newPoolObject()));
		}
	}
	
	/**
	 * Returns the next available T resource from the pool.  If no resource is available, the Pool size is increased by 1 and the new instance is returned.
	 * NOTE: It is important that any resource retrieved from this method is passed to @freePoolObject when no longer in use.
	 * @return An instance of T from the Pool that is currently not in use by the application.
	 */
        @SuppressWarnings("unchecked")
	public T getNextAvailable() {
		T ret = null;
		for (PoolResource<T> resource : pool.getArray()) {
			if (!resource.getInUse()) {
				ret = resource.getResource();
				resource.setInUse(true);
				break;
			}
		}
		if (ret == null) {
			PoolResource<T> next = new PoolResource<T>(factory.newPoolObject());
			ret = next.getResource();
			next.setInUse(true);
			pool.add(next);
		}
		return ret;
	}
	
	/**
	 * Frees the resource for reuse.
	 * @param poolObject The resource originally returned by @getNextAvailable
	 * @return Boolean representing if the resource was successfully freed.
	 */
	public boolean freePoolObject(T poolObject) {
		boolean ret = false;
		for (PoolResource resource : pool.getArray()) {
			if (resource.getResource() == poolObject) {
				resource.setInUse(false);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	private class PoolResource<T> {
		T resource;
		private boolean inUse = false;
		
		public PoolResource(T resource) {
			this.resource = resource;
		}
		
		public T getResource() { return this.resource; }
		
		public void setInUse(boolean inUse) { this.inUse = inUse; }
		
		public boolean getInUse() { return this.inUse; }
	}
}