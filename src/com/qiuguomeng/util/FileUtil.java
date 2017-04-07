package com.qiuguomeng.util;

import java.io.*;
import java.net.Socket;

/**
 * Created by qiuguomeng on 2017/4/5.
 */
public class FileUtil {
    public static boolean push(String srcFile, long serverFileLength) {
        try {
            Socket socket = new Socket(InteractionSocket.HOST,3001);
            OutputStream outputStream = socket.getOutputStream();
            RandomAccessFile randomAccessFile = new RandomAccessFile(srcFile,"r");
            randomAccessFile.seek(serverFileLength);
            int len = 0;
            byte[] bytes = new byte[2048];
            while (-1 != (len = randomAccessFile.read(bytes))) {
                outputStream.write(bytes,0,len);
            }
            randomAccessFile.close();
            socket.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean pull(String desFile, long localFileLength) {
        try {
            File file = new File(desFile);
            if (!file.exists()){
                file.createNewFile();
            }
            Socket socket = new Socket(InteractionSocket.HOST,3001);
            InputStream inputStream = socket.getInputStream();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
            randomAccessFile.seek(localFileLength);
            int len = 0;
            byte[] bytes = new byte[2048];
            while (-1 != (len = inputStream.read(bytes))) {
                randomAccessFile.write(bytes,0,len);
            }
            randomAccessFile.close();
            socket.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
