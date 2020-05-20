var dataFormat = function (time) {
    time = time.slice(0,time.indexOf("T"));
    return time;
}
var timeFormat = function (time) {
    time = time.replace("T"," ");
    time = time.slice(0,time.indexOf("."));
    return time;
}


$(function () {
    var pageSize = 25;
    var pageNum = 0;
    var MaxPageNum = 1;
    //点击查询
    var searchFun = function(){
        if($("input[name='name']").val()==""){
            window.location.href=getContextPath()+"search";
        }
        $.ajax({
            url:getContextPath() + "search",
            type:"get",
            async:false,
            data:$("#searchForm").serialize(),
            success:function (data) {
                $(".videoList").empty();
                data.datas.forEach(function (element){
                    $(".videoList").append("" +
                        "<li>" +
                            "<a href='"+getContextPath() + "videos/page/VO"+element.num + "'>" +
                                "<div class='video_box'>"+
                                    "<div class='img-box'>" +
                                        "<img class='header' src='"+element.header +"' alt=''/>" +
                                    "</div>" +
                                    "<div class='info-box'>" +
                                        "<p class='name'>"+element.name+"</p>" +
                                        "<span class='clicknum'>点击量:"+(element.hotNum==null?'0':element.hotNum)+"</span>&nbsp;&nbsp;<span class='date'>"+dataFormat(element.createdate)+"</span>"+
                                        "<p class='author'>"+element.author.anothername+"</p>" +
                                    "</div>" +
                                "</div>" +
                            "</a>" +
                        "</li>");
                });
                $(".page_num").text(data.MaxPageNum);
            }
        })
    }
    $("#searchForm").on("submit",function () {
        $("input:hidden[name='pageNum']").val(0);
        $("input:hidden[name='pageSize']").val(25);
        searchFun();

        MaxPageNum = $(".page_num").text();
        pageinationTool(1,Number(MaxPageNum));
        pagination();
    })
    var pageinationTool = function (num,maxPageSize) {
        var index = num;
        var minIndex = index - 2;
        var maxIndex = 2+ Number(index);
        if(minIndex <= 0){
            minIndex = 1;
            if (maxIndex > maxPageSize){
                maxIndex = maxPageSize;
            }
        }else{
            if(maxIndex > maxPageSize){
                maxIndex = maxPageSize;
            }
        }
        //遍历分页
        $(".pagination>li").not($(".pagination>li:first")).not($(".pagination>li:last")).remove();
        if(index != maxIndex){
            $(".pagination>li:last").removeClass("disabled");
        }else {
            $(".pagination>li:last").addClass("disabled");
        }

        if(index != minIndex){
            $(".pagination>li:first").removeClass("disabled");
        }else {
            $(".pagination>li:first").addClass("disabled");
        }
        for(var i = maxIndex; i >= minIndex ;i--){
            if(i == index){
                $("<li class='active'><a href='javascript:;'>"+i+"</a></li>").insertAfter($(".pagination>li:first"));
            }else {
                $("<li><a href='javascript:;'>"+i+"</a></li>").insertAfter($(".pagination>li:first"));
            }
        }
        $(".content-list").empty();
    }
    //分页点击
    var pagination = function(){
        //分页工具栏
        $(".pagination-box>.pagination>li").click(function (event) {
            //判断当前点击的页
            if(!$(this).hasClass("disabled")|| !$(this).hasClass("active")){
                var index = $(this).text();
                if($(this).hasClass("last")){
                    index = MaxPageNum;
                }else if($(this).hasClass("first")){
                    index = 1;
                }
                $("input:hidden[name='pageNum']").val((index-1)*25+1);
                searchFun();
                pageinationTool(index,MaxPageNum);
            }
            event.preventDefault();
            pagination();
        })
    }

    $("a#time,a#hotNum").click(function () {
        $("input#time").remove();
        if($(this).attr("id") === "time"){
            $("#pageNum").before("<input type='hidden' form='searchForm' name='time' id='time' value='true'/>");
        }
        searchFun();
        MaxPageNum = $(".page_num").text();
        pageinationTool(1,Number(MaxPageNum));
    })
    $("input:hidden[name='pageNum']").val(0);
    $("input:hidden[name='pageSize']").val(25);
    searchFun();
    MaxPageNum = $(".page_num").text();
    pageinationTool(1,Number(MaxPageNum));
    pagination();
})