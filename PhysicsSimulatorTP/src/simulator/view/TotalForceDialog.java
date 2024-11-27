package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class TotalForceDialog extends JDialog implements SimulatorObserver{

	private Controller _ctrl;

	private InfoTable _dataTableModel;

	
	TotalForceDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		initGUI();
		// registrar this como observer;
		ctrl.addObserver(this);
		
	}
	
	private void initGUI() {
		setTitle("Total force per body");
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		InfoTable _dataTableModel = new InfoTable("Force", new ForceTableModel(_ctrl));
        mainPanel.add(_dataTableModel);
		
		
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		okButton.addActionListener(e ->{
			setVisible(false);
		});
		
		setPreferredSize(new Dimension(700, 400));
		setVisible(false);
		setResizable(false);
		pack();

	}

	
	public void open() {
		// Establecer la posición de la ventana de diálogo de tal manera que se abra en el centro de la ventana principal
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
		
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		
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
		System.out.println("ds");
	}

}
