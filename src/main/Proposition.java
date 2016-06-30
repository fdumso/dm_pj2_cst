package main;

public class Proposition {
    private String text;
    private Proposition son1;
    private Proposition son2;
    private Connective connective;
    private Type type;

    enum Type {
        BINARY, UNARY, LETTER;
    }

    public Proposition(String text, Proposition son1, Proposition son2, Connective connective) {
        this.text = text;
        this.son1 = son1;
        this.son2 = son2;
        this.connective = connective;
        this.type = Type.BINARY;
    }

    public Proposition(String text, Proposition son) {
        this.text = text;
        this.son1 = son;
        this.son2 = null;
        this.connective = Connective.NOT;
        this.type = Type.UNARY;
    }

    public Proposition(String text) {
        this.text = text;
        this.son1 = null;
        this.son2 = null;
        this.connective = null;
        this.type = Type.LETTER;
    }

    public String toString() {
        return text;
    }

    public Proposition getSon1() {
        return son1;
    }

    public Proposition getSon2() {
        return son2;
    }

    public Connective getConnective() {
        return connective;
    }

    public Type getType() {
        return type;
    }

    public boolean equals(Proposition p) {
        return this.text.equals(p.text);
    }

}
