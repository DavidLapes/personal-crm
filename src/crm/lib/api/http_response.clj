(ns crm.lib.api.http-response)

(defn add-response-message [response message]
  (let [body (:body response)]
    (merge response
           {:body (merge body
                         {:message message})})))

(defn response-message [message]
  {:message message})
