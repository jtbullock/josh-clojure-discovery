(defproject josh-clojure-discovery "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
                 [org.clojure/clojure "1.11.1"]
                 [org.clojure/data.csv "1.0.1"]
                 [semantic-csv "0.2.1-alpha1"]
                 [org.clojure/math.numeric-tower "0.0.5"]]
  
  :cljfmt {
           :remove-surrounding-whitespace? false
           :indentation? false}
  
  :main ^:skip-aot josh-clojure-discovery.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
