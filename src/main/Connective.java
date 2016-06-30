package main;

public enum Connective {
    AND, OR, NOT, IMPLY, EQ;

    public String toString() {
        switch (this) {
            case AND: return "\\and";
            case OR: return "\\or";
            case NOT: return "\\not";
            case IMPLY: return "\\imply";
            case EQ: return "\\eq";
            default: return "";
        }
    }
}
