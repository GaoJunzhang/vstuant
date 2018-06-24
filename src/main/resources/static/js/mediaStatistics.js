var table;
var tableMedia;
$(document).ready(function () {

    $('.selectData').datepicker({
        autoclose: true, //自动关闭
        language: 'zh-CN',
        beforeShowDay: $.noop,    //在显示日期之前调用的函数
        calendarWeeks: false,     //是否显示今年是第几周
        clearBtn: true,          //显示清除按钮
        daysOfWeekDisabled: [],   //星期几不可选
        endDate: Infinity,        //日历结束日期
        startDate: -Infinity,        //日历结束日期
        forceParse: true,         //是否强制转换不符合格式的字符串
        format: 'yyyy-mm-dd',     //日期格式
        keyboardNavigation: true, //是否显示箭头导航
        language: 'zh-CN',           //语言
        minViewMode: 0,
        orientation: "auto",      //方向
        rtl: false,
        startView: 0,             //开始显示
        todayHighlight: true,    //今天高亮
        weekStart: 0              //星期几是开始
    });
    $.ajax({
        type: "GET",
        url: "/userMedias/yearMediaData",
        data: {year: 2018},
        dataType: "json",
        success: function (res) {
            var d1 = [];
            if (res.statisList.length > 0) {
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
                data: d1,
                bars: {
                    show: true,
                    barWidth: 0.4,
                    order: 1
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
        }
    });
    maruti = {
        flot_tooltip: function(x, y, contents) {

            $('<div id="tooltip">' + contents + '</div>').css( {
                top: y + 5,
                left: x + 5
            }).appendTo("body").fadeIn(200);
        }
    }
    table = $('#datatable').DataTable({
        "dom": '<"top"i>rt<"bottom"flp><"clear">',
        "searching": false,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "serverSide": true,//开启服务器模式，使用服务器端处理配置datatable
        "processing": true,//开启读取服务器数据时显示正在加载中……特别是大数据量的时候，开启此功能比较好
        ajax: function (data, callback, settings) {
            //封装请求参数
            var param = getQueryCondition(data);
            $.ajax({
                type: "GET",
                url: '/userMedias/userMediaStatistics',
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
            {"data": "realyname"},
            {"data": "usedPlayCount"},
            {"data": "vaildPlayCount"},
            {"data": "sumcount"},
            {"data": "playprogress"},
            {"data": "remark"}
        ],
        columnDefs: [
            {"orderable": false, "targets": 1},
            {
                "orderable": false,
                "render": function (data, type, row) {
                    if (data > 0) {

                        // return "<button class=\"btn btn-success  btn-large\" onclick=\"viewUserMediaInfo('" + row.id + "','"+row.username+"')\">" + data + "次</button>";
                        return "<a href='/userMediaInfo?id=" + row.id + "&username=" + row.username + "' class=\"g\"><span class='badge badge-important' style='font-size: 15px'>" + data + "</span></a>";
                    } else {
                        // return "<button class=\"btn btn-danger  btn-large\" onclick=\"viewUserMediaInfo('" + row.id + "','"+row.username+"')\">" + data + "次</button>";
                        // return "<span class='badge'>" + data + "</span>";
                        return "<a href='/userMediaInfo?id=" + row.id + "&username=" + row.username + "' class=\"g\"><span class='badge' style='font-size: 15px'>" + data + "</span></a>";
                    }
                },
                "targets": 2
            }, {
                "orderable": false,
                "render": function (data, type, row) {
                    if (row.isLimit==0){

                        if (data > 0) {

                            return "<span class='badge badge-success'>" + data + "</span>";
                        } else {
                            return "<span class='badge'>" + data + "</span>";
                        }
                    }else {
                        return "<span class=\"label label-info\">无限</span>";
                    }
                },
                "targets": 3
            },
            {
                "orderable": false,
                "render": function (data, type, row) {
                    if (row.isLimit==1){
                        return "<span class=\"label label-info\">无限</span>";
                    }else {
                        return data;
                    }
                },
                "targets": 4
            },
            {
                "orderable": false,
                "render": function (data, type, row) {
                    if (row.isLimit==0){

                        if (row.sumcount > 0) {
                            var vp = (row.vaildPlayCount / row.sumcount).toFixed(2) * 100;
                            if (vp < 30) {
                                return "<div class='progress progress-striped progress-danger active'><div class='bar' style='width: " + vp + "%;'></div></div>";
                            }
                            if (vp >= 30 && vp < 60) {
                                return "<div class='progress progress-striped progress-warning active'><div class='bar' style='width: " + vp + "%;'></div></div>";
                            }
                            if (vp >= 60) {
                                return "<div class='progress progress-striped progress-success active'><div class='bar' style='width: " + vp + "%;'></div></div>";
                            }
                        } else {
                            return data;
                        }
                    }else{
                        return "<div class='progress progress-striped progress-success active'><div class='bar' style='width:100%;'></div></div>";
                    }

                },
                "targets": 5
            },
            {
                "orderable": false,
                "render": function (data, type, row) {
                    if (data != null&&data!='') {
                        return "<button class=\"btn btn-success\" onclick=\"updateRemark('" + row.id + "','"+data+"')\">编辑</button>";
                    } else {
                        return "<button class='btn btn-danger' onclick='updateRemark(" + row.id + ")'>设置</button>";
                    }
                },
                "targets": 6
            }
        ],

    });
    tableMedia = $('#datatableMedia').DataTable({
        "dom": '<"top"i>rt<"bottom"flp><"clear">',
        "searching": false,
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "serverSide": true,//开启服务器模式，使用服务器端处理配置datatable
        "processing": true,//开启读取服务器数据时显示正在加载中……特别是大数据量的时候，开启此功能比较好
        ajax: function (data, callback, settings) {
            //封装请求参数
            var param = getQueryCondition1(data);

            $.ajax({
                type: "GET",
                url: 'medias',
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
            {
                "sClass": "text-center",
                "data": "id",
                "render": function (data, type, full, meta) {
                    return '<input type="checkbox"  class="checkchild"  value="' + data + '" />';
                },
                "bSortable": false
            },
            // {"data": "id"},
            {"data": "name"},
            {"data": "createtime"}
        ],
        columnDefs: [
            {"orderable": false, "targets": 1},
            {"orderable": false, "targets": 2}
        ],

    });

});

function search() {
    table.ajax.reload();
}
function searchMedia() {
    tableMedia.ajax.reload();
}

//封装查询参数
function getQueryCondition(data) {
    var param = {};
    //组装排序参数
    param.username = $("#name-search").val();//查询条件
    param.realyname = $("#realyname-search").val();
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
    param.name = $("#mediaName-search").val();//查询条件
    //组装分页参数
    param.start = data.start;
    param.length = data.length;
    param.draw = data.draw;
    return param;
}

function updateRemark(uid, remark) {
    console.log(uid);
    console.log(remark);
    if ($('#setRemarAuth').val()=='1'){

        $('#remark').val(remark);
        $('#uid').val(uid);
        $('#remarkCount').modal('show');
    }else {
        layer.msg('无设置权限');
    }
}

function setRemark() {
    var ids = new Array();
    for(var i=0;i<$(".checkchild:checked").length;i++){
        ids.push($(".checkchild:checked")[i].value);
    }
    $.ajax({
        cache: true,
        type: "POST",
        url: 'users/updateRemark',
        data: {
            id: $('#uid').val(),
            ids:ids.toString(),
            remark: $('#remark').val()
        },
        async: false,
        success: function (data) {
            if (data == "success") {
                layer.msg('设置成功');
                table.ajax.reload();
                $('#remarkCount').modal('hide');
            } else {
                layer.msg('设置失败');
                // $('#remarkCount').modal('hide');
            }
        }
    });
}
function showUserMessage() {
    $('#userMessageDiv').show();
}
function mediaSearch() {
    $.ajax({
        cache: true,
        type: "GET",
        url: 'userMedias/getUserMediaCount',
        data: {
            mediaName: $("#tmediaName-search").val(),
            startTime:$("#tstartTime-search").val(),
            endTime: $("#tendTime-search").val()
        },
        async: false,
        success: function (data) {
            $("#mediaCount").val(data);
        }
    });

}
