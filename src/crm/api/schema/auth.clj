(ns crm.api.schema.auth
  (:require [schema.core :as s]))

(s/defschema AuthenticationToken
  {:token s/Str})

(s/defschema SignIn
  {:email    s/Str
   :password s/Str})
