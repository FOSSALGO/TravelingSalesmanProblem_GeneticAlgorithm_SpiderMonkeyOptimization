package fosalgo;

public class Vertex {

    String label;
    double x;
    double y;

    public Vertex(String label, double x, double y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return label + " (" + x + " , " + y + ")";
    }
}
