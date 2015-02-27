package lj

class TestController {
    def webUtilService;
    def index() {
        render(view:webUtilService.getView("index"),model: []);

        return ;
    }
}
