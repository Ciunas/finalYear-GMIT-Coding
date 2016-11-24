//package Assignment_6;
//
//import javax.swing.*;
//import java.awt.*;
//
///**
// * Created by ciunas on 18/11/16.
// */
//public class ExtraDetailsEntry  extends JInternalFrame implements Runnable{
//
//    private static int xOffset = 0, yOffset = 0;
//
//
//    @Override
//    public void run() {
//        Container container = getContentPane();
//        System.out.println("running thread");
//        JPanel jp = new JPanel();
//        jp.setLayout(new GridLayout(9, 1, 0, 5));
//        JPanel jp1 = new JPanel();
//        jp1.setLayout(new GridLayout(9, 1, 0, 5));
//        container.add(jp, BorderLayout.NORTH);
//        container.add(jp1, BorderLayout.SOUTH);
//
//        container.setBounds(500, 500, 550, 500);
//        System.out.println("Thread");
//        //desktop.add( entryFrame );
//        container.setVisible( true );
//    }
//}
