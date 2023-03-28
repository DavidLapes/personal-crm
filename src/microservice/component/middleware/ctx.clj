(ns microservice.component.middleware.ctx)

(defn wrap-with-ctx-middleware
  [ctx handler]
  (fn [request]
    (handler (assoc request :ctx ctx))))
