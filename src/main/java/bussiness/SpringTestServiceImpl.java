package bussiness;


import context.MuziResource;

/**
 * @author lili 2016/12/28
 */
public class SpringTestServiceImpl implements SpringTestService {
    @MuziResource(name = "springTestDao")
    public SpringTestDao springTestDao;

    @Override
    public void hello() {
        springTestDao.save();
    }

  /*  public bussiness.SpringTestDao getSpringTestDao() {
        return springTestDao;
    }

    public void setSpringTestDao(bussiness.SpringTestDao springTestDao) {
        this.springTestDao = springTestDao;
    }*/
}
