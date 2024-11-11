package GUI;

import GUI.Frame;

import java.awt.*;
import javax.swing.*;

public class Window extends JFrame{
    GUI.Frame frame;
    public Window(){
        frame = new Frame();
        this.add(frame);
        ImageIcon img = new ImageIcon("./rsrc/book.png");
        this.setIconImage(img.getImage());
        this.setTitle("eLearning");
        this.setBackground(Color.pink);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(true);
    }
}
