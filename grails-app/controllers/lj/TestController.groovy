package lj

import grails.converters.JSON

class TestController {
    def webUtilService;
    def customerReserveService;
    def index() {
        //render(view:webUtilService.getView("index"),model: []);
        params.dinnerTime="2015-03-16 01:46:00";
        render(customerReserveService.getReserveTable(params) as JSON);
        return ;
    }
}
