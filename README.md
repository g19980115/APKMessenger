# APKMessenger
## 软件介绍
基于https://github.com/ghboke/APKMessenger 开发，JAVA GUI桌面程序  
目前JAVA版本实现了功能  
获取应用名，支持提取多语言名称  
获取包名，版本名，版本号，文件MD5  
查壳功能（不完善），支持获取爱加密，360加固，梆梆加固，阿里云加固，腾讯乐固，百度加固，娜迦加固，顶象加固，通付盾，网秦，几维加固等壳信息  
读取APK图标  
支持提取APK对应的权限，并且给出权限注释 

## 最近更新
### v3.1 (2019-06-24)
修改jar引用aapt的方式，打包jar后无需将aapt放到同级文件夹中，一个jar文件即可实现全部功能~  
原理是读取apk信息前，将aapt.exe文件写入到系统temp目录下，读取完成后再删除。
### v3.0（2019-06-21）
* 新增默认显示中文应用名  
* 新增文件大小显示  
* 完成复制按钮  
* 新增导出图片按钮

## 相关说明  
本软件为APKMessenger的复刻版本，原软件为易语言开发迭代到了2.5版本，下载地址：<s>https://www.ghboke.com/apkinfo.html</s> http://www.pc6.com/softview/SoftView_538551.html  
本JAVA源代码实现了发行版本的大部分功能，如果要用于生产环境，建议您下载上面地址上面的版本。  
开发工具：IDEA  

### 软件部分按钮不能使用，要读取apk信息，直接把文件拖到软件即可
![img](https://github.com/g19980115/APKMessenger/blob/master/apkmessenger1.png)

  
