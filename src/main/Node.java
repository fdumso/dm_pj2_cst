package main;

public class Node {
    private Proposition proposition;
    private Assignment assignment;

    private Node father;
    private Node son1;
    private Node son2;

    private int level;

    private boolean isLeaf;

    private boolean checked;
    private ReduceFlag reduceFlag;
    private boolean contradictory;

    public Node(Proposition proposition, Assignment assignment) {
        this.proposition = proposition;
        this.assignment = assignment;

        this.isLeaf = true;
        this.reduceFlag = new ReduceFlag(false);
        this.contradictory = false;
        this.checked = false;
    }

    public Node copy() {
        Node result = new Node(this.proposition, this.assignment);
        result.reduceFlag = reduceFlag;
        return result;
    }


    public void setProposition(Proposition proposition) {
        this.proposition = proposition;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public void setSon1(Node son1) {
        this.son1 = son1;
    }

    public void setSon2(Node son2) {
        this.son2 = son2;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public Proposition getProposition() {
        return proposition;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public Node getFather() {
        return father;
    }

    public Node getSon1() {
        return son1;
    }

    public Node getSon2() {
        return son2;
    }

    public int getLevel() {
        return level;
    }

    public boolean getReduceFlag() {
        return reduceFlag.isReduced();
    }

    public boolean isContradictory() {
        return contradictory;
    }

    public void setReduceFlag(boolean reduced) {
        this.reduceFlag.setReduced(reduced);
    }

    public void setContradictory(boolean contradictory) {
        if (contradictory) {
            this.isLeaf = true;
            this.son1 = null;
            this.son2 = null;
        }
        this.contradictory = contradictory;
    }

    public boolean contradictoryTo(Node node) {
        if (node.getProposition().equals(proposition) && node.getAssignment()!=this.assignment) {
            return true;
        } else {
            if (father!=null) {
                return father.contradictoryTo(node);
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return this.assignment.toString()+" "+this.proposition.toString();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public class ReduceFlag {
        private boolean reduced;

        public ReduceFlag(boolean reduced) {
            this.reduced = reduced;
        }

        public boolean isReduced() {
            return reduced;
        }

        public void setReduced(boolean reduced) {
            this.reduced = reduced;
        }
    }

    public class ContradictoryFlag {
        private boolean contradictory;

        public ContradictoryFlag(boolean contradictory) {
            this.contradictory = contradictory;
        }

        public boolean isContradictory() {
            return contradictory;
        }

        public void setContradictory(boolean contradictory) {
            this.contradictory = contradictory;
        }
    }
}
