package blackhole.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import blackhole.model.BlackHoleEngine;

public class StartFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private BlackHoleEngine engine;
	private Container buttons;
	
	public StartFrame(BlackHoleEngine engine) {
		super("Válassz táblaméretet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.engine = engine;
	}
	
	public void showFrame() {
		addComponents();
		pack();
		setVisible(true);
	}

	private void addComponents() {
		buttons = new Container();
		buttons.setLayout(new GridLayout(engine.getBoardSize().size(), 1));
		for(Integer num : engine.getBoardSize()) {
			addButton(num.toString() + "x" + num.toString(), (e) -> pushButton(num.intValue()));
		}
		getContentPane().add(buttons, BorderLayout.NORTH);
	}

	private void pushButton(int num) {
		engine.run(num);
		BlackHoleFrame bhf = new BlackHoleFrame(engine);
		bhf.showFrame();
		bhf.setParent(this);
		this.setVisible(false);
	}

	private void addButton(String label, ActionListener listener) {
		Button button = new Button(label);
		button.addActionListener(listener);
		button.setPreferredSize(new Dimension(300, 50));
		buttons.add(button);
	}
	
}
