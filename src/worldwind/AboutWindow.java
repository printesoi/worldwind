package worldwind;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class AboutWindow extends JDialog implements ActionListener {

    LicenceWindow licenceDialog;

    public AboutWindow(JFrame parent, Application instance) {
        super(parent, "About window", true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Object [] options = { "Licence", "Close"};
        setLayout(new BorderLayout(10, 10));

        StringBuffer aboutApp = new StringBuffer("<html>");
        aboutApp.append("Simple World Wind Application<br>");
        aboutApp.append("Author:" + instance.getAuthor() + "<br>");
        aboutApp.append("Version:" + instance.getVersion() + "<br>");
        aboutApp.append("Year: " + instance.getYear() + "<br>");
        aboutApp.append("Licence: " + instance.getLicence());
        JLabel label = new JLabel(aboutApp.toString(), JLabel.RIGHT);
        label.setVerticalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);

        JButton licenceButton = new JButton(options[0].toString());
        JButton closeButton = new JButton(options[1].toString());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(licenceButton);
        panel.add(closeButton);
        add(panel, BorderLayout.SOUTH);
        licenceButton.addActionListener(this);
        closeButton.addActionListener(this);

        licenceDialog = new LicenceWindow(this);
        licenceDialog.setVisible(false);

        pack();
        setResizable(false);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent actionEvent) {
        String src = actionEvent.getActionCommand();
        if (src == "Licence") {
            licenceDialog.setVisible(true);
        }
        if (src == "Close") {
            setVisible(false);
        }
    }

    class LicenceWindow extends JDialog {
        public LicenceWindow(JDialog parent) {
            super(parent, "Licence", true);
            setSize(300, 300);
            setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            StringBuffer licenceText = new StringBuffer("<html>");
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("licence.txt"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    licenceText.append(line + "<br>");

                }
                licenceText.append("</html>");
            } catch (IOException e) {
                System.out.println("Could not print licence!");
            }
            JScrollPane pane = new JScrollPane(new JLabel(licenceText.toString()));
            add(pane);
            setSize(600, 300);
        }
    }
}