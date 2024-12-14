package bepu.bepuutilities.memory;

/**
 * Manages a pool of identifier values. Grabbing an id from the pool picks a number that has been picked and returned before,
 * or if none of those are available, the minimum value greater than any existing id.
 *
 * @remarks This contrasts with the IdPool which operates on unmanaged memory. This version only exists to support use cases where the unmanaged version can't be used,
 *          for example, in the BufferPool. While the implementation can be shared, doing so involves creating enough supporting infrastructure that it's simpler to have a managed-only version.
 */
public class ManagedIdPool {
    private int nextIndex;
    private int availableIdCount;
    private int[] availableIds;

    /**
     * Gets the highest value which any index claimed thus far could possibly have.
     * This is not necessarily the current highest claimed index; this value may represent an earlier claim that has already been released.
     * -1 if nothing has ever been claimed.
     *
     * @return The highest possible ID that could have been claimed.
     */
    public int getHighestPossiblyClaimedId(){
        return nextIndex - 1;
    }

    /**
     * Gets the number of previously returned ids waiting in the pool.
     *
     * @return number of previously returned ids.
     */
    public int getAvailableIdCount() {
        return availableIdCount;
    }

    public ManagedIdPool(int initialCapacity){
        nextIndex = 0;
        availableIdCount = 0;
        assert initialCapacity > 0;
        availableIds = new int[initialCapacity];
    }

    public int take() {
        if (availableIdCount > 0) {
            return availableIds[--availableIdCount];
        }
        return nextIndex++;
    }

    public void release(int id) {
        if (availableIdCount == availableIds.length) {
            assert availableIdCount > 0;
            internalResize(availableIds.length * 2);
        }
        releaseUnsafely(id);
    }

    /**
     * Returns an id to the pool without checking if a resize is required on the available id stack.
     *
     * @param id The id to release.
     */
    public void releaseUnsafely(int id){
        assert availableIds.length > availableIdCount;
        availableIds[availableIdCount++] = id;
    }

    /**
     * Resets the IdPool.
     */
    public void clear() {
        nextIndex = 0;
        availableIdCount = 0;
    }

    /**
     * Ensures that the underlying id queue can hold at least a certain number of ids.
     *
     * @param capacity Number of elements to preallocate space for in the available ids queue.
     */
    public void ensureCapacity(int capacity) {
        if(capacity > availableIds.length){
            internalResize(capacity);
        }
    }

    /**
     * Shrinks the available ids queue to the smallest size that can fit the given count and the current available id count.
     *
     * @param minimumCount Number of elements to guarantee space for in the available ids queue.
     */
    public void compact (int minimumCount) {
        int targetLength = Math.min(minimumCount, availableIdCount);
        if(targetLength != availableIds.length){
            internalResize(targetLength);
        }
    }

    /**
     * Resizes the underlying buffer to the smallest size required to hold the given count and the current available id count.
     *
     * @param count Number of elements to guarantee space for in the available ids queue.
     */
    public void resize(int count){
        int targetLength = Math.min(count, availableIdCount);
        if(targetLength != availableIds.length){
            internalResize(targetLength);
        }
    }

    private void internalResize(int newSize){
        assert availableIds.length != newSize : "Did you really mean to resize this? Nothing changed!";
        int[] newArray = new int[newSize];
        System.arraycopy(availableIds, 0, newArray, 0, availableIds.length);
        availableIds = newArray;
    }
}
