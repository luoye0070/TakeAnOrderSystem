package lj.taos.show

class SearchController {
    def searchService;


    //菜单搜索
    def searchFood(){
        def err=null;
        def msg=null;
        def reInfo=[:];
        //if(request.method=="POST"){
        if(params.inShop){
            params.max=20;
        }
        else{
            params.max=28;
        }

            params.enabled=true;
            reInfo<<searchService.searchFood(params);
        //}
        println("reInfo-->"+reInfo);
        if(params.inShop)
            render(view: "searchFoodInShop",model: reInfo);
        else if(params.showPlace=="page")
            render(view: "searchFoodInPage",model: reInfo);
        else
            render(view: "searchFood",model: reInfo);
    }
}
