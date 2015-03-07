package lj.taos.report

import lj.data.OrderInfo
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReserveType
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.data.JRMapArrayDataSource
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

class JasperReportService {

    def jasperService
    def webUtilService

    def exportOrderList(def params){
         //获取数据源
        def list=searchOrderList(params)
        //报表名称
        def reportName=params.reportName
        //报表参数
        def parameters=new HashMap()
        parameters.beginDate=params.beginDate
        parameters.endDate=params.endDate

        if(params.reserveType=="-1")
            parameters.reserveType="全部"
        else
            parameters.reserveType= ReserveType.getLabel(params.reserveType)

        if(params.status=="-1")
            parameters.status="全部"
        else
            parameters.status=OrderStatus.getLable(params.status)

        if(params.valid=="-1")
            parameters.valid="全部"
        else
            parameters.valid=OrderValid.getLable(params.valid)

        //locale
        def locale=params.locale
        println "list======="+list
        return export(list,reportName,parameters,locale)
    }

    def searchOrderList(def params){

        def session=webUtilService.getSession();

        def restaurantId=webUtilService.getStaff()?.restaurantId
        if(!restaurantId){
            return []
        }

        long userId=session.userId as Long
        if(!userId){
            return []
        }

        int reserveType=(params.reserveType!=null?params.reserveType:-1)  as int
        int status=(params.status!=null?params.status:-1) as int
        int valid=(params.valid?params.valid:-1) as int

        def sql="""
               select new Map(
                   orderInfo.id as orderID,orderInfo.userName as userName,SUBSTRING(orderInfo.date,1,10) as date,SUBSTRING(orderInfo.time,12,19) as time,
                   case when orderInfo.valid=0 then '初始状态'  when orderInfo.valid=1 then '有效' when orderInfo.valid=2 then '用户取消' when orderInfo.valid=3 then '饭店取消'  end as valid,
                   case when orderInfo.status=0 then '初始状态' when orderInfo.status=1 then '点菜完成' when orderInfo.status=2 then '确认点菜完成'
                   when orderInfo.status=3 then '上菜完成' when orderInfo.status=4 then '运送中'
                   when orderInfo.status=5 then '结账完成' when orderInfo.status=6 then '评价完成' else '' end as status,
                   case when orderInfo.reserveType=1 then '早餐' when orderInfo.reserveType=2 then '午餐' when orderInfo.reserveType=3 then '晚餐' when orderInfo.reserveType=0 then '非预定订单' else '未知的预定类型' end as reserveType,
                   orderInfo.totalAccount as total,    orderInfo.tableName as table,
                   dishesInfo.foodName as foodName,dishesInfo.num as num,
                   case when dishesInfo.status=0 then '初始状态' when dishesInfo.status=1 then '确认完成' when dishesInfo.status=2 then '做菜中'
                        when dishesInfo.status=3 then '做菜完成' when dishesInfo.status=4 then '上菜完成' end as status1,
                   case when dishesInfo.valid=0 then '初始状态' when dishesInfo.valid=1 then '有效' when dishesInfo.valid=2 then '用户取消'
                        when dishesInfo.valid=3 then '饭店在订单确认前取消' when dishesInfo.valid=4 then '饭店在订单确认后取消' end as valid1,
                   dishesInfo.foodPrice as price

               )

               from OrderInfo orderInfo ,DishesInfo dishesInfo
               where  orderInfo.id = dishesInfo.orderId
               ${params.beginDate?" and   orderInfo.date >= '${params.beginDate}'":""}
               ${params.endDate?" and   orderInfo.date <= '${params.endDate}'":""}
               ${reserveType >=0?" and orderInfo.reserveType=${reserveType}":""}
               ${status >=0?" and orderInfo.status=${status}":""}
               ${valid >=0?" and orderInfo.valid=${valid}":""}
              order by orderInfo.id



       """

        return OrderInfo.executeQuery(sql)
    }

    //list数据源，reportName报表名称,format导出格式，locale本地化
    def export={list,reportName,parameters,locale->
        //将list转为jasper数据源
        JRDataSource reportData = new JRMapArrayDataSource(list.toArray())
        parameters._file=reportName
        parameters._format="XLS"
        JasperReportDef report = jasperService.buildReportDefinition(parameters,locale,reportData)
        return report
    }

}
