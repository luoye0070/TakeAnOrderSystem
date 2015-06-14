package com.lj.taos.jobs



class DailyReportJob {
    def orderDailyReportService;
    static triggers = {
        //simple repeatInterval: 5000l // execute job once in 5 seconds
        //cron name: 'dailyReportJob', cronExpression: "0 30 23 * * ?"
        cron name: 'dailyReportJob', cronExpression: "0 2 2 * * ?"
    }

    def execute() {
        // execute job
        orderDailyReportService.dailyReport();
    }
}
