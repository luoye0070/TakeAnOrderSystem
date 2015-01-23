package lj.enumCustom

//订单有效性
public enum OrderValid {

    //初始状态，待确认其有效性
    ORIGINAL_VALID(0,'初始状态'),
    //有效
    EFFECTIVE_VALID(1,'有效'),
    //用户取消
    USER_CANCEL_VALID(2,'用户取消'),
    //饭店取消
    RESTAURANT_CANCEL_VALID(3,'饭店取消')
    public Integer code
    public String label
    OrderValid(Integer code,String label){
        this.code=code
        this.label=label
    }
    public static def getCodeList(){
        return [
                //初始状态，待确认其有效性
                ORIGINAL_VALID.code,
                //有效
                EFFECTIVE_VALID.code,
                //用户取消
                USER_CANCEL_VALID.code,
                //饭店取消
                RESTAURANT_CANCEL_VALID.code
        ];
    }

    public static String getLable(Integer code){
        switch (code){
            case ORIGINAL_VALID.code:
                return  ORIGINAL_VALID.label;
            case EFFECTIVE_VALID.code:
                return  EFFECTIVE_VALID.label;
            case USER_CANCEL_VALID.code:
                return  USER_CANCEL_VALID.label;
            case RESTAURANT_CANCEL_VALID.code:
                return  RESTAURANT_CANCEL_VALID.label;
            default:
                return "未知有效性"
        }
    }
    public static OrderValid[] valids=[
            //初始状态，待确认其有效性
            ORIGINAL_VALID,
            //有效
            EFFECTIVE_VALID,
            //用户取消
            USER_CANCEL_VALID,
            //饭店取消
            RESTAURANT_CANCEL_VALID
    ];
}
