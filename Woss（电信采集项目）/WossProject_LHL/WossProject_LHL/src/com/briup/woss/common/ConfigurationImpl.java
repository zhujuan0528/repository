package com.briup.woss.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.WossModule;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;

public class ConfigurationImpl implements Configuration{
	private Map<String,WossModule> mapWoss=new HashMap<String,WossModule>();

	public ConfigurationImpl(){
		this("src/conf.xml");
	}

	public ConfigurationImpl(String filePath){
		// 实例化解析器
		SAXReader reader=new SAXReader();
		try{
			// 获取文档节点
			Document document=reader.read(filePath);
			// 获取根节点
			Element root=document.getRootElement();
			// 遍历根节点下的一级子节点
			for(Object object1:root.elements()){
				Element element1=(Element)object1;
				String clsName=element1.attributeValue("class");
				WossModule wossModule=(WossModule)Class.forName(clsName).newInstance();
				Properties porperties=new Properties();
				// 遍历一级子节点下的二级子节点
				for(Object object2:element1.elements()){
					Element element2=(Element)object2;
					// 获取节点的名字
					String pkey=element2.getName();
					// 获取节点的值
					String pvalue=element2.getText();
					porperties.put(pkey,pvalue);
				}
				wossModule.init(porperties);
				mapWoss.put(element1.getName(),wossModule);
				for(Object o:mapWoss.values()){
					if(o instanceof ConfigurationAWare){
						((ConfigurationAWare)o).setConfiguration(this);
					}
				}
			}
		}catch(DocumentException e){
			e.printStackTrace();
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	@Override
	public BackUP getBackup() throws Exception{
		return (BackUP)mapWoss.get("backup");
	}

	@Override
	public Client getClient() throws Exception{
		return (Client)mapWoss.get("client");
	}

	@Override
	public DBStore getDBStore() throws Exception{
		return (DBStore)mapWoss.get("dbstore");
	}

	@Override
	public Gather getGather() throws Exception{
		return (Gather)mapWoss.get("gather");
	}

	@Override
	public Logger getLogger() throws Exception{
		return (Logger)mapWoss.get("logger");
	}

	@Override
	public Server getServer() throws Exception{
		return (Server)mapWoss.get("server");
	}

	public static void main(String[] args){
		ConfigurationImpl configuration=new ConfigurationImpl();
		try{
			System.out.println(configuration.getGather());
			System.out.println(configuration.getClient());
			System.out.println(configuration.getServer());
			System.out.println(configuration.getDBStore());
			System.out.println(configuration.getLogger());
			System.out.println(configuration.getBackup());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
