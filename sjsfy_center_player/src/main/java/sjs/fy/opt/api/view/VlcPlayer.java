package sjs.fy.opt.api.view;

import sjs.fy.opt.api.service.CameraOptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.service.PropertyService;
import sjs.fy.opt.api.service.TimerSerive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.LineBorder;

@Component
public class VlcPlayer extends JFrame{
	private static final long serialVersionUID = 1L;

	@Autowired
	private InfoPanel optionPanel;
	@Autowired
	private CameraOptService cameraOptService;
	@Autowired
	private PropertyService propertyService;



	public void _run(){
		/**
		 * 主面板
		 */
		String iconPath = propertyService.getValueByKey("iconpath");
		setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath+"logo.png"));
		getContentPane().setLayout(new BorderLayout(0, 0));
		setBounds(200, 150, 800, 650);
		setResizable(false);
		setTitle("庭审系统");

		/**
		 * Logo面板
		 */
		JPanel logoPanel = new JPanel();
		logoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		getContentPane().add(logoPanel, BorderLayout.NORTH);
		JLabel logo_Label = new JLabel("");
		logo_Label.setIcon(new ImageIcon(iconPath+"title.png"));
		logoPanel.add(logo_Label);

		/**
		 * 播放器面板
		 */
//		EmbeddedMediaPlayerComponent playerComponent = new EmbeddedMediaPlayerComponent();
//		getContentPane().add(playerComponent,BorderLayout.CENTER);
		getContentPane().add(cameraOptService.getPlayerComponent(),BorderLayout.CENTER);


		/**
		 * 操作面板
		 */
		optionPanel._run();
		optionPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		getContentPane().add(optionPanel, BorderLayout.EAST);

		/**
		 * 主界面关闭事件
		 */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cameraOptService.stop();
				optionPanel.windowClose();
				System.exit(0);
			}

		});
		setVisible(true);
//		updateShow(optionPanel.start_luzhi_btn,optionPanel.zhanting_luzhi_btn,optionPanel.stop_luzhi_btn,optionPanel.starttime_w_label,optionPanel.anjianbianhao_textfield,optionPanel.anyou_textfield);
		doAfterSetUp();
	}

//	/**
//	 * 客户端启动后，定时跟新的信息
//	 */
//	private void updateShow(JButton start_luzhi_btn,JButton zhanting_luzhi_btn,JButton stop_luzhi_btn,JLabel starttime_w_label,JTextField anjianbianhao_textfield,JTextField anyou_textfield){
//		if(faTingService.preCheck().size()>0){
//			faTingService.preCheckDeal(configs,start_luzhi_btn,zhanting_luzhi_btn,stop_luzhi_btn,starttime_w_label,anjianbianhao_textfield,anyou_textfield);
//		}
//		timerSerive.showTime(optionPanel.systime_w_label);
//	}


	@Autowired
	TimerSerive timerSerive;
	private void doAfterSetUp(){
		timerSerive.showTime(optionPanel.systime_w_label);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				cameraOptService.stop();
			}
		}));
	}
}
