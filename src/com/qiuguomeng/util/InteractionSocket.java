package com.qiuguomeng.util;

import com.qiuguomeng.CommandBeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiuguomeng on 2017/4/5.
 */
public class InteractionSocket {
    public static final byte PUSH_TYPE = 0;
    public static final byte PULL_TYPE = 1;
    public static final byte READ_TYPE = 2;
    public static final byte MOVE_TYPE = 3;
    public static final byte DELETE_TYPE = 4;
    public static final byte MKDIR_TYPE = 5;
    public static final String HOST = "118.89.50.173";
//        public static final String HOST = "localhost";
    private static Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static InteractionSocket interactionSocket;

    private InteractionSocket(){
    }

    public static synchronized InteractionSocket getInstance(){
        if (socket == null) {
            try {
                socket = new Socket(HOST, 3000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (interactionSocket == null){
            interactionSocket = new InteractionSocket();
        }
        return interactionSocket;
    }

    public void request(int id,final CommandBeans commandBeans1) {
        CommandBeans commandBeans2 = commandBeans1;
        Log.i("InteractionSocket.id:"+id,commandBeans2);
        switch (commandBeans2.operateType){
            case PUSH_TYPE:
                File file1 = new File(commandBeans2.srcFileName);
                if (file1.exists()){
                    sendCommand("push:"+commandBeans2.desFileName+","+file1.length());
                }else {
                    commandBeans2.interactionInterface.pushFeedback(false,id);
                    return;
                }
                try {
                    long serverFileLength = Long.parseLong(commandProcess("UTF-8"));
                    new TransferThread(id,commandBeans2,serverFileLength);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PULL_TYPE:
                File file2 =  new File(commandBeans2.desFileName);
                long localFileLength = 0;
                if (file2.exists()) {
                    localFileLength = file2.length();
                }
                sendCommand("pull:"+commandBeans2.srcFileName+","+localFileLength);//locaFileLength表示从服务器开始读取的字节
                new TransferThread(id,commandBeans2,localFileLength);
                break;
            case READ_TYPE:
                sendCommand("read:"+commandBeans2.desFileName);
                try {
                    String result = commandProcess("UTF-8");
                    String[] results = result.split(",");
                    commandBeans2.interactionInterface.readFeedback(results,id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MOVE_TYPE:
                try {
                    sendCommand("move:"+commandBeans2.srcFileName+","+commandBeans2.desFileName);
                    int status = Integer.parseInt(commandProcess("UTF-8"));
                    if (status == 0){
                        commandBeans2.interactionInterface.moveFeedback(false,id);
                    }else {
                        commandBeans2.interactionInterface.moveFeedback(true,id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case DELETE_TYPE:
                try {
                    sendCommand("delete:"+commandBeans2.desFileName);
                    int status = Integer.parseInt(commandProcess("UTF-8"));
                    if (status == 0){
                        commandBeans2.interactionInterface.deleteFeedback(false,id);
                    }else {
                        commandBeans2.interactionInterface.deleteFeedback(true,id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MKDIR_TYPE:
                try {
                    sendCommand("mkDir:"+commandBeans2.desFileName);
                    int status = Integer.parseInt(commandProcess("UTF-8"));
                    if (status == 0){
                        commandBeans2.interactionInterface.makeDirectoryFeedback(false,id);
                    }else {
                        commandBeans2.interactionInterface.makeDirectoryFeedback(true,id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public  void destroy(){
        try {
            socket.close();
            interactionSocket = null;
            Log.i("InteractionSocket:","destroy");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(String a){
        try {
            if (outputStream == null){
                outputStream = socket.getOutputStream();
            }
            if (inputStream == null){
                inputStream = socket.getInputStream();
            }
            String command = "1<start>"+a+"<end>";
            outputStream.write(command.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String commandProcess(String charsetName) throws Exception {
        List<Byte> byteList = new ArrayList<Byte>();
        out:
        while (true) {
            byte[] b0 = new byte[1];
            if (-1 == inputStream.read(b0)) {
                break;
            } else if ('<' == b0[0]) {
                byte[] b00 = new byte[6];
                inputStream.read(b00);
                if (new String(b00).contains("start>")) {

                    while (true) {
                        inputStream.read(b0);
                        if (b0[0] == '<') {
                            inputStream.read(b00,0,4);
                            if (new String(b00, 0, 4).contains("end>")) {
                                break out;
                            } else {
                                for (int i = 0;i < 4;i++) {
                                    byteList.add(b00[i]);
                                }
                            }
                        }
                        byteList.add(b0[0]);
                    }
                }
            }

        }
        byte[] b = new byte[byteList.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = byteList.get(i);
        }
        return new String(b,charsetName);
    }

    class TransferThread extends Thread{
        private int id;
        private CommandBeans commandBeans;
        private long startTransferPoint;
        public TransferThread(int id,CommandBeans commandBeans,long startTransferPoint){
            this.id = id;
            this.commandBeans = commandBeans;
            this.startTransferPoint = startTransferPoint;
            this.start();
        }
        @Override
        public void run() {
            switch (commandBeans.operateType) {
                case InteractionSocket.PUSH_TYPE:
                    commandBeans.interactionInterface.pushFeedback(FileUtil.push(commandBeans.srcFileName,startTransferPoint),id);
                    break;
                case InteractionSocket.PULL_TYPE:
                    commandBeans.interactionInterface.pullFeedback(FileUtil.pull(commandBeans.desFileName,startTransferPoint),id);
                    break;
            }
        }
    }
}
