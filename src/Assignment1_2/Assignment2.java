package Assignment1_2;

/**
 * Created by ciunas on 06/10/16.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Assignment2 extends JFrame {

    List<String> mylistStr = new ArrayList<String>();
    List<String> mylistRevStr = new ArrayList<String>();
    List<String> myCountlistStr = new ArrayList<String>();
    List<AtomicInteger> myCountlistNum = new ArrayList<AtomicInteger>();
    JButton readButton;
    JButton reverseButton;
    JButton reversePairsButton;
    JButton countAndDispayButton;
    JButton clearButton;
    JTextArea printOutput;
    JTextArea reverseOutput;
    JTextArea reversePairs;
    JTextArea countAndDisplay;

    JLabel message;
    JLabel publishRuns;
    BufferedReader br = null;
    JPanel panel;
    JPanel panel2;
    JPanel displayPanel;
    JPanel displayPanel1;
    JPanel displayPanel2;
    JPanel displayPanel3;
    JPanel displayPanel4;
    String manipulation;
    int iterations;

    public static void main(String[] args) {

        new Assignment2().setVisible(true);
    }

    public Assignment2() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());                     //1 = row 4 =column
        setSize(1000, 700);

        panel = new JPanel(new GridLayout(1, 4));
        panel2 = new JPanel(new GridLayout(1, 2));
        add(panel2, BorderLayout.NORTH);
        add(panel);
        displayPanel4 = new JPanel();
        displayPanel4.setLayout(new GridLayout(1, 2));
        panel2.add(displayPanel4);
        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        panel.add(displayPanel);
        displayPanel1 = new JPanel();
        displayPanel1.setLayout(new BorderLayout());
        panel.add(displayPanel1);
        displayPanel2 = new JPanel();
        displayPanel2.setLayout(new BorderLayout());
        panel.add(displayPanel2);
        displayPanel3 = new JPanel();
        displayPanel3.setLayout(new BorderLayout());
        panel.add(displayPanel3);

        //create text areas
        printOutput = new JTextArea();
        reverseOutput = new JTextArea();
        reversePairs = new JTextArea();
        countAndDisplay = new JTextArea();
        publishRuns = new JLabel("              " +
                "                       Iterations of publish: " + iterations);

        //Create buttons
        readButton = new JButton("Choose your Book.");
        readButton.setSize(new Dimension(880, 100));
        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                readButton.setEnabled(false);
                processBook();
            }
        });

        reverseButton = new JButton("Press to reverse.");
        reverseButton.setSize(new Dimension(500, 100));
        reverseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                reverseButton.setEnabled(false);
                processBookLineReverse();
            }
        });

        reversePairsButton = new JButton("Reverse every second Word.");
        reversePairsButton.setSize(new Dimension(500, 100));
        reversePairsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                reversePairsButton.setEnabled(false);
                processWordReverse();
            }
        });

        countAndDispayButton = new JButton("Count instances of words.");
        countAndDispayButton.setSize(new Dimension(500, 100));
        countAndDispayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                countAndDispayButton.setEnabled(false);
                processWordCount();
            }
        });

        clearButton = new JButton("Clear Data.");
        clearButton.setSize(new Dimension(500, 100));
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearButton.setEnabled(false);
                printOutput.setText("");
                reverseOutput.setText("");
                reversePairs.setText("");
                countAndDisplay.setText("");

                mylistStr.clear();
                mylistRevStr.clear();
                myCountlistStr.clear();
                myCountlistNum.clear();
                clearButton.setEnabled(true);
            }
        });

        displayPanel4.add(clearButton);
        displayPanel4.add(publishRuns);
        displayPanel.add(readButton, BorderLayout.NORTH);
        displayPanel.add(printOutput, BorderLayout.SOUTH);
        displayPanel.add(new JScrollPane(printOutput));
        displayPanel1.add(reverseButton, BorderLayout.NORTH);
        displayPanel1.add(reverseOutput, BorderLayout.SOUTH);
        displayPanel1.add(new JScrollPane(reverseOutput));
        displayPanel2.add(reversePairsButton, BorderLayout.NORTH);
        displayPanel2.add(reversePairs, BorderLayout.SOUTH);
        displayPanel2.add(new JScrollPane(reversePairs));
        displayPanel3.add(countAndDispayButton, BorderLayout.NORTH);
        displayPanel3.add(countAndDisplay, BorderLayout.SOUTH);
        displayPanel3.add(new JScrollPane(countAndDisplay));

    }

    public void processBook() {
        (new BookWorker()).execute();
    }


    //Method for reading a text file and printing to a JtextArea
    class BookWorker extends SwingWorker<Void, String> {
        @Override
        protected void process(List<String> list) {
            super.process(list);
            iterations++;
            publishRuns.setText("                                     Iterations of process: " + iterations);

            for (String model : list) {
                printOutput.append(model);
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

            try {

                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                String line;
                int temp;
                line = br.readLine();

                while (line != null) {
                    sb.append(line + "\n");
                    if ((temp = sb.length()) >= 200) {
                        publish(sb.toString());
                        sb.delete(0, sb.length());

                    }

                    mylistStr.add(line);                                                //add string to List
                    line = br.readLine();
                    if (line == null) {
                        manipulation = "readBook";
                        publish(sb.toString());
                        sb.delete(0, sb.length());
                    }
                }
                System.out.println("Finished printing Book.");
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            readButton.setEnabled(true);

            return null;
        }

    }

    public void processBookLineReverse() {
        (new BookWorkerReverse()).execute();
    }


    //Method for reversing a line of text
    class BookWorkerReverse extends SwingWorker<Void, String> {
        @Override
        protected void process(List<String> list) {
            super.process(list);
            iterations++;
            publishRuns.setText("                                     Iterations of process: " + iterations);

            for (String model : list) {
                reverseOutput.append(model);
            }
        }

        @Override
        public Void doInBackground() {

            StringBuffer sb = new StringBuffer();
            File selectedFile = null;
            iterations = 0;
            int temp = 0;
            StringBuffer sb2 = new StringBuffer();

            for (String obj : mylistStr) {                                   //Print List
                String reverse = new StringBuffer(obj).reverse().toString();
                sb2.append(reverse + "\n");
                mylistRevStr.add(reverse);

                if ((temp = sb2.length()) >= 200) {
                    publish(sb2.toString());
                    sb2.delete(0, sb2.length());
                    temp = 0;
                }
            }

            publish(sb2.toString());
            manipulation = "reverseBook";
            reverseButton.setEnabled(true);
            System.out.println("Finished Reversing all text.");

            return null;
        }

    }


    public void processWordReverse() {
        (new BookWorkerWordReverse()).execute();
    }


    // Method for reversing every  two words.
    class BookWorkerWordReverse extends SwingWorker<Void, String> {
        @Override
        protected void process(List<String> list) {
            // super.process(list);
            iterations++;
            publishRuns.setText("                                     Iterations of process: " + iterations);

            for (String model : list) {

                reversePairs.append(model);
            }
        }

        @Override
        public Void doInBackground() {

            StringBuffer sb = new StringBuffer();
            File selectedFile = null;
            iterations = 0;
            int temp = 0;
            StringBuffer sb1 = new StringBuffer();

            for (String obj : mylistRevStr) {                                             //Print List

                String[] tokens = obj.split("[ ]{1,}");

                if ((tokens.length % 2) == 1) {
                    for (int i = 0; i < (tokens.length - 1); i += 2) {

                        sb1.append(tokens[i + 1] + " ");
                        sb1.append(tokens[i] + " ");
                    }

                    sb1.append(tokens[tokens.length - 1] + " ");

                } else {
                    for (int i = 0; i < tokens.length; i += 2) {

                        sb1.append(tokens[i + 1] + " ");
                        sb1.append(tokens[i] + " ");

                    }
                }
                sb1.append("\n");

                if ((temp = sb1.length()) >= 200) {

                    manipulation = "reverseWord";
                    publish(sb1.toString());
                    sb1.delete(0, sb1.length());
                    temp = 0;
                }

            }

            manipulation = "reverseWord";
            publish(sb1.toString());
            reversePairsButton.setEnabled(true);
            System.out.println("Finished Reversing two words.");

            return null;
        }

    }

    public void processWordCount() {
        (new BookWorkerCountWords()).execute();
    }


    //Swingworker for counting the number of times a word shows up.
    class BookWorkerCountWords extends SwingWorker<Void, String> {
        @Override
        protected void process(List<String> list) {
            super.process(list);
            iterations++;
            publishRuns.setText("                                     Iterations of process: " + iterations);

            for (String model : list) {
                countAndDisplay.append(model);
            }
        }

        @Override
        public Void doInBackground() {
            StringBuffer sb = new StringBuffer();
            File selectedFile = null;
            iterations = 0;


            for (String obj : mylistStr) {                      //Print List

                String temp4 = obj.replaceAll("/", " ");
                String[] tokens = temp4.split("[ ]{1,}");                     //Tokenise
                int i = 0;

                while (i < tokens.length) {

                    int temp;
                    String temp3 = tokens[i].toLowerCase();                                            //change all words to lower case.
                    String temp2 = temp3.replaceAll("[^\\p{L}\\p{Z}]", "");

                    if ((temp = myCountlistStr.indexOf(temp2)) == -1) {

                        myCountlistStr.add(temp2);
                        AtomicInteger atomicInteger = new AtomicInteger(1);
                        myCountlistNum.add(atomicInteger);

                    } else {
                        myCountlistNum.get(temp).incrementAndGet();
                    }
                    i++;
                }

            }

            int j = 0;
            countAndDisplay.setText("");
            for (String result : myCountlistStr) {
                publish(result + "  " + myCountlistNum.get(j) + "\n");
                j++;

            }
            System.out.println("Finished Counting the numbers.");
            countAndDispayButton.setEnabled(true);

            return null;
        }

    }

}

