$(function () {
    var pageSize = 25;
    var pageNum = 0;
    var MaxPageNum = 1;
    var showTag = function(){
        $.ajax({
            url: "/tags",
            type: "post",
            success: function (data) {
                $(".value-box").empty();
                $(".tagTables>ul").empty();
                if (data.success) {
                    data.datas.forEach(function (element) {
                        $(".value-box").append("<input type='checkbox' id='column' name='column' value='" + element.id + "' form='searchForm'/>" + element.name);
                        $(".tagTables>ul").append("<li><span>" + element.name + "</span></li>");
                        $(".tagList>ul").append("<li><span>" + element.name + "</span></li>");
                    })
                } else {
                    alert("系统繁忙，请稍候再试");
                }
                clickTag();

                $("input[name='column']").each(function (i,e) {
                    if($(e).val() == $(".value-box").data("tags")){
                        $("input[name='column']").eq(i).prop("checked",true);
                        $(".user-select>li").eq(i).addClass("active");
                    }
                })
                $("input:hidden[name='pageNum']").val(pageNum);
                $("input:hidden[name='pageSize']").val(pageSize);
                searchFun();
                MaxPageNum = $(".page_num").text();
                pageinationTool(1,Number(MaxPageNum));
                pagination();
                $table.bootstrapTable("refresh");
            }
        })
    }
    showTag();


    //点击事件---点击tag标签
    var clickTag = function () {
        $(".tagList>ul>li>span").click(function () {
            var index = $(this).parent().index();
            var checkbox = $(".value-box>input").eq(index);
            console.log(checkbox)
            if (checkbox.get(0).checked) {
                checkbox.get(0).checked = false;
                $(this).parent().removeClass("active");
            } else {
                checkbox.get(0).checked = true;
                $(this).parent().addClass("active");
            }
        })
    }


    //点击查询
    var searchFun = function(){
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
        $(".pagination-box>.pagination>li").not($(".pagination-box>.pagination>li:first")).not($(".pagination-box>.pagination>li:last")).remove();
        if(index != maxIndex){
            $(".pagination-box>.pagination>li:last").removeClass("disabled");
        }else {
            $(".pagination-box>.pagination>li:last").addClass("disabled");
        }

        if(index != minIndex){
            $(".pagination-box>.pagination>li").removeClass("disabled");
        }else {
            $(".pagination-box>.pagination>li").addClass("disabled");
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
                console.log(MaxPageNum,123,index);
                searchFun();
                pageinationTool(index,MaxPageNum);
            }
            event.preventDefault();
            pagination();
        })
    }

    var queryURL = getContextPath();
    $table = $('#tag_Article').bootstrapTable({
        method: 'GET',
        url:queryURL + "search",
        columns: [{
            article: 'article',
            formatter:articleformatter,
        }],
        classes:'table table-hover',
        showHeader: false,
        rowStyle:function (row,index) {
            return{
                css:{
                    height:'100px',
                }
            }
        },
        uniqueId: 'id',
        cache: false,
        sortable: true,
        sortOrder: "asc",
        sidePagination: "server",
        undefinedText: '--',
        pagination: true,
        pageNumber: 1,
        pageSize: 15,
        pageList: [4, 10, 15, 30],
        queryParams: function(params) {
            delete params.search;
            delete params.sort;
            delete params.order;
            params.pageSize=params.limit;
            delete params.limit;
            params.pageNum=params.offset;
            delete params.offset;
            params.tagStatus = 1;
            params.status = 4;
            var nameParam = $("#searchForm").find("input[name='name']").val();
            if(nameParam){
                params.name=nameParam;
            }else {
                params.name = "";
            }
            var tags = [];
            $("input[name='column']:checked").each(function (i,e) {
                tags[i] = Number($(e).val());
            });
            if (tags){
                params.articleColumn = tags.join(",");
            }
            return params;
        },
        responseHandler: function(res) {
            console.log(res.datas);
            return {
                "total": res.count,
                //总页数
                "rows": res.datas //数据
            };
            return res;
        }
    });
    function articleformatter(value,row,index) {
        var tagsName = [];
        row.tags.forEach(function (element,index) {
            tagsName[index] = element.name;
        })
        return "<article class='article-box'>" +
            "<a href='"+getContextPath()+"articles/page/AC"+row.num+"'>"+
            "<h3 class='article-title'>"+row.name+"</h3>" +
            "<p class='description' title='"+row.description+"'>"+row.description+"</p>" +
            "<div class='info-box'>" +
            "<span class='clickNum'>点击数:"+(row.clickNum==null?"0":row.clickNum)+"</span><span class='author'>"+row.author.anothername+"</span>" +
            "<span class='tags' title='"+tagsName.join(',')+"'>"+tagsName.join(',')+"</span>" +
            "<span class='createdate'>"+dataFormat(row.createdate)+"</span>" +
            "</div>" +
            "</a>"+
            "</article>";
    }

    $("#searchForm").on("submit",function () {
        $("input:hidden[name='pageNum']").val(pageNum);
        $("input:hidden[name='pageSize']").val(pageSize);
        searchFun();

        MaxPageNum = $(".page_num").text();
        pageinationTool(1,Number(MaxPageNum));
        pagination();
        $table.bootstrapTable('refresh');
    })
})

var dataFormat = function (time) {
    time = time.slice(0,time.indexOf("T"));
    return time;
}
var timeFormat = function (time) {
    time = time.replace("T"," ");
    time = time.slice(0,time.indexOf("."));
    return time;
}
$("a#seeVideo,a#seeArticle").click(function () {
    console.log($(this).attr("id"));
    if($(this).attr("id")== "seeVideo"){
        $(".video-list-box").removeAttr("style");
        $(".article-list-box").css("display","none");
    }
    else {
        $(".article-list-box").removeAttr("style");
        $(".video-list-box").css("display","none");
    }
    return;
})