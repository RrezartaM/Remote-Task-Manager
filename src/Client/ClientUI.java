package Client;

import javax.swing.*;
import javax.swing.border.*;

import Server.ServerInterface;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

public class ClientUI {
    private Client client;
    private static ServerInterface server;
 

    String selectedClientName;

    static ClientUI clientUI;

    public static boolean administrator;

    String password = "";
    String myName;

    boolean connected = false;
    DefaultListModel listModel = new DefaultListModel();
    DefaultListModel admListModel = new DefaultListModel();
    DefaultListModel checkList = new DefaultListModel();


    
    JTextField  ip, name;
    JButton connect, admConnect;
    JList clientsList;
    JList administratorList;
    JFrame frame;
    JButton getTm;
    JPanel clients = new JPanel();
    JPanel admPanel = new JPanel();
    JPanel connectedPanel = new JPanel();

    private static final String TASKLIST = "tasklist.exe";//top/nh ps -ax
    private static final String KILL = "taskkill /F /IM "; //kill -9

    public static void main(String[] args) {
       
        clientUI = new ClientUI();
    }


    //User Interface code.
    public ClientUI() {
        frame = new JFrame("Connection");
        JPanel main = new JPanel();
        JPanel top = new JPanel();
        JPanel cn = new JPanel();
        JPanel bottom = new JPanel();
        ip = new JTextField();
       
        name = new JTextField();
       
        connect = new JButton("Connect");
        connect.setBackground(Color.WHITE);
        connect.setForeground(Color.BLUE);
               getTm = new JButton("Get premission");
        getTm.setBackground(Color.WHITE);
        getTm.setForeground(Color.BLUE);
        clientsList = new JList();
        clientsList.setBorder(new EmptyBorder(13, 3, 10, 3));
       
        administratorList = new JList();
        administratorList.setBorder(new EmptyBorder(2, 3, 10, 3));
        administratorList.setBackground(Color.lightGray);
        connectedPanel.setLayout(new BorderLayout(10, 5));
       
        top.setLayout(new GridLayout(1, 0, 5, 5));
        cn.setLayout(new BorderLayout(5, 5));
        bottom.setLayout(new BorderLayout(5, 5));
        top.add(new JLabel("Your name: "));
        top.add(name);
        top.add(new JLabel("Server Address: "));
        top.add(ip);
        top.add(connect);
        cn.add(connectedPanel, BorderLayout.WEST);
        main.add(top, BorderLayout.NORTH);
        main.add(cn, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
       getTm.addActionListener(new ActionListener() {
           
    	   
    	   
    	   
    	   public void actionPerformed(ActionEvent e) {



                int selectedIndex = clientsList.getSelectedIndex();

                if (selectedIndex == -1) {

                    JOptionPane.showMessageDialog(frame, "Select a client first");
                } else {
                    selectedClientName = (String) listModel.getElementAt(selectedIndex);

                    String getTmMessage = "GETTM" + selectedClientName + "^";
                    sendFromTm(getTmMessage);
    
                }


            }
        });

        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (!connected) {
                    if (name.getText().length() < 2) {
                        JOptionPane.showMessageDialog(frame, "You need to type a name.");
                        return;
                    }
                    if (ip.getText().length() < 2) {
                        JOptionPane.showMessageDialog(frame, "You need to type an IP.");
                        return;
                    }

                    Connect connect = new Connect();
                } else {

                    try {
                        clientUI.doConnect();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


        frame.setContentPane(main);
        frame.setSize(550, 200);
        frame.setVisible(true);
    }



    public void sendText() {
        if (connect.getText().equals("Connect")) {
            JOptionPane.showMessageDialog(frame, "You need to connect first.");
            return;
        }
  
    }

    public void writeMsg(String st) {


        int removeName = st.indexOf(":");
        int tmIndex = st.indexOf("^");

        if (tmIndex == -1 || removeName == -1) {
          
            int index = st.indexOf(" ");
            String connected = st.substring(index, st.length() - 1);
            if (connected.equals(" has just connected")) {

        

            }
        } else {

            if(!administrator) {
                String fromAdm = st.substring(removeName + 1, tmIndex);
                String fromAdmMessage = TmMessage + myName;

                String killAppsMessage = killTmMessage + myName;

                if (fromAdm.trim().toLowerCase().equals(fromAdmMessage.trim().toLowerCase())) {

                    final Object[] options = {"Allow", "Deny",};

                    SwingUtilities.invokeLater(new Runnable() {
                     
                        public void run() {

                            int choice = JOptionPane.showOptionDialog(frame,
                                    "Administrator wants to have access in your Task Manager!",
                                    "Warning!", 
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    null);
                            if (choice == 0) {

                                ArrayList<String> appNames = listRunningProcesses();
                                StringBuilder str = new StringBuilder();

                                for (int i = 0; i < appNames.size(); i++) {
                                    str.append(appNames.get(i)).append(",");
                                }

                                String size = " " + appNames.size();
                                str.append(size).append(",");

                                System.out.println("allowed");
                                sendTmToAdministrator(showTmMessage + myName + "^" + str.toString());

                            } else {

                                System.out.println("denyed");
                            }
                        }
                    });


                }else if(fromAdm.trim().toLowerCase().equals(killAppsMessage.trim().toLowerCase())){

                    int appsSize = Integer.parseInt(st.substring(st.length() - 2,st.length() - 1).trim());

                    String appListFromCLient = st.substring(tmIndex + 1, st.length());
                    System.out.println(appListFromCLient);

                    StringTokenizer stringTokenizer = new StringTokenizer(appListFromCLient, ",");
                    ArrayList<String> killlist = new ArrayList<>();

                    for (int i = 0; i < appsSize; i++) {

                        killlist.add(stringTokenizer.nextToken());
                    }

                    for (int j = 0; j < killlist.size(); j++)
                        killProcess(killlist.get(j));
                }

            }else{

                String fromAdm = st.substring(removeName + 1, tmIndex);
                String fromAdmMessage = showTmMessage + selectedClientName;
                if (fromAdm.trim().toLowerCase().equals(fromAdmMessage.trim().toLowerCase())) {

                    int appsSize = Integer.parseInt(st.substring(st.length() - 3,st.length() - 1).trim());

                    String appListFromCLient = st.substring(tmIndex + 1, st.length());
                    System.out.println(appListFromCLient);

                    StringTokenizer stringTokenizer = new StringTokenizer(appListFromCLient, ",");
                    ArrayList<String> tmList = new ArrayList<>();

                    for (int i = 0; i < appsSize; i++) {

                        tmList.add(stringTokenizer.nextToken());
                    }

                    TaskManager c = new TaskManager(selectedClientName ,tmList);
                }
            }
        }
    }

    public static void killProcess(String serviceName) {

        try {
        	Runtime.getRuntime().exec("kill -9 "+serviceName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void updateUsers(int c, String name, Vector v) {

        myName = name;
        if (c == 1)
            for (int i = 0; i < v.size(); i++) {
                try {
                    String tmp = ((ClientInt) v.get(i)).getName();
                    listModel.addElement(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        else {
            try {
                int index = listModel.indexOf(name);
                v.remove(index);
                listModel.removeElement(name);
                checkList.removeElement(name);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clientsList.setModel(listModel);
        clients.setLayout(new BorderLayout(5, 5));
        clients.add(new JLabel("Clients:"), BorderLayout.NORTH);
        clients.add(clientsList, BorderLayout.CENTER);

        administratorList.setModel(admListModel);
        admPanel.setLayout(new BorderLayout(5, 5));
        admPanel.add(new JLabel("Administrator:"), BorderLayout.NORTH);
        admPanel.add(administratorList, BorderLayout.CENTER);

        connectedPanel.add(clients, BorderLayout.CENTER);

        if (administrator) {
            connectedPanel.add(getTm, BorderLayout.SOUTH);
        }

    }


    public static ArrayList<String> listRunningProcesses() {
        ArrayList<String> processes = new ArrayList<String>();
        try {
            String line;
            //String sysUserName=System.getProperty("servisName");
           // Process p = Runtime.getRuntime().exec("ps -u "+sysUserName+"");
            Process p = Runtime.getRuntime().exec("TASKLIST");
            BufferedReader input = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {

                    if (line.contains("Console") && line.contains(".exe")) {
                        if (!processes.contains(line.substring(0, line.indexOf(" "))))
                            processes.add(line.substring(0, line.indexOf(" ")));
                    }
                }

            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return processes;
    }

    public static void sendFromTm(String text) {

        text = "Administrator" + ": " + text;

        try {
            server.publish(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTmToAdministrator(String text) {

        text = "client" + ": " + text;

        try {
            server.publish(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void connectedAsAdministrator(boolean adm) {

        administrator = adm;
        try {

            clientUI.doConnect();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public void doConnect() throws RemoteException {
        if (!connected) {
            if (name.getText().length() < 2) {
                JOptionPane.showMessageDialog(frame, "You need to type a name.");
                return;
            }
            if (ip.getText().length() < 2) {
                JOptionPane.showMessageDialog(frame, "You need to type an IP.");
                return;
            }

            try {
                client = new Client(name.getText());
                client.setGUI(this);
                server = (ServerInterface) Naming.lookup("rmi://" + ip.getText() + ":" + "1099/rrezartamusmurati");

                for (int i = 0; i < server.getConnected().size(); i++) {
                    try {
                        String tmp = ((ClientInt) server.getConnected().get(i)).getName();
                        checkList.addElement(tmp.toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (checkList.contains(client.getName().toLowerCase())) {

                    JOptionPane.showMessageDialog(frame, "Login with a different name.");
                    return;
                }

                server.login(client);
                updateUsers(1, name.getText(), server.getConnected());
                connect.setText("Disconnect");
                connected = true;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "ERROR, we couldn't connect....");
            }
        } else {
            try {
                updateUsers(0, name.getText(), server.getConnected());
                connected = false;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            connect.setText("Connect");
            
        }
    }
    public static String IP = "192.168.0.100";
    public String TmMessage = "GETTM";
    public String showTmMessage = "SHOWTMadm";
    public String killTmMessage = "KILLtm";
}