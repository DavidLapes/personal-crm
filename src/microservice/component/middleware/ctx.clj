(ns microservice.component.middleware.ctx)

(defn wrap-with-ctx-middleware
  ;;TODO - Write docs to functions!!!
  [ctx handler]
  (fn [request]
    (handler (assoc request :ctx ctx))))
