package lj.enumCustom

//订单状态
public enum OrderStatus {

    //初始状态,订单创建完成
    ORIGINAL_STATUS(0,'初始状态'),
    //点菜完成
    ORDERED_STATUS(1,'点菜完成'),
    //确认点菜完成
    VERIFY_ORDERED_STATUS(2,'确认点菜完成'),
    //上菜完成
    SERVED_STATUS(3,'上菜完成'),
    //运送中
    SHIPPING_STATUS(4,'运送中'),
    //结账完成
    CHECKOUTED_STATUS(5,'结账完成'),
    //评价完成
    APPRAISED_STATUS(6,"评价完成");

    public Integer code
    public String label
    OrderStatus(Integer code,String label){
        this.code=code
        this.label=label
    }
    public static String getLable(Integer code){
        switch (code){
            case ORIGINAL_STATUS.code:
                return  ORIGINAL_STATUS.label;
            case ORDERED_STATUS.code:
                return  ORDERED_STATUS.label;
            case VERIFY_ORDERED_STATUS.code:
                return  VERIFY_ORDERED_STATUS.label;
            case SERVED_STATUS.code:
                return  SERVED_STATUS.label;
            case SHIPPING_STATUS.code:
                return  SHIPPING_STATUS.label;
            case CHECKOUTED_STATUS.code:
                return  CHECKOUTED_STATUS.label;
            case APPRAISED_STATUS.code:
                return  APPRAISED_STATUS.label;
            default:
                return "未知状态"
        }
    }
    public static def getCodeList(){
        return [
                ORIGINAL_STATUS.code,
                //点菜完成
                ORDERED_STATUS.code,
                //确认点菜完成
                VERIFY_ORDERED_STATUS.code,
                //上菜完成
                SERVED_STATUS.code,
                //运送中
                SHIPPING_STATUS.code,
                //结账完成
                CHECKOUTED_STATUS.code,
                //评价完成
                APPRAISED_STATUS.code
        ];
    }
    public static OrderStatus[] statuses=[
            //初始状态,订单创建完成
            ORIGINAL_STATUS,
            //点菜完成
            ORDERED_STATUS,
            //确认点菜完成
            VERIFY_ORDERED_STATUS,
            //上菜完成
            SERVED_STATUS,
            //运送中
            SHIPPING_STATUS,
            //结账完成
            CHECKOUTED_STATUS,
            //评价完成
            APPRAISED_STATUS
    ];
}
