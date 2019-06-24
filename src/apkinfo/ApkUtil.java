package apkinfo;

import javax.swing.filechooser.FileSystemView;
import java.io.*;

public class ApkUtil {

    public static final String APPLICATION = "application:";
    public static final String APPLICATION_ICON = "application-icon";
    public static final String APPLICATION_LABEL = "application-label";
    public static final String APPLICATION_LABEL_N = "application: label";
    public static final String DENSITIES = "densities";
    public static final String AppName = "application-label";
    public static final String LAUNCHABLE_ACTIVITY = "launchable";
    public static final String PACKAGE = "package";
    public static final String SDK_VERSION = "sdkVersion";
    public static final String SUPPORTS_ANY_DENSITY = "support-any-density";
    public static final String SUPPORTS_SCREENS = "support-screens";
    public static final String TARGET_SDK_VERSION = "targetSdkVersion";
    public static final String VERSION_CODE = "versionCode";
    public static final String VERSION_NAME = "versionName";
    public static final String USES_FEATURE = "uses-feature";
    public static final String USES_IMPLIED_FEATURE = "uses-implied-feature";
    public static final String USES_PERMISSION = "uses-permission";

    private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";

    private ProcessBuilder builder;
    // aapt 所在目录
    private String aaptToolPath = "src/aapt/";

    public ApkUtil() {
        builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
    }

    public String getAaptToolPath() {
        return aaptToolPath;
    }

    public void setAaptToolPath(String aaptToolPath) {
        this.aaptToolPath = aaptToolPath;
    }

    public ApkInfo parseApk(String apkPath) throws Exception {
        String aaptTool = aaptToolPath + getAaptToolName();
        Process process = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            aaptTool = getAaptHomePath();

            process = builder.command(aaptTool, "d", "badging", apkPath).start();
            inputStream = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            ApkInfo apkInfo = new ApkInfo();
            apkInfo.setSize(new File(apkPath).length());
            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                setApkInfoProperty(apkInfo, temp);
                //System.out.println("全部文本"+temp);
            }
            return apkInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //删除aapt临时文件
            try {
                if (aaptTool != null) {
                    File export = new File(aaptTool);
                    export.deleteOnExit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getAaptToolName() {
        String aaptToolName = "aapt";
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
            aaptToolName += ".exe";
        }
        return aaptToolName;
    }


    private String getAaptHomePath() {
        String aaptTool = aaptToolPath + getAaptToolName();

        String tempPath = System.getProperty("java.io.tmpdir") + File.separator;//获取临时文件目录
        InputStream is = null;
        File sourceAapt = new File(aaptTool);
        if (sourceAapt.exists()) {
            try {
                is = new FileInputStream(sourceAapt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            is = this.getClass().getResourceAsStream("/aapt/" + getAaptToolName());
        }
        //BufferedInputStream in = null;
        //BufferedOutputStream out = null;
        try {
            int byteread = 0;
            File export = new File(tempPath + getAaptToolName());
            FileOutputStream fs = new FileOutputStream(export.getPath());
            byte[] buffer = new byte[1444];

            while ((byteread = is.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            is.close();
            fs.close();
            return export.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //try {
            //    if (in != null) {
            //        in.close();
            //    }
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}
            //try {
            //    if (out != null) {
            //        out.close();
            //    }
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}
        }
        return null;
    }

    private void setApkInfoProperty(ApkInfo apkInfo, String source) {
        //System.out.println("*******************************");
        if (source.startsWith(APPLICATION)) {
            //System.out.println(APPLICATION + " : ");
            if (apkInfo.getIcon() == null) {
                String[] rs = source.split("( icon=')|'");
                apkInfo.setIcon(rs[rs.length - 1]);
            }
        } else if (source.startsWith(APPLICATION_ICON)) {
            //System.out.println(APPLICATION_ICON + " : ");
            String key = getKeyBeforeColon(source);
            String value = getPropertyInQuote(source);
            apkInfo.addToIcons(key, value);
            if (key.equals(ApkInfo.APPLICATION_ICON_320)) {
                apkInfo.setIcon(value);
            }

        } else if (source.startsWith(AppName)) {
            //System.out.println("添加名称："+getPropertyInQuote(source));
            String[] rs = getKeyBeforeColon(source).split("-");
            apkInfo.addAppNameKey(rs[rs.length - 1]);
            apkInfo.addAppName(rs[rs.length - 1], getPropertyInQuote(source));
        } else if (source.startsWith(APPLICATION_LABEL)) {
            //System.out.println(APPLICATION_LABEL + " : ");
            apkInfo.setLabel(getPropertyInQuote(source));
        } else if (source.startsWith(LAUNCHABLE_ACTIVITY)) {
            //System.out.println(LAUNCHABLE_ACTIVITY + " : ");
            apkInfo.setLaunchableActivity(getPropertyInQuote(source));
        } else if (source.startsWith(PACKAGE)) {
            //System.out.println(PACKAGE + " : ");
            String[] packageInfo = source.split(SPLIT_REGEX);
            apkInfo.setPackageName(packageInfo[2]);
            apkInfo.setVersionCode(packageInfo[4]);
            apkInfo.setVersionName(packageInfo[6]);
        } else if (source.startsWith(SDK_VERSION)) {
            //System.out.println(SDK_VERSION + " : ");
            apkInfo.setSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(TARGET_SDK_VERSION)) {
            //System.out.println(TARGET_SDK_VERSION + " : ");
            apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(USES_PERMISSION)) {
            //System.out.println(USES_PERMISSION + " : ");
            apkInfo.addToUsesPermissions(getPropertyInQuote(source));
        } else if (source.startsWith(USES_FEATURE)) {
            // System.out.println(USES_FEATURE + " : ");
            apkInfo.addToFeatures(getPropertyInQuote(source));
        } else {
            //System.out.println("Others : ");
        }
        // System.out.println(source);
    }

    private String getKeyBeforeColon(String source) {
        return source.substring(0, source.indexOf(':'));
    }

    private String getPropertyInQuote(String source) {
        int index = source.indexOf("'") + 1;
        return source.substring(index, source.indexOf('\'', index));
    }

}