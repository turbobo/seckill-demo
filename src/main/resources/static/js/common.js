//展示loading
function g_showLoading() {
    var idx = layer.msg('处理中...',{icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: '0px',
    time: 5000});
    return idx;
}

//salt
var g_password_salt = "1a2b3c4d"
//获取url参数
function g_getQueryString(name){
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)
        return unescape(r[2]);
    return null;
};
//设定时间格式化函数，使用new Date().format("yyyy-MM-dd HH:mm:ss");
/*Date.prototype.format = function(format) {
    var args = {
        'M+': this.getMonth() + 1,
        'd+': this.getDate(),
        'H+': this.getHours(),
        'm+': this.getMinutes(),
        's+': this.getSeconds()
    }
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr( 4 - RegExp.$1.length));
    }
    for (var i in args) {
        var n = args[i];
        if (new RegExp("(" + i + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1 == 1 ? n : ("00" + n).substr(("" + n).length));
        }
        return format;
    }
    return format;
}*/

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

