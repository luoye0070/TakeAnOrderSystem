package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-3-11
 * Time: 下午11:01
 * To change this template use File | Settings | File Templates.
 */
public enum DishesCollectionStatus {
    //初始状态,订单创建完成
    ORIGINAL_STATUS(0,'初始状态'),
    //确认完成
    VERIFYING_STATUS(1,'确认完成');
    public Integer code;
    public String label;
    DishesCollectionStatus(int code,String label){
        this.code=code
        this.label=label
    }
    public static String getLable(Integer code){
        switch (code){
            case ORIGINAL_STATUS.code:
                return  ORIGINAL_STATUS.label;
            case VERIFYING_STATUS.code:
                return  VERIFYING_STATUS.label;
            default:
                return "未知状态"
        }
    }
    public static def getCodeList(){
        return [
                //初始状态,订单创建完成
                ORIGINAL_STATUS.code,
                //确认完成
                VERIFYING_STATUS.code
        ];
    }
}