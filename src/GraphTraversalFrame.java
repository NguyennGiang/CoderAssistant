
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class GraphTraversalFrame extends JFrame {

    private static final long serialVersionUID = 536632083968634236L;
    private GraphTraversalPanel contentPane;

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

    public GraphTraversalFrame() {
        setResizable(false);
        setTitle("Graph Traversal Algorithms");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10, 10, 450, 500);
        ImageIcon logo = new ImageIcon("D:\\GraphTraversalAlgorithms\\GraphMiniProject\\images\\Logo_Hust.png");
        this.setIconImage(logo.getImage());

        contentPane = new GraphTraversalPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        this.pack();
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
    }

    public void start(){
        Thread t = new Thread(this.contentPane);
        t.start();
    }

}
