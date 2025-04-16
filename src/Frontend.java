import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frontend {

    private final static int Width = 500;      //Width and Height for Jpanel and Jframe
    private final static int Height = 1000;
    public static int check;

    public static void Main_Window(){
        JFrame Main = new JFrame("Chaty");
        Main.setBounds(0,0,Width,Height);
        Main.setResizable(false);
        Main.setLocationRelativeTo(null);
        Main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel Welcome = new JPanel();
        Welcome.setBounds(0,0,Width,Height);
        Welcome.setBackground(new Color(186,171,146));
        Welcome.setLayout(null);
        Main.add(Welcome);

        JTextField Username = new JTextField("Must Enter Username to Join");
        Username.setBounds(100,300,300,50);
        Username.setLayout(null);
        Welcome.add(Username);


        JButton Enter = new JButton("ENTER CHAT ROOM");
        Enter.setBounds(150,350,200,100);
        Welcome.add(Enter);

        JLabel errorcode = new JLabel("ERROR!: Username Required",SwingConstants.CENTER);
        errorcode.setFont(new Font("Times New Roman", Font.BOLD,20));
        errorcode.setForeground(Color.red);
        errorcode.setBounds(0,0,200,200);


        JFrame Error = new JFrame("ERROR");
        Error.setResizable(false);
        Error.setBounds(0,0,300,200);
        Error.setLocationRelativeTo(null);
        Error.add(errorcode);

        JLabel Logo = new JLabel(new ImageIcon("src/Image/Logo.png"));
        Logo.setBounds(50,-100,400,500);
        Welcome.add(Logo);

        Welcome.setVisible(true);
        Enter.setVisible(true);
        Error.setVisible(false);
        Username.setVisible(true);

        JPanel Online = new JPanel();
        Online.setBounds(0,0,200,1000);
        Online.setBackground(Color.cyan);
        Online.setVisible(true);



        //Method to see if add a username and checks if username is not there
        Enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = Username.getText();
                if(user.isEmpty())
                {
                    Error.setVisible(true);
                }
                else if (user.equals("Must Enter Username to Join")){
                    Error.setVisible(true);
                }
                else {
                    check = 70;
                    Welcome.setVisible(false);
                    Main.setVisible(false);
                    Error.dispose();
                    Chatroom();
                }
            }
        });

        Main.setVisible(true);
    }

    public static void Chatroom(){
    JFrame Chattingscreen = new JFrame("Chaty");
    Chattingscreen.setBounds(0,0,500,1000);
    Chattingscreen.setResizable(false);
    Chattingscreen.setLocationRelativeTo(null);
    Chattingscreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    Chattingscreen.setVisible(true);
    }
}
