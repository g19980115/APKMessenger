package GUI;

import ToolUtil.SignUtil;
import com.sun.deploy.panel.JreTableModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class WinMain {
    private JFrame win;
    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    private Label label5;
    private Label label6;
    private Label label7;
    private Label label8;
    private Label label9;
    private JTextField Edit_FilePath;
    private JTextField Edit_AppName;
    private JTextField Edit_PackageName;
    private JTextField Edit_VersionName;
    private JTextField Edit_Version;
    private JTextField Edit_MD5;
    private JTextField Edit_PEInfo;
    private JTextField Edit_Json;
    private JTextField Edit_FileSize;
    private JButton Btn_ll, Btn_pathCopy;
    private JButton Btn_AppName;
    private JButton Btn_PackageName;
    private JButton Btn_VersionName;
    private JButton Btn_Version;
    private JButton Btn_MD5;
    private JButton Btn_PEInfo;
    private JButton Btn_json;
    private JButton Btn_ExportImg;
    private JComboBox jComboBox_lang;
    private Thread ThreadMain;
    private ApkinfotoUI apkinfotoUI;
    private JTabbedPane jTabbedPane;
    private JPanel jPanel_Main;
    private JPanel jPanel_Permissions;
    private JPanel jPanel_Sign;
    private JPanel jPanel_Otherinfo;
    private JTable jTable_Permissions;
    private JTable jTable_Sign;
    private JTable jTable_Otherinfo;
    private final Color WRITE = new Color(16777215);
    public static float scale = 1.8f;
    private int maxHeight = 567;

    public void CreatWin() {
        setUI();
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        if (screenWidth > 1500) {
            scale = screenWidth / 1500f;
        }
        win = new JFrame("APK Messenger v3.2");
        win.setSize((int) (586 * scale), (int) (309 * scale));
        win.setLocationRelativeTo(null);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        win.setResizable(false);
        win.setLayout(null);
        win.getContentPane().setBackground(WRITE);
        //创建菜单
        MenuBar menuBar = new MenuBar();
        Menu menu_1 = new Menu("软件");
        Menu menu_2 = new Menu("文件");
        Menu menu_3 = new Menu("工具");
        Menu menu_4 = new Menu("帮助");

        Font font = new Font(null, 0, (int) (14 * scale));
        menu_1.setFont(font);
        menu_2.setFont(font);
        menu_3.setFont(font);
        menu_4.setFont(font);

        //软件菜单
        MenuItem menuItem_1_1 = new MenuItem("软件设置");
        MenuItem menuItem_1_2 = new MenuItem("生成分析报告");
        menu_1.add(menuItem_1_1);
        menu_1.add(menuItem_1_2);
        //另存为菜单
        Menu menu_2_1 = new Menu("另存为");
        MenuItem menuItem_2_1 = new MenuItem("文件名另存");
        MenuItem menuItem_2_2 = new MenuItem("自定义另存");
        MenuItem menuItem_2_3 = new MenuItem("软件名称-版本号另存");
        MenuItem menuItem_2_4 = new MenuItem("包名另存");
        menu_2_1.add(menuItem_2_1);
        menu_2_1.add(menuItem_2_2);
        menu_2_1.add(menuItem_2_3);
        menu_2_1.add(menuItem_2_4);
        //工具菜单
        Menu menu_3_1 = new Menu("在线查询");
        MenuItem menuItem_3_1 = new MenuItem("Google Play");
        MenuItem menuItem_3_2 = new MenuItem("酷安");
        MenuItem menuItem_3_3 = new MenuItem("PP助手");
        MenuItem menuItem_3_4 = new MenuItem("豌豆荚");
        menu_3_1.add(menuItem_3_1);
        menu_3_1.add(menuItem_3_2);
        menu_3_1.add(menuItem_3_3);
        menu_3_1.add(menuItem_3_4);
        //帮助菜单
        MenuItem menuItem_4_1 = new MenuItem("关于软件");
        MenuItem menuItem_4_2 = new MenuItem("软件主页");
        MenuItem menuItem_4_3 = new MenuItem("常见问题");
        menu_4.add(menuItem_4_1);
        menu_4.add(menuItem_4_2);
        menu_4.add(menuItem_4_3);
        //设置菜单
        menuBar.add(menu_1);
        menuBar.add(menu_2);
        menu_2.add(menu_2_1);
        menuBar.add(menu_3);
        menu_3.add(menu_3_1);
        menuBar.add(menu_4);
        win.setMenuBar(menuBar);
        //图标图片框
        JLabel IcoBox = new JLabel();

        IcoBox.setBounds((int) (8 * scale), (int) (24 * scale), (int) (152 * scale), (int) (152 * scale));
        IcoBox.setBorder(BorderFactory.createLineBorder(Color.black));
        ImageIcon andico = new ImageIcon(getClass().getResource("andico.png"));
        IcoBox.setHorizontalAlignment(JLabel.CENTER);//图片居中
        IcoBox.setIcon(andico);
        win.add(IcoBox);
        label1 = DrowLable("文件路径：", (int) (168 * scale), (int) (16 * scale), (int) (56 * scale), (int) (24 * scale));
        label2 = DrowLable("应用名：", (int) (168 * scale), (int) (48 * scale), (int) (56 * scale), (int) (24 * scale));
        label3 = DrowLable("包名：", (int) (168 * scale), (int) (75 * scale), (int) (56 * scale), (int) (24 * scale));
        label4 = DrowLable("版本名：", (int) (168 * scale), (int) (104 * scale), (int) (56 * scale), (int) (24 * scale));
        label5 = DrowLable("文件MD5：", (int) (168 * scale), (int) (136 * scale), (int) (56 * scale), (int) (24 * scale));
        label6 = DrowLable("加固类型：", (int) (168 * scale), (int) (168 * scale), (int) (56 * scale), (int) (24 * scale));
        label7 = DrowLable("版本号：", (int) (377 * scale), (int) (104 * scale), (int) (45 * scale), (int) (24 * scale));
        label8 = DrowLable("展开》》》", (int) (500 * scale), (int) (232 * scale), (int) (64 * scale), (int) (24 * scale));
        label9 = DrowLable("JSON：", (int) (168 * scale), (int) (200 * scale), (int) (56 * scale), (int) (24 * scale));
        label8.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label8.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (win.getHeight() >= (int) (maxHeight * scale)) {
                    label8.setText("展开》》》");
                    win.setSize((int) (586 * scale), (int) (309 * scale));
                } else {
                    label8.setText("收起《《《");
                    win.setSize((int) (586 * scale), (int) (maxHeight * scale));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        //语言选择下拉列表
        jComboBox_lang = new JComboBox();
        jComboBox_lang.setBounds((int) (232 * scale), (int) (49 * scale), (int) (72 * scale), (int) (20 * scale));
        jComboBox_lang.addItem("默认");
        //编辑框绘制
        Edit_FilePath = DrowEdit("", (int) (232 * scale), (int) (16 * scale), (int) ((256 - 24) * scale), (int) (24 * scale));
        Edit_AppName = DrowEdit("", (int) (312 * scale), (int) (48 * scale), (int) (176 * scale), (int) (24 * scale));
        Edit_PackageName = DrowEdit("", (int) (232 * scale), (int) (75 * scale), (int) (256 * scale), (int) (24 * scale));
        Edit_VersionName = DrowEdit("", (int) (232 * scale), (int) (104 * scale), (int) (64 * scale), (int) (24 * scale));
        Edit_Version = DrowEdit("", (int) (424 * scale), (int) (104 * scale), (int) (64 * scale), (int) (24 * scale));
        Edit_MD5 = DrowEdit("", (int) (232 * scale), (int) (136 * scale), (int) (256 * scale), (int) (24 * scale));
        Edit_PEInfo = DrowEdit("", (int) (232 * scale), (int) (168 * scale), (int) (256 * scale), (int) (24 * scale));
        Edit_Json = DrowEdit("", (int) (232 * scale), (int) (200 * scale), (int) (256 * scale), (int) (24 * scale));
        Edit_FileSize = DrowEdit("", (int) (8 * scale), (int) (186 * scale), (int) (64 * scale), (int) (24 * scale));

        Edit_FilePath.setEditable(false);
        setEditable(false, Edit_FilePath, Edit_AppName, Edit_PackageName,
                Edit_VersionName, Edit_Version, Edit_MD5, Edit_PEInfo, Edit_Json, Edit_FileSize);


        //绘制按钮
        Btn_ll = DrowBtn("浏览", (int) ((497 + 24) * scale), (int) (16 * scale), (int) (48 * scale), (int) (24 * scale));
        Btn_pathCopy = DrowBtn("复制", (int) ((497 - 24) * scale), (int) (16 * scale), (int) (48 * scale), (int) (24 * scale));
        Btn_AppName = DrowBtn("复制", (int) (497 * scale), (int) (46 * scale), (int) (72 * scale), (int) (24 * scale));
        Btn_PackageName = DrowBtn("复制", (int) (497 * scale), (int) (74 * scale), (int) (72 * scale), (int) (24 * scale));
        Btn_VersionName = DrowBtn("复制", (int) (301 * scale), (int) (104 * scale), (int) (72 * scale), (int) (24 * scale));
        Btn_Version = DrowBtn("复制", (int) (497 * scale), (int) (104 * scale), (int) (72 * scale), (int) (24 * scale));
        Btn_MD5 = DrowBtn("复制", (int) (497 * scale), (int) (136 * scale), (int) (72 * scale), (int) (24 * scale));
        Btn_PEInfo = DrowBtn("复制", (int) (497 * scale), (int) (168 * scale), (int) (72 * scale), (int) (24 * scale));
        Btn_json = DrowBtn("复制", (int) (497 * scale), (int) (200 * scale), (int) (72 * scale), (int) (24 * scale));
        Btn_ExportImg = DrowBtn("导出图片", (int) (82 * scale), (int) (186 * scale), (int) (80 * scale), (int) (24 * scale));
        //事件绑定
        Btn_ll.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("安卓apk文件", "apk");
            fileChooser.addChoosableFileFilter(fileNameExtensionFilter);
            fileChooser.setFileFilter(fileNameExtensionFilter);
            fileChooser.showOpenDialog(win.getContentPane());
        });
        Btn_pathCopy.addActionListener(actionListener);
        Btn_AppName.addActionListener(actionListener);
        Btn_PackageName.addActionListener(actionListener);
        Btn_VersionName.addActionListener(actionListener);
        Btn_Version.addActionListener(actionListener);
        Btn_MD5.addActionListener(actionListener);
        Btn_PEInfo.addActionListener(actionListener);
        Btn_json.addActionListener(actionListener);
        Btn_ExportImg.addActionListener(actionListener);

        JCheckBox top = new JCheckBox("窗口顶置");
        top.setBounds((int) (8 * scale), (int) (232 * scale), (int) (96 * scale), (int) (24 * scale));
        top.setBackground(WRITE);
        top.addActionListener(e -> {
            win.setAlwaysOnTop(top.isSelected());//窗口顶置
        });
        top.setFocusable(false);

        //绘制详细信息的面板
        jPanel_Main = new JPanel();
        jPanel_Permissions = new JPanel();
        jPanel_Permissions.setOpaque(false);


        jPanel_Sign = new JPanel();
        jPanel_Sign.setBackground(WRITE);
        jPanel_Otherinfo = new JPanel();
        jPanel_Otherinfo.setBackground(WRITE);
        jTabbedPane = new JTabbedPane();
        jTabbedPane.setBackground(WRITE);
        jPanel_Main.setBounds((int) (8 * scale), (int) (264 * scale), (int) (560 * scale), (int) (248 * scale));
        jPanel_Main.setBackground(WRITE);
        jTabbedPane.setBounds((int) (8 * scale), (int) (0 * scale), (int) (550 * scale), (int) (240 * scale));
        jPanel_Main.setLayout(null);
        jPanel_Main.add(jTabbedPane);
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);


        //权限表

        String[] jTable_Permissions_colname = {"编号", "英文名", "权限名称", "权限注释"};
        jTable_Permissions = new JTable(null, jTable_Permissions_colname);
        //禁止表格编辑
        DefaultTableModel jTable_PermissionsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_Permissions.setGridColor(WRITE);

        JScrollPane jScrollPane_Permissions = new JScrollPane(jTable_Permissions);//拖动面板
        jScrollPane_Permissions.setBorder(BorderFactory.createLineBorder(WRITE));
        jTable_Permissions.getTableHeader().setReorderingAllowed(false);//禁止拖动

        jPanel_Permissions.setLayout(new BorderLayout());
        // jScrollPane_Permissions.setOpaque(false);
        jScrollPane_Permissions.setBackground(WRITE);
        jPanel_Permissions.add(jScrollPane_Permissions, BorderLayout.CENTER);

        jTable_PermissionsTableModel.setDataVector(null, jTable_Permissions_colname);
        jTable_Permissions.setModel(jTable_PermissionsTableModel);
        jTable_Permissions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable_Permissions.getColumnModel().getColumn(0).setPreferredWidth((int) (40 * scale));
        jTable_Permissions.getColumnModel().getColumn(1).setPreferredWidth((int) (120 * scale));
        jTable_Permissions.getColumnModel().getColumn(2).setPreferredWidth((int) (120 * scale));
        jTable_Permissions.getColumnModel().getColumn(3).setPreferredWidth((int) (400 * scale));
        //签名表
        String[] jTable_Sign_colname = {"项目名称", "项目内容"};
        jTable_Sign = new JTable(null, jTable_Sign_colname);
        //禁止表格编辑
        DefaultTableModel jTable_SignTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_Sign.setGridColor(WRITE);
        jTable_Sign.getTableHeader().setReorderingAllowed(false);//禁止拖动
        jPanel_Sign.setLayout(new BorderLayout());
        JTableHeader jTableHeaderSign = jTable_Sign.getTableHeader();
        jPanel_Sign.add(jTableHeaderSign, BorderLayout.NORTH);//添加表头
        jPanel_Sign.add(jTable_Sign, BorderLayout.CENTER);
        jTable_SignTableModel.setDataVector(null, jTable_Sign_colname);
        jTable_Sign.setModel(jTable_SignTableModel);
        //其他信息表
        String[] jTable_Other_colname = {"项目名称", "项目内容", "注释"};
        jTable_Otherinfo = new JTable(null, jTable_Other_colname);
        //禁止表格编辑
        DefaultTableModel jTable_OtherTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_Otherinfo.setGridColor(WRITE);
        jTable_Otherinfo.getTableHeader().setReorderingAllowed(false);//禁止拖动
        jPanel_Otherinfo.setLayout(new BorderLayout());
        JTableHeader jTableHeaderOther = jTable_Otherinfo.getTableHeader();
        jPanel_Otherinfo.add(jTableHeaderOther, BorderLayout.NORTH);//添加表头
        jPanel_Otherinfo.add(jTable_Otherinfo, BorderLayout.CENTER);
        jTable_OtherTableModel.setDataVector(null, jTable_Other_colname);
        jTable_Otherinfo.setModel(jTable_OtherTableModel);


        jTabbedPane.addTab("权限信息", jPanel_Permissions);
        jTabbedPane.addTab("签名信息", jPanel_Sign);
        jTabbedPane.addTab("其他信息", jPanel_Otherinfo);
        //添加组件
        addobj(Edit_AppName, Edit_FilePath, Edit_MD5, Edit_PackageName, Edit_PEInfo,
                Edit_Json, Edit_FileSize, Edit_Version, Edit_VersionName);
        addobj(label1, label2, label3, label4, label5, label6, label7, label8, label9);
        addobj(Btn_AppName, Btn_ll, Btn_pathCopy, Btn_MD5, Btn_PackageName, Btn_PEInfo, Btn_json,
                Btn_ExportImg, Btn_Version, Btn_VersionName, jComboBox_lang, top);
        addobj(jPanel_Main);
        addDropTarget(win.getContentPane());
        addDropTarget(Edit_AppName);
        addDropTarget(Edit_FilePath);
        addDropTarget(Edit_MD5);
        addDropTarget(Edit_PackageName);
        addDropTarget(Edit_PEInfo);
        addDropTarget(Edit_Version);
        addDropTarget(Edit_VersionName);
        addDropTarget(Edit_Json);
        addDropTarget(Edit_FileSize);

        addDropTarget(IcoBox);
        win.setVisible(true);
        apkinfotoUI = new ApkinfotoUI(Edit_FilePath, Edit_AppName, Edit_PackageName,
                Edit_VersionName, Edit_Version, Edit_MD5, Edit_PEInfo, Edit_Json, Edit_FileSize,
                jComboBox_lang, IcoBox, jTable_Permissions, jTable_Sign, jTable_Otherinfo);
    }

    private void setEditable(boolean enable, JTextField... ts) {
        for (JTextField t : ts) {
            t.setEditable(enable);
        }
    }

    private void copy(String text) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(text);
        clip.setContents(tText, null);
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == Btn_AppName) {//APP名
                copy(Edit_AppName.getText());
            } else if (source == Btn_PackageName) {//包名
                copy(Edit_PackageName.getText());
            } else if (source == Btn_VersionName) {//版本名
                copy(Edit_VersionName.getText());
            } else if (source == Btn_Version) {//版本号
                copy(Edit_Version.getText());
            } else if (source == Btn_MD5) {//MD5
                copy(Edit_MD5.getText());
            } else if (source == Btn_json) {//JSON
                copy(Edit_Json.getText());
            } else if (source == Btn_PEInfo) {
                copy(Edit_PEInfo.getText());
            } else if (source == Btn_ExportImg) {
                copy(apkinfotoUI.exportImg());
                JOptionPane.showMessageDialog(win, "已导出到桌面且已复制路径", "注意！", 1);
            } else if (source == Btn_pathCopy) {
                copy(Edit_FilePath.getText());
            }
        }
    };

    private void addDropTarget(Component obj) {
        DropTarget dropTarget = CreatdropTarget();
        obj.setDropTarget(dropTarget);

    }

    private DropTarget CreatdropTarget() {
        DropTargetAdapter dropTargetAdapter = new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
                    {
                        //只能单一拖放
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
                        List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        //获取文件后缀名
                        String FileName = list.get(0).getAbsolutePath();
                        String prefix = FileName.substring(FileName.lastIndexOf(".") + 1).toLowerCase();
                        if (prefix.equals("apk")) {

                            ThreadMain = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        apkinfotoUI.OpenApkFile(FileName);
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(win, e.getMessage(), "错误", 1);
                                    }

                                }
                            });
                            ThreadMain.start();

                        }


                        dtde.dropComplete(true);//指示拖拽操作已完成
                    } else {
                        dtde.rejectDrop();//否则拒绝拖拽来的数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DropTarget dropTarget = new DropTarget(win, dropTargetAdapter);
        return dropTarget;
    }


    private void addobj(Object... objs) {
        for (Object obj : objs) {
            win.add((Component) obj);
        }

    }

    private Label DrowLable(String str, int x, int y, int w, int h) {
        Label lab = new Label(str);
        lab.setBounds(x, y, w, h);
        return lab;
    }

    private JTextField DrowEdit(String str, int x, int y, int w, int h) {
        JTextField Edit = new JTextField(str);
        Edit.setBounds(x, y, w, h);
        return Edit;
    }

    private JButton DrowBtn(String str, int x, int y, int w, int h) {
        JButton Btn = new JButton(str);
        Btn.setBounds(x, y, w, h);
        return Btn;
    }


    private void setUI() {
        String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(windows);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
