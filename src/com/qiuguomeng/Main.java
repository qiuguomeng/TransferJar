package com.qiuguomeng;

import com.qiuguomeng.util.InteractionSocket;
import com.qiuguomeng.util.Log;
import com.qiuguomeng.util.ThreadUtil;

/**
 * Created by qiuguomeng on 2017/4/5.
 */
public class Main implements InteractionInterface{
    private static Main instance = new Main();
    public static void main(String[] args){
//        instance.push();//1
//        instance.pull();//2
//        instance.delete();//3
//        instance.mkDir();//4
//        instance.read();//5
//        instance.move();//6
    }

    private void move() {
        CommandBeans commandBeans = new CommandBeans(InteractionSocket.MOVE_TYPE,"C:\\0101\\0202.txt",  "C:\\0101\\010101\\0202.txt", 5,this);
        ThreadUtil.getInstance().setTask(/*5,*/commandBeans);
    }

    private void read() {
        CommandBeans commandBeans = new CommandBeans(InteractionSocket.READ_TYPE,  "C:\\0101", 5,this);
        ThreadUtil.getInstance().setTask(/*5,*/commandBeans);
    }

    private void mkDir() {
        CommandBeans commandBeans = new CommandBeans(InteractionSocket.MKDIR_TYPE,  "C:\\0101\\010101",4, this);
        ThreadUtil.getInstance().setTask(/*4,*/commandBeans);
    }

    private void delete() {
        CommandBeans commandBeans = new CommandBeans(InteractionSocket.DELETE_TYPE,  "C:\\0101\\010101",3, this);
        ThreadUtil.getInstance().setTask(/*3,*/commandBeans);
    }

    private void pull() {
        CommandBeans commandBeans = new CommandBeans(InteractionSocket.PULL_TYPE,  "C:\\0101\\0202.txt","D:\\0102\\0203.txt",1, this);
        ThreadUtil.getInstance().setTask(/*2,*/commandBeans);
    }

    private void push() {
        CommandBeans commandBeans = new CommandBeans(InteractionSocket.PUSH_TYPE, "D:\\0102\\0202.txt", "C:\\0101\\0202.txt", 1,this);
        ThreadUtil.getInstance().setTask(/*1,*/commandBeans);
//        try {
//            Thread.currentThread().sleep(3000);
//            ThreadUtil.isRun = false;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void pushFeedback(boolean b, int id) {
        switch (id) {
            case 1:
                Log.i("pushFeedBack:",b);
                break;
        }
    }

    @Override
    public void pullFeedback(boolean b, int id) {
        switch (id) {
            case 2:
                Log.i("pullFeedBack:",b);
                break;
        }
    }

    @Override
    public void moveFeedback(boolean b, int id) {

    }

    @Override
    public void deleteFeedback(boolean b, int id) {
        switch (id) {
            case 3:
                Log.i("deleteFeedBack:",b);
                break;
        }
    }

    @Override
    public void makeDirectoryFeedback(boolean b, int id) {
        switch (id) {
            case 4:
                Log.i("mkDirFeedBack:",b);
                break;
        }
    }

    @Override
    public void readFeedback(String[] files, int id) {
        switch (id) {
            case 5:
                for (String a: files){
                    Log.i("readFeedBack:",a);
                }
                break;

        }
    }
}
