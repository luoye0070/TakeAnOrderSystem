package lj

import grails.converters.JSON

class TestController {
    def webUtilService;
    def customerReserveOrderService;
    def index() {
        //render(view:webUtilService.getView("index"),model: []);
        params.dinnerTime="2015-03-16 01:46:00";
        render(customerReserveOrderService.getReserveTable(params) as JSON);
        return ;
    }
}
