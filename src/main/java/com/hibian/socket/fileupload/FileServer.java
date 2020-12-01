package com.hibian.socket.fileupload;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xin.chen
 * @date 2020/12/1 9:24
 */
public class FileServer {
    private static String uploadPath ="/temp/upload";

    public static void main(String[] args) throws IOException {
        //1.设置上传路径
        if (args != null && args.length!=0) {
            uploadPath = args[0];
        }
        System.out.println("上传路径为:"+uploadPath);
        int port = 43960;
        //2.开启服务
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("文件上传服务开启,监听"+port+"端口");

        while (true) {
            final Socket socket = serverSocket.accept();
            new Thread(()-> {
                try {
                    saveFile(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void saveFile(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();

        //1.指定文件存储地址
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        //2.获取文件名
        byte[] bytes = new byte[1 << 20];
        String filename = getString(inputStream, bytes);
        FileOutputStream fileOutputStream = new FileOutputStream(file+"/"+filename);

        //3.文件传输
        int len;
        while ((len = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes,0,len);
        }

        //4.输出信息
        System.out.println("文件"+filename+"上传成功");
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(("文件"+filename+"上传成功").getBytes());

        //5.关闭资源
        fileOutputStream.close();
        socket.close();
        System.out.println("连接已关闭");
    }

    /**
    * @description: 获取文件名
    * @author: xin.chen
    * @date: 2020/12/1
    */
    private static String getString(InputStream inputStream, byte[] bytes) throws IOException {
        //读取文件名长度到byte缓存中
        int fileNameByteLength = inputStream.read(bytes, 0, 1);
        byte fileNameLength = bytes[fileNameByteLength-1];
        //通过文件名长度获取文件名
        inputStream.read(bytes, 0, fileNameLength);
        return new String(bytes, 0, fileNameLength);
    }
}
