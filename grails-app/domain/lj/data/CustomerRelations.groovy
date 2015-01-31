package lj.data

//客户关系表
class CustomerRelations {
    ClientInfo customerClient;//顾客客户端
    Integer type=0;//客户类型
    static constraints = {
        customerClient(nullable: false);//用户ID
        type(nullable: false);//客户类型
    }
}
