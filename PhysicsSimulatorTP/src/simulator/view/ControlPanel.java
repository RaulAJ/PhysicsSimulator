package simulator.view;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import simulator.model.removeBodieDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

class ControlPanel extends JPanel implements SimulatorObserver {
    private Controller _ctrl;
    private JToolBar _toolaBar;
    private JFileChooser _fc;
    private boolean _stopped = true; // utilizado en los botones de run/stop
    private JButton _quitButton;
    // añade más atributos aquí …
    private JButton _fileChooserButton;
    private JButton _physicsModButton;
    private JButton _simulatorViewerButton;
    private JButton _runButton;
    private JButton _stopButton;
    private JTextField _dtTextField;
    private JSpinner _stepsSelector;
    private ForceLawsDialog _forceLawsDialog;
    private removeBodieDialog _removeBodiesDialog;
    private JButton bb;

    ControlPanel(Controller ctrl) {
        _ctrl = ctrl;
        initGUI();
// registrar this como observador
        ctrl.addObserver(this);
    }
    private void initGUI() {
        setLayout(new BorderLayout());
        _toolaBar = new JToolBar();
        add(_toolaBar, BorderLayout.PAGE_START);
        // crear los diferentes botones/atributos y añadirlos a _toolaBar.
        // Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
        // _toolaBar.addSeparator() para añadir la línea de separación vertical
        // entre las componentes que lo necesiten

        _fc = new JFileChooser("./");
        _toolaBar.addSeparator();
        _fileChooserButton = new JButton();
        _fileChooserButton.setToolTipText("Open File");
        _fileChooserButton.setIcon(new ImageIcon("resources/icons/open.png"));
        _fileChooserButton.addActionListener((e) -> {
            int returnVal = _fc.showOpenDialog(Utils.getWindow(this));
            if(returnVal == JFileChooser.APPROVE_OPTION){
                this._ctrl.reset();
                File _inputFile = _fc.getSelectedFile();
                //Sacar la ruta del archivo como string -> loadData
                String filePath = _inputFile.getPath();
                try {
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    _ctrl.loadData(fileInputStream);
                    fileInputStream.close();
                    // Habilita los botones aquí
                    _quitButton.setEnabled(true);
                    _fileChooserButton.setEnabled(true);
                    _physicsModButton.setEnabled(true);
                    _simulatorViewerButton.setEnabled(true);
                    _runButton.setEnabled(true);
                    _stopButton.setEnabled(true);

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
        _toolaBar.add(_fileChooserButton);
        _toolaBar.addSeparator();
        _physicsModButton = new JButton();
        _physicsModButton.setToolTipText("Change Force Law");
        _physicsModButton.setIcon(new ImageIcon("resources/icons/physics.png"));
        _physicsModButton.addActionListener((e) -> {
            if (this._forceLawsDialog == null)
            {
                Frame parentFrame = Utils.getWindow(ControlPanel.this); //Obtiene el frame correspondiente a ControlPanel
                _forceLawsDialog = new ForceLawsDialog(parentFrame, _ctrl);
            }
            _forceLawsDialog.open();
        });
        _toolaBar.add(_physicsModButton);
        _toolaBar.addSeparator();
        _simulatorViewerButton = new JButton();
        _simulatorViewerButton.setToolTipText("View Simulation");
        _simulatorViewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
        _simulatorViewerButton.addActionListener((e) -> {
            ViewerWindow viewerWindow = new ViewerWindow((JFrame) Utils.getWindow(ControlPanel.this), _ctrl);
        });
        _toolaBar.add(_simulatorViewerButton);
        _toolaBar.addSeparator();

        bb = new JButton();
        bb.setToolTipText("Remove bodies");
        
        bb.addActionListener(e ->{
        	if(this._removeBodiesDialog == null) {
                Frame parentFrame = Utils.getWindow(ControlPanel.this); //Obtiene el frame correspondiente a ControlPanel

        		_removeBodiesDialog = new removeBodieDialog(parentFrame, _ctrl);
        	}
        	_removeBodiesDialog.open();
        });
        
        _toolaBar.add(bb);
        _toolaBar.addSeparator();
        
        _runButton = new JButton();
        _runButton.setToolTipText("Run Simulator");
        _runButton.setIcon(new ImageIcon("resources/icons/run.png"));
        _toolaBar.add(_runButton);

        // Stop Button
        _toolaBar.addSeparator();
        _stopButton = new JButton();
        _stopButton.setToolTipText("Stop");
        _stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
        _stopButton.addActionListener((e) -> {
            _stopped = true;
        });
        _toolaBar.add(_stopButton);


        //JSpinner stepSelector
        JLabel stepsLabel = new JLabel("Steps: ");
        _stepsSelector = new JSpinner(new SpinnerNumberModel(10000, 1, 10000, 100));
        _stepsSelector.setToolTipText("Steps");
        _toolaBar.add(stepsLabel);
        _toolaBar.add(_stepsSelector);

        // Delta-time TextField
        JLabel dtLabel = new JLabel("Delta-Time: ");
        _dtTextField = new JTextField(10);
        _dtTextField.setText(String.valueOf(_ctrl.getDeltaTime()));
        _dtTextField.setMaximumSize(_dtTextField.getPreferredSize());
        _dtTextField.setToolTipText("Delta-time");
        _toolaBar.add(dtLabel);
        _toolaBar.add(_dtTextField);


        _runButton.addActionListener((e) -> {
            //1. Deshabilita botones
            _quitButton.setEnabled(false);
            _fileChooserButton.setEnabled(false);
            _physicsModButton.setEnabled(false);
            _simulatorViewerButton.setEnabled(false);
            _runButton.setEnabled(false);
            _stopped = false;

            //2. Actualizar delta-time
            // Asegúrate de que el JTextField _dtTextField ya ha sido creado y configurado antes de este bloque de código
            try {
                double deltaTime = Double.parseDouble(_dtTextField.getText());
                _ctrl.setDeltaTime(deltaTime);
            } catch (NumberFormatException ex) {
                // Muestra un mensaje de error si el valor ingresado no es válido y vuelve a habilitar los botones
                /////Utils.showErrorMsg(this, "Invalid delta-time value.", "Error");
                _quitButton.setEnabled(true);
                _fileChooserButton.setEnabled(true);
                _physicsModButton.setEnabled(true);
                _simulatorViewerButton.setEnabled(true);
                _runButton.setEnabled(true);
                _stopped = true;
                return;
            }


            //3. Llama a run_sim con el valor actual de pasos, especificado en el correspondiente JSpinner
            int steps = (int) _stepsSelector.getValue();

            run_sim(steps);
        });


        // Quit Button
        _toolaBar.add(Box.createGlue()); // this aligns the button to the right
        _toolaBar.addSeparator();
        _quitButton = new JButton();
        _quitButton.setToolTipText("Quit");
        _quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
        _quitButton.addActionListener((e) -> Utils.quit(this));
        _toolaBar.add(_quitButton);
    }

    private void run_sim(int n) {
        if (n > 0 && !_stopped) {
            try {
                _ctrl.run(1);
            } catch (Exception e) {
            // llamar a Utils.showErrorMsg con el mensaje de error que
                Utils.showErrorMsg(e.getMessage());
            // corresponda
            // activar todos los botones
                _quitButton.setEnabled(true);
                _fileChooserButton.setEnabled(true);
                _physicsModButton.setEnabled(true);
                _simulatorViewerButton.setEnabled(true);
                _runButton.setEnabled(true);
                _stopped = true;
                return;
            }
            SwingUtilities.invokeLater(() -> run_sim(n - 1));
        }
        else {
        //llamar a Utils.showErrorMsg con el mensaje de error que corresponda
            /*if (!_stopped) { 
                //Utils.showErrorMsg("Run error!");
            }*/
        //  activar todos los botones

            _quitButton.setEnabled(true);
            _fileChooserButton.setEnabled(true);
            _physicsModButton.setEnabled(true);
            _simulatorViewerButton.setEnabled(true);
            _runButton.setEnabled(true);

            _stopped = true;
        }
    }

    @Override
    public void onAdvance(Map<String, BodiesGroup> groups, double time) {

    }

    @Override
    public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {

    }

    @Override
    public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {

    }

    @Override
    public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {

    }

    @Override
    public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {

    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        _dtTextField.setText(Double.toString(dt));
    }

    @Override
    public void onForceLawsChanged(BodiesGroup g) {

    }
// el resto de métodos van aquí…
}
