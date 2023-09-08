(ns crm.api.schema.filter
  (:require [crm.api.schema.coercion :as c]
            [schema.core :as s :refer [defschema]]))

(defschema general-sql-filters
  {(s/optional-key :limit)           s/Int
   (s/optional-key :order_column)    s/Str
   (s/optional-key :order_direction) s/Str
   (s/optional-key :order_limit)     s/Int
   (s/optional-key :page_number)     s/Int})

(defschema user-filters
  {(s/optional-key :id)                s/Int
   (s/optional-key :email)             s/Str
   (s/optional-key :name)              s/Str
   (s/optional-key :person_id)         s/Int
   (s/optional-key :is_active)         s/Bool
   (s/optional-key :is_deleted)        s/Bool
   (s/optional-key :birthdate_from)    c/LocalDateTimeSpec
   (s/optional-key :birthdate_to)      c/LocalDateTimeSpec
   (s/optional-key :time_created_from) c/LocalDateTimeSpec
   (s/optional-key :time_created_to)   c/LocalDateTimeSpec})
