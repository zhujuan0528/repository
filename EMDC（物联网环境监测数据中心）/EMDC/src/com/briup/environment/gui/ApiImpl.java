package com.briup.environment.gui;

import com.briup.environment.bean.Environment;
import com.briup.environment.bean.MaxMinAvg;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: cxx
 * 接口实现类
 * @Date: 2018/6/12 16:44
 */
public class ApiImpl implements Api {
    /**
     * @param day 日
     * @param type  数据类型 0:all 1:温度 2:湿度 3:光照强度 4:二氧化碳
     * @return
     */
    @Override
    public Collection<Environment> getData(int day, int type) throws Exception {
        Collection<Environment> coll=new ArrayList<>();
        Environment env=null;
        PreparedStatement ps=null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","envir","envir");
        try {
            String sql="";
            if (type!=0){
                String msg="";
                switch (type){
                    case 1:msg="温度";break;
                    case 2:msg="湿度";break;
                    case 3:msg="光照强度";break;
                    default:msg="二氧化碳";break;
                }
                sql="select* from E_DETAIL_"+day+" where name=?";
                ps=conn.prepareStatement(sql);
                ps.setString(1,msg);
            }else {
                sql="select* from E_DETAIL_"+day;
                ps=conn.prepareStatement(sql);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                env = new Environment();
                env.setName(rs.getString(1));
                env.setSrcId(rs.getString(2));
                env.setDesId(rs.getString(3));
                env.setSersorAddress(rs.getString(4));
                env.setCount(rs.getInt(5));
                env.setCmd(rs.getString(6));
                env.setStatus(rs.getInt(7));
                env.setData(rs.getFloat(8));
                env.setGather_data(rs.getTimestamp(9));
                coll.add(env);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ps!=null){
            ps.close();
        }
        if (conn!=null){
            conn.close();
        }
        return coll;
    }

    @Override
    public List<MaxMinAvg> getMaxMinAvg(int day, int type) throws Exception {
        List<MaxMinAvg> list=new ArrayList<>();
        MaxMinAvg maxMinAvg;
        PreparedStatement ps=null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","envir","envir");

        try {
            //SELECT max(data) from E_DETAIL_25 where name='温度';
            //SELECT min(data) from E_DETAIL_25 where name='温度';
            //SELECT avg(data) from E_DETAIL_25 where name='温度';
            if (type!=0){
                list.add(getMaxMinAvg(day,type,ps,conn));
            }else {
               //处理全部数据
                for (int i = 1; i <=4 ; i++) {
                    list.add(getMaxMinAvg(day,i,ps,conn));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ps!=null){
            ps.close();
        }
        if (conn!=null){
            conn.close();
        }
        return list;
    }

    //获取完整MaxMinAvg对象
    public MaxMinAvg getMaxMinAvg(int day,int type,PreparedStatement ps,Connection conn) throws Exception {
        MaxMinAvg maxMinAvg = new MaxMinAvg();
        String msg="";
        String maxsql="";
        String minsql="";
        String avgsql="";
        switch (type){
            case 1:msg="温度";break;
            case 2:msg="湿度";break;
            case 3:msg="光照强度";break;
            default:msg="二氧化碳";break;
        }
        maxMinAvg.setType(type);
        maxsql="select max(data)from E_DETAIL_"+day+" where name=?";
        ps=conn.prepareStatement(maxsql);
        ps.setString(1,msg);
        maxMinAvg.setMax(getValue(ps));

        minsql="select min(data)from E_DETAIL_"+day+" where name=?";
        ps=conn.prepareStatement(minsql);
        ps.setString(1,msg);
        maxMinAvg.setMin(getValue(ps));

        avgsql="select avg(data)from E_DETAIL_"+day+" where name=?";
        ps=conn.prepareStatement(avgsql);
        ps.setString(1,msg);
        maxMinAvg.setAvg(getValue(ps));
        return maxMinAvg;
    }

    //获取max,min,avg
    public float getValue(PreparedStatement ps) {
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getFloat(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }


    //获取有数据的天数
    //select count(name) FROM E_DETAIL_1;
    @Override
    public List<Integer> getDay() {
        int num=31;
        List<Integer> day=new ArrayList<>();
        for (int i = 1; i <=num ; i++) {
            String sql="select count(name) FROM E_DETAIL_"+i;

        }
        return day;
    }

}
