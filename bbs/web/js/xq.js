function detailInfo() {
    var obj2=eval("("+$("#detailInfo").val()+")");
    var cov_200=(obj2.cov_200==0)?"不投保":"足额投保";
    var cov_600=(obj2.cov_600==0)?"不投保":obj2.cov_600;
    var cov_500=(obj2.cov_500==0)?"不投保":"投保："+obj2.cov_500;
    var cov_701=(obj2.cov_701==0)?"不投保":obj2.cov_701+"/每座";
    var cov_702=(obj2.cov_702==0)?"不投保":obj2.cov_702+"/每座";
    var cov_321=(obj2.cov_321==0)?"不投保":"投保";
    var cov_310=(obj2.cov_310==0)?"不投保":"投保："+obj2.cov_310;
    var cov_210=(obj2.cov_210==0)?"不投保":"投保："+obj2.cov_210;
    var cov_291=(obj2.cov_291==0)?"不投保":"投保";
    var cov_640=(obj2.cov_640==0)?"不投保":obj2.cov_640+"/每座";
    var cov_921=(obj2.cov_921==0)?"不投保":"投保";
    var cov_922=(obj2.cov_922==0)?"不投保":"投保";
    var cov_911=(obj2.cov_911==0)?"不投保":"投保";
    var cov_912=(obj2.cov_912==0)?"不投保":"投保";
    var cov_928=(obj2.cov_928==0)?"不投保":"投保";
    var cov_929=(obj2.cov_929==0)?"不投保":"投保";
    var cov_231="";
    if(obj2.cov_231==0){
        cov_231="不投保"
    }else if(obj2.cov_231==1){
        cov_231="国产玻璃"
    }else if(obj2.cov_231==2){
        cov_231="进口玻璃"
    }
    $("#cov_200").val(cov_200);
    $("#cov_600").val(cov_600);
    $("#cov_500").val(cov_500);
    $("#cov_701").val(cov_701);
    $("#cov_702").val(cov_702);
    $("#cov_321").val(cov_321);
    $("#cov_310").val(cov_310);
    $("#cov_210").val(cov_210);
    $("#cov_291").val(cov_291);
    $("#cov_640").val(cov_640);
    $("#cov_921").val(cov_921);
    $("#cov_922").val(cov_922);
    $("#cov_911").val(cov_911);
    $("#cov_912").val(cov_912);
    $("#cov_928").val(cov_928);
    $("#cov_929").val(cov_929);
    $("#cov_231").val(cov_231);
}