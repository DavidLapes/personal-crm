(ns microservice.component.param
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [taoensso.timbre :as timbre]))

(def ^:private params (atom {}))

(defn get-param
  "Returns configured value for given parameter."
  [param]
  (when (not (keyword? param))
    (throw (ex-info "Param must be passed as a keyword" {:cause :param-not-keyword})))
  (when (not (contains? @params param))
    (throw (ex-info "Such param does not exist" {:cause :param-not-exists})))
  (get @params param))

(defn- resolve-edn-params
  "Returns map of parameters configured in config.edn file."
  [profile]
  (let [config (io/resource "config/config.edn")]
    (aero/read-config config {:profile profile})))

(defn init-params
  "Initialize parameters map."
  [profile]
  (assert (some? profile) "Profile was not specified")
  (timbre/info "Initializing params")
  (swap! params (constantly (resolve-edn-params profile)))
  (timbre/info "Params initialized"))
