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
  /*double l1
  double l12
  double l2
  double l22
  double l3
  double l32
  /** Creates a new ExampleSubsystem. */
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
  public Command hold(){
    return runOnce(() -> {
      elevator1.set(0.05);
      elevator2.set(-0.05);
    });
  }

  //algae intake
  public Command intake(){
    return runOnce(() -> {
      intake1.set(-0.5);
      intake2.set(0.5);
    });
  }

  //algae outtake
  public Command outtake(){
    return runOnce(() -> {
      intake1.set(0.5);
      intake2.set(-0.5);
    });
  }

  //algea hold
  public Command algae(){
    return runOnce(() -> {
      intake1.set(-0.1);
      intake2.set(0.1);
    });
  }

  //goto elevator level - get encoder values
  public void gotoLevel(double level, double level2,  Boolean up){
    if (elevator1.getEncoder().getPosition() < level && elevator2.getEncoder().getPosition() > level2){
      //up
      if (up){
        elevator1.set(espeed);
        elevator2.set(-espeed);
      }
    }}

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
