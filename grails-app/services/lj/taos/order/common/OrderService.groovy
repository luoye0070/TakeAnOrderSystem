package lj.taos.order.common

class OrderService {

    def printDishes(def orderInfo,def dishList) {
        log.info("连接网络打印机，打印点菜小票!");
        log.info("orderInfo::"+orderInfo);
        log.info("dishList::"+dishList);
    }
}
