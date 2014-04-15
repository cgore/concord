(defproject concord "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [mysql/mysql-connector-java "5.1.30"]
                 [com.mchange/c3p0 "0.9.2.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler concord.handler/app
         :init concord.handler/init
         :destroy concord.handler/destroy}
  :aot :all
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]]}})
