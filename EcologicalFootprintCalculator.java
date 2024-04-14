// Social Theme : Ecological Footprint Calculator
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class EcologicalFootprintCalculator extends JFrame {
    private JSlider enterlocal;
    private JSlider enterwater;
    private JSlider enterTrash;
    private JButton calculateFootprint;

    public EcologicalFootprintCalculator() {
        super("Ecological Footprint Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        // Initialize sliders
        enterlocal = initSlider(0, 70, 10);
        enterwater = initSlider(0, 70, 10);
        enterTrash = initSlider(0, 70, 10);

        calculateFootprint = new JButton("Calculate Footprint");

        // Add components
        add(new JLabel("How much do you spend on local foods each week? Once (10), Daily (70)"));
        add(enterlocal);
        add(new JLabel("How much water do you reuse each week? Once (10), Daily (70)"));
        add(enterwater);
        add(new JLabel("How often do you use air conditioning (AC) each week? Once (10), Daily (70)"));
        add(enterTrash);
        add(calculateFootprint);

        // Listeners
        addListeners();

        pack();
        setVisible(true);
    }

    private JSlider initSlider(int min, int max, int majorTickSpacing) {
        JSlider slider = new JSlider(min, max);
        slider.setMajorTickSpacing(majorTickSpacing);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void addListeners() {
        calculateFootprint.addActionListener(e -> {
            int local = enterlocal.getValue();
            int water = enterwater.getValue();
            int trash = enterTrash.getValue();
            double footprint = (local + water + trash) / 3.0;
    
            String message = calculateMessage(footprint);
            JOptionPane.showMessageDialog(this, "Your ecological footprint is: " + footprint + " units.\n" + message);
    
            showImprovementSteps(); 
            promptMultipleChoiceQuestions(); // First, show the improvement steps.
            promptUserReflection();  // Then, prompt for reflection.
                                      // Finally, prompt the multiple choice questions.
        });
    }    
    

    private String calculateMessage(double footprint) {
        if (footprint <= 30) {
            return "Your footprint is low, keep it up!";
        } else if (footprint <= 50) {
            return "Your footprint is average, consider improving!";
        } else {
            return "Your footprint is high, consider taking immediate actions!";
        }
    }

    private void showImprovementSteps() {
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText("<html><body>" +
            "<h2>Here are some steps to improve your footprint:</h2>" +
            "<ul>" +
            "<li><b>Support local farmers and sustainable food systems.</b></li>" +
            "<li><b>Reuse water. Ex: Reuse Pasta Water to water the plant outside</b></li>" +
            "<li><b>Use a fan to reduce HC use.</b></li>" +
            "</ul>" +
            "</body></html>");
        textPane.setEditable(false);
    
        JDialog infoDialog = new JDialog(this, "Footprint Improvement Steps", true); // Make it modal
        infoDialog.getContentPane().setLayout(new BorderLayout());
        infoDialog.getContentPane().add(new JScrollPane(textPane), BorderLayout.CENTER);
    
        JButton btnNext = new JButton("Next");
        btnNext.addActionListener(e -> {
            infoDialog.dispose(); // Close the current window
            promptMultipleChoiceQuestions(); // Move to the multiple choice questions
        });
        infoDialog.getContentPane().add(btnNext, BorderLayout.SOUTH);
    
        infoDialog.setSize(500, 300);
        infoDialog.setLocationRelativeTo(this); // Center relative to main frame
        infoDialog.setVisible(true);
    }
    
    private void promptMultipleChoiceQuestions() {
        String[] options = {"Reduce, Reuse, Recycle", "Support local farmers", "Both"};
        int response = JOptionPane.showOptionDialog(null, 
            "What are the steps to reduce your ecological footprint?", 
            "Multiple Choice Question",
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE,
            null, 
            options, 
            options[0]);
    
        // Handle the user's response
        handleMultipleChoiceResponse(response);
    
        // After the multiple choice questions, prompt the user reflection
        promptUserReflection();
    }
    
    private void handleMultipleChoiceResponse(int response) {
        switch (response) {
            case 0:
                JOptionPane.showMessageDialog(this, "Good start, but there's more you can do!");
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "That's a great step, but don't forget about reducing, reusing, and recycling!");
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "Excellent! You're on the right track!");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Please make a selection.");
                break;
        }
    }

    private void promptUserReflection() {
        JDialog reflectionDialog = new JDialog(this, "Reflection", true); // Make it modal
        reflectionDialog.setLayout(new BorderLayout());
    
        JLabel label = new JLabel("What are you going to do right now to reduce your footprint?");
        reflectionDialog.add(label, BorderLayout.NORTH);
    
        JTextArea textArea = new JTextArea();
        reflectionDialog.add(new JScrollPane(textArea), BorderLayout.CENTER);
    
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e -> {
            // Handle the submission of the reflection
            String reflection = textArea.getText();
            handleReflectionSubmission(reflection);
            reflectionDialog.dispose(); // Close after submission
            System.exit(0); // Exit the program after submission for simplicity
        });
        reflectionDialog.add(btnSubmit, BorderLayout.SOUTH);
    
        reflectionDialog.setSize(500, 300);
        reflectionDialog.setLocationRelativeTo(this); // Center relative to main frame
        reflectionDialog.setVisible(true);
    }



    private void handleReflectionSubmission(String reflection) {
    try {
        BufferedWriter writer = new BufferedWriter(new FileWriter("reflection.txt"));
        writer.write(reflection);
        writer.close();
        JOptionPane.showMessageDialog(this, "Your reflection has been saved to reflection.txt");
    } catch (IOException e) {
        e.printStackTrace();
        StringSelection stringSelection = new StringSelection(reflection);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        JOptionPane.showMessageDialog(this, "Your reflection has been copied to clipboard");
    }
}

    public static void main(String[] args) {
        new EcologicalFootprintCalculator();
    }
}
