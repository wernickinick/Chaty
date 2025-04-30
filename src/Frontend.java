import javax.swing.*;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
public class Frontend {

    private final static int Width = 500;      //Width and Height for Jpanel and Jframe
    private final static int Height = 800;
    public static int check;
    public static String User;
    public static ArrayList<String> online = new ArrayList<>();

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
        Username.setFont(new Font("Klose Slab",Font.BOLD,15));
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
                User = Username.getText();
                if(User.isEmpty())
                {
                    Error.setVisible(true);
                }
                else if (User.equals("Must Enter Username to Join")){
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
    Chattingscreen.setBounds(0,0,1000,800);
    Chattingscreen.setResizable(false);
    Chattingscreen.setLocationRelativeTo(null);
    Chattingscreen.setLayout(null);
    Chattingscreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    JPanel OnlinePanel = new JPanel();
    OnlinePanel.setBounds(0,0,300,800);
    OnlinePanel.setBackground(new Color(186,171,146));
    OnlinePanel.setLayout(null);
    Chattingscreen.add(OnlinePanel);

    JPanel TextArea = new JPanel();
    TextArea.setBounds(300,0,700,800);
    TextArea.setBackground(new Color(105,162,168));
    TextArea.setLayout(null);
    Chattingscreen.add(TextArea);

    JTextArea MessageArea = new JTextArea();
    MessageArea.setBounds(50,50,600,600);
    MessageArea.setBackground(new Color(111,168,174));
    MessageArea.setFont(new Font("Klose Slab",Font.PLAIN,30));
    MessageArea.setEditable(false);
    MessageArea.setEnabled(false);
    MessageArea.setLayout(null);
    TextArea.add(MessageArea);

    JTextField Text = new JTextField("");
    Text.setBounds(80,675,500,50);
    Text.setFont(new Font("Klose Slab",Font.PLAIN,20));
    TextArea.add(Text);

    JButton SendMessage = new JButton("↑");
    SendMessage.setFont(new Font("Ariel",Font.PLAIN,40));
    SendMessage.setBounds(580,675,50,50);
    TextArea.add(SendMessage);

    SendMessage.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = Text.getText();
            Text.setForeground(Color.black);
            if(!message.isEmpty()){
                MessageArea.append(User + ": " + message + "\n");
                Text.setText("");
            }
        }
    });

    JLabel Logo = new JLabel(new ImageIcon("src/Image/Logo.png"));
    Logo.setBounds(-100,425,500,400);
    OnlinePanel.add(Logo);

    JLabel OnlineLogo = new JLabel(new ImageIcon("src/Image/Online User.png"));
    OnlineLogo.setBounds(-100,-50,500,200);
    OnlinePanel.add(OnlineLogo);

    JLabel OnlineUser = new JLabel(User,SwingConstants.CENTER);
    OnlineUser.setBounds(0,100,300,100);
    OnlineUser.setFont(new Font("Klose Slab",Font.BOLD,30));
    OnlinePanel.add(OnlineUser);

    online.add(User);
    System.out.println(online + " Is Online");
    //Add intergration with backend/server
        //Still learning on for the backend just caught up with school 4-29-25

    Chattingscreen.setVisible(true);
    }
}
