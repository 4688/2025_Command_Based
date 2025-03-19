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


public class ArmUntested extends SubsystemBase {
  SparkMax elevator1;
  SparkMax elevator2;
  public double encoder1;
  public double encoder2;
  double espeed;
  SparkMax intake1;
  SparkMax intake2;
  SparkMax coral;

  final double reef_1
  final double reef_2
  final double reef_3
  final double reef_4
  final double algae_1
  final double algae_2

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

  private final double Elevator1EncoderOffset = 0; // the offset that the encoder is at a zero
  private final double Elevator2EncoderOffset = 0;

  //make the elevator go up
  public Command goUp() {
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

   public Command GoToLevel1(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_1);}});
  }
  public Command GoToLevel2(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_2);}});
  }
  public Command GoToLevel3(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_3);}});
  }
  public Command GoToLevel4(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_4);}});
  }
  public Command GoToAlgaeLevel1(){
    return runOnce(()-> {if (InAuto()){gotoLevel(algae_1);}});
  }
  public Command GoToAlgaeLevel2(){
    return runOnce(()-> {if (InAuto()){gotoLevel(algae_2);}});
  }

  //goto elevator level - get encoder values
  
  public void gotoLevel(double level){
    if (encoder1 - Elevator1EncoderOffset <= level && -(encoder2 - Elevator2EncoderOffset) <= level){
        elevator1.set(espeed);
        elevator2.set(-espeed);
    }
  }

  public boolean InAuto(){
    return true;
  }


  public Command AutoGoToLevel1(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_1);}});
  }
  public Command AutoGoToLevel2(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_2);}});
  }
  public Command AutoGoToLevel3(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_3);}});
  }
  public Command AutoGoToLevel4(){
    return runOnce(()-> {if (InAuto()){gotoLevel(reef_4);}});
  }
  public Command AutoGoToAlgaeLevel1(){
    return runOnce(()-> {if (InAuto()){gotoLevel(algae_1);}});
  }
  public Command AutoGoToAlgaeLevel2(){
    return runOnce(()-> {if (InAuto()){gotoLevel(algae_2);}});
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
