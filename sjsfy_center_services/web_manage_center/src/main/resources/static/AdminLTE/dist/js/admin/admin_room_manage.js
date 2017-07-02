$(function() {
    var nodes = null;
    var edges = null;
    var network = null;

    draw()

    function destroy() {
        if (network !== null) {
            network.destroy();
            network = null;
        }
    }

    /**
     * 将类型和图形绑定
     * @param type
     */
    function type2shape(type) {
        if(type==1){
            return 'database'
        }else if(type==2){
            return 'box'
        }else if(type==3){
            return 'ellipse'
        }else if(type==4){
            return 'circle'
        }
    }

    /**
     * 将类型和图形绑定
     * @param type
     */
    function generate_label(host,desc) {
        return desc + "\n" + host
    }

    function draw() {
        destroy();
        nodes = [];
        edges = [];
        $.ajax({
            async:true,
            url:'/admin/room/list',
            type:'get',
            dataType:'json',
            data:{},
            success:function (json) {
                if(json.code==0){
                    if(json.data && json.data.length>0){
                        for(var i=0;i<json.data.length;i++){
                            var report = json.data[i]
                            edges.push({to: report.id, from: report.parent_room_id,arrows:'to'})
                            nodes.push({id: report.id, label: generate_label(report.room_host,report.room_desc),shape:type2shape(report.room_type)});
                        }
                        var container = document.getElementById('mynetwork');
                        var data = {
                            nodes: nodes,
                            edges: edges
                        }
                        var options = {
                            layout: {
                                hierarchical: {
                                    sortMethod: 'directed',
                                    direction: 'LR'
                                }
                            },
                            edges: {
                                smooth: {
                                    type: 'cubicBezier',
                                    forceDirection: 'horizontal',
                                    roundness: 0.4
                                },
                                arrows: {to : true }
                            }
                        };
                        network = new vis.Network(container, data, options);
                    }

                    // add event listeners
                    network.on('select', function (params) {
                        document.getElementById('selection').innerHTML = 'Selection: ' + params.nodes;
                        var id = params.nodes[0]
                        $.ajax({
                            async:true,
                            url:'/admin/room/list_id',
                            type:'get',
                            dataType:'json',
                            data:{
                                id : id
                            },
                            success:function (json) {
                                if(json.code==0){
                                    if(json.data && json.data.length>0){
                                        var report = json.data[0]
                                        $('#parent_room_id').val(report.id)
                                        $('#desc_id').val(report.id)
                                        $('#desc_room_host').val(report.room_host)
                                        $('#desc_room_desc').val(report.room_desc)
                                        $('#desc_room_type').val(report.room_type)
                                        $('#desc_device_host').val(report.device_host)
                                        $('#desc_device_username').val(report.device_username)
                                        $('#desc_device_password').val(report.device_password)
                                        $('#show_desc_modal').modal()
                                    }
                                }
                            }
                        })
                    })
                }
            }
        })
    }

    /**
     * 提交子设备保存
     */
    $('#desc_btn_update').on('click',function () {
        $.ajax({
            async:true,
            url:'/admin/room/update',
            type:'get',
            dataType:'json',
            data:{
                "id" : $('#desc_id').val(),
                "room_host" : $('#desc_room_host').val(),
                "room_desc" : $('#desc_room_desc').val(),
                "room_type" : $('#desc_room_type').val(),
                "device_host" : $('#desc_device_host').val(),
                "device_username" : $('#desc_device_username').val(),
                "device_password" : $('#desc_device_password').val()
            },
            success:function (json) {
                if(json.code==0){
                    $('#show_desc_modal').modal('hide')
                    draw()
                }
            }
        })
    })
    /**
     * 提交子设备保存
     */
    $('#desc_btn_save_panel').on('click',function () {
        $('#show_desc_modal').modal('hide')
        $('#myModal').modal()
    })

    /**
     * 提交子设备保存
     */
    $('#btn-submit').on('click',function () {
        $.ajax({
            async:true,
            url:'/admin/room/save',
            type:'get',
            dataType:'json',
            data:{
                "parent_room_id" : $('#parent_room_id').val(),
                "room_host" : $('#room_host').val(),
                "room_desc" : $('#room_desc').val(),
                "room_type" : $('#room_type').val(),
                "device_host" : $('#device_host').val(),
                "device_username" : $('#device_username').val(),
                "device_password" : $('#device_password').val()
            },
            success:function (json) {
                if(json.code==0){
                    draw()
                    $('#myModal').modal('hide')
                }
            }
        })
    })


    /**
     * 添加字节点
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
})
