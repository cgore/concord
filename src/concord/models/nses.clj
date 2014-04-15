(ns concord.models.nses
  [:require [clojure.java.jdbc :as sql]])

(defn compare-ns [n1 n2]
  (let [z (compare (:name n1) (:name n2))]
    (if (zero? z)
      (compare n1 n2)
      z)))

(defn nses [conn lib-id]
  (sort-by :name (sql/query conn ["SELECT * FROM namespaces WHERE library_id = ?" lib-id])))

(defn nsinfo [conn lib-id ns-name]
  (first (sql/query conn ["SELECT * FROM namespaces WHERE library_id = ? AND name = ?" lib-id ns-name])))

(defn fns [conn ns-id]
  (sort-by :name (sql/query conn ["SELECT * FROM functions WHERE namespace_id=?" ns-id])))

(defn finfo [conn ns-id f-name]
  (first (sql/query conn ["SELECT * FROM functions WHERE namespace_id=? AND url_friendly_name=?" ns-id f-name])))

(defn examples [conn f-id]
  (sql/query conn ["SELECT body FROM examples WHERE function_id=?" f-id]))

(defn comments [conn f-id]
  (sql/query conn ["SELECT * FROM comments WHERE commentable_id=? AND commentable_type='Function'" f-id]))
