package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;


public class Connect {

    JFrame frame;

    boolean asAdm;

    public Connect() {

        frame = new JFrame("Connect as:");
        JPanel main = new JPanel();
        JPanel center = new JPanel();
        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        JPanel radioButtons = new JPanel();
        final JButton connect = new JButton("Connect");
        bottom.setSize(20, 20);

       
        center.setLayout(new BorderLayout(5, 5));
        final JPasswordField password = new JPasswordField();
        password.setEnabled(false);

        JLabel connnctAs = new JLabel("Connect as:");
        main.setLayout(new BorderLayout(5, 5));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        center.setBorder(new EmptyBorder(0, 30, 0, 30));

        JRadioButton client = new JRadioButton("Client");
        JRadioButton administrator = new JRadioButton("Administrator");
       
        
         client.setBounds(75,50,100,30);    
         administrator.setBounds(75,100,100,30);
        radioButtons.setLayout(new FlowLayout());
        ButtonGroup bG = new ButtonGroup();
        bG.add(client);
       bG.add(administrator);
        radioButtons.add(client);
        radioButtons.add(administrator);

        top.add(connnctAs, BorderLayout.CENTER);
        main.add(top, BorderLayout.NORTH);
        center.add(radioButtons, BorderLayout.NORTH);
        center.add(password, BorderLayout.SOUTH);
        main.add(center, BorderLayout.CENTER);
        bottom.add(connect, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        frame.setContentPane(main);
        frame.setSize(300, 250);
        frame.setVisible(true);

        administrator.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {

                password.setEnabled(true);
                connect.setEnabled(true);
                asAdm = true;
            }
        });

        client.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                password.setEnabled(false);
                connect.setEnabled(true);
                asAdm = false;
            }
        });

        connect.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {

               if (asAdm) {

                    String passwordTxt = String.valueOf(password.getPassword());

                    if (passwordTxt.isEmpty()) {

                        JOptionPane.showMessageDialog(frame, "You have to type a password \n " +
                                "to connect as administrator!");
                    } else if (!passwordTxt.equals("0000")) {
                        JOptionPane.showMessageDialog(frame, "Password is incorrect", "Wrong password", JOptionPane.ERROR_MESSAGE);
                    }

                    if(passwordTxt.equals("0000")){

                    	ClientUI.connectedAsAdministrator(true);
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }

                }else{

                	ClientUI.connectedAsAdministrator(false);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            }
        });

    }

    public static void main(String[] args) {

        Connect c = new Connect();
    }

}
