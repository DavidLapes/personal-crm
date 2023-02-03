(ns crm.api.schema.message
  (:require [schema.core :as s]))

(s/defschema MessageResponse
  {:message s/Str})
