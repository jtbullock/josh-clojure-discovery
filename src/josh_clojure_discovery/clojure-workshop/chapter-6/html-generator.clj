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
     [:p "But Clojure uses vectors instead."]]
    ]])

(def simple-example [:html [:p "Para 1"] [:p "Para 2"]])

(defn write-attributes
  "Given a map of `attributes`, return a string of the attributes
   in HTML form." 
  [attributes]
  (map (fn [[attr val]] (str (name attr) "=\"" val "\"")) attributes)
)

(defn convert-to-html
  "Given a vector of HTML tags, return a string as HTML"
  [html-vector]
  (let [current-tag (first html-vector)
        ;current-tag-str (name current-tag)
        tag-content (subvec html-vector 1)
        child-content (map (fn [thing] (cond
                                         (map? thing)
                                         "attr='attr'"
                                         (string? thing)
                                         thing
                                         (vector? thing)
                                         (convert-to-html thing))) tag-content)
        ]
    (str "<" (name current-tag) ">" (apply str child-content) "</" (name current-tag) ">"))
)