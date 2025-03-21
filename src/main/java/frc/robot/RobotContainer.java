// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;
import swervelib.SwerveInputStream;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{

  // Replace with CommandPS4Controller or CommandJoystick if needed
  final         CommandXboxController driverXbox = new CommandXboxController(0);
  final         Joystick buttons = new Joystick(1);
  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                                "swerve"));

  //Arm calls
  private final Arm arm = new Arm();
  
  /**
   * Converts driver input into a field-relative ChassisSpeeds that is controlled by angular velocity.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverXbox.getLeftY() * -1,
                                                                () -> driverXbox.getLeftX() * -1)
                                                            .withControllerRotationAxis(driverXbox::getRightX)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(1)
                                                            .allianceRelativeControl(true);

  /**
   * Clone's the angular velocity input stream and converts it to a fieldRelative input stream.
   */
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverXbox::getRightX,
                                                                                             driverXbox::getRightY)
                                                           .headingWhile(true);

  /**
   * Clone's the angular velocity input stream and converts it to a robotRelative input stream.
   */
  SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                                                             .allianceRelativeControl(false);


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer()
  {

    // Configure the trigger bindings
    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
    NamedCommands.registerCommand("test", Commands.print("I EXIST"));
    NamedCommands.registerCommand("gotolevel1",arm.Autogotolevel1());
    NamedCommands.registerCommand("gotolevel2",arm.Autogotolevel2());
    NamedCommands.registerCommand("gotolevel3",arm.Autogotolevel3());
    NamedCommands.registerCommand("gotolevel4",arm.Autogotolevel4());
    NamedCommands.registerCommand("gotoAlgaelevel1",arm.AutogotoAlgaelevel1());
    NamedCommands.registerCommand("gotoAlgaelevel2",arm.AutogotoAlgaelevel2());
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {
    Command driveFieldOrientedDirectAngle      = drivebase.driveFieldOriented(driveDirectAngle);
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
    Command driveRobotOrientedAngularVelocity  = drivebase.driveFieldOriented(driveRobotOriented);
    Command driveSetpointGen = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngle);
    
    //zeroing the navx
    Command zeroNavx = Commands.run(() -> drivebase.zeroGyro());
    //calling the drive function in drivebase, needs translation2d and needs a double rotation
    Command turn180 = Commands.run(()-> drivebase.drive(new Translation2d(0,0),180,false));
    //chaning the max speeds for the brakes
    final Command fullspeed = Commands.run(() -> drivebase.getSwerveDrive().setMaximumAllowableSpeeds(Constants.MAX_SPEED,1));
    final Command halfspeed = Commands.run(() -> drivebase.getSwerveDrive().setMaximumAllowableSpeeds(Constants.MAX_SPEED/4,1));
    
    //lets the thing drive
    drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
    
    driverXbox.button(7).whileTrue(zeroNavx);
    //hold to slow down
    driverXbox.rightTrigger().whileTrue(halfspeed).whileFalse(fullspeed);
    //elevator going up/down
    driverXbox.rightBumper().whileTrue(arm.goUp()).whileFalse(arm.ElevatorHold());
    driverXbox.leftBumper().whileTrue(arm.goDown()).whileFalse(arm.ElevatorHold());
    //algae intake/outtake
    driverXbox.x().whileTrue(arm.AlgaeIntake()).whileFalse(arm.AlgaeHeld());
    driverXbox.b().whileTrue(arm.AlgaeOuttake()).whileFalse(arm.AlgaeHeld());
    driverXbox.povDown().whileTrue(drivebase.centerModulesCommand());
    driverXbox.povLeft().whileTrue(arm.coralleft()).whileFalse(arm.coralstop());
    driverXbox.povRight().whileTrue(arm.coralright()).whileFalse(arm.coralstop());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand("New Auto");
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }
}