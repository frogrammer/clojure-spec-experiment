(defproject clojure-experiment "0.1.0-SNAPSHOT"
  :description "Clojure spec experiments"
;;   :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha12"]]
  :main ^:skip-aot clojure-experiment.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
