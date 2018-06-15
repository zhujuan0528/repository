package com.briup.environment.util;

import javax.swing.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: cxx
 * 静态方法获取
 * 系统参数
 * @Date: 2018/6/11 14:58
 */
public class SystemUtil {

    public static void alert(String msg,int type){
        if (type==0){
            JOptionPane.showMessageDialog(null, msg, "系统提示", JOptionPane.ERROR_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null, msg, "系统提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    //string转时间戳
    public static Timestamp dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return new Timestamp(Long.parseLong(res));
    }

    //转换时间戳为正常格式
    public static String formateTimestamp(Timestamp t){
        try {
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date =sdf.parse(t.toString());
            return sdf.format(date).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
