package main;

import java.util.ArrayList;

public class CST {
    private Node root;
    private ArrayList<Node> premiseNodeList;

    public CST(Proposition rootProp, ArrayList<Proposition> premiseList) {
        this.root = new Node(rootProp, Assignment.F);
        this.premiseNodeList = new ArrayList<>();
        for (int i = 0; i < premiseList.size(); i++) {
            premiseNodeList.add(new Node(premiseList.get(i), Assignment.T));
        }
    }

    public String reduce() {
        String result = "";
        Node node = getLeastUnreducedNode(root);
        if (node==null) {
            return "";
        }
        if (node == root) {
            result += root.toString() + "\n";
        }

        if (node.getProposition().getType() == Proposition.Type.LETTER) {
            // check contradiction
            boolean contradictory = node.contradictoryTo(node);
            node.setContradictory(contradictory);
        } else if (node.getProposition().getType() == Proposition.Type.UNARY) {
            Node son = new Node((node.getProposition()).getSon1(), node.getAssignment().negate());
            result += add1Son(node, node, son);
        } else {
            if (node.getAssignment() == Assignment.T) {
                switch (node.getProposition().getConnective()) {
                    case AND: {
                        Node son1= new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        result += add2SonV(node, node, son1, son2);
                        break;
                    }
                    case OR: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        result += add2SonH(node, node, son1, son2);
                        break;
                    }
                    case IMPLY: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        result += add2SonH(node, node, son1, son2);
                        break;
                    }
                    case EQ: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        Node son3 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son4 = new Node(node.getProposition().getSon2(), Assignment.F);
                        result += add4Son(node, node, son1, son2, son3, son4);
                        break;
                    }
                }
            } else if (node.getAssignment() == Assignment.F) {
                switch (node.getProposition().getConnective()) {
                    case AND: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        result += add2SonH(node, node, son1, son2);
                        break;
                    }
                    case OR: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        result += add2SonV(node, node, son1, son2);
                        break;
                    }
                    case IMPLY: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        result += add2SonV(node, node, son1, son2);
                        break;
                    }
                    case EQ: {
                        Node son1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node son2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        Node son3 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node son4 = new Node(node.getProposition().getSon2(), Assignment.T);
                        result += add4Son(node, node, son1, son2, son3, son4);
                        break;
                    }
                }
            }
        }
        node.setReduced(true);

        if (premiseNodeList.size() > 0) {
            Node premise = premiseNodeList.get(0);
            result += addPremise(root, premise);
            premiseNodeList.remove(0);
        }
        result += reduce();
        return result;
    }

    public String addPremise(Node root, Node premise) {
        String result = "";
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                root.setSon1(premise);
                root.setLeaf(false);
                premise.setFather(root);
                premise.setLevel(root.getLevel()+1);
                result += premise.toString() + "\n";
            }
        } else {
            result += addPremise(root.getSon1(), premise);
            if (root.getSon2()!=null) {
                result += addPremise(root.getSon2(), premise);
            }
        }
        return result;
    }

    public String add1Son(Node root, Node father, Node son) {
        String result = "";
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                root.setSon1(son);
                root.setLeaf(false);
                son.setFather(root);
                son.setLevel(root.getLevel()+1);
                result += father.toString() + "\n";
                result += son.toString() + "\n";
            }
        } else {
            result += add1Son(root.getSon1(), father, son);
            if (root.getSon2()!=null) {
                result += add1Son(root.getSon2(), father, son);
            }
        }
        return result;
    }

    public String add2SonH(Node root, Node father, Node son1, Node son2) {
        String result = "";
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                root.setSon1(son1);
                root.setSon2(son2);
                root.setLeaf(false);
                son1.setFather(root);
                son2.setFather(root);
                son1.setLevel(root.getLevel()+1);
                son2.setLevel(root.getLevel()+1);
                result += father.toString() + "\n";
                result += son1.toString() + "\n";
                result += son2.toString() + "\n";
            }
        } else {
            result += add2SonH(root.getSon1(), father, son1, son2);
            if (root.getSon2()!=null) {
                result += add2SonH(root.getSon2(), father, son1, son2);
            }
        }
        return result;
    }

    public String add2SonV(Node root, Node father, Node son1, Node son2) {
        String result = "";
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                root.setSon1(son1);
                root.setLeaf(false);
                son1.setFather(root);
                son1.setLevel(root.getLevel()+1);

                son1.setSon1(son2);
                son1.setLeaf(false);
                son2.setFather(son1);
                son2.setLevel(son1.getLevel()+1);

                result += father.toString() + "\n";
                result += son1.toString() + "\n";
                result += son2.toString() + "\n";
            }
        } else {
            result += add2SonV(root.getSon1(), father, son1, son2);
            if (root.getSon2()!=null) {
                result += add2SonV(root.getSon2(), father, son1, son2);
            }
        }
        return result;
    }

    public String add4Son(Node root, Node father, Node son1, Node son2, Node son3, Node son4) {
        String result = "";
        if (root.isLeaf()) {
            if (!root.isContradictory()) {
                root.setSon1(son1);
                root.setSon2(son2);
                root.setLeaf(false);
                son1.setFather(root);
                son2.setFather(root);
                son1.setLevel(root.getLevel()+1);
                son2.setLevel(root.getLevel()+1);

                son1.setSon1(son3);
                son1.setLeaf(false);
                son3.setFather(son1);
                son3.setLevel(son1.getLevel()+1);

                son2.setSon1(son4);
                son2.setLeaf(false);
                son4.setFather(son2);
                son4.setLevel(son2.getLevel()+1);

                result += father.toString() + "\n";
                result += son1.toString() + "\n";
                result += son2.toString() + "\n";
                result += son3.toString() + "\n";
                result += son4.toString() + "\n";
            }
        } else {
            result += add4Son(root.getSon1(), father, son1, son2, son3, son4);
            if (root.getSon2()!=null) {
                result += add4Son(root.getSon2(), father, son1, son2, son3, son4);
            }
        }
        return result;
    }


    public Node getLeastUnreducedNode(Node node) {
        if (!node.isReduced()) {
            return node;
        } else {
            if (node.isLeaf()) {
                return null;
            } else {
                if (node.getSon2()!=null) {
                    // has two son
                    Node fromSon1 = getLeastUnreducedNode(node.getSon1());
                    Node fromSon2 = getLeastUnreducedNode(node.getSon2());
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
                    return getLeastUnreducedNode(node.getSon1());
                }
            }
        }

    }
}
