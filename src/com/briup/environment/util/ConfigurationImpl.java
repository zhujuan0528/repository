package com.briup.environment.util;

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.gui.Login;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationImpl implements Configuration {
    /*
    * 读取configuration.xml内容，将标签作为key，类的实例对象作为值存储到map集合中--
    * */
    private static Properties properties;
    private Map<String,EMDCModule> map=new HashMap<>();
    public ConfigurationImpl() {
        this("src/configuration.xml");
    }

    public ConfigurationImpl(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            /*
            * 1.获取根节点
            * 2.获取根节点之后的所有子节点存入NodeList
            * 3.遍历子节点，根据节点类型获取元素节点
            * 4.遍历元素节点，获取节点名称和属性节点的值
            * 5.将Class属性节点的值进行实例化（反射）
            * 6.map按照元素节点名称为key，实例化对象为value进行保存
            * */
            Element e = document.getDocumentElement();
            NodeList nodeList = e.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if(nodeList.item(i).getNodeType()==1){
                    Element element=(Element)nodeList.item(i);
                    String tagName = element.getTagName();
                    String attribute = element.getAttribute("class");

                    EMDCModule emdcModule=(EMDCModule)Class.forName(attribute).newInstance();
                    //EMDCModule emdcModule=(EMDCModule)Class.forName(attribute).newInstance();
                    map.put(tagName,emdcModule);
                    NodeList nodeList1 = element.getChildNodes();
                    properties=new Properties();
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        if(nodeList1.item(j).getNodeType()==1) {
                            Element element1 = (Element) nodeList1.item(j);
                            String tagName1 = element1.getTagName();
                            String value = element1.getTextContent();

                            properties.put(tagName1, value);
                        }
                    }
                    emdcModule.init(properties);
                    /*
                    * 1.获取当前节点的子节点，构建properties对象
                    * 2.根据节点类型判断获取元素节点
                    * 3.取元素节点的名称为key，文本节点值为value
                    * 4.properties按照节点名称为key何文本节点为value进行保存
                    * 5.EMDCModul调用init方法保存properties方法
                    * */
                    map.put(element.getTagName(),emdcModule);
                    for(Object o:map.values()){
                        if(o instanceof ConfigurationAware){
                            ((ConfigurationAware)o).setConfiguration(this);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Log getLogger() throws Exception {
        return (Log)map.get("log");
    }

    @Override
    public BackUp getBackUp() throws Exception {
        return (BackUp) map.get("backup");
    }

    @Override
    public Gather getGather() throws Exception {
        return (Gather)map.get("gather");
    }

    @Override
    public Client getClient() throws Exception {
        return (Client)map.get("client");
    }

    @Override
    public Server getServer() throws Exception {
        return (Server)map.get("server");
    }

    @Override
    public DBStore getDBStore() throws Exception {
        return (DBStore)map.get("dbstore");
    }

    @Override
    public Login getLogin() throws Exception {
        return (Login) map.get("log");
    }
}
