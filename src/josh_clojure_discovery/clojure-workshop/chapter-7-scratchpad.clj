(ns chapter-7-scratchpad)

; Lazy sequences

; Exercise 7.01
(def sample-data
  [[24.2 420031]
   [25.8 492657]
   [25.9 589014]                        ;max
   [23.8 691995]                        ;min
   [24.7 734902]                        ;max
   [23.2 794243]
   [23.1 836204]                        ;min
   [23.5 884120]])
   
(defn local-max? [[[a _] [b _] [c _]]]
  (and (< a b)
       (< c b)))

(defn local-min? [[[a _] [b _] [c _]]]
  (and (> a b)
       (> c b)))

(defn inflection-points [data]
  (lazy-seq
   (let [current-series (take 3 data)]
     (cond
       (< (count current-series) 3) '()
       (local-max? current-series)
        (cons
          (conj (second current-series) :peak)
          (inflection-points (rest data)))
       (local-min? current-series)
        (cons
          (conj (second current-series) :valley)
          (inflection-points (rest data)))
       :otherwise (inflection-points (rest data))))))

; Exercise 7.02
(def endless-potatoes (repeatedly (fn [] (+ 10 (rand-int 390)))))

(defn average-potatoes [prev arrivals]
  (lazy-seq 
   (if-not arrivals 
     '()
     (let [[_ n total] prev
           current [(first arrivals) (inc (or n 0)) (+ (first arrivals) (or total 0))]]
       (cons current (average-potatoes current (next arrivals)))
     )
   )))

; Exercise 7.03

; My activity: family tree
(def family-parents [
  ["Josh Bullock" ["Judith Jenkins" "John Bullock"]]
  ["Judith Jenkins" ["Regina Rauckhorst" "Albert Jenkins"]]
  ["John Bullock" ["Ruth Balog" "Gary Bullock"]]
  ["Regina Rauckhorst" ["Great Grandma 1" "Great Grandpa 1"]]
  ["Albert Jenkins" ["Great Grandma 2" "Great Grandpa 2"]]
  ["Ruth Balog" ["Great Grandma 3" "Great Grandpa 3"]]
  ["Gary Bullock" ["Great Grandma 4" "Great Grandpa 4"]]
])

(def birth-parent-map (into {} family-parents))

(defn build-tree
  [name birth-parent-lookup]
  (lazy-seq
  (let [parents (get birth-parent-lookup name)]
    (if (nil? parents) 
      '()
      [name [(build-tree (first parents) birth-parent-lookup) (build-tree (second parents) birth-parent-lookup)]]
    )
  )
  )
)