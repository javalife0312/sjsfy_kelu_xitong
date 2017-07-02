$(function() {
    var urlpath = window.location.pathname

    /**
     * 初始化菜单信息
     */
    initMenu()

    function initMenu() {
        var menus =
            "<li class='header'>主导航</li>"+
            "<li data-id='/AdminLTE/pages/admin/admin_center_manage.html'><a href='/AdminLTE/pages/admin/admin_center_manage.html'><i class='fa fa-book'></i> <span>中控管理</span></a></li>"+
            "<li class='treeview'>"+
                "<a href='#'>"+
                    "<i class='fa fa-dashboard'></i> <span>管理后台</span>"+
                    "<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span>"+
                "</a>"+
                "<ul class='treeview-menu'>"+
                    "<li data-id='/AdminLTE/pages/admin/admin_kelu_manage.html'><a href='/AdminLTE/pages/admin/admin_kelu_manage.html'><i class='fa fa-circle-o'></i>刻录管理</a></li>"+
                "</ul>"+
            "</li>"

        // var menus =
        //     "<li class='header'>主导航</li>"+
        //     "<li data-id='/AdminLTE/pages/admin/admin_user_manage.html'><a href='/AdminLTE/pages/admin/admin_user_manage.html'><i class='fa fa-book'></i> <span>我的首页</span></a></li>"+
        //     "<li class='treeview'>"+
        //         "<a href='#'>"+
        //             "<i class='fa fa-dashboard'></i> <span>管理后台</span>"+
        //             "<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span>"+
        //         "</a>"+
        //         "<ul class='treeview-menu'>"+
        //             "<li data-id='/AdminLTE/pages/admin/admin_center_manage.html'><a href='/AdminLTE/pages/admin/admin_center_manage.html'><i class='fa fa-book'></i> <span>管理中心</span></a></li>"+
        //             "<li data-id='/AdminLTE/pages/admin/admin_room_manage.html'><a href='/AdminLTE/pages/admin/admin_room_manage.html'><i class='fa fa-circle-o'></i>法庭管理</a></li>"+
        //             "<li data-id='/AdminLTE/pages/admin/admin_camera_manage.html'><a href='/AdminLTE/pages/admin/admin_camera_manage.html'><i class='fa fa-circle-o'></i>摄像机管理</a></li>"+
        //             "<li data-id='/AdminLTE/pages/admin/admin_kelu_manage.html'><a href='/AdminLTE/pages/admin/admin_kelu_manage.html'><i class='fa fa-circle-o'></i>刻录管理</a></li>"+
        //             "<li data-id='/AdminLTE/pages/admin/admin_camera_manage.html'><a href='/AdminLTE/pages/admin/admin_camera_manage.html'><i class='fa fa-circle-o'></i>设备管理</a></li>"+
        //     "</ul>"+
        //     "</li>"

        $('#sys_menu').html(menus)

        $('#sys_menu li').each(function(){
            var data_id = $(this).attr('data-id')
            if(data_id && data_id==urlpath){
                activeCheck($(this))
            }
        });
    }


    function activeCheck(tnode){
        tnode.parents('.treeview').addClass('active')
        tnode.addClass('active')
    }

});