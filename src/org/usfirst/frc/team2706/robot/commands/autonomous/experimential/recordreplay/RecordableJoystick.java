package org.usfirst.frc.team2706.robot.commands.autonomous.experimential.recordreplay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Log;

import com.google.gson.Gson;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Can be used as a Joystick object, it saves Joystick values to a file while recording, that can
 * later be replayed in autonomous mode.
 */
public class RecordableJoystick extends Joystick {

    /**
     * The location of the joystick if the given file cannot be found
     */
    public static final String EMPTY_LOC = "/home/lvuser/joystick-recordings/empty/empty";

    private final boolean replay;

    private final Joystick joy;

    private JoystickConfig config;
    private List<JoystickState> states;

    private boolean[] pressed, released;

    private volatile int index;
    private volatile double time;

    private Supplier<Double> timeSupplier;

    private final String loc;

    /**
     * Sets up recording or replaying from or to a real Joystick
     * 
     * @param joy The real joystick that provides values if there are no values saved, or if
     *        recording
     * @param loc The location to load or save the data from the Joystick
     * @param replay Is true if replaying false if recording
     */
    public RecordableJoystick(Joystick joy, String loc, boolean replay) {
        super(joy.getPort());

        this.replay = replay;
        this.joy = joy;
        this.loc = loc;
    }

    /**
     * Gets the real Joystick that provides values if there are no values saved, or if recording
     * 
     * @return The real Joystick
     */
    public Joystick getRealJoystick() {
        return joy;
    }

    /**
     * Stops recording or replaying and if recording, saves the values to files
     */
    public void end() {
        if (!replay) {
            saveFile(new Gson().toJson(config), new File(loc + "-config.json"));
            saveFile(new Gson().toJson(states), new File(loc + "-states.json"));
            
            this.states.clear();
        }
    }

    private String loadFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            String tmp = "";
            while ((tmp = br.readLine()) != null) {
                line += tmp + "\n";
            }
            return line;
        } catch (IOException e) {
            Log.e("Record and Replay", "Error loading file", e);
        }
        return null;
    }

    private void saveFile(String line, File file) {
        // Create new file if the file doesn't exist already
        file.getParentFile().mkdirs();

        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write(line);
        } catch (IOException e) {
            Log.e("Record and Replay", "Error saving file", e);
        }
    }

    private JoystickState grabJoystickValues() {
        final double[] axes = new double[getAxisCount()];
        final boolean[] buttons = new boolean[getButtonCount()];
        final int[] povs = new int[getPOVCount()];

        for (int i = 0; i < axes.length; i++) {
            axes[i] = getRawAxis(i);
        }

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = getRawButton(i + 1);
        }

        for (int i = 0; i < povs.length; i++) {
            povs[i] = getPOV(i);
        }

        return new JoystickState(axes, buttons, povs, time);
    }

    /**
     * Sets up for recording/replaying. If replaying, load the files to replay
     * 
     * @param timeSupplier The supplier to get the time since the command is initialized. Use
     *        {@code Command::timeSinceInitialized} for this.
     */
    public void init(Supplier<Double> timeSupplier) {
        if (replay) {
            if (new File(loc + "-config.json").isFile()
                            && new File(loc + "-config.json").isFile()) {
                config = new Gson().fromJson(loadFile(new File(loc + "-config.json")),
                                JoystickConfig.class);
                states = Arrays.asList(new Gson().fromJson(loadFile(new File(loc + "-states.json")),
                                JoystickState[].class));

                pressed = new boolean[config.buttonCount];
                released = new boolean[config.buttonCount];
            } else {
                Log.e("Record and Replay", loc + "-config.json and/or " + loc
                                + "-states.json do not exist...");
                config = new Gson().fromJson(loadFile(new File(EMPTY_LOC + "-config.json")),
                                JoystickConfig.class);
                states = Arrays.asList(
                                new Gson().fromJson(loadFile(new File(EMPTY_LOC + "-states.json")),
                                                JoystickState[].class));
            }
        } else {
            config = new JoystickConfig(joy.getAxisCount(), joy.getButtonCount(), joy.getPOVCount(),
                            joy.getType().value, joy.getName());
            states = new ArrayList<JoystickState>();
        }

        this.timeSupplier = timeSupplier;

        reset();
    }

    /**
     * If replaying, updates the values to be replayed
     */
    public void update() {
        updateTick();
    }

    /**
     * Whether there are no more states to be replayed or false if recording
     * 
     * @return Whether finished or not
     */
    public boolean done() {
        return replay && index == -1;
    }

    /**
     * Resets states played to the start
     */
    public void reset() {
        index = 0;
    }

    private boolean isValidReplayState() {
        return replay && config != null && states != null && index < states.size() && index != -1;
    }

    @Override
    public int getAxisCount() {
        if (isValidReplayState())
            return config.axisCount;
        else
            return joy.getAxisCount();
    }

    @Override
    public int getButtonCount() {
        if (isValidReplayState())
            return config.buttonCount;
        else
            return joy.getButtonCount();
    }

    @Override
    public HIDType getType() {
        if (isValidReplayState())
            return HIDType.values()[config.type];
        else
            return joy.getType();
    }

    @Override
    public String getName() {
        if (isValidReplayState())
            return config.name;
        else
            return joy.getName();
    }

    @Override
    public int getPOVCount() {
        if (isValidReplayState())
            return config.povCount;
        else
            return joy.getPOVCount();
    }

    @Override
    public double getRawAxis(final int axis) {
        if (isValidReplayState())
            return states.get(index).axes[axis];
        else
            return joy.getRawAxis(axis);
    }

    @Override
    public boolean getRawButton(final int button) {
        if (isValidReplayState())
            return states.get(index).buttons[button - 1];
        else {
            return joy.getRawButton(button);
        }
    }

    @Override
    public boolean getRawButtonPressed(final int button) {
        if (isValidReplayState()) {
            if (!getRawButton(button)) {
                pressed[button - 1] = false;
                return false;
            } else if (!pressed[button - 1]) {
                pressed[button - 1] = true;
                return true;
            } else {
                return false;
            }
        } else {
            return joy.getRawButtonPressed(button);
        }
    }

    @Override
    public boolean getRawButtonReleased(final int button) {
        if (isValidReplayState()) {
            if (getRawButton(button)) {
                released[button - 1] = false;
                return false;
            } else if (!released[button - 1]) {
                released[button - 1] = true;
                return true;
            } else {
                return false;
            }
        } else {
            return joy.getRawButtonReleased(button);
        }
    }

    @Override
    public int getPOV(int pov) {
        if (isValidReplayState())
            return states.get(index).povs[pov];
        else
            return joy.getPOV(pov);
    }

    private class JoystickConfig {

        private final int axisCount;
        private final int buttonCount;
        private final int povCount;

        private final int type;

        private final String name;

        private JoystickConfig(int axisCount, int buttonCount, int povCount, int type,
                        String name) {
            this.axisCount = axisCount;
            this.buttonCount = buttonCount;
            this.povCount = povCount;
            this.type = type;
            this.name = name;
        }

    }

    private void updateTick() {
        time = timeSupplier.get();

        if (replay) {
            double closest = Double.MAX_VALUE;

            for (int i = index; i < states.size(); i++) {
                double timeToIndex = Math.abs(time - states.get(i).time);

                if (timeToIndex <= closest) {
                    closest = timeToIndex;
                } else {
                    index = Math.max(i - 1, 0);
                    break;
                }
            }
        }
        else {
            states.add(grabJoystickValues());
            index++;
        }

    }

    private class JoystickState {

        private final double axes[];
        private final boolean buttons[];
        private final int povs[];
        private final double time;

        private JoystickState(double axes[], boolean buttons[], int povs[], double time) {
            this.axes = axes;
            this.buttons = buttons;
            this.povs = povs;
            this.time = time;
        }
    }
}
