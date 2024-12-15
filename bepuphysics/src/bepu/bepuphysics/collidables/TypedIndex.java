package bepu.bepuphysics.collidables;

/**
 * Represents an index with an associated type packed into a single integer.
 */
public class TypedIndex {
    private final long packed;

    /**
     * Bit packed representation of the typed index.
     */
    public long getPacked(){
        return packed;
    }

    /**
     * Gets the type index of the object.
     *
     * @return The type index.
     */
    public int getType() {
        return (int)((packed & 0x7F000000) >> 24);
    }

    /**
     * Gets the index of the object.
     *
     * @return The index.
     */
    public int getIndex() {
        return (int)(packed & 0x00FFFFFF);
    }

    /**
     * Gets whether this index actually refers to anything. The Type and Index should only be used if this is true.
     *
     * @return True if there is a reference; otherwise false.
     */
    public boolean exists() {
        return (packed & (1L << 31)) > 0;
    }

    public TypedIndex(int type, int index){
        assert type >= 0 && type < 128 : "Do you really have that many type indices, or is the index corrupt?";
        assert index >= 0 && index < 1 << 24 : "Do you really have that many object indices, or is the index corrupt?";
        //Note the inclusion of a set bit in the most significant slot.
        //This encodes that the index was explicitly constructed, so it is a 'real' reference.
        //A default constructed TypeIndex will have a 0 in the MSB, so we can use the default constructor for empty references.
        packed = (long)type << 24 | (long)index | (1L << 31);
    }

    @Override
    public String toString() {
        return "<" + getType() + ", " + getIndex() + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TypedIndex other) {
            return other.packed == packed;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (int)packed;
    }
}
