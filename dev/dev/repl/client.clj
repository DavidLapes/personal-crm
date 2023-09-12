(ns dev.repl.client
  (:require [cheshire.core :as json]
            [clj-http.client :as client]))

(def api-url "http://localhost:5000/api")

(defn attempt-ping []
  (client/get (str api-url "/public/ping")))

(defn attempt-login []
  (client/post (str api-url "/public/auth/sign-in")
               {:throw-exceptions false
                :accept :json
                :content-type :json
                :body (json/encode {:email "admin@admin.com"
                                    :password "admin"})}))
