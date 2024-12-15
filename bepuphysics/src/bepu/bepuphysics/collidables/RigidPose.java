package bepu.bepuphysics.collidables;

import bepu.bepuutilities.math.QuaternionDouble;
import bepu.bepuutilities.math.Vector3Double;
import bepu.bepuutilities.memory.QuaternionPool;
import bepu.bepuutilities.memory.Vector3Pool;

/**
 * Represents a rigid transformation.
 */
public class RigidPose {
    private final QuaternionDouble orientation = new QuaternionDouble();
    private final Vector3Double position = new Vector3Double();

    // DO NOT MODIFY!
    public static final RigidPose IDENTITY = new RigidPose(Vector3Double.ZERO);

    public QuaternionDouble getOrientation() {
        return orientation;
    }

    public Vector3Double getPosition() {
        return position;
    }

    public RigidPose setPosition(Vector3Double position) {
        this.position.set(position);
        return this;
    }

    public RigidPose set(QuaternionDouble orientation) {
        this.orientation.set(orientation);
        return this;
    }

    public RigidPose set(QuaternionDouble orientation, Vector3Double position) {
        this.orientation.set(orientation);
        this.position.set(position);
        return this;
    }

    public RigidPose set(RigidPose pose) {
        this.orientation.set(pose.orientation);
        this.position.set(pose.position);
        return this;
    }

    /**
     * Creates a rigid pose with the given position and orientation.
     *
     * @param quaternion Position of the pose.
     * @param position Orientation of the pose.
     */
    public RigidPose(QuaternionDouble quaternion, Vector3Double position) {
        this.orientation.set(quaternion);
        this.position.set(position);
    }

    /**
     * Creates a rigid pose with the given position and identity orientation.
     *
     * @param position Position of the pose.
     */
    public RigidPose(Vector3Double position){
        this(QuaternionDouble.IDENTITY, position);
    }

    /**
     * Transforms a vector by the rigid pose: v * pose.Orientation + pose.Position.
     *
     * @param v Vector to transform.
     * @param pose Pose to transform the vector with.
     * @param result Transformed vector.
     *
     * @return The modified transformed vector
     */
    public static Vector3Double transform(final Vector3Double v, final RigidPose pose, Vector3Double result){
        Vector3Double rotated = Vector3Pool.getInstance().take();
        pose.orientation.transformWithoutOverlap(v, rotated);
        result.set(rotated).addLocal(pose.position);
        Vector3Pool.getInstance().release(rotated);
        return result;
    }

    /**
     * Transforms a vector by the inverse of a rigid pose: (v - pose.Position) * pose.Orientation^-1.
     *
     * @param v The vector to be transformed.
     * @param pose The rigid pose whose inverse is used for the transformation.
     * @param result The output vector to store the transformed result. It can be null.
     *
     * @return The modified transformed vector.
     */
    public static Vector3Double transformByInverse(final Vector3Double v, final RigidPose pose, Vector3Double result){
        Vector3Double translated = Vector3Pool.getInstance().take();
        translated.set(v).subtractLocal(pose.position);
        QuaternionDouble inverted = QuaternionPool.getInstance().take();
        pose.orientation.conjugate(inverted);
        result = inverted.transformWithoutOverlap(translated, result);
        Vector3Pool.getInstance().release(translated);
        QuaternionPool.getInstance().release(inverted);
        return result;
    }

    /**
     * Inverts the rigid transformation of the pose.
     *
     * @param pose Pose to invert.
     *
     * @return Inverse of the pose.
     */
    public static RigidPose invert(final RigidPose pose, RigidPose result){
        if(null == result){
            result = new RigidPose(Vector3Double.ZERO);
        }

        pose.orientation.conjugate(result.orientation);
        result.position.set(0, 0, 0).subtractLocal(pose.position);
        result.orientation.transform(result.position, result.position);
        return result;
    }

    /**
     * Concatenates one rigid transform with another. The resulting transform is equivalent to performing transform a followed by transform b.
     *
     * @param a First transform to concatenate.
     * @param b Second transform to concatenate.
     * @param result Result of the concatenation.
     *
     * @return The result of the concatenation.
     */
    public static RigidPose MultiplyWithoutOverlap(final RigidPose a, final RigidPose b, RigidPose result){
        if(null == result){
            result = new RigidPose(Vector3Double.ZERO);
        }

        a.orientation.concatenateWithoutOverlap(b.orientation, result.orientation);
        b.orientation.transform(a.position, result.position);
        result.position.addLocal(b.position);
        return result;
    }

    /**
     * Returns a string representing the RigidPose as "Position, Orientation".
     *
     * @return String representing the RigidPose.
     */
    @Override
    public String toString() {
        return orientation + ", " + position;
    }
}
