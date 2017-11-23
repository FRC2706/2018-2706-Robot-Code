# 2018-2706-Robot-Code
The main robot code for the Merge Robotics (2706) robot.

## Want to help write robot code?

We have a lot of programmers on the team this year, so we've split the code out into chunks so each group can be in charge of a piece. Talk to you group's mentor or project leader to see which chunk you can work on.

## Code Structure
Code structure in general is subject to change, so feel free to add new subheadings and descriptions to the readme that cover your respective group's code.

### Styling

We use Google's code styling standard. To use the Eclipse's autoformatter, go open the source file to be formatted, and press Ctrl + Shift + F.

### Subsystems

Subsystems classes are for the lowest end things like gyro, direct communication with motors, camera etc. These classes will typically be owned by the group working with the hardware, such as the Controls group for motors and sensors, Vision group for cameras, etc.

### Commands

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
