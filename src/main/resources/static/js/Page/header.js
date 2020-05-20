/*JQuery 如有用VUE则需要去修改了*/
$(function(){
    /*投稿按钮显示*/
    $(".contribute-btn").parent().hover(function(){
        $(".Contribute-list").css({"display":"block"});
    },function () {
        $(".Contribute-list").removeAttr("style");
    })

    var scrollLen = 0;
    $(window).scroll(function () {
        if($(this).scrollTop()>scrollLen){
            $("header").css({"height":"0px","overflow":"hidden"});
        }else{
            $("header").removeAttr("style").css({"position":"fixed"});
        }
        if($(this).scrollTop() == 0){
            $("header").css({"background":"none"});
        }else {
            $("header").css({"background":"rgba(205,205,205,.8)"});
        }
        scrollLen = $(this).scrollTop();
    })
})

