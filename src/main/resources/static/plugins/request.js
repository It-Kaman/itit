/**
 * 请求js工具类
 */
var request = function() {};

/**
 * ajax请求
 * @param URL              请求地址
 * @param method           请求方法 
 * @param data             请求数据
 * @param async            是否异步
 * @param callBack         处理回调函数
 */
request.ajax=function(URL,method,data,async,callBack){
	var param = data ? data : {}; 
	
	//处理DELETE请求和PUT的请求的
	if(typeof(param)==="string"){
		param = param+"&_method="+method;     
	}else{
		param._method=method;
	}

	$.ajax({
        cache: true,
        type: method=="get"?"get":"post",
        url:URL,
        data:param,
        async: async,
        success: function(data) {
        	callBack(data);
        	
        	//common.ajaxTreeTip(data,'','');
        	//关闭对话框
            //提示操作信息
        	//刷新指定节点
        }
    });
}