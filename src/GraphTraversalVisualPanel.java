/**
 * @author Maxx Boehme
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GraphTraversalVisualPanel extends JPanel implements MouseMotionListener, MouseListener{

    private static final long serialVersionUID = 1941190661218942493L;
    private Graph graph;

    private Graph.Vertex start;
    private boolean startPressed;

    private Graph.Vertex goal;
    private boolean goalPressed;

    private List<Graph.Vertex> path;

    private PathDisplay pd;


    GraphTraversalVisualPanel(Graph g){
        this.graph = g;

        if(graph.size() >= 4){
            this.start = this.graph.get(0, 0);
            this.start.setStart(true);
            this.goal = this.graph.get(this.graph.width()-1, this.graph.width()-1);
            this.goal.setGoal(true);
        } else {
            this.start = null;
            this.goal = null;
        }

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.path = null;

        this.pd = PathDisplay.Blocks;
    }

    public void setPathDisplay(PathDisplay p){
        this.pd = p;
    }

    public void setGraph(Graph g){
        this.graph = g;
        this.path = null;
        if(graph.size() >= 4){
            this.start = this.graph.get(0, 0);
            this.start.setStart(true);
            this.goal = this.graph.get(this.graph.width()-1, this.graph.width()-1);
            this.goal.setGoal(true);
        } else {
            this.start = null;
            this.goal = null;
        }
    }

    public Graph.Vertex getStart(){
        return this.start;
    }

    public void setPath(List<Graph.Vertex> p){
        this.path = p;
    }

    public Graph.Vertex getGoal(){
        return this.goal;
    }

    public void paintComponent(Graphics g){
        if(this.graph != null && this.graph.size() > 0){
//            System.out.println(this.graph.size());
            int dimensions = (int)Math.sqrt(this.graph.size());
//            System.out.println(this.getWidth());
            int widthSize = this.getWidth()/ dimensions;
            int numOfVertices = this.getWidth()/widthSize;
//            System.out.println(dimensions + " " + widthSize + " " + numOfVertices);
            for(int i = 0; numOfVertices > i; i++){
                for(int j = 0; j < numOfVertices; j++){
                    Graph.Vertex v = this.graph.get(i, j);
                    if(v != null){
                        g.setColor(v.getColor());
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(i*widthSize, j*widthSize, widthSize, widthSize);
                }
            }

            g.setColor(Color.BLACK);
            for(int i = 0; i <= numOfVertices; i++){
                g.drawLine(i*widthSize, 0, i*widthSize, this.getHeight());
            }
            for(int j = 0; j <= numOfVertices; j++){
                g.drawLine(0, j*widthSize, this.getWidth(), j*widthSize);
            }

            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            g.setColor(Color.BLACK);
            if(path != null){
                Point old = null;
                if(this.pd == PathDisplay.Blocks){
                    for(int i = 1; i < this.path.size()-1; i++){
                        Point p = this.path.get(i).getLocation();
                        g.fillRect(p.x*widthSize, p.y*widthSize, widthSize, widthSize);
                    }
                } else {
                    for(Graph.Vertex v: this.path){
                        Point p = v.getLocation();
                        if(old != null){
                            g.drawLine((old.x+1)*widthSize - (widthSize/2), (old.y+1)*widthSize - (widthSize/2), (p.x+1)*widthSize - (widthSize/2), (p.y+1)*widthSize - (widthSize/2));
                        }
                        old = p;
                    }
                }
            }
        } else {
            this.setBackground(Color.black);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println(e.getX() + " " + e.getY());
        if(SwingUtilities.isLeftMouseButton(e)){
            int dem = this.getWidth()/this.graph.width();
            int x = e.getX()/dem;
            int y = e.getY()/dem;
            Graph.Vertex v = this.graph.get(x,y);
            if(!v.isStart() && !v.isGoal()){
                v.setType(Graph.Vertex.VertexType.Block);
            }
        } else if(SwingUtilities.isRightMouseButton(e)){
            int dem = this.getWidth()/this.graph.width();
            int x = e.getX()/dem;
            int y = e.getY()/dem;
            Graph.Vertex v = this.graph.get(x,y);
            if(!v.isStart() && !v.isGoal()){
                v.setType(Graph.Vertex.VertexType.Empty);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(SwingUtilities.isLeftMouseButton(e)){
            int ex = e.getX();
            int ey = e.getY();
//            System.out.println(ex + " " + ey);
            if(ex > 0 && ex < this.getWidth()-1 && ey > 0 && ey < this.getHeight()-1){
                int dem = this.getWidth()/this.graph.width();
//                System.out.println(this.getWidth() + " " + this.graph.width());
                int x = e.getX()/dem;
                int y = e.getY()/dem;
//                System.out.println(x + " " + y + " " + dem);
                Point p = new Point(x, y);
                if(this.goal != null && this.goal.getLocation().equals(p)){
                    this.goalPressed = true;
                } else if(this.start != null && this.start.getLocation().equals(p)){
                    this.startPressed = true;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        this.startPressed = false;
        this.goalPressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if(SwingUtilities.isLeftMouseButton(e)){
            int ex = e.getX();
            int ey = e.getY();
            if(ex > 0 && ex < this.getWidth()-1 && ey > 0 && ey < this.getHeight()-1){
                int dem = this.getWidth()/this.graph.width();
                int x = ex/dem;
                int y = ey/dem;
                Graph.Vertex v = this.graph.get(x,y);
                if(this.startPressed){
                    if(!v.isGoal() && !v.isStart()){
                        this.start.setStart(false);
                        this.start = v;
                        this.start.setType(Graph.Vertex.VertexType.Empty);
                        this.start.setStart(true);
                    }
                } else if(this.goalPressed){
                    if(!v.isGoal() && !v.isStart()){
                        this.goal.setGoal(false);
                        this.goal = this.graph.get(x, y);
                        this.goal.setType(Graph.Vertex.VertexType.Empty);
                        this.goal.setGoal(true);
                    }
                } else {
                    if(!v.isStart() && !v.isGoal() && !v.isVisited() && !v.isInFringe()){
                        v.setType(Graph.Vertex.VertexType.Block);
                    }
                }
            }
        } else if(SwingUtilities.isRightMouseButton(e)){
            int ex = e.getX();
            int ey = e.getY();
            if(ex > 0 && ex < this.getWidth()-1 && ey > 0 && ey < this.getHeight()-1){
                int dem = this.getWidth()/this.graph.width();
                int x = ex/dem;
                int y = ey/dem;
                Graph.Vertex v = this.graph.get(x,y);
                if(!v.isStart() && !v.isGoal()){
                    v.setType(Graph.Vertex.VertexType.Empty);
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public static enum PathDisplay{
        Blocks, Line;
    }
}
