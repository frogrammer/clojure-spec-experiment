(ns clojure-experiment.spec_error
  (:require [clojure.spec :as s]
            [clojure.spec.test :as stest]))
;; I think you can't run alpha12 in lighttables


;; ######### Preciate definitions ###########

(s/def ::check-num number?)
(s/def ::check-int integer?)



;; ######### User defined functions #########

;; (defn inc [x]
;;   (let [x (s/assert ::check-num x)] (clojure.core/inc x)))

(defn inc [x]
  (let [x (try (s/assert ::check-num x)
            (catch clojure.lang.ExceptionInfo e
              (throw
                (ex-info "Modified spec:"
                         (conj (ex-data e) [:fn-name "inc"])))))]
    (clojure.core/inc x)))

(defn odd? [x]
  (let [x (s/assert ::check-int x)] (clojure.core/odd? x)))

(s/check-asserts true)



;; ######### Experiments ####################

(println (inc 10))
(println (inc 5))
(println (inc 1.1))
;; (inc true)

(println (odd? 10))
(println (odd? 9))
;; (odd? 1.1)
;; (odd? inc) ;; parsing the function name?
