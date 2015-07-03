package lj.taos.order.common

import lj.common.StringUtil
import lj.data.DishesInfo
import lj.data.OrderInfo
import lj.enumCustom.ReCode
import lj.internet.PrintClient

import java.text.SimpleDateFormat

class OrderService {
    def shopService;
    def printDishes(OrderInfo orderInfo,def dishList) {
        printDishes(orderInfo,dishList,true);//第一次打印，也就是说，是点菜时的打印
    }
    def printDishes(OrderInfo orderInfo,def dishList,boolean isFirst) {
        log.info("连接网络打印机，打印点菜小票!");
        log.info("orderInfo::"+orderInfo);
        log.info("dishList::"+dishList);
        if(orderInfo==null){
            log.warn("打印出错：没有订单信息，不打印");
            return;
        }
        if(dishList==null||dishList.size()==0){
            log.warn("打印出错：没有点菜信息，不打印");
            return;
        }

        StringBuilder printData=new StringBuilder("");
        printData.append("");//空两行，留出边距

        printData.append("订单编号："+orderInfo.numInRestaurant+"\n");//订单信息
        printData.append("桌位："+orderInfo.tableInfo?.name+"\n");//订单信息
        printData.append("\n");//空行，留出边距
        if(isFirst){
            printData.append("点菜信息：\n");//点菜信息
        }else{
            printData.append("加菜信息：\n");//点菜信息
        }
        List<DishesInfo> dishesInfoList=dishList;
        int size=dishesInfoList.size();
        for (int i=0;i<size;i++){
            DishesInfo dishesInfo=dishesInfoList.get(i);
            if(StringUtil.isEmpty(dishesInfo.remark)){
                printData.append(dishesInfo.foodName+"\t"+dishesInfo.num+"份\n");//点菜信息
            }else{
                printData.append(dishesInfo.foodName+"("+dishesInfo.remark+")\t"+dishesInfo.num+"份\n");//点菜信息
            }
        }
        printData.append("\n");//空行，留出边距
        Date now=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr=sdf.format(now);
        printData.append("打印时间："+nowStr+"\n");//订单信息

        printData.append("\n\n------------------------\n\n\n");//空4行，留出边距

        def reInfo=shopService.getShopEnabled();
        if(reInfo.recode!=ReCode.OK){
           log.warn("打印出错："+reInfo);
           return;
        }

        log.info("小票信息::"+printData.toString());

        String printIp=reInfo.restaurantInfo.printIp;
        int printPort=reInfo.restaurantInfo.printPort;

        PrintClient printClient=new PrintClient(printIp,printPort);
        if(!printClient.printData(printData.toString())){
            log.warn("打印出错");
        }
    }
}
