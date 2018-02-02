# 2018-2706-Robot-Code
The main robot code for the Merge Robotics (2706) robot.

## Want to help write robot code?

We have a lot of programmers on the team this year, so we've split the code out into chunks so each group can be in charge of a piece. Talk to your group's mentor or project leader to see which chunk you can work on.

## Code Structure
Code structure in general is subject to change, so feel free to add new subheadings and descriptions to the readme that cover your respective group's code.

### Styling

We use Google's code styling standard. To use the Eclipse's autoformatter, go open the source file to be formatted, and press Ctrl + Shift + F.

### Subsystems

Subsystems classes are for the lowest end things like gyro, direct communication with motors, camera etc. These classes will typically be owned by the group working with the hardware, such as the Controls group for motors and sensors, Vision group for cameras, etc.

### Commands

__Command Format:__

Commands that have `String` or primitive type parameters in their constructors that benefit from being able to read parameters from a file stored on the robot should include `String name` as the final parameter in the constructor. The first line of the constructor body should be a call to the parent constructor passing the name as the command name. Any values that should be loaded from the config file should use `RobotConfig.get(name + ".[variable name]", defaultValue)` to set the value, for example:

```Java
public MyCommand(String var1, int var2, boolean var3, String name) {
    this.var1 = RobotConfig.get(name + ".var1", var1);
    this.var2 = RobotConfig.get(name + ".var2", var2);
    this.var3 = RobotConfig.get(name + ".var3", var3);
}
```

When choosing a name for a command, make sure to use something that identifies what the specific instance of the command does and that the name is unique. (Unless the name is at a different level like `"CommandGroup1.DriveForward"` and `"DriveForward"` will both work.

For CommandGroups follow the same rules as before, for example:

```Java
public MyCommandGroup(boolean var1, String name) {
    this.addSequential(new MyCommand("Test", 44, var1, name + ".testCommand");
}
```

The Commands folder / package is split into two types of commands:

__Autonomous Commands:__

Autonomous commands are this year all in the base package of robot.commands.autonomous.
Inside that package there are three packages:

auto2018: Only autonomous modes that will be useful in 2018 and 2018 only, for example mechanism specific movements
	
core: Base autonomous movements that work every year and are 100% perfect and tested, eg. move forward on a timer
	
experimental: Everything else that isn't perfected or is still in the works, also unique ideas like curvedrive
		
auto2018 and experimental both have packages called 'automodes' and 'movements' inside of them, movements is commands 			that perform one function, and automodes is commandgroups or commands that perform multiple functions

__Teleop commands__:
(Add new formatting/structure details for this year)
