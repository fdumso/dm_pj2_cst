package main;

/**
 * Created by ocz on 16-6-8.
 */
public enum Assignment {
    T, F;

    @Override
    public String toString() {
        switch (this) {
            case T: return "T";
            case F: return "F";
            default: return "";
        }
    }

    public Assignment negate() {
        switch (this) {
            case T: return F;
            case F: return T;
            default: return null;
        }
    }
}
