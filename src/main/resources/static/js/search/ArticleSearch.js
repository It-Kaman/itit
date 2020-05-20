
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
    var queryURL = getContextPath();
    $table = $('#table_article').bootstrapTable({
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
        // toolbar: '#searchForm',
        //是否要分页
        //是否显示分页
        pageNumber: 1,
        //初始化加载第一页，默认第一页,并记录
        pageSize: 15,                      //默认每页显示多少条记录
        //每页显示的数量
        pageList: [4, 10, 15, 30],

        //表格加载数据的时候会触发该方法,次方法一般用于添加额外参数或者修改参数名
        queryParams: function(params) {
            var status = $("input:hidden[name='status']").val();
            if(status){
                params.status =status;
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

    $("#searchForm").on("submit",function(){
        if($("input[name='name']").val() == ""){
            window.location.href = getContextPath() + "search";
        }
        $table.bootstrapTable('refresh');
    })
    $("#time").click(function () {
        $table.bootstrapTable('refresh',{query:{"time" : true}});
    })
    $("#hot").click(function () {
        $table.bootstrapTable('refresh',{query:{"hot" : true}});
    })
})