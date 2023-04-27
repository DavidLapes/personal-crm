(ns crm.lib.db.filters
  (:require [honey.sql.helpers :as honey]))

(defn add-full-name-filter
  "Adds full-name filter to HoneySQL query."
  ([query full-name-filter]
   (add-full-name-filter query full-name-filter {:first-name-column  :first_name
                                                 :middle-name-column :middle_name
                                                 :last-name-column   :last_name}))
  ([query full-name-filter {:keys [first-name-column middle-name-column last-name-column]}] +
   (honey/where query [:ilike
                       [:replace
                        [:concat
                         [:concat first-name-column " "]
                         [:concat middle-name-column " "]
                         [:concat last-name-column]]
                        "  "
                        " "]
                       [:concat "%" full-name-filter "%"]])))
