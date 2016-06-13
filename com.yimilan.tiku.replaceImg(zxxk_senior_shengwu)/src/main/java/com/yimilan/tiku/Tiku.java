package com.yimilan.tiku;

import com.mysql.jdbc.StringUtils;
import com.yimilan.tiku.Entity.Question;
import com.yimilan.tiku.utils.JdbcUtil;
import com.yimilan.tiku.utils.JsonUtils;
import com.yimilan.tiku.utils.YMLHttpClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xufeng on 15/11/23.
 */
public class Tiku {

    Long finishedCount = 0l;

    public void doTask() throws Exception {
        int thisRecord = 0;
        JdbcUtil jdbcUtil = JdbcUtil.getINSTANCE();
        StringBuilder stringBuilder = new StringBuilder();
        String ids = "";//"421080";
//        ids=    "'2441393'," +
//                "'2441395'," +
//                "'2454993'," +
//                "'2106647'," +
//                "'459312'," +
//                "'459313'," +
//                "'459314'," +
//                "'449082'," +
//                "'449083'," +
//                "'449085'," +
//                "'459044'," +
//                "'459321'," +
//                "'459322'," +
//                "'455245'," +
//                "'459103'," +
//                "'459309'," +
//                "'449081'," +
//                "'2077561'," +
//                "'2113245'," +
//                "'1707425'," +
//                "'2105501'," +
//                "'1991696'," +
//                "'473420'," +
//                "'421507'," +
//                "'473419'," +
//                "'1709702'," +
//                "'1749382'";
        List<Question> questions = jdbcUtil.getQuestions(ids);
        if (questions.size() > 0) {
            for (Question question : questions) {
                thisRecord++;
                String result = parseXML(question.getId(), question.getContent());
                if (!StringUtils.isNullOrEmpty(result)) {
                    if (result.equals("error")) {
                        stringBuilder.append("update question set is_dowith_abc=-1  where id=" + question.getId() + ";");
                    } else {
                        stringBuilder.append("update question set content='" + result.replace("'", "''") + "' ,is_dowith_abc=1  where id=" + question.getId() + ";");
                    }
                } else {
                    stringBuilder.append("update question set is_dowith_abc=2  where id=" + question.getId() + ";");
                }
            }
        } else {
            return;
        }
//        System.out.println(stringBuilder.toString());
        boolean res = jdbcUtil.excuteSql(stringBuilder.toString());
        if (res) {
            System.out.println("更新服务器成功..");
        } else {
            System.out.println("更新服务器失败..");
            thisRecord = 0;
        }
        finishedCount += thisRecord;
        System.out.println("");
        System.out.println("执行了 " + finishedCount + "条数据..");
        doTask();
    }

    /**
     * 构造返回的内容
     *
     * @param _content
     */
    public String parseXML(Long id, String _content) {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <html>" + _content + "</html>";
        try {
            boolean hasFind = false;
            System.out.println(id + ";" + _content);
            Document document = DocumentHelper.parseText(content.replace("&nbsp;", "@@nbsp").replace("&shy;", "@@shy;").replace("&pound;","@@pound;").replace("&reg;","@@reg;").replace("&Oslash;","@@Oslash;").replace("&ocirc;","@@ocirc;").replace("&not;","@@not;").replace("&sup3;","@@sup3;").replace("&yen;","@@yen;").replace("&raquo;","@@raquo;").replace("&ordm;","@@ordm;").replace("&acirc;","@@acirc;").replace("&acute;","@@acute;").replace("&Ugrave;","@@Ugrave;").replace("&sup2;","@@sup2;").replace("&Euml;","@@Euml;").replace("&ntilde;","@@ntilde;"));
            List<Element> elementList = new ArrayList<Element>();
            getOptionTable(document.getRootElement(), elementList);

            for (Element tableElement : elementList) {
                if (tableElement != null && tableElement.attribute("name") != null && tableElement.attribute("name").getValue().toString().toLowerCase().equals("optionstable")) {
                    hasFind = true;
                    List trElementList = tableElement.elements("tr"); //获取tr
                    List trNewList = new ArrayList();
                    //循环旧tr
                    for (int j = 0; j < trElementList.size(); j++) {
                        org.dom4j.tree.DefaultElement trElement = (org.dom4j.tree.DefaultElement) trElementList.get(j);
                        //获取td
                        List tdElementList = trElement.elements("td");
                        //循环旧td
                        for (int k = 0; k < tdElementList.size(); k++) {
                            // 生成新行,每一个td,都生成一个tr
                            DefaultElement trNew = new DefaultElement("tr");
                            DefaultElement tdNew = (DefaultElement) ((DefaultElement) tdElementList.get(k)).clone();//通过老得td clone新的出来
                            tdNew.addAttribute("width", "100%"); //设置宽度100%
                            trNew.add(tdNew);
                            trNewList.add(trNew);
                        }
                    }
                    //移除旧行
                    for (int j = 0; j < trElementList.size(); j++) {
                        org.dom4j.tree.DefaultElement trElement = (org.dom4j.tree.DefaultElement) trElementList.get(j);
                        tableElement.remove(trElement);
                    }
                    //增加新行
                    tableElement.elements().addAll(trNewList);
                }
            }
            if (hasFind) {
                return document.getRootElement().asXML().replace("@@nbsp", "&nbsp;").replace("@@shy;","&shy;").replace("@@pound;","&pound;").replace("@@reg;","&reg;").replace("@@Oslash;","&Oslash;").replace("@@ocirc;","&ocirc;").replace("@@not;","&not;").replace("@@sup3;","&sup3;").replace("@@yen;","&yen;").replace("@@raquo;","&raquo;").replace("@@acirc;","&acirc;").replace("@@ordm;","&ordm;").replace("@@acute;","&acute;").replace("@@Ugrave;","&Ugrave;").replace("@@sup2;","&sup2;").replace("@@Euml;","&Euml;").replace("@@ntilde;","&ntilde;").replace("<html>", "").replace("</html>", "");
            } else {
                System.out.println("没有找到需要转换的table:" + _content);
                return "";
            }
        } catch (Exception e) {
            System.out.println(" parseText  错误!" + "id=" + id + ";" + e.getMessage().toString());
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 获取选项的table
     *
     * @param document
     * @return
     */
    public void getOptionTable(org.dom4j.Element element, List<Element> lsResult) {
        List list = element.elements("table");
        if (list == null || list.size() <= 0) {
            List<Element> elements = element.elements();
            for (Element element1 : elements) {
                getOptionTable(element1, lsResult);
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                org.dom4j.tree.DefaultElement tableElement = (org.dom4j.tree.DefaultElement) list.get(i);
                if (tableElement.attribute("name") != null && tableElement.attribute("name").getValue().toString().toLowerCase().equals("optionstable")) {
                    lsResult.add(tableElement);
                }
            }
        }
    }

    /**
     * 构造返回的内容
     *
     * @param _content
     */
    public String parseXML2(Long id, String _content) {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <html>" + _content + "</html>";
        try {
            boolean hasFind = false;
            System.out.println(id + ";" + _content);
            Document document = DocumentHelper.parseText(content.replace("&nbsp;", "@@nbsp"));
            List list = document.getRootElement().elements("table");
            for (int i = 0; i < list.size(); i++) {

                org.dom4j.tree.DefaultElement tableElement = (org.dom4j.tree.DefaultElement) list.get(i);
                if (tableElement.attribute("name") != null && tableElement.attribute("name").getValue().toString().toLowerCase().equals("optionstable")) {
                    hasFind = true;
                    List trElementList = tableElement.elements("tr"); //获取tr
                    List trNewList = new ArrayList();
                    //循环旧tr
                    for (int j = 0; j < trElementList.size(); j++) {
                        org.dom4j.tree.DefaultElement trElement = (org.dom4j.tree.DefaultElement) trElementList.get(j);
                        //获取td
                        List tdElementList = trElement.elements("td");
                        //循环旧td
                        for (int k = 0; k < tdElementList.size(); k++) {
                            // 生成新行,每一个td,都生成一个tr
                            DefaultElement trNew = new DefaultElement("tr");
                            DefaultElement tdNew = (DefaultElement) ((DefaultElement) tdElementList.get(k)).clone();//通过老得td clone新的出来
                            tdNew.addAttribute("width", "100%"); //设置宽度100%
                            trNew.add(tdNew);
                            trNewList.add(trNew);
                        }
                    }
                    //移除旧行
                    for (int j = 0; j < trElementList.size(); j++) {
                        org.dom4j.tree.DefaultElement trElement = (org.dom4j.tree.DefaultElement) trElementList.get(j);
                        tableElement.remove(trElement);
                    }
                    //增加新行
                    tableElement.elements().addAll(trNewList);
                    break;
                }
            }
            if (hasFind) {
                return document.getRootElement().asXML().replace("@@nbsp", "&nbsp;").replace("<html>", "").replace("</html>", "");
            } else {
                System.out.println("没有找到需要转换的table:" + _content);
                return "";
            }
        } catch (Exception e) {
            System.out.println(" parseText  错误!" + "id=" + id + ";" + e.getMessage().toString());
            e.printStackTrace();
            return "error";
        }
    }
}


/**
 * ALTER TABLE `zxxk_senior_shengwu`.`question`
 * ADD COLUMN `is_dowith_abc` INT NULL AFTER `tempId`;
 * ALTER TABLE `zxxk_senior_shengwu`.`question`
 * ADD COLUMN `content_back` LONGTEXT NULL AFTER `is_dowith_abc`;
 * set sql_safe_updates=0;
 * update question set content_back = content;
 */