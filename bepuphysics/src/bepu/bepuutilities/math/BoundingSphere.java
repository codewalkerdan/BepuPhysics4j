package bepu.bepuutilities.math;

/**
 * Provides XNA-like bounding sphere functionality.
 */
public class BoundingSphere {
    private final Vector3Double center = new Vector3Double();
    private double radius;

    /**
     * Gets the location of the center of the sphere.
     * @return Location of the center of the sphere.
     */
    public Vector3Double getCenter() {
        return center;
    }

    /**
     * Gets the radius of the sphere.
     * @return Radius of the sphere.
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Constructs a new bounding sphere.
     * @param center Location of the center of the sphere
     * @param radius Radius of the sphere.
     */
    public BoundingSphere(final Vector3Double center, final double radius) {
        this.center.set(center);
        this.radius = radius;
    }
}
