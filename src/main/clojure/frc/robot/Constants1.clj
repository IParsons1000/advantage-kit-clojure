;; Copyright (c) 2026 Ira Parsons
;; Copyright (c) 2021-2026 Littleton Robotics
;; http://github.com/Mechanical-Advantage
;;
;; Use of this source code is governed by a BSD
;; license that can be found in the LICENSE file
;; at the root directory of this project.

(ns frc.robot.Constants1
  (:import (edu.wpi.first.wpilibj RobotBase)))

(def REAL 0)
(def SIM 1)
(def REPLAY 2)

(def SimMode SIM)
(def currentMode (if (= (RobotBase/isReal) REAL) REAL SimMode))
