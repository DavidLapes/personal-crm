(ns microservice.logging
  (:require [taoensso.timbre :as timbre]))

(def ^:private default-logging
  {:level        :info
   :ns-blacklist ["com.zaxxer.*"]})

(defn init-logging
  "Initializes Timbre logger."
  ([]
   (init-logging default-logging))
  ([config]
   (timbre/merge-config! config)))
