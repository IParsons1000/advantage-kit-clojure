;; Copyright (c) 2026 Ira Parsons
;; Copyright (c) 2021-2026 Littleton Robotics
;; http://github.com/Mechanical-Advantage
;;
;; Use of this source code is governed by a BSD
;; license that can be found in the LICENSE file
;; at the root directory of this project.

(ns frc.robot.Robot
  (:gen-class
    :extends org.littletonrobotics.junction.LoggedRobot
    :init init)
  (:require [frc.robot.Constants1 :as Constants])
  (:import (edu.wpi.first.wpilibj2.command Command CommandScheduler)
           (org.littletonrobotics.junction LogFileUtil LoggedRobot Logger)
           (org.littletonrobotics.junction.networktables NT4Publisher)
           (org.littletonrobotics.junction.wpilog WPILOGReader WPILOGWriter)
           (frc.robot RobotContainer)))

(def robotContainer (RobotContainer.))
(def autonomousCommand (atom nil))

(defn -init [this]
;;   (Logger/recordMetadata "ProjectName" BuildConstants/MAVEN_NAME)
;;   (Logger/recordMetadata "BuildDate" BuildConstants/BUILD_DATE)
;;   (Logger/recordMetadata "GitSHA" BuildConstants/GIT_SHA)
;;   (Logger/recordMetadata "GitDate" BuildConstants/GIT_DATE)
;;   (Logger/recordMetadata "GitBranch" BuildConstants/GIT_BRANCH)
;;   (Logger/recordMetadata "GitDirty" (case (BuildConstants/DIRTY)
;;                                       0 "All changes committed"
;;                                       1 "Uncommitted changes"
;;                                         "Unknown"))
  (case (Constants/currentMode)
    Constants/REAL ((Logger/addDataReceiver (WPILOGWriter.))
          (Logger/addDataReceiver (NT4Publisher.)))
    Constants/SIM (Logger/addDataReceiver (NT4Publisher.))
    Constants/REPLAY ((. this (setUseTiming false))
            (Logger/setReplaySource (WPILOGReader. (LogFileUtil/findReplayLog)))
            (Logger/addDataReceiver (WPILOGWriter. (LogFileUtil/addPathSuffix (LogFileUtil/findReplayLog) "_sim")))))
  (Logger/start))

(defn -robotPeriodic-void [] (.. CommandScheduler (getInstance) (run)))

(defn -disabledInit-void [] ())

(defn -disabledPeriodic-void [] ())

(defn -autonomousInit-void []
  (swap! autonomousCommand (. robotContainer (getAutonomousCommand)))
  (if (not= autonomousCommand nil) (.. CommandScheduler (getInstance) (schedule autonomousCommand))))

(defn -autonomousPeriodic-void [] ())

(defn -teleopInit-void [] (if (not= autonomousCommand nil) (. autonomousCommand (cancel))))

(defn -teleopPeriodic-void [] ())

(defn -testInit-void [] (.. CommandScheduler (getInstance) (cancelAll)))

(defn -testPeriodic-void [] ())

(defn -simulationInit-void [] ())

(defn -simulationPeriodic-void [] ())
