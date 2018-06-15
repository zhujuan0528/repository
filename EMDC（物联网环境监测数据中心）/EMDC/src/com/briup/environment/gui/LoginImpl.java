package com.briup.environment.gui;

import com.briup.environment.util.SystemUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginImpl extends JFrame implements Login{
    private JFrame frame = new JFrame("登录");
    private Container container = frame.getContentPane();
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton register=new JButton("注册");
    private JButton forget = new JButton("忘记密码");
    private JLabel a1 = new JLabel("用户名");
    private JLabel a2 = new JLabel("密码");
    private JButton ok = new JButton("登录");
    private JButton cancel = new JButton("取消");
    private Font titlefont = new Font("Microsoft YaHei",Font.BOLD,22);
    private Font textfont = new Font( "Microsoft YaHei",Font.ITALIC,20);
    String userName,passWord;
    String userflag=null;
    String newuser = null;String again=null;String newkey = null;
    JLabel warning = new JLabel("");//用于提示

    @Override
    public void login() {
        frame.setSize(600,500);
        container.setLayout(new BorderLayout());
        initFrame();
        frame.setVisible(true);
        dispose();
    }

    private void initFrame() {
        //title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.setBackground(new Color(150, 217, 250));
        titlePanel.add(new JLabel("管理员登录")).setFont(titlefont);
        container.add(titlePanel,"North");
        //middle
        JPanel fieldPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("src/background.PNG");
                imageIcon.paintIcon(this,g,0,0);
            }
        };
        fieldPanel.setLayout(null);
        a1.setFont(textfont);
        a1.setBounds(145,90,60,25);
        a2.setFont(textfont);
        a2.setBounds(145,120,60,25);
        fieldPanel.add(a1);        fieldPanel.add(a2);
        usernameField.setBounds(230,90,130,25);
        passwordField.setBounds(230,120,130,25);
        fieldPanel.add(usernameField);fieldPanel.add(passwordField);
        register.setBounds(160,190,80,28);
        ActionListener listener1= e -> {new LoginImpl().register(); dispose();};
        register.addActionListener(listener1);
        forget.setBounds(260,190,89,28);
        ActionListener listener2=e->{new LoginImpl().foundpass();dispose();};
        forget.addActionListener(listener2);


        fieldPanel.add(register);fieldPanel.add(forget);
        container.add(fieldPanel,"Center");
        //bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        container.add(buttonPanel,"South");

        warning.setBounds(160, 155, 200, 15);
        ok.addActionListener(e -> {
            userName = usernameField.getText();
            passWord = String.valueOf(passwordField.getPassword());
            User user= new UserImpl();
            boolean flag=user.login(userName,passWord);
            fieldPanel.requestFocus(true);
            ok.setEnabled(true);
            if(flag == true){
                userflag = userName;
                new MainWindow();
                dispose();
            }
            else{
                warning.setText("用户名或密码不正确!");
                warning.setForeground(Color.red);
                fieldPanel.add(warning);
                warning.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            }
        });
    }

    private void foundpass() {
        JFrame frame1 = new JFrame("找回密码");
        frame1.setDefaultCloseOperation(frame1.EXIT_ON_CLOSE);
        frame1.setSize(600,500);
        frame1.setVisible(true);
        Container container1 = frame1.getContentPane();
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.setBackground(new Color(150, 217, 250));
        titlePanel1.add(new JLabel("找回密码")).setFont(titlefont);
        container1.add(titlePanel1,"North");
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        JButton yes = new JButton("确定");
        JButton no = new JButton("取消");
        bottom.add(yes);bottom.add(no);
        container1.add(bottom,"South");
        JPanel jPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("src/background.PNG");
                imageIcon.paintIcon(this,g,0,0);
            }
        };
        JLabel telephone = new JLabel("请打电话给4008208380");
        telephone.setFont(titlefont);
        jPanel.add(telephone);
        container1.add(jPanel);

        frame1.add(container1);
    }

    public static void main(String[] args) throws Exception {
        LoginImpl LoginImpl = new LoginImpl();
        LoginImpl.login();
    }
    public void register(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame frame1 = new JFrame("注册");
        frame1.setSize(600,500);
        frame1.setVisible(true);
        Container container1 = frame1.getContentPane();
        JPanel titlePanel1 = new JPanel();
        titlePanel1.setLayout(new FlowLayout());
        titlePanel1.setBackground(new Color(150, 217, 250));
        titlePanel1.add(new JLabel("管理员注册")).setFont(titlefont);
        container1.add(titlePanel1,"North");
        JPanel fieldPanel1 = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("src/background.PNG");
                imageIcon.paintIcon(this,g,0,0);
            }
        };
        fieldPanel1.setLayout(null);

        a1.setFont(textfont);
        a1.setBounds(145,90,60,25);
        a2.setFont(textfont);
        a2.setBounds(145,120,60,25);

        usernameField.setBounds(230,90,130,25);
        passwordField.setBounds(230,120,130,25);
        JLabel confirm = new JLabel("确认密码");
        confirm.setBounds(120,150,120,25);
        confirm.setFont(textfont);
        JPasswordField confirmpassword = new JPasswordField();
        confirmpassword.setBounds(230,150,130,25);
        fieldPanel1.add(usernameField);fieldPanel1.add(passwordField);fieldPanel1.add(confirmpassword);
        fieldPanel1.add(a1);fieldPanel1.add(a2);fieldPanel1.add(confirm);
        container1.add(fieldPanel1,"Center");
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        JButton yes = new JButton("确定");
        JButton no = new JButton("取消");
        bottom.add(yes);bottom.add(no);
        yes.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newuser=usernameField.getText();
                newkey= String.valueOf(passwordField.getPassword());
                again= String.valueOf(confirmpassword.getPassword());
                if(newkey.equals(again)){
                    User user = new UserImpl();
                    boolean flag= user.searchByName(newuser);
                    if (flag==false){
                        if(user.register(newuser,newkey)){
                            SystemUtil.alert("注册成功!",1);
                        }else {
                            SystemUtil.alert("注册失败!",0);
                        }
                    }else {
                        warning.setText("用户名已存在");
                        warning.setForeground(Color.red);
                        warning.setFont(new Font("黑体", Font.PLAIN, 12));
                    }
                }else {
                    again=null;
                    warning.setText("密码不一致");
                    warning.setForeground(Color.red);
                    warning.setFont(new Font("黑体", Font.PLAIN, 12));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        container1.add(bottom,"South");

        frame1.add(container1);
    }
}
