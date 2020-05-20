$(function () {
    var queryURL = getContextPath();
    var dataFormat = function (time) {
        time = time.slice(0,time.indexOf("T"));
        return time;
    }
    $user_table = $('#all_user').bootstrapTable({
        method: 'GET',
        url:queryURL + "search",
        columns: [
            {
                field: 'user',
                title: '名称',
                sortable: true,
                formatter:userformatter,
            },
            {
                field: 'action',
                title: '操作',
                formatter:actionformatter,
            }],
        classes:'table table-hover',
        showHeader: false,
        rowStyle:function (row,index) {
            return{
                css:{
                    height:'150px',
                }
            }
        },
        uniqueId: 'id',
        cache: false,
        sortable: true,
        sortOrder: "asc",
        undefinedText: '--',
        //表格加载数据的时候会触发该方法,次方法一般用于添加额外参数或者修改参数名
        queryParams: function(params) {
            var status = $("input:hidden[name='status']").val();
            delete params.search;
            delete params.sort;
            delete params.order;
            params.pageSize=1;
            delete params.limit;
            params.pageNum=1;
            params.status=1;
            delete params.offset;

            var nameParam = $("#searchForm").find("input[name='name']").val();
            if(nameParam){
                params.name=nameParam;
            }else {
                params.name = "";
            }
            return params;
        },

        //获取到服务器数据会触发该方法,该方法一般用于处理数据的格式
        responseHandler: function(res) {
            //数据响应回来会触发该方法
            //如果是server分页,数据比如按照如下格式
            //{“rows”:数据,”total”:总的记录数}
            return {
                "total": 1,
                //总页数
                "rows": res.datas //数据
            };
            return res;
        }
    });
    $article_table = $('#all_article').bootstrapTable({
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
        undefinedText: '--',

        //表格加载数据的时候会触发该方法,次方法一般用于添加额外参数或者修改参数名
        queryParams: function(params) {
            var status = $("input:hidden[name='status']").val();

            params.status = 3;
            delete params.search;
            delete params.sort;
            delete params.order;
            params.pageSize=8;
            delete params.limit;
            params.pageNum=1;
            delete params.offset;

            var nameParam = $("#searchForm").find("input[name='name']").val();
            if(nameParam){
                params.name=nameParam;
            }else {
                params.name = "";
            }
            console.log(params)
            return params;
        },

        //获取到服务器数据会触发该方法,该方法一般用于处理数据的格式
        responseHandler: function(res) {
            //数据响应回来会触发该方法
            //如果是server分页,数据比如按照如下格式
            //{“rows”:数据,”total”:总的记录数}
            console.log(123);
            return {
                "total": 8,
                //总页数
                "rows": res.datas //数据
            };
            return res;
        },
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            alert("数据加载失败！");
        }, onLoadSuccess: function () {
        },
        onLoadError: function () {
            alert("数据加载失败！");
        },
    });
    //点击查询
    var searchFun = function(){
        if($("input[name='name']").val()==""){
            window.location.href=getContextPath()+"search";
        }
        $.ajax({
            url:getContextPath() + "search",
            type:"get",
            async:false,
            data:{"status":2,"pageNum":1,"pageSize":5,"name":$("input[name='name']").val()},
            success:function (data) {
                $(".videoList").empty();
                if(data.datas.length != 0){
                    data.datas.forEach(function (element){
                        $(".videoList").append("" +
                            "<li>" +
                            "<a href='"+getContextPath() +"videos/page/VO"+ element.num + "'>" +
                            "<div class='video_box'>"+
                            "<div class='img-box'>" +
                            "<img class='header' src='"+element.header +"' alt=''>" +
                            "</div>" +
                            "<div class='info-box'>" +
                            "<p class='name'>"+element.name+"</p>" +
                            "<span class='clicknum'>"+element.hotNum+"</span>&nbsp;&nbsp;<span class='date'>"+dataFormat(element.createdate)+"</span>" +
                            "<p class='author'>"+element.author.anothername+"</p>" +
                            "</div>" +
                            "</div>" +
                            "</a>" +
                            "</li>");
                    });
                }
            }
        })
    }
    searchFun();

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
    function userformatter(value,row,index) {
        return  "<div class='user_left'>" +
            "<div class='img-box'>" +
            "<img class='img-circle' src='"+row.header+"'>" +
            "</div>"+
            "<div class='info-box'>" +
            "<span class='name'><a href='/UserCenter?id="+row.id+"'>"+row.anothername+"</a></span>"+
            "<div class='fan'>" +
            "<span>粉丝数:"+row.fansNum+"</span>"+
            "</div>"+
            "<div class='num'>" +
            "<span>视频:"+row.videoNum+"</span>"+
            "<span>文章:"+row.articleNum+"</span>"+
            "</div>"+
            "<p class='sign' title='"+row.sign+"'>"+(row.sign==null?"":row.sign)+"</p>"+
            "</div>"+
            "</div>";
    }
    function actionformatter(value,row,index) {
        var flag = $.ajax({
            url:queryURL+"fans/"+row.id,
            type: "POST",
            async:false,
            success:function (data) {
                return data;
            }
        }).responseText;
        var data = $.parseJSON(flag);
        if(data.success){
            if(data.count === 1){
                if(data.datas){
                    //已关注
                    return "<div class='user_right'>"+
                        "<button data-ope='follow' data-id='"+row.id+"'>已关注</button>&nbsp;&nbsp;&nbsp;&nbsp;"+
                        "<a href='/UserCenter?id="+row.id+"'>个人页面</a>" +
                        "</div>";
                }else {
                    return "<div class='user_right'>"+
                        "<button data-ope='follow'  data-id='"+row.id+"'>关注</button>&nbsp;&nbsp;&nbsp;&nbsp;"+
                        "<a href='/UserCenter?id="+row.id+"'>个人页面</a>" +
                        "</div>";
                }
            }else{
                return"<div class='user_right'>"+
                    "<button>个人页面</button>" +
                    "</div>";
            }
        }
    }
    $("#searchForm").on("submit",function(){
        if ($("input[name='name']").val() == ""){
            window.location.href=getContextPath() + "search";
        }
        $user_table.bootstrapTable('refresh');
        $("input:hidden[name='pageNum']").val(0);
        $("input:hidden[name='pageSize']").val(5);
        searchFun();
        $article_table.bootstrapTable('refresh');
    })

    //用户关注
    $user_table.on("click","button",function () {
        //点击关注
        $btn = $(this);
        if($btn.data("ope") == "follow"){
            var user_id = $btn.data("id");
            $.ajax({
                url:"/fans/follow",
                type:"post",
                data:{"user_id":user_id},
                success:function (data) {
                    console.log(data);
                    if(!data.success) {
                        window.location.href = "/login";
                    }else {
                        if($btn.text() == "关注"){
                            $btn.text("已关注");
                            alert("关注成功")
                        }else {
                            $btn.text("关注");
                        }
                    }
                }
            })
        }
    })

    $(".search-body>div>a").click(function () {
        var status = 0;
        if($(this).data("ope") == "to_user"){
            status = 1;
        }else if($(this).data("ope") == "to_video") {
            status = 2;
        }else if($(this).data("ope") == "to_article"){
            status = 3;
        }
        else{
            return;
        }
        var $labels = $(".search-tit-list>ul>li>label");
        $labels.eq(status).addClass("click").parent().siblings().children("label").removeClass("click");
        $labels.siblings().prop("checked",true);
        $("#status").val(status);
        $.ajax({
            url:"/search/toolbar",
            data:$("#searchForm").serialize(),
            type:"get",
            success:function (data) {
                $("#searchForm").unbind();
                $(".part2>.search-body").children().remove()
                $(".part2>.search-body").html(data);
            }
        })
    })
})