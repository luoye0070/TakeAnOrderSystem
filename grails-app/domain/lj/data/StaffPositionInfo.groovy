package lj.data

//员工职位
class StaffPositionInfo {
    //员工
    StaffInfo staffInfo
    //职位
    int positionType

    static constraints = {
        staffInfo(nullable:false);
        positionType(nullable:false,min:0);
    }
}
