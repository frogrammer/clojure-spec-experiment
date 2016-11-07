(ns clojure-experiment.spec_error
  (:require [clojure.spec :as s]
            [clojure.spec.test :as stest]))


;; ######### Preciate definitions ###########

(s/def ::check-num number?)
(s/def ::check-int integer?)
(s/def ::check-seq seqable?)
(s/def ::check-fn ifn?)
(s/def ::check-coll coll?)

(s/def ::check-cons
  (s/cat ::first-arg any? ::second-arg ::check-seq))
(s/def ::check-reduce-two
  (s/cat ::first-arg ::check-fn ::second-arg ::check-coll))
(s/def ::check-reduce-three
  (s/cat ::first-arg ::check-fn ::second-arg any? ::third-arg ::check-coll))
(s/def ::check-plus
  (s/cat ::arg-list (s/* ::check-num)))



;; ######### User defined functions #########

;; getting the function name
;; (defn inc [x]
;;   (let [x (try (s/assert ::check-num x)
;;             (catch clojure.lang.ExceptionInfo e
;;               (throw
;;                 (ex-info "Modified spec:"
;;                          (conj (ex-data e) [:fn-name "inc"])))))]
;;     (clojure.core/inc x)))

;; 1. When a function takes only one argument, the value for :in is []
;; 2. When a function takes more than one argument, the value for :in is
;; [index-where-it-fails]

(defn inc [x]
  (do (s/assert ::check-num x) (clojure.core/inc x)))

(defn odd? [x]
  (do (s/assert ::check-int x) (clojure.core/odd? x)))

(defn cons [x seq]
  (do (s/assert ::check-cons [x seq]) (clojure.core/cons x seq)))

(defn reduce
  ([f coll]
   (do
     (s/assert ::check-reduce-two [f coll])
     (clojure.core/reduce f coll)))
  ([f val coll]
   (do
     (s/assert ::check-reduce-three [f val coll])
     (clojure.core/reduce f val coll))))

(defn + [& args]
  (do (s/assert ::check-plus args) (apply clojure.core/+ args)))

(s/check-asserts true)


;; you can parse the function name with .getClass?

;; ######### Experiments ####################

(println (inc 10))
(println (inc 5))
(println (inc 1.1))
;; (inc true)
;; (inc 1 2 3) ;; This throws ArityException

(println (odd? 10))
(println (odd? 9))
;; (odd? 1.1)
;; (odd? inc)

(println (cons 1 [1 2 3]))
(println (cons inc [1 2 3]))
(println (cons false '(1 2 3)))
;; (cons 1 2)
;; (cons 1)

(println (reduce + [1 2 3]))
;; It seems to handle secondary errors
;; (reduce + 3 [(reduce + [1 true]) 2 3])
;; (reduce [1 2 3])
;; (reduce + 3 3 [1 2 3])
;; (reduce 1 + [1 2 3])
;; (reduce + + [1 2 3])

(println (+))
(println (+ 1))
(println (+ 1 9))
(println (+ 1 5 4))
;; (+ false)
;; (+ 1 true)
