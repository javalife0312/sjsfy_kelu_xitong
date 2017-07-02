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
            url:'/admin/kelu/tongji',
            type:'get',
            dataType:'json',
            data:{},
            success:function (json) {
                console.log(json)
                if(json.code==0){
                    if(json.data && json.data.length>0){
                        var report = json.data[0]
                        $('#show_dash_total_span').html(report.total)
                        $('#show_dash_success_span').html(report.success)
                        $('#show_dash_fail_span').html(report.fail)
                        $('#show_dash_doing_span').html(report.doing)
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
        tongji_tag = 'success'
        $('#table_info').html('刻录成功明细')
        initTableData()
    })
    $('#show_dash_fail_div').on('click',function () {
        tongji_tag = 'fail'
        $('#table_info').html('刻录失败明细')
        initTableData()
    })
    $('#show_dash_doing_div').on('click',function () {
        tongji_tag = 'doing'
        $('#table_info').html('进行中明细')
        initTableData()
    })


    /**
     * 显示列表信息
     */
    function initTableData() {
        $.ajax({
            async:false,
            url:'/admin/kelu/listkelu',
            type:'get',
            dataType:'json',
            data:{
                tongji_tag : tongji_tag
            },
            success:function (json) {
                console.log(json)
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
                                "targets": 7,
                                "render": function ( data, type, row ) {
                                    if(data==-1){
                                        return '刻录失败'
                                    }else if(data==1){
                                        return '刻录成功'
                                    }else if(data==2){
                                        return '录像中'
                                    }else if(data==3){
                                        return '暂停录像'
                                    }else if(data==4){
                                        return '录像完成-数据上传中'
                                    }else if(data==5){
                                        return '视频上传成功'
                                    }else if(data==6){
                                        return '刻录-任务生成'
                                    }else {
                                        return '--'
                                    }
                                }
                            },
                            {
                                "targets": 8,
                                "render": function ( data, type, row ) {
                                    var btn_opt =
                                        "<div class='btn-group'>"+
                                            "<button type='button' class='btn btn-box-tool dropdown-toggle' data-toggle='dropdown'>操作<i class='fa fa-wrench'></i></button>"+
                                            "<ul class='dropdown-menu' role='menu'>"+
                                                "<li><a href='javascript:void(0);' class='btn_jgw_update' data-id='"+row.id+"'>修改</a></li>"+ "</ul>"+
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
            url:'/admin/kelu/listkelu_id',
            type:'get',
            dataType:'json',
            data:{
                "id" : id
            },
            success:function (json) {
                if(json.code==0){
                    if(json.data && json.data.length>0){
                        var report = json.data[0]
                        $('#id').val(report.id)
                        $('#status').val(report.status)
                        $('#luxiang_start').val(report.luxiang_start)
                        $('#luxiang_end').val(report.luxiang_end)
                        $('#kelu_start').val(report.kelu_start)
                        $('#kelu_end').val(report.kelu_end)
                        $('#device_host').val(report.device_host)
                        $('#myModal').modal()
                    }
                }
            }
        })
    })

    /**
     * 修改操作
     * 修改的操作要更新模态
     */
    $('#example2').on('click','.btn_jgw_delete',function () {
        var id = $(this).attr('data-id')
        $.ajax({
            async:true,
            url:'/admin/user/listusers_delete_id',
            type:'get',
            dataType:'json',
            data:{
                "id" : id
            },
            success:function (json) {
                if(json.code==0){
                    initTableData()
                }
            }
        })
    })

    $('#btn-submit').on('click',function () {
        $.ajax({
            async:true,
            url:'/admin/kelu/update',
            type:'get',
            dataType:'json',
            data:{
                "id" : $('#id').val(),
                "status" : $('#status').val()
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