(ns crm.api.schema.coercion
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(defrecord LocalDateTimeSchema [_]
  s/Schema
  (spec [_]
    (schema.spec.leaf/leaf-spec
      (fn [x]
        (LocalDateTime/parse x))))
  (explain [_]
    'LocalDateTime))

(def LocalDateTimeSpec
  (LocalDateTimeSchema. nil))
