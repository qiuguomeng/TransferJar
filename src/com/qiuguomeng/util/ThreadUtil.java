package com.qiuguomeng.util;

import com.qiuguomeng.CommandBeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiuguomeng on 2017/4/5.
 */
public class ThreadUtil extends Thread{
//    private int key;
//    private static Map<Integer,CommandBeans> map = new HashMap();
    private static List<CommandBeans> taskList = new ArrayList<>();
    public static boolean isRun = true;
    private static ThreadUtil threadUtil;

    private ThreadUtil(){
        this.start();
    }

    public static synchronized ThreadUtil getInstance(){
        if (threadUtil == null) {
            threadUtil = new ThreadUtil();
        }
        return threadUtil;
    }

    public /*synchronized */void  setTask(/*int key1, */CommandBeans commandBeans) {
        taskList.add(commandBeans);
//        key = key1;
//        map.put(key1,commandBeans);
    }
    @Override
    public void run() {
        while (isRun){
//            Log.i("mapSize:", ""+map.size());
            if (taskList.size() > 0){
//            if (map.size() > 0){
                synchronized (this) {
                    InteractionSocket.getInstance().request(taskList.get(taskList.size()-1));
                    taskList.remove(taskList.size()-1);
//                    InteractionSocket.getInstance().request(key,map.get(key));
//                    map.remove(key);
                }
            }
        }
        InteractionSocket.getInstance().destroy();
        Log.i("ThreadUtil:","destroy");
    }
}
