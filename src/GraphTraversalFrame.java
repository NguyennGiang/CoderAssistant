/**
 * @author Maxx Boehme
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;


public class GraphTraversalFrame extends JFrame {

    private static final long serialVersionUID = 536632083968634236L;
    private GraphTraversalPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GraphTraversalFrame frame = new GraphTraversalFrame();
                    frame.setVisible(true);
                    frame.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public GraphTraversalFrame() {
        setResizable(false);
        setTitle("Graph Traversal Algorithms");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new GraphTraversalPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Creates the thread for the GraphTraversalPanel
     */
    public void start(){
        Thread t = new Thread(this.contentPane);
        t.start();
    }

}
