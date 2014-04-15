(ns concord.models.libs
  [:require [clojure.java.jdbc :as sql]])

(defn libs
  "Return map of library name to seq of version info for each library,
   sorted by library name."
  [conn]
  (let [libs (sql/query conn "SELECT * FROM libraries")]
    (into (sorted-map) (group-by :name libs))))

(defn lib
  "Return library info based on url_friendly_name"
  [conn n]
  (let [ls (sql/query conn ["SELECT * FROM libraries WHERE url_friendly_name=?" n])]
    (when (seq ls)
      (first ls))))
