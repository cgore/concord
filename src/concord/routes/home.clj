(ns concord.routes.home
  (:require [clojure.string :as str]
            [compojure.core :refer :all]
            [concord.views.layout :as layout]
            [concord.models.libs :as libs]
            [concord.models.nses :as nses]))

(defn libs-page [conn]
  (let [libs (libs/libs conn)]
    (layout/common
     [:h1 "API Docs"]
     [:ul
      (for [[name versions] libs]
        (let [{:keys (name url_friendly_name)} (first versions)]
          [:li [:a {:href (str "/" url_friendly_name)} name]]))])))

(defn lib-page [conn lib]
  (let [{:keys (id name description site_url source_base_url copyright)} (libs/lib conn lib)
        nslist (nses/nses conn id)]
    (layout/common
     [:h1 name]
     [:p [:b "Links: "] [:a {:href site_url} "Library"]]
     [:p description]
     [:p copyright]
     [:h2 "Namespaces"]
     (for [{n :name nsid :id} nslist]
       [:li [:p
             [:a {:href (str "/" lib "/" n)} n]
             " - "
             (interpose " " (map #(vector :a {:href (str "/" lib "/" n "/" (:url_friendly_name %))} (:name %))
                                 (nses/fns conn nsid)))]]))))

(defn ns-page [conn lib n]
  (let [{libid :id libname :name} (libs/lib conn lib)
        {:keys (id name doc)} (nses/nsinfo conn libid n)]
    (layout/common
     [:h2 libname " / " name]
     [:p doc]
     [:h2 "Functions"]
     [:p (interpose " " (map #(vector :a {:href (str "/" lib "/" n "/" (:url_friendly_name %))}
                                      (:name %))
                             (nses/fns conn id)))])))

(defn fn-page [conn lib n v]
  (let [{libid :id libname :name} (libs/lib conn lib)
        {nsid :id nsname :name} (nses/nsinfo conn libid n)
        finfo (nses/finfo conn nsid v)]    
    (layout/common
     [:h2 libname " / " nsname " / " v]
     [:pre (:arglists_comp finfo)]
     [:pre (:doc finfo)]
     [:h2 "Source"]
     [:pre (:source finfo)]
     [:h2 "Examples"]
     (for [{body :body} (nses/examples conn (:id finfo))]
         [:pre body])
     [:h2 "Comments"]
     (for [{body :body} (nses/comments conn (:id finfo))]
         [:pre body]))))


(defn home-routes [conn]
  (routes
   (GET "/" [] (libs-page conn))
   (GET "/:lib" [lib] (lib-page conn lib))
   (GET "/:lib/:n" [lib n] (ns-page conn lib n))
   (GET "/:lib/:n/:v" [lib n v] (fn-page conn lib n v))))
