(ns crm.lib.db.filters
  (:require [honey.sql.helpers :as honey]))

(defn full-name-filter
  "Adds full-name filter to HoneySQL query."
  ([query full-name-filter]
   (full-name-filter query full-name-filter {:first-name-column  :first_name
                                             :middle-name-column :middle_name
                                             :last-name-column   :last_name}))
  ([query full-name-filter {:keys [first-name-column middle-name-column last-name-column]}]+
   (honey/where query [:like [first-name-column " " middle-name-column " " last-name-column]
                             full-name-filter])))
