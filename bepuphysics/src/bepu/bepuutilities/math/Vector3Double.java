package bepu.bepuutilities.math;

public class Vector3Double {
    public static final Vector3Double ZERO = new Vector3Double(0, 0, 0);

    public double x, y, z;

    public Vector3Double() {
        x = y = z = 0;
    }

    public Vector3Double(double x, double y, double z) {
        this.set(x, y, z);
    }

    public Vector3Double(Vector3Double other) {
        this.set(other);
    }

    public Vector3Double set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3Double set(Vector3Double other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        return this;
    }

    public Vector3Double addLocal(Vector3Double other){
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vector3Double subtractLocal(Vector3Double other){
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }
}
