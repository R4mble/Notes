(ns nmsl.quicklearning
  (:import (java.util HashMap)
           (java.awt Point)))

;(println ((fn [x y]
;            #{x y})
;           3 4))
;
;(defn make-set [x y]
;  #{x y})
;
;(defn make-set
;  ([x] #{x})
;  ([x y] #{x y}))
;
;(defn set [first & item]
;  (vector first item))
;
;(println (make-set 5))
;
;(println (set 5 34 2 5  2 "fd"))
;
;(def make
;  ((fn []
;     (+ 1 2))))
;
;(println make)


;(def make-list0 #(list %1 %2))

;(println (
;           (let [r 5
;               pi 3
;               r-squared (* r r)]
;           (println "radius is" r)
;           (str (* pi r-squared)))
;           )
;         )

;
;(defn print-down-from [x]
;  (when (pos? x)
;    (println x)
;    (recur (dec x))))
;
;(println (print-down-from 5))


;(println (rand-int 1024))

(def ascii (map char (range 65 (+ 65 26))))
(println ascii)

(println (HashMap. {"foo" 32 "bar" 1 "tra" "get"}))
(println (.-y (Point. 10 20)))

(println (let [origin (Point. 0 0)]
           (set! (.-x origin) 11)
           (str origin)))











