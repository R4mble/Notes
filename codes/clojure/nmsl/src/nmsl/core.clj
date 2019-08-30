(ns nmsl.core
  (:import (java.util List)))

;(defn hello-world []
;  (println "Hello World"))
;
;
;(hello-world)
;
;(def numbers [1 2 3 4])
;
;(println numbers)
;
;(println (apply + numbers))
;(println (< 0 4 0230))
;(println 0230)

(defprotocol Concatenatable
  (cat [this other]))

(extend-type String
  Concatenatable
  (cat [this other]
    (.concat this other)))

(extend-type List
  Concatenatable
  (cat [this other]
    (concat this other)))

(println (cat "s" "as"))
(println (cat [1 2 3] [3 4 5]))