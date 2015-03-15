package lj.enumCustom

//订单状态
public enum ReserveOrderStatus {

    //初始状态,订单创建完成
    ORIGINAL_STATUS(0,'初始状态'),
    //点菜完成
    REACHED_STATUS(1,'已到店');

    public int code
    public String label

    ReserveOrderStatus(Integer code,String label){
        this.code=code
        this.label=label
    }
    public static String getLable(Integer code){
        switch (code){
            case ORIGINAL_STATUS.code:
                return  ORIGINAL_STATUS.label;
            case REACHED_STATUS.code:
                return  REACHED_STATUS.label;
            default:
                return "未知状态"
        }
    }
    public static def getCodeList(){
        return [
                ORIGINAL_STATUS.code,
                //点菜完成
                REACHED_STATUS.code
        ];
    }
    public static ReserveOrderStatus[] statuses=[
            //初始状态,订单创建完成
            ORIGINAL_STATUS,
            //点菜完成
            REACHED_STATUS
    ];
}
