(ns crm.api.schema.coercion
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(defrecord LocalDateTimeSchema [_]
  s/Schema
  (spec [_]
    (schema.spec.leaf/leaf-spec
      (fn [x]
        (if (instance? LocalDateTime x)
          x
          (LocalDateTime/parse x)))))
  (explain [_]
    'LocalDateTimeSpec))

(def LocalDateTimeSpec
  (LocalDateTimeSchema. nil))
