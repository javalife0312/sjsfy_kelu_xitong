$(function() {
    var datatable = null
    initTableData()


    /**
     * 显示列表信息
     */
    function initTableData() {
        $.ajax({
            async:false,
            url:'/admin/center/listinfos',
            type:'get',
            dataType:'json',
            data:{},
            success:function (json) {
                if(json.code == 0){
                    if(datatable){
                        datatable.destroy();
                    }
                    console.log(json.data)
                    console.log(json.titles)
                    datatable = $('#example2').DataTable({
                        searching: true,
                        data: json.data,
                        columns: json.titles,
                        pageLength: 100,
                        paging: false,
                        columnDefs:[
                            {
                                "targets": 8,
                                "render": function ( data, type, row ) {
                                    if(data==0){
                                        return '初始化'
                                    }else if(data==-1){
                                        return '废弃'
                                    }else if(data==1){
                                        return '使用中'
                                    }
                                }
                            },
                            {
                                "targets": 11,
                                "render": function ( data, type, row ) {
                                    var btn_opt =
                                        "<div class='btn-group'>"+
                                            "<button type='button' class='btn btn-box-tool dropdown-toggle' data-toggle='dropdown'>操作<i class='fa fa-wrench'></i></button>"+
                                            "<ul class='dropdown-menu' role='menu'>"+
                                                "<li><a href='javascript:void(0);' class='btn_jgw_update' data-id='"+row.id+"'>修改</a></li>"+
                                                // "<li><a href='javascript:void(0);' class='btn_jgw_delete' data-id='"+row.id+"'>删除</a></li>"+
                                        "</ul>"+
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
        $('#fating_desc').val('')
        $('#faguan_code').val('')
        $('#faguan_name').val('')

        $('#shexiangji_ip').val('')
        $('#shexiangji_desc').val('')
        $('#shexiangji_username').val('')
        $('#shexiangji_password').val('')
        $('#shexiangji_zhuangtai').val(1)

        $('#diannaozhuji_ip').val('')
        $('#diannaozhuji_panfu').val('')

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
            url:'/admin/center/list_by_id',
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
                        $('#fating_desc').val(report.fating_desc)
                        $('#faguan_code').val(report.faguan_code)
                        $('#faguan_name').val(report.faguan_name)

                        $('#shexiangji_ip').val(report.shexiangji_ip)
                        $('#shexiangji_desc').val(report.shexiangji_desc)
                        $('#shexiangji_username').val(report.shexiangji_username)
                        $('#shexiangji_password').val(report.shexiangji_password)
                        $('#shexiangji_zhuangtai').val(report.shexiangji_zhuangtai)

                        $('#diannaozhuji_ip').val(report.diannaozhuji_ip)
                        $('#diannaozhuji_panfu').val(report.diannaozhuji_panfu)

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

        var id = $('#id').val()
        var fating_desc = $('#fating_desc').val()
        var faguan_code = $('#faguan_code').val()
        var faguan_name = $('#faguan_name').val()

        var shexiangji_ip = $('#shexiangji_ip').val()
        var shexiangji_desc = $('#shexiangji_desc').val()
        var shexiangji_username = $('#shexiangji_username').val()
        var shexiangji_password = $('#shexiangji_password').val()
        var shexiangji_zhuangtai = $('#shexiangji_zhuangtai').val()

        var diannaozhuji_ip = $('#diannaozhuji_ip').val()
        var diannaozhuji_panfu = $('#diannaozhuji_panfu').val()

        $.ajax({
            async:true,
            url:'/admin/center/save_or_update',
            type:'get',
            dataType:'json',
            data:{
                "id" : id,
                "fating_desc" : fating_desc,
                "faguan_code" : faguan_code,
                "faguan_name" : faguan_name,
                "shexiangji_ip" : shexiangji_ip,
                "shexiangji_desc" : shexiangji_desc,
                "shexiangji_username" : shexiangji_username,
                "shexiangji_password" : shexiangji_password,
                "shexiangji_zhuangtai" : shexiangji_zhuangtai,
                "diannaozhuji_ip" : diannaozhuji_ip,
                "diannaozhuji_panfu" : diannaozhuji_panfu
            },
            success:function (json) {
                if(json.code==0){
                    initTableData()
                    $('#myModal').modal('hide')
                }
            }
        })

    })
});