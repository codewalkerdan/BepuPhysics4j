package bepu.bepuutilities.memory;

import bepu.bepuutilities.math.QuaternionDouble;

/**
 * QuaternionPool is a singleton class that provides an object pool for {@link QuaternionDouble} instances.
 * It extends the {@link ManagedPool} class, managing a fixed pool of reusable {@link QuaternionDouble} objects.
 * The pool reduces the overhead of frequent object creation and destruction by reusing objects.
 */
public class QuaternionPool extends ManagedPool<QuaternionDouble>{
    private static final QuaternionPool INSTANCE = new QuaternionPool();

    public static QuaternionPool getInstance() {
        return INSTANCE;
    }

    private QuaternionPool() {
        super(16);
    }

    @Override
    protected QuaternionDouble getNewObject() {
        return new QuaternionDouble();
    }
}
