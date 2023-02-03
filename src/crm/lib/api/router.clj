(ns crm.lib.api.router
  (:require [reitit.ring :as ring]))

(defn request->ctx
  "Returns ctx from incoming request."
  [request]
  (-> request ring/get-match :data :ctx))

(defn wrap-with-context [handle-function]
  (fn [request]
    (let [ctx (request->ctx request)
          query-params (-> (:query-params request) (clojure.walk/keywordize-keys))]
      (handle-function (assoc request :ctx ctx :query-params query-params)))))
