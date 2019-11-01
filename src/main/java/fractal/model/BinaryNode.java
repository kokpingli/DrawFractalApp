package fractal.model;

public class BinaryNode<V extends Comparable<V>> {
	private BinaryNode<V> left;
	private BinaryNode<V> right;
	private V value;

	public BinaryNode(V value) {
		this.value = value;
		left = null;
		right = null;
	}

	public BinaryNode<V> getLeft() {
		return left;
	}

	public BinaryNode<V> getRight() {
		return right;
	}

	public V getValue() {
		return value;
	}

	public void setLeft(BinaryNode<V> left) {
		this.left = left;
	}

	public void setRight(BinaryNode<V> right) {
		this.right = right;
	}

	boolean isOperator() {
		if (this.value.equals("+") || this.value.equals("-") || this.value.equals("*") || this.value.equals("/")
				|| this.value.equals("%"))
			return true;
		else
			return false;
	}
}
