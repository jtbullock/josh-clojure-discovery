(ns html-generator)

(def sample-page 
  [:html
   [:head
    [:title "HTML output from vectors!"]]
   [:body
    [:h1 {:id "page-title"} "HTML output from vectors!"]
    [:div {:class "main-content"}
     [:p "Converting nested lists into HTML is an old Lisp trick"]
     [:p "But Clojure uses vectors instead."]]
    [:div
     [:p "Converting nested lists into HTML is an old Lisp trick"]
     [:p "But Clojure uses vectors instead."]]]])
    
(def simple-example [:html [:p "Para 1"] [:p "Para 2"]])

(defn write-attributes
  "Given a map of `attributes`, return a string of the attributes
   in HTML form." 
  [attributes]
  (apply str (map (fn [[attr val]] (str " " (name attr) "=\"" val "\"")) attributes)))

(defn parse-tag-vector [tag-vector]
  (let [tag (first tag-vector)]
    (reduce
     (fn [acc, item] 
      (cond 
        (map? item) (assoc acc :attributes item)
        (string? item) (assoc acc :content item)
        (vector? item) (update acc :children conj (parse-tag-vector item))))
     {:tag tag :attributes {} :content "" :children []}
     (rest tag-vector))))

(defn print-html*
  [parsed-vec]
  (let [tag-str (name (parsed-vec :tag))
        formatted-attributes (write-attributes (parsed-vec :attributes))
        printed-children (map (fn [x] (print-html* x)) (parsed-vec :children))
        children-str (apply str printed-children)
        content (parsed-vec :content)]
    (str "<" tag-str formatted-attributes ">" content children-str "</" tag-str ">")))

(defn print-html [vec-html] (print-html* (parse-tag-vector vec-html)))
