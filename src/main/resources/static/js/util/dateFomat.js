var dataFormat = function (time) {
    time = time.slice(0,time.indexOf("T"));
    return time;
}
var timeFormat = function (time) {
    time = time.replace("T"," ");
    time = time.slice(0,time.indexOf("."));
    return time;
}