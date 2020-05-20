$(function () {
    var queryURL = getContextPath();
    console.log(queryURL);
    $table = $('#table_user').bootstrapTable({
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
        sidePagination: "server",
        undefinedText: '--',
        pagination: true,
        // toolbar: '#searchForm',
        //是否要分页
        //是否显示分页
        pageNumber: 1,
        //初始化加载第一页，默认第一页,并记录
        pageSize: 10,                      //默认每页显示多少条记录
        //每页显示的数量
        pageList: [4, 10, 20, 30],

        //表格加载数据的时候会触发该方法,次方法一般用于添加额外参数或者修改参数名
        queryParams: function(params) {
            var status = $("input:hidden[name='status']").val();
            if(status){
                params.status =1;
            }
            delete params.search;
            delete params.sort;
            delete params.order;
            params.pageSize=params.limit;
            delete params.limit;
            params.pageNum=params.offset;
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
                "total": res.count,
                //总页数
                "rows": res.datas //数据
            };
            return res;
        }
    });
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
        $table.bootstrapTable('refresh');
    })

    $("#time").click(function () {
        $table.bootstrapTable('refresh',{query:{"time" : true}});
    })

    $("#fans").click(function () {
        $table.bootstrapTable('refresh',{query:{"fans" : true}});
    })

    $table.on("click","button",function () {
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
})