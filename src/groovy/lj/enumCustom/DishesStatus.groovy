package lj.enumCustom

public enum DishesStatus {
    //初始状态,订单创建完成
    ORIGINAL_STATUS(0,'初始状态'),
    //确认完成
    VERIFYING_STATUS(1,'确认完成'),
    //做菜中
    COOKING_ORDERED_STATUS(2,'做菜中'),
    //做菜完成
    COOKED_STATUS(3,'做菜完成'),
    //上菜完成
    SERVED_STATUS(4,'上菜完成');

    public Integer code;
    public String label;
    DishesStatus(Integer code,String label){
        this.code=code
        this.label=label
    }
    public static String getLable(Integer code){
        switch (code){
            case ORIGINAL_STATUS.code:
                return  ORIGINAL_STATUS.label;
            case VERIFYING_STATUS.code:
                return  VERIFYING_STATUS.label;
            case COOKING_ORDERED_STATUS.code:
                return  COOKING_ORDERED_STATUS.label;
            case COOKED_STATUS.code:
                return  COOKED_STATUS.label;
            case SERVED_STATUS.code:
                return  SERVED_STATUS.label;
            default:
                return "未知状态"
        }
    }
    public static def getCodeList(){
        return [
                //初始状态,订单创建完成
                ORIGINAL_STATUS.code,
                //确认完成
                VERIFYING_STATUS.code,
                //做菜中
                COOKING_ORDERED_STATUS.code,
                //做菜完成
                COOKED_STATUS.code,
                //上菜完成
                SERVED_STATUS.code
        ];
    }
}