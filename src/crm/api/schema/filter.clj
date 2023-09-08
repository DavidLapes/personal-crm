(ns crm.api.schema.filter
  (:require [schema.core :as s :refer [defschema]]))

(defschema general-sql-filters
  {:limit           s/Int
   :order_column    s/Str
   :order_direction s/Str
   :order_limit     s/Int
   :page_number     s/Int})

(defschema user-filters
  {:id                s/Int
   :email             s/Str
   :name              s/Str
   :person_id         s/Int
   :is_active         s/Bool
   :is_deleted        s/Bool
   :birthdate_from    s/Inst
   :birthdate_to      s/Inst
   :time_created_from s/Inst
   :time_created_to   s/Inst})
