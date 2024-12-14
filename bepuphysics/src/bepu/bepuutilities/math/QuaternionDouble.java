package bepu.bepuutilities.math;

public class QuaternionDouble {
    public static final QuaternionDouble IDENTITY = new QuaternionDouble(0,0,0,1);

    protected double x, y, z, w;

    public QuaternionDouble(){
        x = 0;
        y = 0;
        z = 0;
        w = 1;
    }

    public QuaternionDouble(double x, double y, double z, double w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }

    public double getW(){
        return w;
    }

    public QuaternionDouble set(double x, double y, double z, double w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public QuaternionDouble set(QuaternionDouble other){
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
        return this;
    }

    /**
     * Transforms the vector, assuming that the output does not alias with the input.
     * @param v Vector to transform.
     * @param result Transformed vector. It can be null;
     *
     * @return the result vector
     */
    public Vector3Double transformWithoutOverlap(Vector3Double v, Vector3Double result){
        if(result == null) {
            result = new Vector3Double();
        }

        if(v.x == 0 && v.y == 0 && v.z == 0){
            return result.set(0,0,0);
        }

        //This operation is an optimized-down version of v' = q * v * q^-1.
        //The expanded form would be to treat v as an 'axis only' quaternion
        //and perform standard quaternion multiplication.  Assuming q is normalized,
        //q^-1 can be replaced by a conjugation.
        double x2 = x + x;
        double y2 = y + y;
        double z2 = z + z;
        double xx2 = x * x2;
        double xy2 = x * y2;
        double xz2 = x * z2;
        double yy2 = y * y2;
        double yz2 = y * z2;
        double zz2 = z * z2;
        double wx2 = w * x2;
        double wy2 = w * y2;
        double wz2 = w * z2;

        result.x = v.x * (1.0 - yy2 - zz2) + v.y * (xy2 - wz2) + v.z * (xz2 + wy2);
        result.y = v.x * (xy2 + wz2) + v.y * (1.0 - xx2 - zz2) + v.z * (yz2 - wx2);
        result.z = v.x * (xz2 - wy2) + v.y * (yz2 + wx2) + v.z * (1.0 - xx2 - yy2);

        return result;
    }

    /**
     * Computes the conjugate of the quaternion.
     *
     * @param result Conjugated quaternion
     * @return The modified conjugated quaternion
     */
    public QuaternionDouble conjugate(QuaternionDouble result){
        if(result == null) {
            result = new QuaternionDouble();
        }

        result.x = -x;
        result.y = -y;
        result.z = -z;
        result.w = w;

        return result;
    }
}
