package com.hibian.socket.fileupload;

import java.io.*;
import java.net.Socket;

/**
 * @author xin.chen
 * @date 2020/12/1 9:43
 */
public class FileClient {

    public static void fileupload(String filePath,String serverHost,Integer port) throws IOException {
        //1.指定上传文件
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        //2.连接服务端
        Socket socket = new Socket(serverHost,port);
        OutputStream outputStream = socket.getOutputStream();

        //3.储存文件名
        byte[] fileMetaBytes = getFileMetaBytes(file);
        outputStream.write(fileMetaBytes);

        //4.文件上传
        int len = 0;
        byte[] bytes = new byte[1 << 20];
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        //5.通知服务端文件上传已结束
        socket.shutdownOutput();

        //6.获取服务端返回信息
        InputStream inputStream = socket.getInputStream();
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }

        //7.关闭资源
        fileInputStream.close();
        socket.close();
    }

    /**
    * @description: byte[0]为文件名长度,用于获取完整文件名,文件名byte长度最大为127长度.byte[1]到byte[byte[0]]为文件名
    * @author: xin.chen
    * @date: 2020/12/1
    */
    private static byte[] getFileMetaBytes(File file) {
        byte[] fileNameBytes = file.getName().getBytes();
        int length = fileNameBytes.length;
        byte[] fileMetaBytes = new byte[fileNameBytes.length + 1];
        if (length < 128) {
            fileMetaBytes[0] = (byte) length;
        }
        for (int i = 1; i < fileMetaBytes.length; i++) {
            fileMetaBytes[i] = fileNameBytes[i - 1];
        }
        return fileMetaBytes;
    }
}
