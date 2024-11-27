package simulator.view;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

class ForceLawsDialog extends JDialog implements SimulatorObserver {
	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	// en caso de ser necesario, añadir los atributos aquí…
	//se usara para guardar el iesimo elemento de forcelawsinfo mas adelante.
	private int _selectedLawsIndex;
	private int _status;
	ForceLawsDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		initGUI();
		// registrar this como observer;
		ctrl.addObserver(this);
	}
	private void initGUI() {
		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		// _forceLawsInfo se usará para establecer la información en la tabla
		_forceLawsInfo = _ctrl.getForceLawsInfo();

		//crear un JTable que use _dataTableModel y configurarla
		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// hacer editable solo la columna 1
				return column == 1;
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		JTable dataTable = new JTable(_dataTableModel);
		mainPanel.add(new JScrollPane(dataTable));
		
		JPanel comboBoxPanel = new JPanel(new FlowLayout());
		//añadir la descripción de todas las leyes de fuerza a _lawsModel
		_lawsModel = new DefaultComboBoxModel<>();
		for (JSONObject lawInfo : _forceLawsInfo){
			_lawsModel.addElement(lawInfo.getString("desc"));
		}
		// crear un combobox que use _lawsModel y añadirlo al panel
		JComboBox<String> lawsComboBox = new JComboBox<>(_lawsModel);
		JLabel lawsComboLabel = new JLabel("Force Law: ");
		comboBoxPanel.add(lawsComboLabel);
		comboBoxPanel.add(lawsComboBox);

		_groupsModel = new DefaultComboBoxModel<>();

		// crear un combobox que use _groupsModel y añadirlo al panel
		JComboBox<String> groupsComboBox = new JComboBox<>(_groupsModel);
		JLabel groupsComboLabel = new JLabel("Group: ");
		comboBoxPanel.add(groupsComboLabel);
		comboBoxPanel.add(groupsComboBox);

		mainPanel.add(comboBoxPanel);
		// crear los botones OK y Cancel y añadirlos al panel
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		mainPanel.add(buttonsPanel);

		//Listener de eventos de los componentes
		lawsComboBox.addActionListener(e -> {
			_selectedLawsIndex = lawsComboBox.getSelectedIndex();
			//System.out.println(_forceLawsInfo.get(_selectedLawsIndex).toString());
			updateDataTable(_forceLawsInfo.get(_selectedLawsIndex));
		});
		okButton.addActionListener(e -> {
			try {
				StringBuilder s = new StringBuilder();
				s.append('{');
				for (int i = 0; i < _dataTableModel.getRowCount(); i++) {
					String k = (String) _dataTableModel.getValueAt(i, 0);
					String v = (String) _dataTableModel.getValueAt(i, 1);
					if (!v.isEmpty()) {
						s.append('"');
						s.append(k);
						s.append('"');
						s.append(':');
						s.append(v);
						s.append(',');
					}
				}

				if (s.length() > 1)
					s.deleteCharAt(s.length() - 1);
				s.append('}');
				
				JSONObject data = new JSONObject(s.toString());

				JSONObject selectedLaw = _forceLawsInfo.get(_selectedLawsIndex);
				JSONObject law = new JSONObject();
				law.put("type", selectedLaw.getString("type"));
				law.put("data", data);

				String selectedGroup = (String) groupsComboBox.getSelectedItem();
				_ctrl.setForceLaws(selectedGroup, law);

				_status = 1;
				setVisible(false);
			} catch (Exception ex) {
				Utils.showErrorMsg(this, ex.getMessage());
				_status = 0;
			}
		});

		cancelButton.addActionListener(e -> {
			_status = 0;
			setVisible(false);
		});

		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}
	//en el enunciado esta como void pero no tiene sentido
	public int open() {
		if (_groupsModel.getSize() == 0)
			return _status;
		// Establecer la posición de la ventana de diálogo de tal manera que se abra en el centro de la ventana principal
		setLocationRelativeTo(getParent());
		pack();
		setVisible(true);
		return _status;
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		updateGroupsComboBox(groups);
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		updateGroupsComboBox(groups);
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		updateGroupsComboBox(groups);
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {

	}

	@Override
	public void onDeltaTimeChanged(double dt) {

	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {

	}
	//  el resto de métodos van aquí…

	//Metodo agregado para actualizar la tabla
	private void updateDataTable(JSONObject info)
	{
		_dataTableModel.setRowCount(0);
		if(info == null) {
			System.out.println("d");
		}
		JSONObject data = info.getJSONObject("data");
		for(String key : data.keySet()){
			_dataTableModel.addRow(new Object[]{key, "", data.getString(key)});
		}
		
		
	}
	//Para mantener la lista de grupos actualizada
	private void updateGroupsComboBox(Map<String, BodiesGroup> groups) {
		_groupsModel.removeAllElements();
		for (String groupId : groups.keySet()) {
			_groupsModel.addElement(groupId);
		}
	}

}
