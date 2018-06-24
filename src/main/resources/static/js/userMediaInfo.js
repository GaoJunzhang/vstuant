/**
 * Created by user on 2018/5/22.
 */
var table;
var table1;
$(document).ready(function(){
    $.ajax({
        type: "GET",
        url: "/userMedias/myUserMediaData",
        data: {uid:$("#userId").val(),username:$("#username").val()},
        dataType: "json",
        success: function(res){
            var data = [];
            if (res.isLimit==0){

                data[1] = { label: "已播"+res.sumPlayCount, data: Math.floor((res.sumPlayCount/res.sumCount)*100)+1 }
                data[0] = { label: "可播"+(res.sumCount-res.sumPlayCount), data: Math.floor(((res.sumCount-res.sumPlayCount)/res.sumCount)*100)+1 }
                data[2] = { label: "总数"+res.sumCount }
                var pie = $.plot($(".pie"), data,{
                    series: {
                        pie: {
                            show: true,
                            radius: 3/4,
                            label: {
                                show: true,
                                radius: 3/4,
                                formatter: function(label, series){
                                    return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'+label+'<br/>'+Math.round(series.percent)+'%</div>';
                                },
                                background: {
                                    opacity: 0.5,
                                    color: '#000'
                                }
                            },
                            innerRadius: 0.2
                        },
                        legend: {
                            show: false
                        }
                    }

                });
            }
            var d1 = [];
            // for (var i = 0; i <= res.statisList; i += 1) d1.push([i, parseInt(Math.random() * 30)]);
            if (res.statisList.length>0){
                d1.push([1, parseInt(res.statisList[0].Jans)]);
                d1.push([2, parseInt(res.statisList[0].Febs)]);
                d1.push([3, parseInt(res.statisList[0].Mars)]);
                d1.push([4, parseInt(res.statisList[0].Aprs)]);
                d1.push([5, parseInt(res.statisList[0].Mays)]);
                d1.push([6, parseInt(res.statisList[0].Juns)]);
                d1.push([7, parseInt(res.statisList[0].Juls)]);
                d1.push([8, parseInt(res.statisList[0].Augs)]);
                d1.push([9, parseInt(res.statisList[0].Septs)]);
                d1.push([10, parseInt(res.statisList[0].Octs)]);
                d1.push([11, parseInt(res.statisList[0].Novs)]);
                d1.push([12, parseInt(res.statisList[0].Dects)]);
            }

            var data = new Array();
            data.push({
                data:d1,
                bars: {
                    show: true,
                    barWidth: 0.4,
                    order: 1,
                }
            });


            //Display graph
            var bar = $.plot($(".bars"), data, {
                grid: { hoverable: true, clickable: true },
                legend: true
            });
            $(".bars").bind("plothover", function (event, pos, item) {
                if (item) {
                    console.log(item)
                    if (previousPoint != item.dataIndex) {
                        previousPoint = item.dataIndex;

                        $('#tooltip').fadeOut(200,function(){
                            $(this).remove();
                        });
                        var x = item.datapoint[0],
                            y = item.datapoint[1];

                        maruti.flot_tooltip(item.pageX, item.pageY, x + " 月: " + y+"次");
                    }

                } else {
                    $('#tooltip').fadeOut(200,function(){
                        $(this).remove();
                    });
                    previousPoint = null;
                }
            });
            if (res.isLimit==1){
                $("#staticPid").hide();
                $("#staticPidUnlimit").show();
                $("#playLimit").html("无限播放");
            }else {
                $("#playLimit").html(res.sumCount+"次");
            }
        }
    });
    table = $('#datatable').DataTable({
        "dom": '<"top"i>rt<"bottom"flp><"clear">',
        "searching": false,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "serverSide": true,//开启服务器模式，使用服务器端处理配置datatable
        "processing": true,//开启读取服务器数据时显示正在加载中……特别是大数据量的时候，开启此功能比较好
        //"ajax": '${ss}/user/userList.do',
        ajax: function (data, callback, settings) {
            //封装请求参数
            var param = getQueryCondition(data);

            $.ajax({
                type: "GET",
                url: '/userMedias/myUserMedias',
                cache: false,  //禁用缓存
                data: param,    //传入已封装的参数
                dataType: "json",
                success: function (result) {
                    //封装返回数据  如果参数相同，可以直接返回result ，此处作为学习，先这么写了。
                    var returnData = {};
                    returnData.draw = result.draw;//这里直接自行返回了draw计数器,应该由后台返回
                    returnData.recordsTotal = result.recordsTotal;//总记录数
                    returnData.recordsFiltered = result.recordsFiltered;//后台不实现过滤功能，每次查询均视作全部结果
                    returnData.data = result.data;
                    //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                    //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                    callback(returnData);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("查询失败");
                }
            });
        },
        "columns": [

            // {"data": "id"},
            {"data": "username"},
            {"data": "mediaName"},
            {"data": "playtime"}
        ],
        columnDefs: [
            {"orderable": false, "targets": 0},
            {"orderable": false, "targets": 1},
            {"orderable": false, "targets": 2}
        ],

    });

    table1 = $('#datatable1').DataTable({
        "dom": '<"top"i>rt<"bottom"flp><"clear">',
        "searching": false,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "serverSide": true,//开启服务器模式，使用服务器端处理配置datatable
        "processing": true,//开启读取服务器数据时显示正在加载中……特别是大数据量的时候，开启此功能比较好
        //"ajax": '${ss}/user/userList.do',
        ajax: function (data, callback, settings) {
            //封装请求参数
            var param = getQueryCondition1(data);

            $.ajax({
                type: "GET",
                url: '/userMedias/myMediasPlayInfo',
                cache: false,  //禁用缓存
                data: param,    //传入已封装的参数
                dataType: "json",
                success: function (result) {
                    //封装返回数据  如果参数相同，可以直接返回result ，此处作为学习，先这么写了。
                    var returnData = {};
                    returnData.draw = result.draw;//这里直接自行返回了draw计数器,应该由后台返回
                    returnData.recordsTotal = result.recordsTotal;//总记录数
                    returnData.recordsFiltered = result.recordsFiltered;//后台不实现过滤功能，每次查询均视作全部结果
                    returnData.data = result.data;
                    //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                    //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                    var totalPlay = 0;
                    for (var i=0;i<result.data.length;i++){
                        totalPlay += Number(result.data[i].usedPlayCount);
                    }
                    $("#sumPlayCount").html(totalPlay);
                    callback(returnData);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("查询失败");
                }
            });
        },
        "columns": [

            // {"data": "id"},
            {"data": "mediaName"},
            {"data": "usedPlayCount"},
        ],
        columnDefs: [
            {"orderable": false, "targets": 0},
            {"orderable": false, "targets": 1}
        ],

    });


});
function search() {
    table.ajax.reload();
}

//封装查询参数
function getQueryCondition(data) {
    var param = {};
    //组装排序参数
    param.uid = $("#userId").val();
    param.username = $("#name-search").val();//查询条件
    param.startTime = $("#startTime-search").val();//查询条件
    param.endTime = $("#endTime-search").val();//查询条件
    //组装分页参数
    param.start = data.start;
    param.length = data.length;
    param.draw = data.draw;
    return param;
}//封装查询参数
function getQueryCondition1(data) {
    var param = {};
    //组装排序参数
    param.uid = $("#userId").val();
    param.username = $("#name-search").val();//查询条件
    param.startTime = $("#startTime-search").val();//查询条件
    param.endTime = $("#endTime-search").val();//查询条件
    //组装分页参数
    param.start = data.start;
    param.length = data.length;
    param.draw = data.draw;
    return param;
}
maruti = {
    // === Tooltip for flot charts === //
    flot_tooltip: function(x, y, contents) {

        $('<div id="tooltip">' + contents + '</div>').css( {
            top: y + 5,
            left: x + 5
        }).appendTo("body").fadeIn(200);
    }
}


