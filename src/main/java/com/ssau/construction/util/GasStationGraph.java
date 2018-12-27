package com.ssau.construction.util;

import com.ssau.construction.domain.GasStation;
import com.ssau.construction.domain.template.*;

import java.util.*;

public class GasStationGraph implements Iterable<Node> {

    private Node entry;
    private Node departure;
    private ArrayList<Node> nodesList = new ArrayList<>();

    public Node getEntry() {
        return entry;
    }


    public Node getDeparture() {
        return departure;
    }

    public ArrayList<Node> getNodesList() {
        return nodesList;
    }


    public GasStationGraph(GasStation gasStation) {  //Построение графа по парковке
        for (int i = 0; i < gasStation.getFunctionalBlockH(); i++) {
            for (int j = 0; j < gasStation.getFunctionalBlockV(); j++) {
                if (gasStation.getGasStation()[i][j] instanceof Road) {
                    Node node = new Node(Template.Road, i, j);
                    nodesList.add(node);
                }
                if (gasStation.getGasStation()[i][j] instanceof GasolinePump) {
                    Node node = new Node(Template.GasolinePump, i, j);
                    nodesList.add(node);
                }
                if (gasStation.getGasStation()[i][j] instanceof Entry) {
                    Node node = new Node(Template.Entry, i, j);
                    this.entry = node;
                    nodesList.add(node);
                }
                if (gasStation.getGasStation()[i][j] instanceof Departure) {
                    Node node = new Node(Template.Departure, i, j);
                    this.departure = node;
                    nodesList.add(node);
                }
            }
        }
        int size = nodesList.size();
        for (int i = 0; i < size; i++) {
            int indexJ = nodesList.get(i).getJ();
            int indexI = nodesList.get(i).getI();
            if (indexI > 0) {
                for (int j = 0; j < size; j++) {
                    if ((nodesList.get(j).getI() == indexI - 1) && (nodesList.get(j).getJ() == indexJ)) {
                        if (nodesList.get(j).getType() != Template.GasolinePump || nodesList.get(i).getType() != Template.GasolinePump)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                    }
                }
            }
            if (indexI < gasStation.getFunctionalBlockH() - 1) {
                for (int j = 0; j < size; j++) {
                    if ((nodesList.get(j).getI() == indexI + 1) && (nodesList.get(j).getJ() == indexJ))
                        if (nodesList.get(j).getType() != Template.GasolinePump || nodesList.get(i).getType() != Template.GasolinePump)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                }
            }
            if (indexJ > 0) {
                for (int j = 0; j < size; j++) {
                    if ((nodesList.get(j).getI() == indexI) && (nodesList.get(j).getJ() == indexJ - 1))
                        if (nodesList.get(j).getType() != Template.GasolinePump || nodesList.get(i).getType() != Template.GasolinePump)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                }
            }
            if (indexJ < gasStation.getFunctionalBlockV() - 1) {
                for (int j = 0; j < size; j++) {
                    if ((nodesList.get(j).getI() == indexI) && (nodesList.get(j).getJ() == indexJ + 1))
                        if (nodesList.get(j).getType() != Template.GasolinePump || nodesList.get(i).getType() != Template.GasolinePump)
                            nodesList.get(i).getAdjacentNodes().add(nodesList.get(j));
                }
            }
        }

    }

    public void fillFreeGasolinePumps() {
        try {
            for (Node node : nodesList) {
                if (node.getType() == Template.GasolinePump)
                    freeGasolinePumps.add(new GasolinePumpNode(node));
            }
        } catch (NullPointerException e) {
            System.out.println("Топология некорректна!");
        }
    }

    public boolean isReachable(Node first, Node last) {
        Queue<Node> queue = new LinkedList<>();
        ArrayList<Node> nodes = new ArrayList<>(nodesList);
        queue.offer(first);
        nodes.remove(first);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            for (Node node : currentNode.getAdjacentNodes()
                    ) {
                if (node.equals(last)) {
                    return true;
                }
                if (nodes.contains(node) && (node.getType() != Template.GasolinePump) && (node.getType() != Template.Departure)) {
                    queue.offer(node);
                    nodes.remove(node);
                }
            }
        }
        return false;
    }

    public ArrayList<Node> getPath1(Node first, Node last) {
        Queue<Node> queue = new LinkedList<>();
        ArrayList<Node> nodes = new ArrayList<>(nodesList);
        LinkedHashMap<Node, Node> map = new LinkedHashMap<Node, Node>();
        queue.offer(first);
        nodes.remove(first);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            for (Node node : currentNode.getAdjacentNodes()
                    ) {
                if (node.equals(last)) {
                    ArrayList<Node> path = new ArrayList<>();
                    if (currentNode.equals(first)) {
                        path.add(last);
                        return path;
                    }
                    path.add(last);
                    path.add(0, currentNode);
                    while (map.get(currentNode) != first) {
                        path.add(0, map.get(currentNode));
                        currentNode = map.get(currentNode);
                    }
                    return path;
                }
                if (nodes.contains(node) && (node.getType() != Template.GasolinePump) && (node.getType() != Template.Departure)) {
                    queue.offer(node);
                    nodes.remove(node);
                    map.put(node, currentNode);
                }
            }
        }
        return null;
    }

    public ArrayList<Node> getPath(Node first, Node last) {
        ArrayList<Node> path = new ArrayList<>();
        if (first.equals(last)) {
            path.add(first);
            return path;
        }
        ArrayList<Node> nodes = new ArrayList<>(nodesList);
        nodes.remove(first);
        if (getNext(nodes, path, first.getAdjacentNodes(), last)) {
            path.add(0, first);
            return path;
        } else return null;
    }

    private boolean getNext(List<Node> nodes, List<Node> path, ArrayList<Node> adjacentNodes, Node last) {
        if (adjacentNodes.contains(last)) {
            path.add(0, last);
            return true;
        }
        for (Node currentNode : adjacentNodes
                ) {
            if (nodes.contains(currentNode)) {
                nodes.remove(currentNode);
                if (getNext(nodes, path, currentNode.getAdjacentNodes(), last)) {
                    path.add(0, currentNode);
                    return true;
                }
            }
        }
        return false;
    }

    public PriorityQueue<GasolinePumpNode> getFreeGasolinePumps() {
        return freeGasolinePumps;
    }

    private PriorityQueue<GasolinePumpNode> freeGasolinePumps = new PriorityQueue<>();


    public GasolinePumpNode getFreeGasolinePump() {

        if (freeGasolinePumps.size() > 0) {
            return freeGasolinePumps.remove();
        } else return null;
    }

    public boolean hasFreeGasolinePumps() {
        return freeGasolinePumps.size() > 0;
    }

    public void freeParkingPlace(GasolinePumpNode parkingPlace) {
        freeGasolinePumps.add(parkingPlace);
    }

    public class GasolinePumpNode implements Comparable<GasolinePumpNode> {

        private Node gasolinePump;
        private ArrayList<Node> pathToEntry;
        private ArrayList<Node> pathToDeparture;

        public ArrayList<Node> getPathToEntry() {
            return pathToEntry;
        }

        public ArrayList<Node> getPathToDeparture() {
            return pathToDeparture;
        }

        public GasolinePumpNode(Node parkingPlace) {
            this.gasolinePump = parkingPlace;
            pathToEntry = GasStationGraph.this.getPath1(GasStationGraph.this.entry, parkingPlace);
            pathToDeparture = GasStationGraph.this.getPath1(parkingPlace, GasStationGraph.this.departure);
        }

        public int getI() {
            return gasolinePump.getI();
        }

        public int getJ() {
            return gasolinePump.getJ();
        }

        @Override
        public int compareTo(GasolinePumpNode o) {
            return pathToEntry.size() - o.pathToEntry.size();
        }

        @Override
        public String toString() {
            return gasolinePump.toString();
        }
    }

    @Override
    public Iterator<Node> iterator() {
        return nodesList.iterator();
    }
}


