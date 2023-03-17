(ns crm.api.schema.person
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(s/defschema CreatePerson
  {:first_name                   s/Str
   (s/optional-key :middle_name) (s/maybe s/Str)
   :last_name                    s/Str
   (s/optional-key :birthdate)   (s/maybe LocalDateTime)})

(s/defschema PersonOutput
  {:id           s/Int
   :first_name   s/Str
   :middle_name  (s/maybe s/Str)
   :last_name    s/Str
   :full_name    s/Str
   :user         (s/maybe {:id          s/Int
                           :email       s/Str
                           :person_id   s/Int
                           :first_name  s/Str
                           :middle_name (s/maybe s/Str)
                           :last_name   s/Str
                           :full_name   s/Str})
   :birthdate    (s/maybe LocalDateTime)
   :time_created LocalDateTime})

(s/defschema UpdatePerson
  {(s/optional-key :first_name)  s/Str
   (s/optional-key :middle_name) (s/maybe s/Str)
   (s/optional-key :last_name)   s/Str
   (s/optional-key :birthdate)   (s/maybe LocalDateTime)})

(s/defschema PersonListOutput
  {:data  [PersonOutput]
   :count s/Int})