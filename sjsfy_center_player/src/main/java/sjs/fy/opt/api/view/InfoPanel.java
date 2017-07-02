package sjs.fy.opt.api.view;


import sjs.fy.opt.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class InfoPanel extends JPanel {

    @Autowired
    TimerSerive timerSerive;

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    JLabel systime_label = new JLabel("系统时间:");
    JLabel systime_w_label = new JLabel("系统时间-标签");
    JLabel starttime_label = new JLabel("开庭时间:");
    public JLabel starttime_w_label = new JLabel("开庭时间-动态变化");
    JLabel chixutime_label = new JLabel("持续时间:");
    public JLabel chixutime_w_label = new JLabel("持续时间-动态变化");

    public JButton start_luzhi_btn = new JButton("开庭");
    public JButton zhanting_luzhi_btn = new JButton("休庭");
    public JButton stop_luzhi_btn = new JButton("闭庭");

    JLabel anjianbianhao_label = new JLabel("案件编号(最大允许15个字符)");
    public JTextField anjianbianhao_textfield = new JTextField("案件编号-输入框");

    JLabel anyou_label = new JLabel("案由");
    public JTextField anyou_textfield = new JTextField("案由-输入框");

    JLabel faguan_label = new JLabel("法官");
    public JTextField faguan_textfield = new JTextField("法官名字-输入框");

    JLabel stoptime_label = new JLabel("闭庭时间");
    public JTextField stoptime_textfield = new JTextField("庭审闭庭-输入框");

    JTextArea tishi_textarea = new JTextArea("整个庭审不能跨天\n在关闭系统等同【闭庭】操作");


    final Map<String,Object> configs = new HashMap<String,Object>();
    @Autowired
    DeviceService deviceService;
    @Autowired
    FaTingService faTingService;
    @Autowired
    CameraOptService cameraOptService;
    @Autowired
    PropertyService propertyService;

    /**
     * 开始跑数据
     */
    public void _run(){
        preShow();

        systime_w_label = new JLabel(dateFormat.format(new Date()));
        zhanting_luzhi_btn.setEnabled(false);
        stop_luzhi_btn.setEnabled(false);

        /**
         * 开始录制操作
         */
        start_luzhi_btn.addActionListener(e -> {
            if(anjianbianhao_textfield.getText().equals("") || anyou_textfield.getText().equals("")){
                JOptionPane.showMessageDialog(null, "案件编号&案由必须填写", "案件信息提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if(!configs.containsKey("anhao")){
                configs.put("anhao",anjianbianhao_textfield.getText());
            }
            faTingService.start_luzhi(configs,starttime_w_label,anjianbianhao_textfield,anyou_textfield);
            start_luzhi_btn.setEnabled(false);
            zhanting_luzhi_btn.setEnabled(true);
            stop_luzhi_btn.setEnabled(true);
            cameraOptService.play(configs);
        });


        /**
         * 暂停录制操作
         */
        zhanting_luzhi_btn.addActionListener(e -> {
            faTingService.xiuting_luzhi(configs);
            start_luzhi_btn.setEnabled(true);
            zhanting_luzhi_btn.setEnabled(false);
            stop_luzhi_btn.setEnabled(true);
            cameraOptService.stop();
        });


        /**
         * 停止录制操作
         */
        stop_luzhi_btn.addActionListener(e -> {
            faTingService.stop_xiuting(configs,starttime_w_label,anjianbianhao_textfield,anyou_textfield);
            stop_luzhi_btn.setEnabled(false);
            zhanting_luzhi_btn.setEnabled(false);
            start_luzhi_btn.setEnabled(true);
            cameraOptService.stop();
            configs.remove("anhao");
        });

        anjianbianhao_textfield = new JTextField();
        anjianbianhao_textfield.setColumns(20);

        anyou_textfield = new JTextField();
        anyou_textfield.setColumns(20);

        faguan_textfield = new JTextField(propertyService.getConfig(configs,"faguan_name"));
        faguan_textfield.setColumns(20);
        faguan_textfield.setDisabledTextColor(Color.GREEN);
        faguan_textfield.setEnabled(false);

        stoptime_textfield = new JTextField();
        stoptime_textfield.setColumns(20);

        tishi_textarea.setFont(new Font("宋体", Font.BOLD, 16));
        tishi_textarea.setForeground(new Color(255, 0, 0));
        tishi_textarea.setEditable(false);
        tishi_textarea.setColumns(15);
        tishi_textarea.setLineWrap(true);
        GroupLayout groupLayout = new GroupLayout(this);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                groupLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(systime_label)
                                                                .addGap(18)
                                                                .addComponent(systime_w_label)
                                                        )
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(starttime_label)
                                                                .addGap(18)
                                                                .addComponent(starttime_w_label)
                                                        )
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(chixutime_label)
                                                                .addGap(18)
                                                                .addComponent(chixutime_w_label)
                                                        )
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addGap(8)
                                                                .addComponent(start_luzhi_btn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(zhanting_luzhi_btn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(stop_luzhi_btn)
                                                        )
                                                        .addComponent(anjianbianhao_label)
                                                        .addComponent(anjianbianhao_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(faguan_label)
                                                        .addComponent(faguan_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(anyou_label)
                                                        .addComponent(anyou_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(stoptime_label)
                                                        .addComponent(stoptime_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tishi_textarea, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                        )
                                        .addContainerGap()
                        )
        );

        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                groupLayout.createSequentialGroup()
                                        .addGap(40)
                                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(systime_label)
                                                .addComponent(systime_w_label)
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(starttime_label)
                                                .addComponent(starttime_w_label)
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(chixutime_label)
                                                .addComponent(chixutime_w_label)
                                        )
                                        .addGap(18)
                                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(start_luzhi_btn)
                                                .addComponent(zhanting_luzhi_btn)
                                                .addComponent(stop_luzhi_btn)
                                        )
                                        .addGap(18)
                                        .addComponent(anjianbianhao_label)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(anjianbianhao_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18)
                                        .addComponent(faguan_label)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(faguan_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18)
                                        .addComponent(anyou_label)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(anyou_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18)
                                        .addComponent(stoptime_label)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(stoptime_textfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18)
                                        .addComponent(tishi_textarea, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(20, Short.MAX_VALUE)
                        )
        );
        this.setLayout(groupLayout);
    }

    /**
     * 客户端启动时预先加载的信息
     */
    private void preShow(){
        configs.putAll(deviceService.getRoomInfo());
        if(configs.size()==0){
            JOptionPane.showMessageDialog(null, "该法庭没有配置录像设备,请联系管理员处理", "启动配置检测告警", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     */
    public void windowClose(){
        faTingService.stop_xiuting(configs,starttime_w_label,anjianbianhao_textfield,anyou_textfield);
    }
}