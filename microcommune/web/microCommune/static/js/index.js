$(document).ready(function(){
    var leftHeight=$(".mainContern").height();
    $(".sildLeft").css("height",leftHeight);
});


//添加点击空白处关闭弹出框事件
function addEvent(obj,eventType,func){
	if(obj.attachEvent){obj.attachEvent("on" + eventType,func);}
	else{obj.addEventListener(eventType,func,false)}
}
function clickother(el){
	thisObj = el.target?el.target:event.srcElement;
    if(thisObj.tagName == "BODY"){
        document.getElementById("accountSwitching").style.display = "none";
        return;
    }
    if(thisObj.id == "accountSwitching"||thisObj.id == "weixinNow"||(thisObj.parentNode).parentNode.parentNode.id=="accountSwitching"){
        return;
    }
    do{
        if(thisObj.tagName == "BODY"){
            if(document.getElementById("accountSwitching")){
                document.getElementById("accountSwitching").style.display = "none";
                return;
            }
        };
        thisObj = thisObj.parentNode;
    }while(thisObj.parentNode);
}
//背景变黑弹出窗口
function showBlackPage(clarity,tipword){
    var bWidth='100%';
    var bHeight=$("BODY").height()+87;
    //var bHeight=$(".content").offset().top+$(".content").height()+19;
    // var wWidth = 602;
    //var left = bWidth/2-wWidth/2-19;
    var back=document.createElement("div");
    back.id="blackbackcommon";
    var styleStr="top:0px;left:0;position:absolute;background:#000;z-index:21;width:"+bWidth+";height:"+bHeight+"px;opacity:0.2;";
    //styleStr+=(isIe)?"filter:alpha(opacity=0);":"opacity:0;";
    back.style.cssText=styleStr;
    document.body.appendChild(back);
    showBackground(back,clarity);
    var mesW=document.createElement("div");
    mesW.id="blackbackCommonWindow";
//    mesW.innerHTML = $("#promptedShows")[0].innerHTML;
    var src = "/static/images/face.jpg";
    mesW.innerHTML="<div class='prompteds' id='promptedShows'>" +
        "<div class='topWord'><span class='topleft'>详细信息</span><span class='topright'>信息</span></div>" +
        "<div class='message'>" +
        "<table border='1px;'>" +
        "<tr><td rowspan='3' style='width: 100px;'><img src="+src+"></td><td>bb1</td></tr>" +
        "<tr><td>bb2</td></tr>" +
        "<tr><td>bb3</td></tr>" +
        "<tr><td colspan='2' style='text-align: center;'><hr style='color: red;'/>bb4<hr style='color: red;'/></td></tr>" +
        "</table>" +
        "</div>" +
        "<div><a class='buttonblue changeSaveButton' href='javascript:closeBlackBackground();'>发消息</a>" +
        "</div>" +
        "</div>" ;
    document.body.appendChild(mesW);
    $(".tipWord").html(tipword);
    var popWidth = parseInt($("#promptedShow").css("width"))+60;
    $("#promptedShow").css("margin-left",-(popWidth/2));
    $(".buttonblue")[0].focus();
}
http://weixintool.com/change_info.html
// 显示弹出背景
    function showBackground(obj,endInt){
        var al=parseFloat(obj.style.opacity);al+=0.1;
        obj.style.opacity=al;
        if(al<(endInt/100)){setTimeout(function(){showBackground(obj,endInt)},1);}
    }
function closeBlackBackground(){
    $("#blackbackCommonWindow").remove();
    $("#blackbackcommon").remove();
}
function f1()
{
    //alert($(".mainContern").height());
    var leftHeight=$(".mainContern").height();
    $(".sildLeft").css("height",leftHeight);
}
function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function() {
            if (oldonload) {
                oldonload();
            }
            func();
        }
    }
}
addLoadEvent(f1);