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

    public RigidPose set(Vector3Double position, QuaternionDouble orientation) {
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
    public static Vector3Double transform(Vector3Double v, final RigidPose pose, Vector3Double result){
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
    public static Vector3Double transformByInverse(Vector3Double v, final RigidPose pose, Vector3Double result){
        Vector3Double translated = Vector3Pool.getInstance().take();
        translated.set(v).subtractLocal(pose.position);
        QuaternionDouble inverted = QuaternionPool.getInstance().take();
        pose.orientation.conjugate(inverted);
        result = inverted.transformWithoutOverlap(translated, result);
        Vector3Pool.getInstance().release(translated);
        QuaternionPool.getInstance().release(inverted);
        return result;
    }


}
