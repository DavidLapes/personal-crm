(ns crm.api.schema.user
  (:require [schema.core :as s]))

(s/defschema CreateUser
  {:email                        s/Str
   :password                     s/Str
   :first_name                   s/Str
   (s/optional-key :middle_name) (s/maybe s/Str)
   :last_name                    s/Str})

(s/defschema UserOutput
  {:id           s/Int
   :email        s/Str
   :first_name   s/Str
   :middle_name  (s/maybe s/Str)
   :last_name    s/Str
   :full_name    s/Str
   :person_id    (s/maybe s/Int)
   :person       (s/maybe {:id          s/Int
                           :first_name  s/Str
                           :middle_name (s/maybe s/Str)
                           :last_name   s/Str
                           :full_name   s/Str})
   :is_active    s/Bool
   :is_deleted   s/Bool
   :time_created s/Inst})

(s/defschema UpdateUser
  {(s/optional-key :email)       s/Str
   (s/optional-key :first_name)  s/Str
   (s/optional-key :middle_name) (s/maybe s/Str)
   (s/optional-key :last_name)   s/Str
   (s/optional-key :person_id)   (s/maybe s/Int)})

(s/defschema UserListOutput
  {:data  [UserOutput]
   :count s/Int})
