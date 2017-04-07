package com.qiuguomeng;

/**
 * Created by qiuguomeng on 2017/4/5.
 */
public class CommandBeans {
    public byte operateType;
    public String srcFileName;
    public String desFileName;
    public InteractionInterface interactionInterface;
    public int id;

    public CommandBeans(byte operateType, String srcFileName, String desFileName,int id,InteractionInterface interactionInterface) {
        this.operateType = operateType;
        this.srcFileName = srcFileName;
        this.desFileName = desFileName;
        this.id = id;
        this.interactionInterface = interactionInterface;
    }

    public CommandBeans(byte operateType, String desFileName, int id,InteractionInterface interactionInterface) {
        this.operateType = operateType;
        this.desFileName = desFileName;
        this.id = id;
        this.interactionInterface = interactionInterface;
    }
}
