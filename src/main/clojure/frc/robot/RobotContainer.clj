;; Copyright (c) 2026 Ira Parsons
;; Copyright (c) 2021-2026 Littleton Robotics
;; http://github.com/Mechanical-Advantage
;;
;; Use of this source code is governed by a BSD
;; license that can be found in the LICENSE file
;; at the root directory of this project.

(ns frc.robot.RobotContainer
  (:gen-class
    :init init)
  (:require [frc.robot.Constantsa :as Constants])
  (:import (com.pathplanner.lib.auto AutoBuilder)
           (edu.wpi.first.math.geometry Pose2d Rotation2d)
           (edu.wpi.first.wpilibj GenericHID XboxController)
           (edu.wpi.first.wpilibj2.command Command Commands)
           (edu.wpi.first.wpilibj2.command.button CommandXboxController)
           (edu.wpi.first.wpilibj2.command.sysid SysIdRoutine)
           (frc.robot.commands DriveCommands)
           (frc.robot.generated TunerConstants)
           (frc.robot.subsystems.drive Drive GyroIO GyroIOPigeon2 GyroIONone ModuleIO ModuleIOSim ModuleIOTalonFX ModuleIONone)
           (org.littletonrobotics.junction.networktables LoggedDashboardChooser)))

(def drive (atom nil))
(def controller (delay (CommandXboxController. 0)))
(def autoChooser (atom nil))

(defn configureButtonBindings []
  (. @drive (setDefaultCommand (DriveCommands/joystickDrive @drive #(- (. @controller (getLeftY))) #(- (. @controller (getLeftX))) #(- (. @controller (getRightX))))))
  (.. @controller (a) (whileTrue (DriveCommands/joystickDriveAtAngle @drive #(- (. @controller (getLeftY))) #(- (. @controller (getLeftX))) #(Rotation2d/kZero))))
  (.. @controller (a) (onTrue (Commands/runOnce #(. @drive stopWithX) @drive)))
  (.. @controller (b) (onTrue (Commands/runOnce @(. @drive (setPose (Pose2d. (.. @drive (getPose) (getTranslation)) Rotation2d/kZero))) @drive)) (ignoringDisable true)))

(defn -init []
  (case Constants/currentMode
    Constants/REAL (reset! drive (Drive. (GyroIOPigeon2.) (ModuleIOTalonFX. TunerConstants/FrontLeft) (ModuleIOTalonFX. TunerConstants/FrontRight) (ModuleIOTalonFX. TunerConstants/BackLeft) (ModuleIOTalonFX. TunerConstants/BackRight)))
    Constants/SIM  (reset! drive (Drive. (GyroIONone.) (ModuleIOSim. TunerConstants/FrontLeft) (ModuleIOSim. TunerConstants/FrontRight) (ModuleIOSim. TunerConstants/BackLeft) (ModuleIOSim. TunerConstants/BackRight)))
    (reset! drive (Drive. (GyroIONone.) (ModuleIONone.) (ModuleIONone.) (ModuleIONone.) (ModuleIONone.))))
  (reset! autoChooser (LoggedDashboardChooser. "Auto Choices" (AutoBuilder/buildAutoChooser)))
  (doto @autoChooser
    (.addOption "Drive Wheel Radius Characterization" (DriveCommands/wheelRadiusCharacterization @drive))
    (.addOption "Drive Simple FF Characterization" (DriveCommands/feedforwardCharacterization @drive))
    (.addOption "Drive SysId (Quasistatic Forward)" (. @drive (sysIdQuasistatic (edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine$Direction/kForward))))
    (.addOption "Drive SysId (Quasistatic Reverse)" (. @drive (sysIdQuasistatic (edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine$Direction/kReverse))))
    (.addOption "Drive SysId (Dynamic Forward)" (. @drive (sysIdDynamic (edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine$Direction/kForward))))
    (.addOption "Drive SysId (Dynamic Reverse)" (. @drive (sysIdDynamic (edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine$Direction/kReverse)))))
  (configureButtonBindings))

(defn -getAutonomousCommand [] (. @autoChooser (get)))
