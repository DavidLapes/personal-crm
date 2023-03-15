(ns crm.api.schema.user
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(s/defschema CreateUser
  {:email                        s/Str
   :password                     s/Str
   :first_name                   s/Str
   (s/optional-key :middle_name) (s/maybe s/Str)
   :last_name                    s/Str})

(s/defschema UserOutput
  {:id s/Int
   :email s/Str
   :first_name s/Str
   :middle_name (s/maybe s/Str)
   :last_name s/Str
   :is_active s/Bool
   :is_deleted s/Bool
   :time_created LocalDateTime})

(s/defschema UpdateUser
  {:email                        s/Str
   :first_name                   s/Str
   (s/optional-key :middle_name) (s/maybe s/Str)
   :last_name                    s/Str})

(s/defschema UserListOutput
  {:data  [UserOutput]
   :count s/Int})
