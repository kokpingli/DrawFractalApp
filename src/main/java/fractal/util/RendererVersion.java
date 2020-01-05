package fractal.util;

public class RendererVersion {
    private double[][] data;

    public double[][] data() {
        return data;
    }

    public RendererVersion copy() {
        return new RendererVersion(data);
    }

    public RendererVersion(double[][] data) {
        this.data = data;
    }
}
