package main;

import java.util.ArrayList;

public class CST {
    private Node root;
    private ArrayList<Node> premiseNodeList;
    private String output;
    private String counterExample;

    public CST(Proposition rootProp, ArrayList<Proposition> premiseList) {
        this.root = new Node(rootProp, Assignment.F);
        this.premiseNodeList = new ArrayList<>();
        for (int i = 0; i < premiseList.size(); i++) {
            premiseNodeList.add(new Node(premiseList.get(i), Assignment.T));
        }
        output = check();
        counterExample = checkCounter();
    }

    public String getOutput() {
        return output;
    }

    public String getCounterExample() {
        return counterExample;
    }

    private String checkCounter() {
        String result = "";
        Node counterNode = getCounterNode(root);
        if (counterNode!=null) {
            Node node = counterNode;
            while (node!=null) {
                if (node.getProposition().getType() == Proposition.Type.LETTER) {
                    result += node.toString() + "\n";
                }
                node = node.getFather();
            }
        }
        return result;
    }

    private Node getCounterNode(Node node) {
        if (node.isLeaf()) {
            if (!node.isContradictory()) {
                return node;
            } else {
                return null;
            }
        } else {
            Node fromSon1 = getCounterNode(node.getSon1());
            if (fromSon1!=null) {
                return fromSon1;
            }
            if (node.getSon2()!=null) {
                return getCounterNode(node.getSon2());
            }
            return null;
        }
    }

    private String check() {
        String result = "";
        Node node = next(root);
        if (node==null) {
            return "";
        }
        result += node.toString() + "\n";
        if (node.getProposition().getType() == Proposition.Type.LETTER) {
            // check contradiction
            boolean contradictory = node.contradictoryTo(node);
            node.setContradictory(contradictory);
        }
        if (!node.getReduceFlag()) {
            reduce(node);
        }
        if (premiseNodeList.size() > 0) {
            Node premise = premiseNodeList.get(0);
            addPremise(root, premise);
            premiseNodeList.remove(0);
        }
        node.setChecked(true);
        result += check();
        return result;
    }



    private void reduce(Node node) {
        if (node.getProposition().getType() == Proposition.Type.LETTER) {
            // do nothing
        } else if (node.getProposition().getType() == Proposition.Type.UNARY) {
            Node son = new Node((node.getProposition()).getSon1(), node.getAssignment().negate());
            add1Son(node, node, son);
        } else {
            if (node.getAssignment() == Assignment.T) {
                switch (node.getProposition().getConnective()) {
                    case AND: {
                        Node son1= new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        add2SonV(node, node, son1, son2);
                        break;
                    }
                    case OR: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        add2SonH(node, node, son1, son2);
                        break;
                    }
                    case IMPLY: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        add2SonH(node, node, son1, son2);
                        break;
                    }
                    case EQ: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son3 = new Node(node.getProposition().getSon2(), Assignment.T);
                        Node son4 = new Node(node.getProposition().getSon2(), Assignment.F);
                        add4Son(node, node, son1, son2, son3, son4);
                        break;
                    }
                }
            } else if (node.getAssignment() == Assignment.F) {
                switch (node.getProposition().getConnective()) {
                    case AND: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        add2SonH(node, node, son1, son2);
                        break;
                    }
                    case OR: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        add2SonV(node, node, son1, son2);
                        break;
                    }
                    case IMPLY: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        add2SonV(node, node, son1, son2);
                        break;
                    }
                    case EQ: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son3 = new Node(node.getProposition().getSon2(), Assignment.F);
                        Node son4 = new Node(node.getProposition().getSon2(), Assignment.T);
                        add4Son(node, node, son1, son2, son3, son4);
                        break;
                    }
                }
            }
        }
        node.setReduceFlag(true);
    }

    private void addPremise(Node root, Node premise) {
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                premise = premise.copy();

                root.setSon1(premise);
                root.setLeaf(false);
                premise.setFather(root);
                premise.setLevel(root.getLevel()+1);
            }
        } else {
            addPremise(root.getSon1(), premise);
            if (root.getSon2()!=null) {
                addPremise(root.getSon2(), premise);
            }
        }
    }

    private void add1Son(Node root, Node father, Node son) {
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                father = father.copy();
                son = son.copy();

                root.setSon1(father);
                root.setLeaf(false);
                father.setFather(root);
                father.setLevel(root.getLevel()+1);

                father.setSon1(son);
                father.setLeaf(false);
                son.setFather(father);
                son.setLevel(father.getLevel()+1);
            }
        } else {
            add1Son(root.getSon1(), father, son);
            if (root.getSon2()!=null) {
                add1Son(root.getSon2(), father, son);
            }
        }
    }

    private void add2SonH(Node root, Node father, Node son1, Node son2) {
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                father = father.copy();
                son1 = son1.copy();
                son2 = son2.copy();

                root.setSon1(father);
                root.setLeaf(false);
                father.setFather(root);
                father.setLevel(root.getLevel()+1);


                father.setSon1(son1);
                father.setSon2(son2);
                father.setLeaf(false);
                son1.setFather(father);
                son2.setFather(father);
                son1.setLevel(father.getLevel()+1);
                son2.setLevel(father.getLevel()+1);
            }
        } else {
            add2SonH(root.getSon1(), father, son1, son2);
            if (root.getSon2()!=null) {
                add2SonH(root.getSon2(), father, son1, son2);
            }
        }
    }

    private void add2SonV(Node root, Node father, Node son1, Node son2) {
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                father = father.copy();
                son1 = son1.copy();
                son2 = son2.copy();

                root.setSon1(father);
                root.setLeaf(false);
                father.setFather(root);
                father.setLevel(root.getLevel()+1);

                father.setSon1(son1);
                father.setLeaf(false);
                son1.setFather(father);
                son1.setLevel(father.getLevel()+1);

                son1.setSon1(son2);
                son1.setLeaf(false);
                son2.setFather(son1);
                son2.setLevel(son1.getLevel()+1);
            }
        } else {
            add2SonV(root.getSon1(), father, son1, son2);
            if (root.getSon2()!=null) {
                add2SonV(root.getSon2(), father, son1, son2);
            }
        }
    }

    private void add4Son(Node root, Node father, Node son1, Node son2, Node son3, Node son4) {
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                father = father.copy();
                son1 = son1.copy();
                son2 = son2.copy();
                son3 = son3.copy();
                son4 = son4.copy();

                root.setSon1(father);
                root.setLeaf(false);
                father.setFather(root);
                father.setLevel(root.getLevel()+1);

                father.setSon1(son1);
                father.setSon2(son2);
                father.setLeaf(false);
                son1.setFather(father);
                son2.setFather(father);
                son1.setLevel(father.getLevel()+1);
                son2.setLevel(father.getLevel()+1);

                son1.setSon1(son3);
                son1.setLeaf(false);
                son3.setFather(son1);
                son3.setLevel(son1.getLevel()+1);

                son2.setSon1(son4);
                son2.setLeaf(false);
                son4.setFather(son2);
                son4.setLevel(son2.getLevel()+1);
            }
        } else {
            add4Son(root.getSon1(), father, son1, son2, son3, son4);
            if (root.getSon2()!=null) {
                add4Son(root.getSon2(), father, son1, son2, son3, son4);
            }
        }
    }

    private Node next(Node node) {
        if (!node.isChecked()) {
            return node;
        } else {
            if (node.isLeaf()) {
                return null;
            } else {
                if (node.getSon2()!=null) {
                    // has two son
                    Node fromSon1 = next(node.getSon1());
                    Node fromSon2 = next(node.getSon2());
                    if (fromSon1==null&&fromSon2==null) {
                        return null;
                    } else if (fromSon1 == null) {
                        return fromSon2;
                    } else if (fromSon2 == null) {
                        return fromSon1;
                    } else {
                        if (fromSon1.getLevel()<=fromSon2.getLevel()) {
                            return fromSon1;
                        } else {
                            return fromSon2;
                        }
                    }
                } else {
                    // has one son
                    return next(node.getSon1());
                }
            }
        }
    }

}
