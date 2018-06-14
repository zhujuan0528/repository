package com.briup.environment.gui;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.SystemUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainWindow extends JFrame{
    private JTabbedPane leftPagePane = new JTabbedPane();
    private JTabbedPane rightPagePane = new JTabbedPane();
    private JPanel selectPanel = new JPanel();
    private JPanel northPanel = new JPanel();
    private JTable table;
    private JComboBox comboBox=new JComboBox();
    private JComboBox comboBox1=new JComboBox();
    private ButtonGroup bgp = new ButtonGroup();
    private DefaultTableModel model;
    private Api api =null;
    private ArrayList<Environment> tableList;

    public MainWindow(){
        this.setSize(1200, 600);
        initLeftPagePane();
        initRightPagePane();
        //菜单栏
        JMenu jm=new JMenu("基本操作") ;     //创建JMenu菜单对象
        JMenuItem t1=new JMenuItem("item1") ;  //菜单项
        JMenuItem t2=new JMenuItem("item2") ;//菜单项
        jm.add(t1) ;   //将菜单项目添加到菜单
        jm.add(t2) ;    //将菜单项目添加到菜单
        JMenuBar  br=new  JMenuBar() ;  //创建菜单工具栏
        br.add(jm) ;      //将菜单增加到菜单工具栏
        JMenu other=new JMenu("其他操作") ;
        JMenuItem o2=new JMenuItem("退出登录") ;
        other.add(o2);
        br.add(other);
        this.setJMenuBar(br) ;

        JSplitPane splitePane = new JSplitPane();
        splitePane.setLeftComponent(leftPagePane);
        splitePane.setRightComponent(rightPagePane);
        splitePane.setEnabled(false);
        JLabel label = new JLabel("<html><h1>物联网监控后台管理中心</h1></html>");
        northPanel.add(label);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(splitePane);
        this.setTitle("物联网监控后台管理中心");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //退出登录
        o2.addActionListener(e -> {
            LoginImpl login = new LoginImpl();
            login.setVisible(true);
            try {
                login.login();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            dispose();
        });

    }

    private void initRightPagePane() {
        JPanel selectRSPanel = new JPanel();
        JPanel southPanel = rightPageSouthPane();
        String[] columnNames = {"","名称","发送端id", "树莓派id", "传感器地址", "传感器个数","指令标号","数据","状态","采集时间"};
        String[][] data = {};
        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        TableColumn tc = table.getTableHeader().getColumnModel().getColumn(0);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setWidth(0);
        tc.setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, cr);
        JScrollPane centerPanel = new JScrollPane(table);
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.blue));

        selectRSPanel.setLayout(new BorderLayout());
        selectRSPanel.add(centerPanel, BorderLayout.CENTER);
        selectRSPanel.add(southPanel, BorderLayout.SOUTH);
        rightPagePane.add("结果信息展现与操作", selectRSPanel);
    }

    private JPanel rightPageSouthPane() {
        JPanel southPanel = new JPanel();
        JButton btExportMsg = new JButton("导出信息到excel");
        JButton btClearData = new JButton("清空数据");
        JButton btRefrashData = new JButton("刷新数据");
        btExportMsg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ArrayList<Environment> tableList = getTableList();
                if (tableList != null && tableList.size() != 0) {
                    exportEnvInfo(tableList);
                    SystemUtil.alert("已经将信息导出到xls文件中！", 1);
                } else {
                    SystemUtil.alert("列表为空，没有信息到导出！", 0);
                }
            }
        });




        //刷新数据
        btRefrashData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //根据当前选择信息，刷新数据
                selectDataInfo();
            }
        });

        //清空数据
        btClearData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                model.setRowCount(0);

            }
        });
        southPanel.setLayout(new GridLayout(1, 5));
        southPanel.add(btExportMsg);
        southPanel.add(btClearData);
        southPanel.add(btRefrashData);

        southPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        return southPanel;
    }

    private void exportEnvInfo(ArrayList list) {
        try {
            WritableWorkbook book = Workbook.createWorkbook(new File("D:\\environment.xls"));
            // 生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet(" 第一页 ", 0);
            String[] title = {"名称","发送端id", "树莓派id", "传感器地址", "传感器个数","指令标号","数据","状态","采集时间"};
            for (int i = 0; i < title.length; i++) {
                Label lb = new Label(i, 0, title[i]);
                sheet.addCell(lb);
            }
            int size = list.size();
            for (int j = 1; j <= size; j++) {
                Environment e = (Environment) list.get(j - 1);
                Label lbName = new Label(0, j, e.getName());
                Label lbSrcId = new Label(1, j, e.getSrcId());
                Label lbDesId = new Label(2, j, e.getDesId());
                Label lbSersorAddress = new Label(3, j, e.getSersorAddress());
                jxl.write.Number lbCount = new jxl.write.Number(4, j,e.getCount());
                Label lbCmd = new Label(5, j, e.getDesId());
                Label lbData = new Label(6, j,String.valueOf(e.getData()));
                Label lbStatu = new Label(7, j, String.valueOf(e.getStatus()));
                Label lbDate = new Label(8, j, e.getGather_data().toString());
                sheet.addCell(lbName);
                sheet.addCell(lbSrcId);
                sheet.addCell(lbDesId);
                sheet.addCell(lbSersorAddress);
                sheet.addCell(lbCount);
                sheet.addCell(lbCmd);
                sheet.addCell(lbData);
                sheet.addCell(lbStatu);
                sheet.addCell(lbDate);
            }
            // 写入数据并关闭文件
            book.write();
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void initselectPanel() {
        JLabel lbSid = new JLabel("日期：");
        JLabel lbName = new JLabel("类型：");
        JButton btSelect = new JButton("开始查找");
        btSelect.addActionListener(actionEvent->{
            //System.out.println("开始查找！");
            selectDataInfo();
        });
        //按enter来查询
        InputMap inputMapSelect = btSelect
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMapSelect = btSelect.getActionMap();
        inputMapSelect.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "enter");
        actionMapSelect.put("enter", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                selectDataInfo();
            }
        });
        selectPanel.setLayout(null);
        lbSid.setBounds(20, 25, 70, 30);
        lbName.setBounds(20, 65, 70, 30);
        btSelect.setBounds(75, 110, 100, 30);

        //下拉列表日期选择
        comboBox1.setBounds(70, 30, 130, 20);
        for (int i=1;i<=31;i++){
            comboBox1.addItem(i);
        }
        selectPanel.add(comboBox1);
        //下拉列表类型选择
        comboBox.setBounds(70, 70, 130, 20);
        comboBox.addItem("所有数据");
        comboBox.addItem("温度");
        comboBox.addItem("湿度");
        comboBox.addItem("光照强度");
        comboBox.addItem("二氧化碳");
        selectPanel.add(comboBox);
        selectPanel.add(lbSid);
        selectPanel.add(lbName);
        selectPanel.add(btSelect);
        selectPanel.setBorder(BorderFactory.createTitledBorder(null, "信息查询"));

    }

    private void initLeftPagePane() {
        JPanel leftPanel = new JPanel();
        initselectPanel();
        //JPanel addPanel = addPanel();
        leftPanel.setLayout(new GridLayout(2, 1));
        leftPanel.add(selectPanel);
        //leftPanel.add(addPanel);

        int w = this.getWidth();
        int h = this.getHeight();
        leftPanel.setPreferredSize(new Dimension(w / 5, h));
        leftPagePane.add("基本操作", leftPanel);

    }
    //按条件获取Collection数据
    public void selectDataInfo() {
        int day = comboBox1.getSelectedIndex()+1;
        int type = comboBox.getSelectedIndex();
        api = new ApiImpl();
        try {
            setTable(api.getData(day,type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置表单数据
    public void setTable(Collection<Environment> env){
        model.setRowCount(0);
        if (env.size()>0) {
            try {
                int id=0;
                for (Environment o : env) {
                    id++;
                    Object[] data = {
                            id,
                            o.getName(),
                            o.getSrcId(),
                            o.getDesId(),
                            o.getSersorAddress(),
                            o.getCount(),
                            o.getCmd(),
                            o.getData(),
                            o.getStatus(),
                            SystemUtil.formateTimestamp(o.getGather_data())
                    };
                    model.addRow(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            SystemUtil.alert("没有记录...",1);
        }
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainWindow stuMis = new MainWindow();
                stuMis.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ArrayList<Environment> getTableList() {
        try{
            ArrayList<Environment> list = new ArrayList<Environment>();
            int size = table.getModel().getRowCount();
            for (int i = 0; i < size; i++) {
                Environment e = new Environment();
                e.setName(table.getModel().getValueAt(i, 1).toString());
                e.setSrcId(table.getModel().getValueAt(i, 2).toString());
                e.setDesId(table.getModel().getValueAt(i, 3).toString());
                e.setSersorAddress(table.getModel().getValueAt(i, 4).toString());
                e.setCount((int)table.getModel().getValueAt(i, 5));
                e.setCmd(table.getModel().getValueAt(i, 6).toString());
                e.setData((float)table.getModel().getValueAt(i, 7));
                e.setStatus((int)table.getModel().getValueAt(i, 8));
                e.setGather_data(SystemUtil.dateToStamp(table.getModel().getValueAt(i, 9).toString()));
                list.add(e);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
