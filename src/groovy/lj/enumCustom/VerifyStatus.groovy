package lj.enumCustom

public enum  VerifyStatus {

    //审核中
    PROCESSING(0,'审核中'),
    //审核未通过
    NOTPASS(1,'审核未通过'),
    //审核通过
    PASS(2,'审核通过')

    public Integer code
    public String label
    VerifyStatus(Integer code,String label){
        this.code=code
        this.label=label
    }
    //根据code获取label
    public static String getLabel(int code){
        String label="未知的审核状态";
        switch (code){
            case 0:
                label="审核中";
                break;
            case 1:
                label="审核未通过";
                break;
            case 2:
                label="审核通过";
                break;
        }
        return label;
    }
}
