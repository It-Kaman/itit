//普通用户/教师用户注册页面表单验证等等通用js

/*滚动头部显示*/
/*取消默认方法*/
$("a").click(function(event){
    event.preventDefault();
})
//获取相关协约
$("section>.register-content>.commit-before>span>a").click(function(){
    var num = this.id;
    if (num === "user"){
        $(".user-agree").css({"display":"block"});
        $(".black-ball").css({"display":"block"});
    }else if(num == "teacher"){
        $(".teacher-agree").css({"display":"block"});
        $(".black-ball").css({"display":"block"});
    }else{
        $(".default-agree").css({"display":"block"});
        $(".black-ball").css({"display":"block"});
    }
})
//    关闭协约
$(".agree-thing>i.guanbi").click(function () {
    $(this).parent().css({"display":"none"});
    $(".black-ball").css({"display":"none"});
})


/*验证注册表单*/
$(".form-group>input").on("change",function () {
    var flag =false;
    if ($(this).val() == "" || $(this).val().trim() == ""){
        console.log($(this).parent().children("label"));
        $(this).parent().children("div.alert").eq(0).css({"display":"block"});
        flag = false;
    }else {
        $(this).parent().children("div.alert").eq(0).css({"display":"none"});
        flag = true;

        /*用户名*/
        var Reg1 = /[0-9a-z]{6,15}/ig;
        var username = $(".form-group>input").eq(0).val();
        if(username.trim().match(Reg1) == null || username.trim().match(/[ ]+/) != null){
            $(".form-group>input").eq(0).parent().children("div.alert").eq(0).css({"display":"block"});
            flag = false
        }else {
            $(".form-group>input").eq(0).parent().children("div.alert").eq(0).css({"display":"none"});
            $.ajax({
                url:"/register/check",
                type:"post",
                contentType:"application/json;charset=UTF-8",
                data:JSON.stringify({"username":username}),
                success:function (data) {
                    if(data != "success"){
                        $(".form-group>input").eq(0).parent().children("div.alert").eq(1).css({"display":"block"});
                        flag=false;
                    }else {
                        $(".form-group>input").eq(0).parent().children("div.alert").eq(1).css({"display":"none"});
                        flag = true;
                    }
                }
            })
        }


        var Reg2 = /[0-9a-z]{8,16}/ig;
        var password = $(".form-group>input").eq(1).val();
        if(password.trim().match(Reg2) == null || password.trim().match(/[ ]+/) != null){
            $(".form-group>input").eq(1).parent().children("div.alert").eq(0).css({"display":"block"});
            flag = false;
        }else {
            $(".form-group>input").eq(1).parent().children("div.alert").eq(0).css({"display":"none"});
            flag = true;
        }

        var anothername = $(".form-group>input").eq(2).val();
        if(anothername.trim().length > 10 || anothername.trim().length<1 ){
            $(".form-group>input").eq(2).parent().children("div.alert").eq(0).css({"display":"block"});
            flag = false;
        }else{
            $(".form-group>input").eq(2).parent().children("div.alert").eq(0).css({"display":"none"});
            flag = true;
        }

        var reg3 =/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        var email = $(".form-group>input").eq(3).val();
        if(email.trim().match(reg3) == null || email.trim().match(/[ ]+/) != null){
            $(".form-group>input").eq(3).parent().children("div.alert").eq(0).css({"display":"block"});
            flag = false;
        }else {
            $(".form-group>input").eq(3).parent().children("div.alert").eq(0).css({"display":"none"});

            $.ajax({
                url:"/register/check",
                type:"post",
                contentType:"application/json;charset=UTF-8",
                data:JSON.stringify({"email":email}),
                success:function (data) {
                    if(data != "success"){
                        $(".form-group>input").eq(3).parent().children("div.alert").eq(1).css({"display":"block"});
                        flag=false;
                    }else{
                        $(".form-group>input").eq(3).parent().children("div.alert").eq(1).css({"display":"none"});
                        flag = true;
                    }
                }
            })
        }

        var reg5 = /^1[3456789]\d{9}$/;
        var phone = $(".form-group>input").eq(4).val();
        if(phone.trim().match(reg5) == null || phone.trim().match(/[ ]+/) != null){
            $(".form-group>input").eq(4).parent().children("div.alert").eq(0).css({"display":"block"});
            flag = false;
        }else {
            console.log(123);
            $(".form-group>input").eq(4).parent().children("div.alert").eq(0).css({"display":"none"});
            //判断当前手机号是否已存在
            $.ajax({
                url:"/register/check",
                type:"post",
                contentType:"application/json;charset=UTF-8",
                data:JSON.stringify({"phone":phone}),
                success:function (data) {
                    if(data != "success"){
                        $(".form-group>input").eq(4).parent().children("div.alert").eq(1).css({"display":"block"});
                        flag=false;
                    }else {
                        $(".form-group>input").eq(4).parent().children("div.alert").eq(1).css({"display":"none"});
                        flag = true;
                    }
                }
            })
        }


        if(flag == true){
            $(".commit").removeAttr("disabled");
        }else {
            $(".commit").attr("disabled","");
        }
    }

})


//判断是否同意协约
function isAgree() {
    if($(".agree")[0].checked == false){
        return false;
    }
}