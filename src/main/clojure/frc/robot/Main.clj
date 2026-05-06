;; Copyright (c) 2026 Ira Parsons
;; Copyright (c) 2021-2026 Littleton Robotics
;; http://github.com/Mechanical-Advantage
;;
;; Use of this source code is governed by a BSD
;; license that can be found in the LICENSE file
;; at the root directory of this project.

(ns frc.robot.Main
  (:gen-class)
  (:import (edu.wpi.first.wpilibj.RobotBase)
           (frc.robot.Robot)))

(defn -main [args]
  (RobotBase/startRobot (Robot.)))