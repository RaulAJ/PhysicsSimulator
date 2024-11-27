package simulator.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import simulator.control.Controller;

public class removeBodieDialog extends JDialog implements SimulatorObserver{

	private Controller _ctrl;
	private String[] ids;
	private int size;
	
	public removeBodieDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		size = 0;
		// registrar this como observer;
		_ctrl.addObserver(this);
		initGUI();

	}
	
	private void initGUI() {
		
		setTitle("Remove bodies");
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel lPanel = new JPanel(new BorderLayout());
		JList<String> list = new JList<>(ids);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		list.setFixedCellWidth(120);
		list.setFixedCellHeight(30);
		JButton ok = new JButton("OK");
		ok.addActionListener(e ->{
			String id = list.getSelectedValue();
			_ctrl.removeBody(id);
			/*for(int i = list.getSelectedIndex(); i < ids.length - 1; i++) {
				ids[i] = ids[i + 1];
			}*/
			list.setListData(ids);
			setVisible(false);
		});
		lPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		lPanel.add(list, BorderLayout.LINE_START);
		mainPanel.add(lPanel);
		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public void open() {
		
		setLocationRelativeTo(getParent());
		pack();
		setVisible(true);
	}
	
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		size = 0;
		ids = new String[100];

		for(Map.Entry<String, BodiesGroup> e : groups.entrySet()) {
			for(Body b : e.getValue()) {
				ids[size] = b.getId();
				size++;
			}
		}
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		size = 0;
		ids = new String[100];

		for(Map.Entry<String, BodiesGroup> e : groups.entrySet()) {
			for(Body b : e.getValue()) {
				ids[size] = b.getId();
				size++;
			}
		}

	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
		
	}

}
