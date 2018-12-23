layui.use(['form', 'layedit', 'laydate','carousel'], function(){
    var form = layui.form
        ,layer = layui.layer
        ,layedit = layui.layedit
        ,laydate = layui.laydate;


    var carousel = layui.carousel;
    //建造实例
    carousel.render({
        elem: '#test1'
        ,width: '100%' //设置容器宽度
        ,arrow: 'always' //始终显示箭头
        //,anim: 'updown' //切换动画方式
    });


//监听提交
    form.on('submit(demo1)', function(data){
        $.ajax({
			//http://www.xqdiary.top/wy
            type: 'post',
            url:"http://www.xqdiary.top/wy/userCtrl/getid",
            data: $('#formData').serialize(),
            dataType:'json',
            success:function(msg){
                if(msg[0].code==1){
                    $("#url").attr("value",msg[0].Uurl)
                }else if(msg[0].code==-1){

                }else{
                    layer.alert("输入正确的id")
                }
            },
            error:function(){
                layer.alert("网络错误")
            }

        });
        return false;
    });




});
function do2(){
    var id=window.document.getElementById("mID").value
    id = id.replace(/\s*/g,"");
    window.location.href = 'http://www.xqdiary.top/wy/userCtrl/downmp3?musicID='+id;
    return false;
}