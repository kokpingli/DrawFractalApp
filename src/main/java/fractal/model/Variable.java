package fractal.model;

import fractal.util.StringUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Variable {
	private StringProperty name;

	public void setName(String value) {
		nameProperty().set(value);
	}

	public String getName() {
		return nameProperty().get();
	}

	public StringProperty nameProperty() {
		if (name == null)
			name = new SimpleStringProperty(this, "name");
		return name;
	}

	private StringProperty initialValue;

	public void setInitialValue(String value) {
		try {
			setComplexNumber(value);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		initialValueProperty().set(value);
	}

	public String getInitialValue() {
		return initialValueProperty().get();
	}

	public StringProperty initialValueProperty() {
		if (initialValue == null)
			initialValue = new SimpleStringProperty(this, "initialValue");
		return initialValue;
	}

	private BooleanProperty constant;

	public void setConstant(Boolean constant) {
		constantProperty().set(constant);
	}

	public boolean isConstant() {
		return constantProperty().get();
	}

	public BooleanProperty constantProperty() {
		if (constant == null)
			constant = new SimpleBooleanProperty(this, "constant", false);
		return constant;
	}

	private BooleanProperty input;

	public void setInput(Boolean input) {
		inputProperty().set(input);
	}

	public boolean isInput() {
		return inputProperty().get();
	}

	public BooleanProperty inputProperty() {
		if (input == null)
			input = new SimpleBooleanProperty(this, "input", false);
		return input;
	}

	private BooleanProperty iterable;

	public void setIterable(Boolean iterable) {
		iterableProperty().set(iterable);
	}

	public boolean isIterable() {
		return iterableProperty().get();
	}

	public BooleanProperty iterableProperty() {
		if (iterable == null)
			iterable = new SimpleBooleanProperty(this, "iterable", false);
		return iterable;
	}

	private ComplexNumber initialValueComplex;

	private void setComplexNumber(String initialValue) {
		try {
			initialValueComplex = StringUtil.toComplexNumber(initialValue);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	public ComplexNumber getComplexNumber() {
		return initialValueComplex;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Variable))
			return false;
		Variable otherVar = (Variable) other;
		return this.getName().equals(otherVar.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
