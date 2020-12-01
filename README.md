# MySimpleFileUploadTool

服务端 `java -jar server.jar <服务端存放上传文件位置> &`,默认为`/temp/upload`

客户端 直接运行ClientGUI.jar

---------------
#### 实现

java socket,通过自建协议完成文件传输.

#### 协议规范
    
![协议规范](./img/Snipaste_2020-12-01_15-49-47.png)
    
    
    