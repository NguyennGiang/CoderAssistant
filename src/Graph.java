
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeMap;

import java.util.concurrent.locks.*;


public class Graph {
    private TreeMap<Point, Vertex> vertices;

    Graph(){
        Comparator<Point> c = new Comparator<Point>(){
            public int compare(Point p1, Point p2) {
                if(p1.x < p2.x){
                    return -1;
                } else if(p1.x > p2.x){
                    return 1;
                } else {
                    if(p1.y < p2.y){
                        return -1;
                    } else if(p1.y > p2.y){
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        };
        this.vertices = new TreeMap<Point, Vertex>(c);
    }

    public Collection<Vertex> getGraph(){
        return this.vertices.values();
    }

    public void resetToSize(int size){
        this.vertices.clear();

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                this.add(new Vertex(i, j, 0, Integer.MAX_VALUE));
            }
        }

        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                Vertex v = this.vertices.get(new Point(x, y));
                if(y > 0){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX(), v.getY()-1)), 1);
                }
                if(y < (size-1)){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX(), v.getY()+1)), 1);
                }
                if(x > 0){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX()-1, v.getY())), 1);
                }
                if(x < size-1){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX()+1, v.getY())), 1);
                }
            }
        }

        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                Vertex v = this.vertices.get(new Point(x, y));
                if(y > 0  && x > 0){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX()-1, v.getY()-1)), 1);
                }
                if(y < (size-1) && x > 0){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX()-1, v.getY()+1)), 1);
                }
                if(y > 0  && x < (size -1)){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX()+1, v.getY()-1)), 1);
                }
                if(y < (size-1) && x < (size -1)){
                    v.addEdgeTo(this.vertices.get(new Point(v.getX()+1, v.getY()+1)), 1);
                }
            }
        }
    }

    public Vertex add(Vertex v){
        return this.vertices.put(v.getLocation(), v);
    }

    public Vertex get(Point l){
        return this.vertices.get(l);
    }

    public Vertex get(int x, int y){
        return this.get(new Point(x, y));
    }

    public int size(){
        return this.vertices.size();
    }

    public int width(){
        return (int)Math.sqrt(this.size());
    }

    public void clearStates(){
        for(Vertex v: this.vertices.values()){
            v.setInFringe(false);
            v.setVisited(false);
        }
    }

    public void clear(){
        for(Vertex v: this.vertices.values()){
            v.setInFringe(false);
            v.setVisited(false);
            v.setType(Vertex.VertexType.Empty);
        }
    }


    /*
    public static int dijkstra(Graph graph, Vertex s, Vertex g) {
        for(Vertex v : graph.vertices.values()){
            v.setVisited(false);
            v.setCost(Integer.MAX_VALUE);
        }
        s.setCost(0);
        s.setParent(null);
        Comparator<Vertex> c = new Comparator<Vertex>(){
            public int compare(Vertex i, Vertex j){
                double difference = (i.getCost()-j.getCost());
                if(difference > 0){
                    return 1;
                }
                if(difference < 0){
                    return -1;
                }
                return 0;
            }
        };
        PriorityQueue<Vertex> fringe = new PriorityQueue<Vertex>(20, c);
        fringe.add(s);
        int numRemoved = 0;
        while(!fringe.isEmpty()){
            Vertex v = fringe.remove();
            v.setInFringe(false);
            numRemoved++;
            if(v.equals(g)){
                return numRemoved;
            }else if(!v.isVisited()){
                v.setVisited(true);
                for(Edge e: v.getEdges()){
                    double newCost = v.getCost() + e.getCost();
                    Vertex to = e.getTo();
                    if(to.type != Vertex.VertexType.Block && newCost < to.getCost()){
                        to.setCost(newCost);
                        to.setParent(v);
                        fringe.add(to);
                        to.setInFringe(true);

                    }
                }
            }
        }
        return numRemoved;
    }

    public static int aStar(Graph graph, Vertex start, Vertex goal, Heuristic h) {
        for(Vertex v : graph.vertices.values()){
            v.visited = false;
            v.cost = Integer.MAX_VALUE;
        }
        start.cost = 0;
        start.parent = null;
        Comparator<Vertex> c = new Comparator<Vertex>(){
            public int compare(Vertex i, Vertex j){
                double difference = (i.getEstimate()-j.getEstimate());
                if(difference > 0){
                    return 1;
                }
                if(difference < 0){
                    return -1;
                }
                return 0;
            }
        };
        PriorityQueue<Vertex> fringe = new PriorityQueue<Vertex>(20, c);
        fringe.add(start);
        int result = 0;
        boolean found = false;
        while(!fringe.isEmpty() && !found){
            Vertex v = fringe.remove();
            v.setInFringe(false);
            result++;
            if(v.equals(goal)){
                found = true;
            }else if(!v.isVisited()){
                v.setVisited(true);
                for(Edge e: v.getEdges()){
                    double newcost = v.getCost() + e.getCost();
                    Vertex to = e.getTo();
                    if(to.type == Vertex.VertexType.Empty && newcost < to.getCost()){
                        to.setCost(newcost);
                        to.setEstimate(h.estimate(e.to, goal));
                        to.setParent(v);
                        fringe.add(to);
                        to.setInFringe(true);
                    }
                }
            }
        }
        return result;
    }
    */

    public static class Vertex{
        private Point location;
        private double cost;
        private double estimate;
        private boolean visited;
        private Vertex parent;

        private VertexType type;

        private boolean inFringe;
        private boolean isStart;
        private boolean isGoal;

        private Lock m;

        private ArrayList<Edge> edges;

        Vertex(Point l, int cost, int estimate, VertexType t){
            this.m = new ReentrantLock();
            this.setLocation(l);
            this.setCost(cost);
            this.setEstimate(estimate);

            this.setType(t);
            this.setStart(false);
            this.setGoal(false);

            this.setVisited(false);
            this.setParent(null);
            this.setInFringe(false);
            this.setEdges(new ArrayList<Edge>());
        }

        Vertex(int x, int y, int cost, int estimate, VertexType t){
            this(new Point(x, y), cost, estimate, t);
        }

        Vertex(int x, int y, int cost, int estimate){
            this(new Point(x, y), cost, estimate, VertexType.Empty);
        }

        public Double distanceTo(Vertex v){
            return this.location.distance(v.location);
        }

        public Point getLocation() {
            return location;
        }

        public int getX(){
            return this.location.x;
        }

        public int getY(){
            return this.location.y;
        }

        public void setLocation(Point location) {
            this.m.lock();
            this.location = location;
            this.m.unlock();
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.m.lock();
            this.cost = cost;
            this.m.unlock();
        }

        public double getEstimate() {
            return estimate;
        }

        public void setEstimate(double estimate) {
            this.m.lock();
            this.estimate = estimate;
            this.m.unlock();
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.m.lock();
            this.visited = visited;
            this.m.unlock();
        }

        public boolean isStart() {
            return isStart;
        }

        public void setStart(boolean isStart) {
            this.m.lock();
            this.isStart = isStart;
            this.m.unlock();
        }

        public boolean isGoal() {
            return isGoal;
        }

        public void setGoal(boolean isGoal) {
            this.m.lock();
            this.isGoal = isGoal;
            this.m.unlock();
        }

        public Vertex getParent() {
            return parent;
        }

        public void setParent(Vertex parent) {
            this.m.lock();
            this.parent = parent;
            this.m.unlock();
        }

        public VertexType getType() {
            return type;
        }

        public void setType(VertexType type) {
            this.m.lock();
            this.type = type;
            this.m.unlock();
        }

        public boolean isInFringe() {
            return inFringe;
        }

        public void setInFringe(boolean inFringe) {
            this.m.lock();
            this.inFringe = inFringe;
            this.m.unlock();
        }

        public Color getColor(){
            if(this.type == Vertex.VertexType.Block){
                return Color.BLUE;
            } else {
                if(this.isStart){
                    return Color.GREEN;
                }
                if(this.isGoal){
                    return Color.MAGENTA;
                }
                if(this.inFringe){
                    return Color.CYAN;
                }
                if(this.visited){
                    return Color.RED;
                }
                return Color.WHITE;
            }
        }

        public ArrayList<Edge> getEdges() {
            return edges;
        }

        public void setEdges(ArrayList<Edge> edges) {
            this.edges = edges;
        }

        public boolean addEdgeTo(Vertex v, int cost){
            return this.edges.add(new Edge(v, cost));
        }

        public static enum VertexType{
            Empty, Block;
        }

        public boolean equals(Object o){
            if(o instanceof Vertex){
                Vertex other = (Vertex)o;
                if(this.location.equals(other.location) && this.type == other.type){
                    return true;
                }
            }
            return false;
        }
    }

    public static class Edge{
        private Vertex to;
        private double cost;

        Edge(Vertex to, double c){
            this.to = to;
            this.cost = c;
        }

        public Vertex getTo(){
            return this.to;
        }

        public double getCost(){
            return this.cost;
        }

        public boolean equals(Object o){
            if(o instanceof Edge){
                Edge other = (Edge)o;
                if(this.to.equals(other.to) && this.cost == other.cost){
                    return true;
                }
            }
            return false;
        }
    }
}
