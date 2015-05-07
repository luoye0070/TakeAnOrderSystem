<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
  <title>编辑菜单</title>
</head>
<body>

<link rel="stylesheet" href="${resource(dir:"js/jwysiwyg-0.97",file:"jquery.wysiwyg.css")}" type="text/css" media="screen" />
<script type="text/javascript" src="${resource(dir:"js/jwysiwyg-0.97",file:"jquery.wysiwyg.js")}"></script>
<script type="text/javascript" src="${resource(dir:"js/jwysiwyg-0.97/controls",file:"wysiwyg.image.js")}"></script>
<script type="text/javascript" src="${resource(dir:"js/jwysiwyg-0.97/controls",file:"wysiwyg.link.js")}"></script>
<script type="text/javascript" src="${resource(dir:"js/jwysiwyg-0.97/controls",file:"wysiwyg.table.js")}"></script>

<script type="text/javascript">
    /*<![CDATA[*/
    $(function()
    {
        $('#description').wysiwyg({
            initialContent: "",
            controls: {
                bold          : { visible : true },
                italic        : { visible : true },
                underline     : { visible : true },
                strikeThrough : { visible : true },

                justifyLeft   : { visible : true },
                justifyCenter : { visible : true },
                justifyRight  : { visible : true },
                justifyFull   : { visible : true },

                indent  : { visible : true },
                outdent : { visible : true },

                subscript   : { visible : true },
                superscript : { visible : true },

                undo : { visible : true },
                redo : { visible : true },

                insertOrderedList    : { visible : true },
                insertUnorderedList  : { visible : true },
                insertHorizontalRule : { visible : true },
                colorpicker:{visible:true},
                h4: {
                    visible: true,
                    className: 'h4',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h4>' : 'h4',
                    tags: ['h4'],
                    tooltip: 'Header 4'
                },
                h5: {
                    visible: true,
                    className: 'h5',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h5>' : 'h5',
                    tags: ['h5'],
                    tooltip: 'Header 5'
                },
                h6: {
                    visible: true,
                    className: 'h6',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h6>' : 'h6',
                    tags: ['h6'],
                    tooltip: 'Header 6'
                },
                insertImage:{visible:true},
                cut   : { visible : true },
                copy  : { visible : true },
                paste : { visible : true },
                html  : { visible: true },
                increaseFontSize : { visible : true },
                decreaseFontSize : { visible : true },
            },
            events: {
                click: function(event) {
                    if ($("#click-inform:checked").length > 0) {
                        event.preventDefault();
                        alert("You have clicked jWysiwyg content!");
                    }
                }
            }
        });
        $('#description').wysiwyg("addControl",
                "insertImageFromImageSpace",
                {
                    icon: "${resource(dir:"js/jwysiwyg-0.97",file:"insert_img_ico.gif")}",
                    exec:  function() {
                        //this.insertHtml('<abbr title="exam">Jam</abbr>');
                        //alert('Hello World');
                        $("#imageSelect").attr('src','${createLink(controller: "imageSpace",action: "selectImage",params: [callBack:"insertImage"])}');
                        $("#ratyService").modal();
                        //$('#description').wysiwyg('insertImage', '${resource(dir:"js/jwysiwyg-0.97",file:"insert_img_ico.gif")}');
                    }
                }
        );
        //$("#ratyService").modal();
    });
    function insertImage(imgUrl){
        $('#description').wysiwyg('insertImage', '${createLink(controller: "imageShow", action: "download")}?imgUrl='+imgUrl);
    }
    function selectImage(){
        $("#imageSelect").attr('src','${createLink(controller: "imageSpace",action: "selectImage",params: [callBack:"showImage"])}');
        $("#ratyService").modal();
    }
    function showImage(imgUrl){
       $("#imageHidden").val(imgUrl);
       $("#imageShow").attr('src','${createLink(controller: "imageShow", action: "downloadThumbnail")}?imgUrl='+imgUrl);
    }
    /*]]>*/
</script>

<style type="text/css">
.mc_main {
    width: 1000px;
    height: auto;
    margin: 0px 50px;
    background-color: #FFFFFF;
    float: left;
}
</style>

<body>
<div class="mc_main">
    <div  class="span10" style="margin-left: 10px;margin-top: 10px;">

        %{--<g:render template="../layouts/shopMenu"/>--}%
        <g:render template="../layouts/msgs_and_errors"></g:render>

        <form method="post" action="editFoodInfo"  class="form-horizontal">
            <g:hiddenField name="foodId" value="${foodInfoInstance?.id}" />
            <g:hiddenField name="version" value="${foodInfoInstance?.version}" />
            <fieldset class="form">
                <g:render template="form"/>
            </fieldset>
            <div  class="form-actions">
                <a href="${createLink(controller: "foodManage", action: "foodList")}" class="btn send_btn">取消</a>
                <g:submitButton name="create"  value="${message(code: 'default.button.create.label', default: 'Create')}" class="btn send_btn"/>
            </div>
        </form>


     </div>
    </div>



<!--rating modal's content-->
<div id="ratyService" class="modal hide fade">

    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h3>选择图片</h3>
    </div><!--Modal header-->
    <div class="modal-body">
        <iframe id="imageSelect" src="${createLink(controller: "imageSpace",action: "selectImage",params: [callBack:"insertImage"])}"
        width="500px" height="500px"></iframe>
    </div><!--Modal body-->
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal" >Close</a>
        %{--<a href="#" class="btn btn-primary">Save changes</a>--}%
    </div><!--Modal footer-->
</div> <!--Modal-->
</body>
</html>