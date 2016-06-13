package com.yimilan.tiku;

import com.mysql.jdbc.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultEntity;
import sun.swing.StringUIClientPropertyKey;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        Tiku tiku = new Tiku();
        tiku.doTask();

       // System.out.println(" task finished!");
       // tiku.parseXML(1l, "<link href=\"http://kdb.ishilipai.com/static/css/zykdb.css?version=1\" rel=\"stylesheet\" />（09成都一诊）下列关于微生物的叙述正确的是<table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"50%\">A．蛋白胨可用于培养大肠杆菌，也可用于培养噬菌体<br /><div style=\"padding:3.6pt 7.2pt 3.6pt 7.2pt\"><p class=\"MsoNormal\" align=\"right\" style=\"text-align:right\">Tesoon.com<br />天星版权<br /></div></td><td width=\"50%\">B．蓝藻在有丝分裂的间期染色体数目通过复制而加倍</p></td><td width=\"50%\">C．酵母菌呼吸作用产生的CO<sub>2</sub>均来自于线粒体</td></tr><tr><td width=\"50%\">D．大肠杆菌质粒上的抗四环素基因可作为运载体的标记基因</td></tr></table>");

    }
    private static void parseXML(){
        String content="<?xml version=\"1.0\" encoding=\"UTF-8\"?> <html>"+"【题文】线粒体在合成ATP过程中需要下列哪些条件&nbsp;<table name=\"optionsTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"50%\"><span>A．基质中含有0：</span></td><td width=\"50%\">B．基质中含ADP</td></tr><tr><td width=\"50%\">C．基质中的H<sup>+</sup>浓度大于膜间隙</td><td width=\"50%\">D．基质中的H<sup>+</sup>浓度小于膜间隙</td></tr></table>"+"</html>";
        try {
            boolean hasFind=false;
            Document document = DocumentHelper.parseText(content.replace("&nbsp;", "@@nbsp"));
            List list =  document.getRootElement().elements("table");
            for (int i = 0 ;i<list.size();i++){

                org.dom4j.tree.DefaultElement tableElement =(org.dom4j.tree.DefaultElement ) list.get(i);
                if((!StringUtils.isNullOrEmpty(String.valueOf(tableElement.attribute("name").getValue()))) &&tableElement.attribute("name").getValue().toString().toLowerCase().equals("optionstable")){
                    hasFind=true;
                    List trElementList = tableElement.elements("tr"); //获取tr
                    List trNewList = new ArrayList();
                    //循环旧tr
                    for(int j = 0;j<trElementList.size();j++){
                        org.dom4j.tree.DefaultElement trElement =(org.dom4j.tree.DefaultElement ) trElementList.get(j);
                        //获取td
                        List tdElementList =trElement.elements("td");
                        //循环旧td
                        for(int k = 0;k<tdElementList.size();k++) {
                            // 生成新行,每一个td,都生成一个tr
                            DefaultElement trNew = new DefaultElement("tr");
                            DefaultElement tdNew =(DefaultElement)((DefaultElement)tdElementList.get(k)).clone();//通过老得td clone新的出来
                            tdNew.addAttribute("width", "100%"); //设置宽度100%
                            trNew.add(tdNew);
                            trNewList.add(trNew);
                        }
                    }
                    //移除旧行
                    for(int j = 0;j<trElementList.size();j++) {
                        org.dom4j.tree.DefaultElement trElement =(org.dom4j.tree.DefaultElement ) trElementList.get(j);
                        tableElement.remove(trElement);
                    }
                    //增加新行
                        tableElement.elements().addAll(trNewList);
                    int kkkk=0;
                    break;
                }
            }
        if(hasFind){
            System.out.println(document.getRootElement().asXML().replace("@@nbsp", "&nbsp;").replace("<html>","").replace("</html>",""));
        }

//            for (Element item:elementList){
////                if(item.)
//                int i =0;
//                item.getSimpleName()
//            }
        } catch (DocumentException e) {
            System.out.println(" parseText  错误!"+e.getMessage().toString());
            e.printStackTrace();
        }

    }
}
