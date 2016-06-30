package main;

import java.util.Stack;

public class Parser {
    public static Proposition parse(String string) {
        string = string.trim();
        int length = string.length();
        if (length == 0) {
            return null;
        }
        if (string.charAt(0) != '(') {
            if ((string.charAt(0) >= 'A' && string.charAt(0) <= 'Z') || (string.charAt(0) >= 'a' && string.charAt(0) <= 'z')) {
                if (string.length() > 1) {
                    if (string.charAt(1) == '_' && string.charAt(2) == '{' && string.charAt(string.length()-1) == '}' && string.length() > 4) {
                        new Proposition(string);
                    } else return null;
                } else return new Proposition(string);
            } else return null;
        }
        Stack<Object> stack = new Stack<>();
        int pointer = 0;
        try {
            while (pointer < length) {
                if (string.charAt(pointer) == '(') {
                    stack.push("(");
                    pointer++;
                } else if (string.charAt(pointer) == ')') {
                    Object p1;
                    Object p2;
                    Object c;
                    if (!stack.peek().equals("(")) {
                        p1 = stack.pop();
                    } else {
                        return null;
                    }
                    if (!stack.peek().equals("(")) {
                        c = stack.pop();
                    } else {
                        return null;
                    }
                    if (!(stack.peek().equals("("))) {
                        p2 = stack.pop();
                        if (stack.peek().equals("(")) {
                            stack.pop(); // clear the '('
                        } else {
                            return null;
                        }
                        Proposition p = new Proposition(String.format("(%s %s %s)", p2.toString(), c.toString(), p1.toString()), (Proposition) p2, (Proposition) p1, (Connective) c);
                        stack.push(p);
                    } else {
                        stack.pop(); // clear the '('
                        Proposition p = new Proposition(String.format("(%s %s)", c.toString(), p1.toString()), (Proposition) p1);
                        stack.push(p);
                    }
                    pointer++;
                } else if (string.charAt(pointer) == ' ' || string.charAt(pointer) == '\t' || string.charAt(pointer) == '\b') {
                    pointer++;
                } else if (string.charAt(pointer) == '\\') {
                    switch (string.charAt(pointer + 1)) {
                        case 'a': {
                            if (string.substring(pointer + 1, pointer + 4).equals("and")) {
                                stack.push(Connective.AND);
                                pointer += 4;
                            } else return null;
                            break;
                        }
                        case 'o': {
                            if (string.substring(pointer + 1, pointer + 3).equals("or")) {
                                stack.push(Connective.OR);
                                pointer += 3;
                            } else return null;
                            break;
                        }
                        case 'n': {
                            if (string.substring(pointer + 1, pointer + 4).equals("not")) {
                                stack.push(Connective.NOT);
                                pointer += 4;
                            } else return null;
                            break;
                        }
                        case 'i': {
                            if (string.substring(pointer + 1, pointer + 6).equals("imply")) {
                                stack.push(Connective.IMPLY);
                                pointer += 6;
                            } else return null;
                            break;
                        }
                        case 'e': {
                            if (string.substring(pointer + 1, pointer + 3).equals("eq")) {
                                stack.push(Connective.EQ);
                                pointer += 3;
                            } else return null;
                            break;
                        }
                        default:
                            return null;
                    }
                } else if ((string.charAt(pointer) >= 'A' && string.charAt(pointer) <= 'Z') || (string.charAt(pointer) >= 'a' && string.charAt(pointer) <= 'z')) {
                    if (string.charAt(pointer + 1) == '_') {
                        if (string.charAt(pointer + 2) == '{') {
                            int temp = pointer + 3;
                            while (string.charAt(temp) != '}') temp++;
                            if (temp == pointer + 3) {
                                return null;
                            }
                            stack.push(new Proposition(string.substring(pointer, temp + 1)));
                            pointer = temp + 1;
                        } else return null;
                    } else {
                        stack.push(new Proposition(string.charAt(pointer) + ""));
                        pointer++;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        if (stack.size() == 1) return (Proposition) stack.pop();
        else return null;
    }
}
