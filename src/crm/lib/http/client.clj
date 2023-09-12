(ns crm.lib.http.client
  (:require [clj-http.client :as c]))

(defn- parse-response
  "Takes clj-http response and decodes the JSON body as well as do other transformations."
  [{:keys [body] :as response}]
  (let [body (if (some? body)
               {:body (-> (cheshire.core/decode body)
                          clojure.walk/keywordize-keys)}
               {})]
    (merge response body)))

(defn request
  "Executes request and returns decoded response."
  [{:keys [method body uri headers] :or {headers {}}}]
  (let [request-fn (case method
                     :get    c/get
                     :post   c/post
                     :put    c/put
                     :patch  c/patch
                     :delete c/delete)
        body (if (some? body)
               {:body (cheshire.core/encode body)}
               {})
        headers {:headers headers}
        request (merge body headers {:throw-exceptions false
                                     :accept :json
                                     :content-type :json})]
    (-> (request-fn uri request)
        parse-response)))
