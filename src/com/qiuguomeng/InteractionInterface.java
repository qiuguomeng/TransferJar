package com.qiuguomeng;

/**
 * Created by qiuguomeng on 2017/4/5.
 */
public interface InteractionInterface {
    void pushFeedback(boolean b,int id);
    void pullFeedback(boolean b,int id);
    void moveFeedback(boolean b,int id);
    void deleteFeedback(boolean b,int id);
    void makeDirectoryFeedback(boolean b,int id);
    void readFeedback(String[] files, int id);
}
