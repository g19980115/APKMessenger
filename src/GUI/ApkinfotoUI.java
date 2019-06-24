package GUI;

import ToolUtil.*;
import apkinfo.ApkInfo;
import apkinfo.ApkUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkinfotoUI {
    private JTextField Edit_FilePath;
    private JTextField Edit_AppName;
    private JTextField Edit_PackageName;
    private JTextField Edit_VersionName;
    private JTextField Edit_Version;
    private JTextField Edit_MD5;
    private JTextField Edit_PEInfo;
    private JTextField Edit_Json;
    private JTextField Edit_FileSize;
    private JComboBox jComboBox_lang;
    private Map AppName;
    private List AppNamekey;
    private String icoPath;
    private ItemListener itemListener;
    private String FilePath;
    private JLabel IcoBox;
    private JTable jTable_Permissions;
    private JTable jTable_Sign;
    private JTable jTable_Otherinfo;

    public ApkinfotoUI(JTextField Edit_FilePath, JTextField Edit_AppName,
                       JTextField Edit_PackageName, JTextField Edit_VersionName,
                       JTextField Edit_Version, JTextField Edit_MD5, JTextField Edit_PEInfo,
                       JTextField Edit_Json, JTextField Edit_FileSize, JComboBox jComboBox_lang, JLabel IcoBox,
                       JTable jTable_Permissions
            , JTable jTable_Sign, JTable jTable_Otherinfo
    ) {


        this.Edit_FilePath = Edit_FilePath;
        this.Edit_AppName = Edit_AppName;
        this.Edit_MD5 = Edit_MD5;
        this.Edit_PackageName = Edit_PackageName;
        this.Edit_PEInfo = Edit_PEInfo;
        this.Edit_Version = Edit_Version;
        this.Edit_VersionName = Edit_VersionName;
        this.Edit_Json = Edit_Json;
        this.Edit_FileSize = Edit_FileSize;
        this.jComboBox_lang = jComboBox_lang;
        this.IcoBox = IcoBox;
        this.jTable_Permissions = jTable_Permissions;
        this.jTable_Sign = jTable_Sign;
        this.jTable_Otherinfo = jTable_Otherinfo;

        itemListener = getItemListener();
        jComboBox_lang.removeItemListener(itemListener);
        jComboBox_lang.addItemListener(itemListener);
    }

    public void OpenApkFile(String FilePath) throws Exception{
        this.FilePath = FilePath;
        ApkUtil apkUtil = new ApkUtil();
        ApkInfo apkInfo = apkUtil.parseApk(FilePath);
        Edit_FilePath.setText(FilePath);
        Edit_PackageName.setText(apkInfo.getPackageName());
        Edit_AppName.setText(apkInfo.getLabel());

        Edit_VersionName.setText(apkInfo.getVersionName());
        Edit_Version.setText(apkInfo.getVersionCode());
        Edit_FileSize.setText(convertFileSize(apkInfo.getSize()));
        AppName = apkInfo.getAppName();
        AppNamekey = apkInfo.getAppNameKey();

        int zh_CN_index = -1;
        int index = -1;
        if (AppNamekey.size() != 0) {
            jComboBox_lang.removeAllItems();
            for (Object key : AppNamekey) {
                ++index;
                if (key.equals("zh_CN")) {
                    zh_CN_index = index;
                }
                if (key.equals("label")) {
                    jComboBox_lang.addItem("默认");
                } else {
                    jComboBox_lang.addItem(key);
                }
            }

            if (zh_CN_index != -1) {
                jComboBox_lang.setSelectedIndex(zh_CN_index);
                jComboBox_lang.setSelectedItem("zh_CN");
                Edit_AppName.setText((String) AppName.get("zh_CN"));

            } else {
                Edit_AppName.setText((String) AppName.get("label"));
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Edit_MD5.setText("正在读取MD5");
                File file = new File(FilePath);
                String md5 = Md5CaculateUtil.getMD5(file);
                if (md5 == null) md5 = "读取失败";
                Edit_MD5.setText(md5.toUpperCase());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Edit_PEInfo.setText("读取加固信息中");
                    List SoList = ZipUtil.readZipFile(FilePath, "lib/");
                    SoInfoUtil soInfoUtil = new SoInfoUtil();
                    String soname = "";
                    for (Object s : SoList) {
                        System.out.println(s);
                        File tempFile = new File((String) s);
                        String fileName = tempFile.getName();
                        soname = soInfoUtil.IsThisSo(fileName);
                        if (soname.length() != 0) {
                            break;
                        }
                    }
                    if (soname.length() != 0) {
                        Edit_PEInfo.setText(soname);
                    } else {
                        Edit_PEInfo.setText("没有加固或者未知加固");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        //图标读取

        icoPath = apkInfo.getIcon();
    /*    Map map=apkInfo.getIcons();
        for (Object key : map.keySet()) {
            System.out.println(key + ":" + map.get(key));
        }*/
        //遍历ICON列表
        PermissionsUtil permissionsUtil = new PermissionsUtil();
        List<String> list1 = permissionsUtil.getList_PermissionsKey();
        List<String> list2 = permissionsUtil.getList_PermissionsName();
        List<String> list3 = permissionsUtil.getList_PermissionsNotes();
        Map<String, Object> map = new HashMap<>();

        List list = apkInfo.getUsesPermissions();

        for (Object key : list) {
            boolean isput = false;
            for (int i = 0; i < list1.size(); i++) {
                if (key.equals(list1.get(i))) {
                    PermissionsObj permissionsObj = new PermissionsObj((String) key, list2.get(i), list3.get(i));
                    map.put((String) key, permissionsObj);
                    isput = true;
                    break;
                }
                if (isput == false) {
                    PermissionsObj permissionsObj = new PermissionsObj((String) key, "未知权限", "");
                    map.put((String) key, permissionsObj);
                }
            }
        }

        String json = "{\"appName\":\"%s\",\"versionCode\":\"%s\",\"versionName\":\"%s\",\"pkgId\":\"%s\""
                + ",\"apkPath\":\"%s\",\"iconPath\":\"%s\"}";
        String outJson = String.format(json, Edit_AppName.getText(),
                apkInfo.getVersionCode(), apkInfo.getVersionName(),
                apkInfo.getPackageName(), FilePath.replaceAll("\\\\", "\\\\\\\\"), "");
        Edit_Json.setText(outJson);

        DefaultTableModel jTable_PermissionsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        int i = 0;
        //权限转数组
        String DataArray[][] = new String[map.size()][4];
        for (Object key : map.keySet()) {
            PermissionsObj obj = (PermissionsObj) map.get(key);
            DataArray[i][0] = String.valueOf(i + 1);
            DataArray[i][1] = obj.getPermissionskey();
            DataArray[i][2] = obj.getPermissionsName();
            DataArray[i][3] = obj.getPermissionsExplan();
            i++;
        }
        //构建表格模型
        String[] jTable_Permissions_colname = {"编号", "英文名", "权限名称", "权限注释"};
        DefaultTableModel model = new DefaultTableModel(DataArray, jTable_Permissions_colname) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_Permissions.setModel(model);
        jTable_Permissions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable_Permissions.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTable_Permissions.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTable_Permissions.getColumnModel().getColumn(2).setPreferredWidth(120);
        jTable_Permissions.getColumnModel().getColumn(3).setPreferredWidth(400);

        System.out.println("iconpath=" + icoPath);
        //end
        File file = new File(FilePath);
        try {
            ZipFile zf = new ZipFile(file);
            ZipEntry zipEntry = zf.getEntry(icoPath);
            InputStream inputStream = zf.getInputStream(zipEntry);
            byte[] bite = readStream(inputStream);
            ImageIcon icon = new ImageIcon(bite);
            Image img = icon.getImage();
            //img = img.getScaledInstance(96, 96, Image.SCALE_SMOOTH);//图片平滑优先，大小默认96*96
            icon.setImage(img);
            IcoBox.setIcon(icon);
            /*=null;
            inputStream.read(bite);
            ImageIcon icon=new ImageIcon(bite);
            IcoBox.setIcon(icon);*/
            zf.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String exportImg() {
        Image image = ((ImageIcon) IcoBox.getIcon()).getImage();
        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File file = fsv.getHomeDirectory();
        String entryFilePath = file.getPath() + "\\OutFile\\Image\\" + icoPath.substring(icoPath.lastIndexOf("/") + 1);
        File export = new File(entryFilePath);
        try {
            if (export.exists()) {
                export.delete();
            }
            export.mkdirs();
            export.createNewFile();
            ImageIO.write(bi, "png", export);
        } catch (IOException e) {
            e.printStackTrace();
            export.deleteOnExit();
        }
        return entryFilePath;
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public ItemListener getItemListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().equals("默认")) {
                    Edit_AppName.setText(String.valueOf(AppName.get("label")));

                } else {
                    Edit_AppName.setText(String.valueOf(AppName.get(e.getItem())));
                }
            }
        };
    }

    public byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


}