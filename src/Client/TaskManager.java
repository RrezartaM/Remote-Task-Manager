package Client;


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class TaskManager
        implements ItemListener {

    ArrayList<JCheckBox> apps = new ArrayList<>();
    ArrayList<String> appNames = new ArrayList<>();
    ArrayList<String> selectedApps = new ArrayList<>();
    private static final String KILL = "taskkill /F /IM ";// kill -9

    public String killTmMessage = "KILLtm";

    String clientName;

    JFrame frame;

    public TaskManager(final String clientName, ArrayList<String> names) {

        appNames = names;
        this.clientName = clientName;
        JButton endTastButton = new JButton("Kill process");

        JList list = new JList();
        DefaultListModel listModel = new DefaultListModel();

        JPanel checkPanel = new JPanel(new GridLayout(0, 4, 3, 3));
        JPanel main = new JPanel();

        for (int i = 0; i < appNames.size(); i++) {

            listModel.addElement(appNames.get(i));
            apps.add(i, new JCheckBox((String) listModel.get(i)));
            apps.get(i).addItemListener(this);
            checkPanel.add(apps.get(i));
        }

        list.setModel(listModel);

        main.setLayout(new BorderLayout(5, 5));
        checkPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        checkPanel.setOpaque(true); 

        main.add(new JScrollPane(checkPanel), BorderLayout.CENTER);
        main.add(endTastButton, BorderLayout.SOUTH);

        frame = new JFrame("Task Manager");

        frame.add(main, BorderLayout.CENTER);
        frame.setContentPane(main);
        frame.pack();
        frame.setVisible(true);

        endTastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                StringBuilder str = new StringBuilder();

                if(selectedApps.size() > 0) {

                    for (int i = 0; i < selectedApps.size(); i++) {
                        str.append(selectedApps.get(i)).append(",");
                    }
                    str.append(selectedApps.size()).append(",");

                    ClientUI.sendFromTm(killTmMessage + clientName + "^" + str.toString());
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
                else
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

                

            }
        });

    }

    public static void killProcess(String serviceName) {

        try {
            Runtime.getRuntime().exec(KILL + serviceName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Listens to the check boxes.
     */
    public void itemStateChanged(ItemEvent e) {

        Object source = e.getItemSelectable();
        String a = source.toString();
        int length = a.length();
        int last = a.lastIndexOf("text");
        String name = a.substring(last + 5, length - 1);

        if (e.getStateChange() == ItemEvent.SELECTED) {

            if (!selectedApps.contains(name))
                selectedApps.add(name);
        }

        if (e.getStateChange() == ItemEvent.DESELECTED) {

            selectedApps.remove(name);
        }

    }

 
}