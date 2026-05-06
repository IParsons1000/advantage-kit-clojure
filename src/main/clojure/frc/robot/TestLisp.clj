;; Test Class
(ns frc.robot.TestLisp
  (:gen-class
    :init init
    :methods [#^{:static true} [gettestlispprivate [] int]]))

(def test-lisp-private 10)

(defn -init [] ())

(defn -gettestlispprivate [] test-lisp-private)