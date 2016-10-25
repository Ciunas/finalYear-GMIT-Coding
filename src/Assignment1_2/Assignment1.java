package Assignment1_2;
/**
 * Created by ciunas on 04/10/16.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import java.io.File;

public class Assignment1 extends JFrame  {

    JButton readButton;
    JTextArea readOutput;
    JLabel message;
    JLabel publishRuns;
    BufferedReader br = null;
    JPanel dp;
    JPanel dp1;
    int iterations;

    public static void main(String[] args) {
        new Assignment1().setVisible(true);
    }

    public Assignment1() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 700);

        dp = new JPanel();
        dp.setLayout(new GridLayout(2, 1));
        add(dp, BorderLayout.NORTH);
        dp1 = new JPanel();
        dp1.setLayout(new GridLayout());
        dp.add(dp1);

        //create text areas
        readOutput = new JTextArea();
        message =  new JLabel("Opensource Book Reader");
        publishRuns = new JLabel("Iterations of publish: " + iterations);

        //Create buttons
        readButton = new JButton("Press to choose the book you want to read");
        readButton.setSize(new Dimension(500, 100));
        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                readButton.setEnabled(false);
                processBook();
            }
        });

        dp1.add(message, BorderLayout.WEST);
        dp1.add(publishRuns, BorderLayout.EAST);
        dp.add(readButton, BorderLayout.SOUTH);
        add(new JScrollPane(readOutput));
    }

    public void processBook() {

        (new BookWorker()).execute();
    }

    class BookWorker extends SwingWorker<Void, String> {

        @Override
        protected void process(List <String> list) {
           // super.process(list);
            iterations ++;
            publishRuns.setText("Iterations of process: " + iterations );
            for(String model : list) {
                readOutput.append(model);
            }
        }

        @Override
        public Void doInBackground() {
            StringBuffer sb = new StringBuffer();
            File selectedFile = null;
            iterations = 0;
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
            }

            try {BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                String line;
                int temp;
                line = br.readLine();
                while (line  != null  ) {
                    sb.append(line + "\n");
                    if( (temp = sb.length()) >= 10000 ){
                        publish(sb.toString());
                        sb.delete(0, sb.length());
                    }
                    line = br.readLine();
                    if(line == null){
                        sb.append(line + "\n");
                        publish(sb.toString());
                        sb.delete(0, sb.length());
                    }

                }
                System.out.println("Finished");
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            readButton.setEnabled(true);
            return null;
        }
    }

}
