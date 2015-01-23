package lj.common

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-23
 * Time: 下午11:17
 * To change this template use File | Settings | File Templates.
 */
class HttpSessionListenerCustom implements HttpSessionListener  {
    @Override
    void sessionCreated(HttpSessionEvent se) {
        //To change body of implemented methods use File | Settings | File Templates.
//        def staffId=se.session.getAttribute("staffId");
//        println("sessionCreated-staffId-->"+staffId);
//        def userId=se.session.getAttribute("userId");
//        println("sessionCreated-userId-->"+userId);
    }

    @Override
    void sessionDestroyed(HttpSessionEvent se) {
        //To change body of implemented methods use File | Settings | File Templates.

//        long staffId=lj.Number.toLong(se.session.getAttribute("staffId"));
//        println("sessionDestroyed-staffId-->"+staffId);
//        StaffInfo.withTransaction {
//            //更新工作人员的在线状态为下线
//            StaffInfo staffInfo=StaffInfo.get(staffId);
//            if(staffInfo){
//                staffInfo.isOnline=false;
//                if(!staffInfo.save(flush: true)){
//                    println("更新工作人员在线状态为下线错误，请查证：");
//                    staffInfo.errors.allErrors.each {
//                        println(it);
//                    }
//                }
//            }
//        }

    }
}
