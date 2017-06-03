package context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import context.BeanDefine;
import context.MuziResource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author lili 2016/12/28
 */
public class MuziXmlApplicationContext{
    //所有的实体
    List<BeanDefine> beanList = new ArrayList<>();
    Map<String, Object> sigletionBeanMap = new HashMap<String, Object>();//xmlbean 的id-bean对象
    public MuziXmlApplicationContext(String fileName) {
        //读取配置文件中的bean
        readXml(fileName);

        //实例化bean
        instanceBean();

        //注解处理器
        annotationInject();
    }

    private void annotationInject() {
        for(String beanName : sigletionBeanMap.keySet()){
            Object bean = sigletionBeanMap.get(beanName);
            if(bean != null){
                this.filedAnnotation(bean);
            }
        }
    }

    private void filedAnnotation(Object bean) {
        //1.bean获取所有的字段
        Field[] fields = bean.getClass().getFields();
        for(Field field :fields){//字段
            if(field !=null && field.isAnnotationPresent(MuziResource.class)){
                MuziResource annotation = field.getAnnotation(MuziResource.class);
                Object annotationFieldValue = null;
                if(annotation.name() != null){//注解有名称
                    annotationFieldValue = sigletionBeanMap.get(annotation.name());
                }else {//按类型查找
                    for(String beanId : sigletionBeanMap.keySet()){
                        Object object = sigletionBeanMap.get(beanId);
                        if(field.getType().isAssignableFrom(object.getClass())){//判断当前属性所属的类型是否在配置文件中存在
                            annotationFieldValue = object;
                            break;
                        }
                    }
                }
                try {
                    //field.setAccessible(true); //判断当前属性所属的类型是否在配置文件中存在
                    field.set(bean, annotationFieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void instanceBean() {
        for(BeanDefine bean : beanList){
            try {
                sigletionBeanMap.put(bean.getId(),Class.forName(bean.getClazz()).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readXml(String fileName){
        Document document = null;
        SAXReader saxReader = new SAXReader();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            document = saxReader.read(loader.getResourceAsStream(fileName));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Element beans = document.getRootElement();
        Iterator<Element> beansList = beans.elementIterator();
        while (beansList.hasNext()){
            Element element = beansList.next();
            BeanDefine bean = new BeanDefine(
                    element.attributeValue("id"),
                    element.attributeValue("class"));
            beanList.add(bean);
        }
    }

    public Object getBean(String beanId){
        return sigletionBeanMap.get(beanId);
    }
}
