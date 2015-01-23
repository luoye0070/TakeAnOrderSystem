package lj.enumCustom

public enum ReserveType {

    MORNING(1,'早餐'),
    NOON(2,'午餐'),
    NIGHT(3,'晚餐'),
    NOT_RESERVE(0,"非预定订单"),
    NOT_KNOW(-1,"未知的预定类型")

    public Integer code
    public String label
    ReserveType(Integer code,String label){
        this.code=code
        this.label=label
    }
    public static ReserveType getReserveType(int code){
        switch (code){
            case 1:
                return MORNING;
            case 2:
                return NOON;
            case 3:
                return NIGHT;
            case 0:
                return NOT_RESERVE;
            default:
                return NOT_KNOW;
        }
    }
    public static String getLabel(int code){
        switch (code){
            case 1:
                return MORNING.label;
            case 2:
                return NOON.label;
            case 3:
                return NIGHT.label;
            case 0:
                return NOT_RESERVE.label;
            default:
                return NOT_KNOW.label;
        }
    }

    public static ReserveType[] reserveTypes=[MORNING,NOON,NIGHT];
}
