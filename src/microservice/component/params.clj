(ns microservice.component.params
  (:require [clojure.java.io :as io]
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

(defmulti resolve-param (fn [{:keys [params/provider]}] provider))

(defmethod resolve-param :params/env [{:keys [params/value]}] (-> value name (.replaceAll "-" "_") System/getenv))

(defmethod resolve-param :params/static [{:keys [params/value]}]
  (println value)
  value)

(defn- resolve-edn-params
  "Returns map of parameters configured in params.edn file."
  []
  (let [config-edn (-> "config/params.edn"
                       io/resource
                       io/file
                       .getAbsolutePath
                       slurp
                       read-string)]
    (reduce
      (fn [params-map [param {:keys [providers optional]
                              :or   {optional false}}]]
        (timbre/info (str "Resolving param - " param ""))
        (loop [coll providers]
          (let [resolved-param (resolve-param (first coll))]

            ;;TODO maybe use if-let ?

            (if (some? resolved-param)
              (assoc params-map param resolved-param)
              (if (empty? (rest coll))
                (if optional
                  (assoc params-map param resolved-param)
                  (throw
                    (ex-info
                      (str "Required parameter " param " could not be resolved.")
                      {:cause :unresolved-application-parameter})))
                (recur (rest coll)))))))
      {}
      config-edn)))

(defn init-params
  "Initialize parameters map."
  []
  (timbre/info "Initializing params")
  (swap! params (constantly (resolve-edn-params)))
  (timbre/info "Params initialized"))
