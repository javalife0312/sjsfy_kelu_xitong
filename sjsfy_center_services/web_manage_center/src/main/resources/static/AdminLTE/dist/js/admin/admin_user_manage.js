$(function() {
    var datatable = null
    initTableData()


    /**
     * 显示列表信息
     */
    function initTableData() {
        $.ajax({
            async:false,
            url:'/admin/user/listusers',
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
                                "targets": 4,
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
                                "targets": 5,
                                "render": function ( data, type, row ) {
                                    var btn_opt =
                                        "<div class='btn-group'>"+
                                            "<button type='button' class='btn btn-box-tool dropdown-toggle' data-toggle='dropdown'>操作<i class='fa fa-wrench'></i></button>"+
                                            "<ul class='dropdown-menu' role='menu'>"+
                                                "<li><a href='javascript:void(0);' class='btn_jgw_update' data-id='"+row.id+"'>修改</a></li>"+
                                                "<li><a href='javascript:void(0);' class='btn_jgw_delete' data-id='"+row.id+"'>删除</a></li>"+
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
        $('#username').val('')
        $('#password').val('')
        $('#showname').val('')
        $('#status').val(1)
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
            url:'/admin/user/listusers_id',
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
                        $('#username').val(report.username)
                        $('#password').val(report.password)
                        $('#showname').val(report.showname)
                        $('#status').val(report.status)
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
        var username = $('#username').val()
        var password = $('#password').val()
        var showname = $('#showname').val()
        var status = $('#status').val()

        $.ajax({
            async:true,
            url:'/admin/user/save_or_update',
            type:'get',
            dataType:'json',
            data:{
                "id" : id,
                "username" : username,
                "password" : password,
                "showname" : showname,
                "status" : status
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