import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;


public class GraphTraversalPanel extends JPanel implements Runnable, MouseMotionListener, MouseListener {

    private static final long serialVersionUID = 6382904795938277521L;

    private GraphTraversalVisualPanel vp;
    private Graph graph;
    private GraphTraversal gt;
    public static final int FRAMES_PER_SECOND = 24;
    private static final long FRAME_TIME = 1000L / FRAMES_PER_SECOND;

    private Heuristic heuristics;
    private boolean stopFlag = false;
    private JButton aboutButton;
    private JButton startButton;
    private JButton clearButton;
    private JComboBox<GraphTraversal.TraversalType> comboBox;
    private JSpinner spinner;
    private JButton clearTraversal;
    private JComboBox<Integer> graphSizeBox;
    private final JComboBox<GraphTraversalVisualPanel.PathDisplay> pathDisplayBox;

    /**
     * Create the panel.
     */
    public GraphTraversalPanel() {
        this.graph = new Graph();
        this.graph.resetToSize(20);
        setLayout(null);
        this.setSize(1200, 800);
        this.setPreferredSize(getSize());
        this.vp = new GraphTraversalVisualPanel(this.graph);
        this.vp.setSize(761, 761);
        this.vp.setLocation(10, 10);
        this.vp.setVisible(true);
        this.add(this.vp);
        this.gt = null;
        this.heuristics = new Heuristic() {
            public double estimate(Graph.Vertex from, Graph.Vertex to) {
                return from.distanceTo(to);
            }
        };
        Font font1 = new Font("Serif", Font.ITALIC, 30);
        Font font2 = new Font("Serif", Font.ITALIC, 20);
        this.addMouseListener(this);

        // Title
        JLabel lblGraphTraversalAlgorithm1 = new JLabel("Graph");
        lblGraphTraversalAlgorithm1.setFont(font1);
        lblGraphTraversalAlgorithm1.setBounds(950, 30, 200, 50);
        add(lblGraphTraversalAlgorithm1);
        JLabel lblGraphTraversalAlgorithm2 = new JLabel("Traversal Algorithms");
        lblGraphTraversalAlgorithm2.setFont(font1);
        lblGraphTraversalAlgorithm2.setBounds(874, 70, 300, 50);
        add(lblGraphTraversalAlgorithm2);

        // Traversal Type
        JLabel lbTraversalType = new JLabel("Traversal Type: ");
        lbTraversalType.setFont(font2);
        lbTraversalType.setBounds(815, 135, 140, 30);
        add(lbTraversalType);

        this.comboBox = new JComboBox<GraphTraversal.TraversalType>();
        comboBox.setModel(new DefaultComboBoxModel<GraphTraversal.TraversalType>(GraphTraversal.TraversalType.values()));
        comboBox.setBounds(957, 143, 89, 20);
        add(comboBox);

        // wait time
        JLabel lblWaitMilliseconds = new JLabel("Wait(ms): ");
        lblWaitMilliseconds.setFont(font2);
        lblWaitMilliseconds.setBounds(815, 180, 140, 30);
        add(lblWaitMilliseconds);

        this.spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(10, 0, 100, 1));
        spinner.setBounds(957, 188, 89, 20);
        add(spinner);

        // Graph size
        JLabel lblGraphSizeWidth = new JLabel("Graph Size:");
        lblGraphSizeWidth.setFont(font2);
        lblGraphSizeWidth.setBounds(815, 225, 140, 30);
        add(lblGraphSizeWidth);
        this.graphSizeBox = new JComboBox<Integer>();
        graphSizeBox.setModel(new DefaultComboBoxModel<Integer>(getSizes()));
        graphSizeBox.setSelectedIndex(8);
        graphSizeBox.setBounds(957, 233, 89, 20);
        add(graphSizeBox);

        graphSizeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg) {
                if (gt != null) {
                    gt.stop();
                }
                graph.resetToSize(graphSizeBox.getItemAt(graphSizeBox.getSelectedIndex()));
                vp.setGraph(graph);
            }
        });

        // Path display
        JLabel lblNewLabel = new JLabel("Path Display:");
        lblNewLabel.setFont(font2);
        lblNewLabel.setBounds(815, 270, 140, 30);
        add(lblNewLabel);

        this.pathDisplayBox = new JComboBox<GraphTraversalVisualPanel.PathDisplay>();
        pathDisplayBox.setModel(new DefaultComboBoxModel<GraphTraversalVisualPanel.PathDisplay>(GraphTraversalVisualPanel.PathDisplay.values()));
        pathDisplayBox.setBounds(957, 278, 89, 20);
        add(pathDisplayBox);

        pathDisplayBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg) {
                vp.setPathDisplay(pathDisplayBox.getItemAt(pathDisplayBox.getSelectedIndex()));
            }
        });

        // Start button
        this.startButton = new JButton("Start");
        startButton.setBounds(815, 325, 150, 35);
        startButton.setFont(font2);
        add(startButton);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (gt != null) {
                    gt.stop();
                }
                gt = null;
                graph.clearStates();
                vp.setPath(null);
                gt = new GraphTraversal(graph, comboBox.getItemAt(comboBox.getSelectedIndex()), vp.getStart(), vp.getGoal(), (Integer) spinner.getValue(), heuristics, vp);
                Thread t = new Thread(gt);
                t.start();
            }
        });

        // Clear Screen Button
        this.clearButton = new JButton("Clear Screen");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (gt != null) {
                    gt.stop();
                }
                graph.clear();
                vp.setPath(null);
            }
        });
        clearButton.setBounds(1015, 325, 150, 35);
        clearButton.setFont(font2);
        add(clearButton);

        // Clear Button
        this.clearTraversal = new JButton("Clear");
        clearTraversal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gt != null) {
                    gt.stop();
                }
                graph.clearStates();
                vp.setPath(null);
            }
        });
        clearTraversal.setBounds(915, 385, 150, 35);
        clearTraversal.setFont(font2);
        add(clearTraversal);

        // About Button
        this.aboutButton = new JButton("About");
        aboutButton.setFont(font2);
        aboutButton.setBounds(815, 500, 150, 35);
        add(aboutButton);

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Giang Nguyen", "Author", JOptionPane.OK_OPTION);
            }
        });

    }

    @Override
    public void run() {
        while (true) {
            long start = System.currentTimeMillis();

            this.vp.repaint();

            long delta = (System.currentTimeMillis() - start);
            if (delta < FRAME_TIME) {
                try {
                    Thread.sleep(FRAME_TIME - delta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Integer[] getSizes() {
        double width = this.vp.getWidth() - 1;
        LinkedList<Integer> result = new LinkedList<Integer>();
        for (int i = 1; i < width; i++) {
            double n = (int) width / i;
            if ((width / i) == n) {
                result.addFirst((int) n);
            }
        }
        result.removeLast();
        Integer[] r = new Integer[result.size()];
        result.toArray(r);
        return r;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getX() + " " + e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println(e.getX() + " " + e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        System.out.println(e.getX() + " " + e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private static enum HeuristicTypes {
        Distance;
    }
}
