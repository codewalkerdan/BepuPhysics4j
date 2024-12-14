package bepu.bepuutilities.memory;

import bepu.bepuutilities.math.Vector3Double;

/**
 * A pool for managing reusable instances of the {@link Vector3Double} class. This class extends
 * {@link ManagedPool} and provides a singleton instance for pooling and reusing Vector3Double objects.
 * The pool minimizes object creation and garbage collection overhead by reusing objects.
 */
public class Vector3Pool extends ManagedPool<Vector3Double>{
    private static final Vector3Pool INSTANCE = new Vector3Pool();

    public static Vector3Pool getInstance() {
        return INSTANCE;
    }

    private Vector3Pool() {
        super(16);
    }

    @Override
    protected Vector3Double getNewObject() {
        return new Vector3Double();
    }
}
