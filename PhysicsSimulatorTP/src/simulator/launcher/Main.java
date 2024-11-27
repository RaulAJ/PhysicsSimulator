package simulator.launcher;

import org.apache.commons.cli.*;
import org.json.JSONObject;
import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Main {

	// default values for some parameters
	//
	private final static Integer _stepsDefaultValue = 150;
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _modeDefaultValue = "gui";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Integer _steps = null;
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static JSONObject _forceLawsInfo = null;
	private static String _mode = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;

	private static void initFactories() {
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new MovingBodyBuilder());
		bodyBuilders.add(new StationaryBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

		ArrayList<Builder<ForceLaws>> forceBuilders = new ArrayList<>();
		forceBuilders.add(new MovingTowardsFixedPointBuilder());
		forceBuilders.add(new NewtonUniversalGravitationBuilder());
		forceBuilders.add(new NoForceBuilder());
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(forceBuilders);
	}
	
	//-i resources/examples/input/ex1.json -o resources/tmp/myout.json -s 1000 -dt 3000 -fl nlug

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Bodies JSON output file").build());

		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc(
				"An integer representing the number of simulation steps. Default value:" + _stepsDefaultValue + "."
		).build());
		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());
		//mode option
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg()
				.desc("Execution Mode. Possible values: " + modePossibleValues() +
						". Default value:" + _modeDefaultValue).build());
		
		cmdLineOptions.addOption(Option.builder("dzone").longOpt("danger-zone").hasArg().desc(
				"times that a body has entereder dz"
		).build());
		
		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		String s = "";
		if (factory != null) {

			for (JSONObject fe : factory.getInfo()) {
				if (s.length() > 0) {
					s = s + ", ";
				}
				s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}

			s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		} else {
			s = "No values found";
		}
		return s;
	}
	public static String modePossibleValues () {
		return "'batch' (Batch mode), 'gui' (Graphical User Interface mode).\n";
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && _mode.equals("batch")) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		try	{
			_outFile = line.getOptionValue("o");
		}
		catch (Exception e)
		{
			throw new ParseException("Invalid outfile value: " + _outFile);
		}
	}
	private static void parseStepsOption(CommandLine line) throws ParseException {
		try {
			_steps = Integer.parseInt(line.getOptionValue("s", _stepsDefaultValue.toString()));
		}
		catch (Exception e)
		{
			throw new ParseException("Invalid steps value: " + _steps);
		}
	}
	private static void parseModeOption(CommandLine line) throws ParseException  {
		try {
			_mode = line.getOptionValue("m", _modeDefaultValue);
		}
		catch (Exception e)
		{
			throw new ParseException("Invalid mode value: " + _mode);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		if (factory != null) {
			for (JSONObject fe : factory.getInfo()) {
				if (type.equals(fe.getString("type"))) {
					found = true;
				}
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void startBatchMode() throws Exception {
		PhysicsSimulator physicsSimulator = new PhysicsSimulator(_forceLawsFactory.createInstance(_forceLawsInfo), _dtime);
		Controller controller = new Controller(physicsSimulator,_bodyFactory,_forceLawsFactory);
		
		try {
			FileInputStream fileInputStream = new FileInputStream(_inFile);
			controller.loadData(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Elegir OutputStream: System.out o escribir a un archivo directamente.
		OutputStream out;
		if (_outFile == null)
		{
			out = System.out;
		}
		else
		{
			out = new FileOutputStream(_outFile);
		}
		controller.run(_steps, out);
		out.close();
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if (_mode.equals("batch"))
		{
			startBatchMode();
		}
		else if (_mode.equals("gui"))
		{
			startGUIMode();
		}
		else
		{
			throw new ParseException("Unknown mode selected: " + _mode);
		}
	}

	public static void main(String[] args) {
		try {
			initFactories();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
	private static void startGUIMode() throws InterruptedException, InvocationTargetException {
		PhysicsSimulator physicsSimulator = new PhysicsSimulator(_forceLawsFactory.createInstance(_forceLawsInfo), _dtime);
		Controller controller = new Controller(physicsSimulator, _bodyFactory, _forceLawsFactory);

		if (_inFile != null) {
			try {
				FileInputStream fileInputStream = new FileInputStream(_inFile);
				controller.loadData(fileInputStream);
				fileInputStream.close();
			} catch (IOException e) {
				System.err.println("Error loading input file: " + _inFile);
				e.printStackTrace();
			}
		}
		SwingUtilities.invokeAndWait(() -> new MainWindow(controller));
	}
}
