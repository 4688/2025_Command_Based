// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;


public class Arm extends SubsystemBase {
  SparkMax elevator1;
  SparkMax elevator2;
  public double encoder1;
  public double encoder2;
  double espeed;
  SparkMax intake1;
  SparkMax intake2;
  SparkMax coral;

  double l1;
  double l12;
  double l2;
  double l22;
  double l3;
  double l32;
  double l4;
  double l42;
  double a1;
  double a12;
  double a2;
  double a22;

  /* Creates a new ExampleSubsystem. */
  public Arm() {
    elevator1 = new SparkMax(26, MotorType.kBrushless);
    elevator2 = new SparkMax(25, MotorType.kBrushless);
    encoder1 = elevator1.getEncoder().getPosition();
    encoder2 = elevator2.getEncoder().getPosition();
    espeed = 0.25;
    intake1 = new SparkMax(20, MotorType.kBrushless);
    intake2 = new SparkMax(22, MotorType.kBrushless);
    coral = new SparkMax(21, MotorType.kBrushless);

  }
  private static double algaeIntakeSpeed = 0.5;
  private static double algaeOutakeSpeed = 0.5;
  private static double algaeHoldspeed = 0.1;
  private static double elevatorHoldSpeed = 0.05;
  /**
   * Example command factory method.
   *
   * @return a command
   */
  

  //make the elevator go up
  public Command goUp() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          if (elevator1.getEncoder().getPosition() < 65.7 && elevator2.getEncoder().getPosition() > -66){
            elevator1.set(espeed);
            elevator2.set(-espeed);
          }
        });
  }
  //make the elevator go down
  public Command goDown(){
    return runOnce(() -> {
      elevator1.set(-espeed);
      elevator2.set(espeed);
    });
  }

  //make the elevator stay in place
  public Command ElevatorHold(){
    return runOnce(() -> {
      elevator1.set(elevatorHoldSpeed);
      elevator2.set(-elevatorHoldSpeed);
    });
  }

  //algae intake
  public Command AlgaeIntake(){
    return runOnce(() -> {
      intake1.set(-algaeIntakeSpeed);
      intake2.set(algaeIntakeSpeed);
    });
  }

  //algae outtake
  public Command AlgaeOuttake(){
    return runOnce(() -> {
      intake1.set(algaeOutakeSpeed);
      intake2.set(-algaeOutakeSpeed);
    });
  }

  //algea hold
  public Command AlgaeHeld(){ // should be renamed algaeHold for readablility
    return runOnce(() -> {
      intake1.set(-algaeHoldspeed);
      intake2.set(algaeHoldspeed);
    });
  }

  public Command gotolevel1(){
    return runOnce(()-> {gotoLevel(l1, l12);});
  }
  public Command gotolevel2(){
    return runOnce(()-> {gotoLevel(l2, l22);});
  }
  public Command gotolevel3(){
    return runOnce(()-> {gotoLevel(l3, l32);});
  }
  public Command gotolevel4(){
    return runOnce(()-> {gotoLevel(l4, l42);});
  }
  public Command gotoAlgaelevel1(){
    return runOnce(()-> {gotoLevel(a1, a12);});
  }
  public Command gotoAlgaelevel2(){
    return runOnce(()-> {gotoLevel(a2, a22);});
  }

  //goto elevator level - get encoder values
  public void gotoLevel(double level, double level2){
    if (elevator1.getEncoder().getPosition() <= level && elevator2.getEncoder().getPosition() >= level2){
        elevator1.set(espeed);
        elevator2.set(-espeed);
    }
  }

  public boolean autotrue(){
    return true;
  }


  public Command Autogotolevel1(){
    return runOnce(()-> {if (autotrue()== true){gotoLevel(l1, l12);}});
  }
  public Command Autogotolevel2(){
    return runOnce(()-> {if (autotrue()== true){gotoLevel(l2, l22);}});
  }
  public Command Autogotolevel3(){
    return runOnce(()-> {if (autotrue()== true){gotoLevel(l3, l32);}});
  }
  public Command Autogotolevel4(){
    return runOnce(()-> {if (autotrue()== true){gotoLevel(l4, l42);}});
  }
  public Command AutogotoAlgaelevel1(){
    return runOnce(()-> {if (autotrue()== true){gotoLevel(a1, a12);}});
  }
  public Command AutogotoAlgaelevel2(){
    return runOnce(()-> {if (autotrue()== true){gotoLevel(a2, a22);}});
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("encoder1", encoder1);
    SmartDashboard.putNumber("encoder2", encoder2);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
