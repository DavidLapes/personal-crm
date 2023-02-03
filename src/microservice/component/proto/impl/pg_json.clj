(ns microservice.component.proto.impl.pg_json
  (:require [clojure.java.jdbc :as jdbc]
            [cheshire.core :as json]
            [clj-postgresql.types])
  (:import org.postgresql.util.PGobject))

(extend-protocol jdbc/IResultSetReadColumn
  PGobject
  (result-set-read-column [pg-object metadata idx]
    (let [type  (.getType pg-object)
          value (.getValue pg-object)]
      (case type
        "json" (json/parse-string value true)
        "jsonb" (json/parse-string value true)
        :else value))))
