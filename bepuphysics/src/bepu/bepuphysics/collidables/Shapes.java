package bepu.bepuphysics.collidables;

public class Shapes {
    private ShapeBatch[] batches;
    private int registeredTypeSpan;
    private int initialCapacityPerBatch;

    //Note that not every index within the batches list is guaranteed to be filled. For example, if only a cylinder has been added, and a cylinder's type id is 7,
    //then the batches.Count and RegisteredTypeSpan will be 8- but indices 0 through 6 will be null.
    //We don't tend to do any performance sensitive iteration over shape type batches, so this lack of contiguity is fine.
    public int getRegisteredTypeSpan() {
        return registeredTypeSpan;
    }

    public int getInitialCapacityPerBatch() {
        return initialCapacityPerBatch;
    }

    public void setInitialCapacityPerBatch(int initialCapacityPerBatch) {
        this.initialCapacityPerBatch = initialCapacityPerBatch;
    }

    public ShapeBatch get(int typeIndex){
        return batches[typeIndex];
    }

    public Shapes(int initialCapacityPerBatch) {
        this.initialCapacityPerBatch = initialCapacityPerBatch;
        //This list pretty much will never resize unless something really strange happens, and since batches use virtual calls,
        //we have to allow storage of reference types.
        batches = new ShapeBatch[16];
    }


}
