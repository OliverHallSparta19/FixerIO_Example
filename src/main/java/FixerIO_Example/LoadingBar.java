package FixerIO_Example;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class LoadingBar extends JFrame {

    // create a frame
    static JFrame f;

    static JProgressBar b;

    static JPanel p;

    public static void main(String[] args)
    {

        // create a frame
        f = new JFrame("ProgressBar demo");

        // create a panel
        p = new JPanel();

        // create a progressbar
        b = new JProgressBar();

        // set initial value
        b.setValue(0);

        b.setStringPainted(true);

        // add progressbar
        p.add(b);

        // add panel
        f.add(p);

        // set the size of the frame
        f.setSize(500, 50);
        f.setVisible(true);

        fill();
    }

    // function to increase progress
    public static void fill()
    {
        int i = 0;
        try {
            while (i <= 100) {
                // fill the menu bar
                b.setValue(i);

                // delay the thread
                Thread.sleep(100);
                i += 1;
            }
            b.setString("DONE DONE DONE");
//            Thread.sleep(1000);
//            f.setSize(50, 500);
//            Thread.sleep(1000);
//            f.setSize(50, 50);
//            Thread.sleep(1000);
//            f.setSize(500, 500);
//            Thread.sleep(1000);
//            f.getGraphics();
//            Thread.sleep(1000);
//            JMenuBar jmdf = new JMenuBar();
//            f.setJMenuBar(jmdf);
//            Thread.sleep(1000);
//            f.repaint(1,1,23,23);
//            Thread.sleep(1000);
//            p.getInputContext();
//            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            f.setUndecorated(true);
//            f.setUndecorated(false);
//            f.setVisible(false);
            Thread.sleep(1000);



//            f.remove(setDefaultLookAndFeelDecorated(););

            System.exit(1);
        }
        catch (Exception e) {
        }
    }
}