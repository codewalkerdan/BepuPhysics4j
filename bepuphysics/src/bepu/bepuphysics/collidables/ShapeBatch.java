package bepu.bepuphysics.collidables;

import bepu.bepuutilities.memory.IdPool;

import java.nio.ByteBuffer;

public abstract class ShapeBatch {
    protected ByteBuffer shapesData;
    protected int shapeDataSize;
    protected IdPool idPool;

    private int typeId;
    private boolean compound;

    public int getCapacity() {
        return shapesData.capacity() / shapeDataSize;
    }

    /**
     * Gets the size of the shape type stored in this batch in bytes.
     *
     * @return The size in bytes
     */
    public int getShapeDataSize() {
        return shapeDataSize;
    }

    /**
     * Gets the type id of the shape type in this batch.
     * @return The type id
     */
    public int getTypeId() {
        return typeId;
    }

    protected void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /**
     * Gets whether this shape batch's contained type potentially contains children that require other shape batches.
     *
     * @return True if it contains children; otherwise false.
     */
    public boolean isCompound() {
        return compound;
    }

    protected void setCompound(boolean compound) {
        this.compound = compound;
    }

    protected abstract void dispose(int index);
    protected abstract void removeAndDisposeChildren(int index, Shapes shapes);

    public void remove(int index){
        idPool.release(index);
    }

    public void removeAndDispose(int index){
        dispose(index);
        remove(index);
    }

    public void recursivelyRemoveAndDispose(int index, Shapes shapes){
        removeAndDisposeChildren(index, shapes);
        removeAndDispose(index);
    }


}
