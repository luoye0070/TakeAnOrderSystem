/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-6-6
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
$(function(){
   var as=$("a[confirm]");
   as.each(function(){
       var url=$(this).attr("href");
       var confirmMessage=$(this).attr("confirm");
       if(confirmMessage==""){
           confirmMessage="你确定要执行该操作吗？";
       }
       $(this).attr("href","#");
       $(this).click(function(){
           if(confirm(confirmMessage))
           {
               location.href=url;
           }
       });
   });
});
