package bepu.bepuutilities.memory;

import java.nio.IntBuffer;

/**
 * Manages a pool of identifier values. Grabbing an id from the pool picks a number that has been picked and returned before,
 * or if none of those are available, the minimum value greater than any existing id.
 */
public class IdPool {
    private int nextIndex;
    private int availableIdCount;
    private IntBuffer availableIds;

    /**
     * Gets the highest value which any index claimed thus far could possibly have.
     * This is not necessarily the current highest claimed index; this value may represent an earlier claim that has already been released.
     * -1 if nothing has ever been claimed.
     *
     * @return the highest value claimed
     */
    public int getHighestPossiblyClaimedId(){
        return nextIndex - 1;
    }

    /**
     * Gets the number of previously returned ids waiting in the pool.
     *
     * @return the number of returned ids
     */
    public int getAvailableIdCount() {
        return availableIdCount;
    }

    public int getCapacity(){
        return availableIds.capacity();
    }

    public IdPool(int initialCapacity){
        nextIndex = 0;
        availableIdCount = 0;
        availableIds = IntBuffer.allocate(initialCapacity);
    }

    public boolean isAllocated(){
        return availableIds.hasArray();
    }

    public int take() {
        assert availableIds.hasArray();
        if (availableIdCount > 0) {
            return availableIds.get(--availableIdCount);
        }
        return nextIndex++;
    }

    public void release(int id) {
        assert availableIds.hasArray();
        if (availableIdCount == availableIds.capacity()) {
            internalResize(availableIdCount * 2);
        }
        releaseUnsafely(id);
    }

    /**
     * Returns an id to the pool without checking if a resize is required on the available id stack.
     * @param id The id to release
     */
    public void releaseUnsafely(int id){
        assert availableIds.hasArray();
        availableIds.put(availableIdCount++, id);
    }

    public void clear() {
        nextIndex = 0;
        availableIdCount = 0;
    }

    private void internalResize(int newSize){
        assert newSize != availableIds.capacity() : "Did you really mean to resize this? Nothing changed!";
        IntBuffer newArray = IntBuffer.allocate(newSize);
        availableIds.flip();
        newArray.put(availableIds);
        availableIds.clear();
        availableIds = newArray;
    }

    /**
     * Ensures that the underlying id queue can hold at least a certain number of ids.
     *
     * @param capacity Number of elements to preallocate space for in the available ids queue.
     */
    public void ensureCapacity(int capacity){
        if(!availableIds.hasArray()){
            // If this was disposed, we must explicitly rehydrate it.
            availableIds = IntBuffer.allocate(capacity);
        } else {
            if(capacity > availableIds.capacity()){
                internalResize(capacity);
            }
        }
    }

    /**
     * Shrinks the available ids queue to the smallest size that can fit the given count and the current available id count.
     *
     * @param minimumCount Number of elements to guarantee space for in the available ids queue.
     */
    public void compact (int minimumCount) {
        assert availableIds.hasArray();
        int targetLength = Math.min(minimumCount, availableIdCount);
        if(targetLength < availableIds.capacity()){
            internalResize(targetLength);
        }
    }

    /**
     * Resizes the underlying buffer to the smallest size required to hold the given count and the current available id count.
     *
     * @param count Number of elements to guarantee space for in the available ids queue.
     */
    public void resize(int count){
        if(!availableIds.hasArray()){
            // If this was disposed, we must explicitly rehydrate it.
            availableIds = IntBuffer.allocate(count);
        } else {
            int targetLength = Math.min(count, availableIdCount);
            if(targetLength != availableIds.capacity()){
                internalResize(targetLength);
            }
        }
    }
}
