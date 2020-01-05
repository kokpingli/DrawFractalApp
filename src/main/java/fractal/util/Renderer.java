package fractal.util;

import java.util.LinkedList;

public class Renderer {

    private LinkedList<RendererVersion> history = new LinkedList<>();

    public void setData(double[][] data) {
        history.push(new RendererVersion(data));
    }

    public RendererVersion getWorkingCopy() {
        return current().copy();
    }

    private RendererVersion current() {
        return history.peekLast();
    }
}
