package com.lj.taos.jobs



class MarkExpireJob {
    def orderAndReserveService;
    static triggers = {
        //simple repeatInterval: 5000l // execute job once in 5 seconds
        cron name: 'MarkExpireJob', cronExpression: "0 0 0 * * ?"
    }

    def execute() {
        // execute job
        orderAndReserveService.markExpire();
    }
}
