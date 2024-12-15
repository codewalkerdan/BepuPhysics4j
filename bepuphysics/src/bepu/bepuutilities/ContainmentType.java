package bepu.bepuutilities;

/**
 * The current containment state of two objects.
 */
public enum ContainmentType {
    /**
     * The objects are separate.
     */
    DISJOINT,
    /**
     * One object fully contains the other.
     */
    CONTAINS,
    /**
     * The objects are intersecting, but neither object fully contains the other.
     */
    INTERSECTS
}
