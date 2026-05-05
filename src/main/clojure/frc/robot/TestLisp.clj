;; Test Class
(ns frc.robot.TestLisp
  (:gen-class
    :methods [[gettestlispprivate [] int]]))

(def test-lisp-private 10)

(defn gettestlispprivate [] (test-lisp-private))