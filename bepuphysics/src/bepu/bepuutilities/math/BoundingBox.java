package bepu.bepuutilities.math;

import bepu.bepuutilities.ContainmentType;
import bepu.bepuutilities.memory.Vector3Pool;

import java.util.List;

/**
 * Provides simple axis-aligned bounding box functionality.
 */
public class BoundingBox {
    public final Vector3Double min = new Vector3Double();
    public final Vector3Double max = new Vector3Double();

    /**
     * Default empty bounding box constructor
     */
    public BoundingBox() {
    }

    /**
     * Constructs a bounding box from the specified minimum and maximum.
     * @param min Location with the lowest X, Y, and Z coordinates contained by the axis-aligned bounding box.
     * @param max Location with the highest X, Y, and Z coordinates contained by the axis-aligned bounding box.
     */
    public BoundingBox(Vector3Double min, Vector3Double max) {
        this.min.set(min);
        this.max.set(max);
    }

    /**
     * Determines if a bounding box intersects another bounding box.
     *
     * @param a First bounding box to test.
     * @param b Second bounding box to test.
     *
     * @return Whether the bounding boxes intersected.
     */
    public static boolean isIntersecting(BoundingBox a, BoundingBox b) {
        return a.min.x <= b.max.x && a.max.x >= b.min.x &&
                a.min.y <= b.max.y && a.max.y >= b.min.y &&
                a.min.z <= b.max.z && a.max.z >= b.min.z;
    }

    /**
     * Determines if a bounding box intersects another bounding box.
     *
     * @param aMin Minimum bounds of bounding box A
     * @param aMax Maximum bounds of bounding box A
     * @param bMin Minimum bounds of bounding box B
     * @param bMax Maximum bounds of bounding box B
     *
     * @return Whether the bounding boxes intersected.
     */
    public static boolean isIntersecting(Vector3Double aMin, Vector3Double aMax, Vector3Double bMin, Vector3Double bMax) {
        return aMin.x <= bMax.x && aMax.x >= bMin.x &&
                aMin.y <= bMax.y && aMax.y >= bMin.y &&
                aMin.z <= bMax.z && aMax.z >= bMin.z;
    }

    /**
     * Computes a bounding box which contains two other bounding boxes.
     *
     * @param minA Minimum of the first bounding box to merge
     * @param maxA Maximum of the first bounding box to merge.
     * @param minB Minimum of the second bounding box to merge.
     * @param maxB Maximum of the second bounding box to merge.
     * @param resultMin Minimum of the merged bounding box.
     * @param resultMax Maximum of the merged bounding box.
     */
    public static void createMerged(Vector3Double minA, Vector3Double maxA, Vector3Double minB, Vector3Double maxB,
                                    Vector3Double resultMin, Vector3Double resultMax) {
        Vector3Double.min(minA, minB, resultMin);
        Vector3Double.max(maxA, maxB, resultMax);
    }

    /**
     * Computes a bounding box which contains two other bounding boxes.
     * @param a First bounding box to contain.
     * @param b Second bounding box to contain.
     * @param result Bounding box to contain both input boxes.
     *
     * @return The containing bounding box
     */
    public static BoundingBox createMerged(BoundingBox a, BoundingBox b, BoundingBox result) {
        if (result == null) {
            result = new BoundingBox();
        }

        createMerged(a.min, a.max, b.min, b.max, result.min, result.max);
        return result;
    }

    /**
     * Determines if a bounding box intersects a bounding sphere.
     * @param sphere Sphere to test for intersection.
     *
     * @return Whether the bounding shapes intersect
     */
    public boolean intersects(BoundingSphere sphere){
        Vector3Double offset = Vector3Pool.getInstance().take();
        Vector3Double result = Vector3Pool.getInstance().take();

        offset.set(sphere.getCenter()).subtractLocal(Vector3Double.min(Vector3Double.max(sphere.getCenter(), min, result), max, result));
        double dotProduct = offset.dot(offset);
        Vector3Pool.getInstance().release(offset);
        Vector3Pool.getInstance().release(result);
        return dotProduct <= sphere.getRadius() * sphere.getRadius();
    }

    public ContainmentType contains(BoundingBox other){
        if (max.x < other.min.x || min.x > other.max.x ||
                max.y < other.min.y || min.y > other.max.y ||
                max.z < other.min.z || min.z > other.max.z){
            return ContainmentType.DISJOINT;
        }
        if (min.x <= other.min.x && max.x >= other.max.x &&
                min.y <= other.min.y && max.y >= other.max.y &&
                min.z <= other.min.z && max.z >= other.max.z){
            return ContainmentType.CONTAINS;
        }
        return ContainmentType.INTERSECTS;
    }

    /**
     * Creates the smallest possible bounding box that contains a list of points.
     * @param points Points to enclose with a bounding box.
     *
     * @return Bounding box which contains the list of points.
     */
    public static BoundingBox createFromPoints(List<Vector3Double> points){
        BoundingBox result = new BoundingBox();
        if(points.isEmpty()){
            throw new IllegalArgumentException("Cannot create a bounding box from an empty list of points.");
        }
        result.min.set(points.getFirst());
        result.max.set(result.min);
        for(int i = points.size() - 1; i >= 1; i--){
            Vector3Double point = points.get(i);
            Vector3Double.min(point, result.min, result.min);
            Vector3Double.max(point, result.max, result.max);
        }
        return result;
    }

    /**
     * Creates a bounding box from a bounding sphere.
     *
     * @param sphere Bounding sphere to be used to create the bounding box.
     * @param result Bounding box created from the bounding sphere.
     *
     * @return Bounding box created from the bounding sphere.
     */
    public static BoundingBox createFromSphere(final BoundingSphere sphere, BoundingBox result){
        if(null == result){
            result = new BoundingBox();
        }

        Vector3Double radius = Vector3Pool.getInstance().take();
        radius.set(sphere.getRadius(), sphere.getRadius(), sphere.getRadius());
        Vector3Double.min(sphere.getCenter(), radius, result.min);
        Vector3Double.max(sphere.getCenter(), radius, result.max);
        Vector3Pool.getInstance().release(radius);
        return result;
    }

    /**
     * Creates a string representation of the bounding box.
     *
     * @return String representation of the bounding box.
     */
    @Override
    public String toString() {
        return "(" + min + ", " + max + ")";
    }
}
