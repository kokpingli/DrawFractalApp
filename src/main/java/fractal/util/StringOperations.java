package fractal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import fractal.model.BinaryNode;
import fractal.model.ComplexNumber;

public class StringOperations {

	public static ComplexNumber evaluateTree(BinaryNode<String> root, Map<String, ComplexNumber> variables) {
		if (root == null)
			return new ComplexNumber(0, 0);

		if (root.getLeft() == null && root.getRight() == null) {
			String nodeValue = root.getValue();
			if (StringUtils.isNumeric(nodeValue))
				return new ComplexNumber(Integer.parseInt(nodeValue), 0);
			if (StringUtils.isAlpha(nodeValue))
				return variables.get(nodeValue);
			if (nodeValue.matches("\\d*?.\\d*"))
				return new ComplexNumber(Float.parseFloat(nodeValue), 0);
		}

		ComplexNumber l_val = evaluateTree(root.getLeft(), variables);
		ComplexNumber r_val = evaluateTree(root.getRight(), variables);

		if (root.getValue().equals("+")) {
			return l_val.add(r_val);
		}

		if (root.getValue().equals("-")) {
			return l_val.subtract(r_val);
		}

		if (root.getValue().equals("*")) {
			return l_val.multiply(r_val);
		}

		if (root.getValue().equals("/")) {
			return l_val.divide(r_val);
		}

		return l_val.modulo(r_val);
	}

	private static boolean isOperator(String exp) {
		String opRegex = "^[+\\-*/%]$";
		return exp.matches(opRegex) ? true : false;
	}

	private static BinaryNode<String> buildTree(List<String> postfixExp) {
		Stack<BinaryNode<String>> treeInProgress = new Stack<>();

		for (String str : postfixExp) {
			if (!isOperator(str)) {
				treeInProgress.push(new BinaryNode<String>(str));
			} else {
				BinaryNode<String> root = new BinaryNode<String>(str);
				root.setRight(treeInProgress.pop());
				root.setLeft(treeInProgress.pop());
				treeInProgress.push(root);
			}
		}

		return treeInProgress.pop();
	}

	private static ArrayList<String> postfix(ArrayList<String> exp) {

		Stack<String> opStack = new Stack<>();
		Map<String, Integer> opMap = new HashMap<>();
		opMap.put("+", 1);
		opMap.put("-", 1);
		opMap.put("*", 2);
		opMap.put("/", 2);
		opMap.put("%", 2);
		ArrayList<String> postExp = new ArrayList<>();
		StringBuilder operandBuild = new StringBuilder();

		for (String str : exp) {
			if (opMap.containsKey(str)) { // operator
				// add the operand into postExp when encounter an operator
				postExp.add(operandBuild.toString());
				operandBuild.delete(0, operandBuild.length());

				if (opStack.isEmpty()) {
					opStack.push(str);
				} else {
					String opInStack = opStack.peek();
					// if opInStack has lower priority, push into stack
					if (opMap.get(opInStack) < opMap.get(str)) {
						opStack.push(str);
					} else {
						while (!opStack.isEmpty())
							postExp.add(opStack.pop());
						opStack.push(str);
					}
				}

			} else { // operand
				operandBuild.append(str);
			}
		}

		if (operandBuild.length() != 0)
			postExp.add(operandBuild.toString());
		while (!opStack.isEmpty())
			postExp.add(opStack.pop());

		return postExp;
	}

	private static void swap(ArrayList<String> exp) {
		int lastIdx = exp.size() - 1;
		for (int idx = 0; idx < exp.size() / 2; idx++) {
			Collections.swap(exp, idx, lastIdx);
			lastIdx--;
		}
	}

	private static String validateBalancedParentheses(String exp) {
		if (exp.contains("(") || exp.contains(")")) {
			if (StringUtils.countMatches(exp, "(") != StringUtils.countMatches(exp, ")"))
				throw new IllegalArgumentException("Unbalanced parentheses!");
		} else {
			StringBuilder addBracket = new StringBuilder();
			addBracket.append("(");
			addBracket.append(exp);
			addBracket.append(")");
			exp = addBracket.toString();
		}

		return exp;
	}

	public static BinaryNode<String> evaluate(String infix, Map<String, ComplexNumber> variables) {
		String regex = "^[a-zA-Z0-9+\\-*/%().]+$";
		if (!infix.matches(regex) && infix.length() > 0)
			throw new IllegalArgumentException("Only equation is allowed!");

		infix = validateBalancedParentheses(infix);

		Stack<String> expBuilder = new Stack<>();
		List<String> postfixExp = new ArrayList<>();

		for (int idx = 0; idx < infix.length(); idx++) {
			ArrayList<String> strInProgress = new ArrayList<>();
			if (!infix.substring(idx, idx + 1).equals(")")) {
				expBuilder.push(infix.substring(idx, idx + 1));
			} else {
				String element = "";
				do {
					element = expBuilder.pop();
					if (!element.equals("("))
						strInProgress.add(element);
				} while (!expBuilder.isEmpty() && !element.equals("("));

				swap(strInProgress);
				postfixExp = postfix(strInProgress);

				StringBuilder sb = new StringBuilder();
				for (String s : postfixExp)
					sb.append(s + " ");
				expBuilder.push(sb.toString());
			}
		}

		postfixExp = Arrays.asList(expBuilder.pop().split("[ ]+"));

		BinaryNode<String> root = buildTree(postfixExp);

		return root;
	}

}
