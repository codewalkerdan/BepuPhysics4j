package bepu.bepuutilities.memory;


/**
 * ManagedPool is an abstract class designed to facilitate object pooling, where objects can be reused
 * instead of being created or destroyed frequently. This class manages a collection of reusable objects
 * and provides mechanisms to take objects from the pool and release them back into the pool.
 * It provides basic functionality for manipulating the pool of objects and must be extended by a concrete subclass
 * that implements the method {@code getNewObject()} to create instances of the pooled object type.
 *
 * @param <T> The type of objects managed by the pool.
 */
public abstract class ManagedPool<T> {
    private int availableCount;
    private T[] availableObjects;

    /**
     * Gets the number of previously returned objects waiting in the pool.
     *
     * @return number of previously returned objects.
     */
    public int getAvailableCount() {
        return availableCount;
    }

    @SuppressWarnings("unchecked")
    public ManagedPool(int initialCapacity){
        availableCount = 0;
        assert initialCapacity > 0;
        availableObjects = (T[]) new Object[initialCapacity];
    }

    public T take() {
        if (availableCount > 0) {
            return availableObjects[--availableCount];
        }
        return getNewObject();
    }

    public void release(T object) {
        if(availableCount == availableObjects.length) {
            assert availableCount > 0;
            internalResize(availableCount * 2);
        }
        releaseUnsafely(object);
    }

    /**
     * Returns an object to the pool without checking if a resize is required on the available object stack.
     *
     * @param obj The object to release.
     */
    public void releaseUnsafely(T obj){
        assert availableObjects.length > availableCount;
        availableObjects[availableCount++] = obj;
    }

    /**
     * Resets the Pool.
     */
    public void clear() {
        availableCount = 0;
    }

    /**
     * Ensures that the underlying object queue can hold at least a certain number of objects.
     *
     * @param capacity Number of elements to preallocate space for in the available objects queue.
     */
    public void ensureCapacity(int capacity) {
        if(capacity > availableObjects.length){
            internalResize(capacity);
        }
    }

    /**
     * Shrinks the available object queue to the smallest size that can fit the given count and the current available object count.
     *
     * @param minimumCount Number of elements to guarantee space for in the available object queue.
     */
    public void compact (int minimumCount) {
        int targetLength = Math.min(minimumCount, availableCount);
        if(targetLength != availableObjects.length){
            internalResize(targetLength);
        }
    }

    /**
     * Resizes the underlying buffer to the smallest size required to hold the given count and the current available id count.
     *
     * @param count Number of elements to guarantee space for in the available object queue.
     */
    public void resize(int count){
        int targetLength = Math.min(count, availableCount);
        if(targetLength != availableObjects.length){
            internalResize(targetLength);
        }
    }

    protected abstract T getNewObject();

    @SuppressWarnings("unchecked")
    private void internalResize(int newSize){
        assert availableObjects.length != newSize : "Did you really mean to resize this? Nothing changed!";
        T[] newArray = (T[]) new Object[newSize];
        System.arraycopy(availableObjects, 0, newArray, 0, availableObjects.length);
        availableObjects = newArray;
    }
}
