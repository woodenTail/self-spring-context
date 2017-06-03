import bussiness.SpringTestService;
import context.MuziXmlApplicationContext;


public class Main {

    public static void main(String[] args) {

        //自定义ApplicationContext
        MuziXmlApplicationContext context1 = new MuziXmlApplicationContext("spring-main.xml");
        SpringTestService service = (SpringTestService) context1.getBean("springTestService");
        service.hello();
    }
}
