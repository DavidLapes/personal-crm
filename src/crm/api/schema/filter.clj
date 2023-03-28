(ns crm.api.schema.filter
  (:require [crm.lib.db.utils :as db-utils]))

;;TODO: Implement filter for full_name?
;;TODO: Implement full-text search?

(def general-sql-filters db-utils/general-filters)

(def user-filters #{:id :first_name :last_name :middle_name :full_name})
