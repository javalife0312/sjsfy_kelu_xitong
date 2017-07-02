$(function() {
    var datatable = null
    var tongji_tag = 'total'

    initDashboard()
    initTableData()

    /**
     * 初始化DashBoard
     */
    function initDashboard() {
        $.ajax({
            async:true,
            url:'/admin/camera/tongji',
            type:'get',
            dataType:'json',
            data:{},
            success:function (json) {
                console.log(json)
                if(json.code==0){
                    if(json.data && json.data.length>0){
                        var report = json.data[0]
                        $('#show_dash_total_span').html(report.total)
                        $('#show_dash_success_span').html(report.online)
                        $('#show_dash_fail_span').html(report.offline)
                        $('#show_dash_doing_span').html(report.sleeping)
                    }
                }
            }
        })
    }
    /**
     * 统计 - 表格联动
     */
    $('#show_dash_total_div').on('click',function () {
        tongji_tag = 'total'
        $('#table_info').html('总计明细')
        initTableData()
    })
    $('#show_dash_success_div').on('click',function () {
        tongji_tag = 'online'
        $('#table_info').html('在线明细')
        initTableData()
    })
    $('#show_dash_fail_div').on('click',function () {
        tongji_tag = 'offline'
        $('#table_info').html('离线明细')
        initTableData()
    })
    $('#show_dash_doing_div').on('click',function () {
        tongji_tag = 'sleeping'
        $('#table_info').html('休眠明细')
        initTableData()
    })


    /**
     * 显示列表信息
     */
    function initTableData() {
        $.ajax({
            async:false,
            url:'/admin/camera/listcamera',
            type:'get',
            dataType:'json',
            data:{
                tongji_tag : tongji_tag
            },
            success:function (json) {
                if(json.code == 0){
                    if(datatable){
                        datatable.destroy();
                    }
                    datatable = $('#example2').DataTable({
                        searching: true,
                        data: json.data,
                        columns: json.titles,
                        pageLength: 100,
                        paging: false,
                        columnDefs:[
                            {
                                "targets": 3,
                                "render": function ( data, type, row ) {
                                    if(data==1){
                                        return '正常'
                                    }else {
                                        return '异常'
                                    }
                                }
                            },
                            {
                                "targets": 4,
                                "render": function ( data, type, row ) {
                                    var btn_opt =
                                        "<div class='btn-group'>"+
                                            "<button type='button' class='btn btn-box-tool dropdown-toggle' data-toggle='dropdown'>操作<i class='fa fa-wrench'></i></button>"+
                                            "<ul class='dropdown-menu' role='menu'>"+
                                                "<li><a href='javascript:void(0);' class='btn_jgw_update' data-id='"+row.sjs_deviceIP+"'>修改</a></li>"+ "</ul>"+
                                        "</div>"
                                    return btn_opt
                                }
                            }
                        ]
                    });
                }
            }
        })
    }

    /**
     * 修改操作
     * 修改的操作要更新模态
     */
    $('#btn_jgw_new').on('click',function () {
        $('#id').val('')
        $('#username').val('')
        $('#password').val('')
        $('#device_host').val('')
        $('#status').val(0)
        $('#myModal').modal()
    })
    /**
     * 修改操作
     * 修改的操作要更新模态
     */
    $('#example2').on('click','.btn_jgw_update',function () {
        var id = $(this).attr('data-id')
        $.ajax({
            async:true,
            url:'/admin/camera/listcamera_ip',
            type:'get',
            dataType:'json',
            data:{
                "sjs_deviceIP" : id
            },
            success:function (json) {
                if(json.code==0){
                    if(json.data && json.data.length>0){
                        var report = json.data[0]
                        $('#sjs_deviceFaguan').val(report.sjs_deviceFaguan)
                        $('#sjs_deviceUsrname').val(report.sjs_deviceUsrname)
                        $('#sjs_devicePwd').val(report.sjs_devicePwd)
                        $('#sjs_deviceIP').val(report.sjs_deviceIP)
                        $('#myModal').modal()
                    }
                }
            }
        })
    })

    $('#btn-submit').on('click',function () {
        $.ajax({
            async:true,
            url:'/admin/camera/update',
            type:'get',
            dataType:'json',
            data:{
                "sjs_deviceIP" : $('#sjs_deviceIP').val(),
                "sjs_deviceFaguan" : $('#sjs_deviceFaguan').val(),
                "sjs_deviceUsrname" : $('#sjs_deviceUsrname').val(),
                "sjs_devicePwd" : $('#sjs_devicePwd').val(),
            },
            success:function (json) {
                if(json.code==0){
                    initTableData()
                    initDashboard()
                    $('#myModal').modal('hide')
                }
            }
        })

    })
});