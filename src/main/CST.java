package main;

import java.util.ArrayList;

/**
 * Created by ocz on 16-6-7.
 */
public class CST {
    private Node root;
    private ArrayList<Node> premiseNodeList;

    public CST(Proposition rootProp, ArrayList<Proposition> premiseList) {
        this.root = new Node(rootProp, Assignment.F);
        for (int i = 0; i < premiseList.size(); i++) {
            premiseNodeList.add(new Node(premiseList.get(i), Assignment.T));
        }
    }

    public void reduce() {
        Node node = getLeastUnreducedNode(root);
        if (node==null) {
            return;
        }
        System.out.println(node.toString());

        if (node.getProposition().getType() == Proposition.Type.LETTER) {
            // check contradiction
            boolean contradictory = node.contradictoryTo(node);
            node.setContradictory(contradictory);
        } else if (node.getProposition().getType() == Proposition.Type.UNARY) {
            node.add1Son(new Node((node.getProposition()).getSon1(), node.getAssignment().negate()));
        } else {
            if (node.getAssignment() == Assignment.T) {
                switch (node.getProposition().getConnective()) {
                    case AND: {
                        Node node1= new Node(node.getProposition().getSon1(), Assignment.T);
                        Node node2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        node.add2SonV(node1, node2);
                        break;
                    }
                    case OR: {
                        Node node1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node node2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        node.add2SonH(node1, node2);
                        break;
                    }
                    case IMPLY: {
                        Node node1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node node2 = new Node(node.getProposition().getSon2(), Assignment.T);
                        node.add2SonH(node1, node2);
                        break;
                    }
                    case EQ: {
                        Node node1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node node3 = new Node(node.getProposition().getSon2(), Assignment.T);
                        Node node2 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node node4 = new Node(node.getProposition().getSon2(), Assignment.F);
                        node.add4Son(node1, node2, node3, node4);
                        break;
                    }
                }
            } else if (node.getAssignment() == Assignment.F) {
                switch (node.getProposition().getConnective()) {
                    case AND: {
                        Node node1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node node2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        node.add2SonH(node1, node2);
                        break;
                    }
                    case OR: {
                        Node node1 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node node2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        node.add2SonV(node1, node2);
                        break;
                    }
                    case IMPLY: {
                        Node node1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node node2 = new Node(node.getProposition().getSon2(), Assignment.F);
                        node.add2SonV(node1, node2);
                        break;
                    }
                    case EQ: {
                        Node node1 = new Node(node.getProposition().getSon1(), Assignment.T);
                        Node node3 = new Node(node.getProposition().getSon2(), Assignment.F);
                        Node node2 = new Node(node.getProposition().getSon1(), Assignment.F);
                        Node node4 = new Node(node.getProposition().getSon2(), Assignment.T);
                        node.add4Son(node1, node2, node3, node4);
                        break;
                    }
                }
            }
        }
        node.setReduced(true);

        // TODO add premise
        reduce();
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
