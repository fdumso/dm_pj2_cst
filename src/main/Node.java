package main;

import java.util.ArrayList;

/**
 * Created by ocz on 16-6-7.
 */
public class Node {
    private Proposition proposition;
    private Assignment assignment;

    private Node father;
    private Node son1;
    private Node son2;

    private int level;

    private boolean isLeaf;

    private boolean reduced;
    private boolean contradictory;

    public Node(Proposition proposition, Assignment assignment) {
        this.proposition = proposition;
        this.assignment = assignment;

        this.isLeaf = true;
        this.reduced = false;
        this.contradictory = false;
    }




    public void add1Son(Node node) {
        if (this.isLeaf) {
            if (!contradictory&&!reduced) {
                this.son1 = node;
                this.isLeaf = false;
                node.father = this;
                node.level = level+1;
            }
        } else {
            this.son1.add1Son(node);
            if (this.son2!=null) {
                this.son2.add1Son(node);
            }
        }
    }

    public void add2SonH(Node node1, Node node2) {
        if (this.isLeaf) {
            if (!contradictory&&!reduced) {
                this.son1 = node1;
                this.son2 = node2;
                this.isLeaf = false;
                node1.father = this;
                node2.father = this;
                node1.level = level+1;
                node2.level = level+1;
            }
        } else {
            this.son1.add2SonH(node1, node2);
            if (this.son2!=null) {
                this.son2.add2SonH(node1, node2);
            }
        }
    }

    public void add2SonV(Node node1, Node node2) {
        if (this.isLeaf) {
            if (!contradictory&&!reduced) {
                this.son1 = node1;
                this.isLeaf = false;
                node1.father = this;
                node1.level = level+1;
                node1.son1 = node2;
                node1.isLeaf = false;
                node2.father = node1;
                node2.level = node1.level+1;
            }
        } else {
            this.son1.add2SonV(node1, node2);
            if (this.son2!=null) {
                this.son2.add2SonV(node1, node2);
            }
        }
    }

    public void add4Son(Node node1, Node node2, Node node3, Node node4) {
        if (this.isLeaf) {
            if (!contradictory&&!reduced) {
                this.son1 = node1;
                this.son2 = node2;
                this.isLeaf = false;
                node1.father = this;
                node2.father = this;
                node1.level = level+1;
                node2.level = level+1;

                node1.son1 = node3;
                node1.isLeaf = false;
                node3.father = node1;
                node3.level = node1.level+1;

                node2.son1 = node4;
                node2.isLeaf = false;
                node4.father = node2;
                node4.level = node2.level+1;
            }
        } else {
            this.son1.add4Son(node1, node2, node3, node4);
            if (this.son2!=null) {
                this.son2.add4Son(node1, node2, node3, node4);
            }
        }
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

    public boolean isReduced() {
        return reduced;
    }

    public boolean isContradictory() {
        return contradictory;
    }

    public void setReduced(boolean reduced) {
        this.reduced = reduced;
    }

    public void setContradictory(boolean contradictory) {
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
}
